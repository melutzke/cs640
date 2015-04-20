package edu.wisc.cs.sdn.simpledns;

import java.net.*;
import java.util.*;
import java.util.Arrays;
import edu.wisc.cs.sdn.simpledns.packet.*;

public class SimpleDNS {
	// need to handle A, AAAA, CNAME and NS queries
	private final static int PACKETSIZE = 2048; // nice safe value?
	private static String ROOTSERVER;

	public static String ipAddressPlz( String domain, String nameserver ){
		// this will recursively follow records / cnames until we achieve an a record (ip address)

		try {
         // Convert the argument to ensure that is it valid
			int port = 53;

         // Construct the socket
			DatagramSocket newSocket = new DatagramSocket( port, InetAddress.getByName(nameserver) );

           	// Create a packet
			DatagramPacket packet = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE );

			packet.setAddress(InetAddress.getByName(nameserver));

			DNS newPayload = new DNS();
			newPayload.setQuery( true );
			newPayload.setOpcode( DNS.OPCODE_STANDARD_QUERY );
			newPayload.addQuestion( new DNSQuestion(domain, DNS.TYPE_A) );

			packet.setData( newPayload.serialize() );

			newSocket.send( packet ) ;


           	// Receive a packet (blocking)
	       	DatagramPacket packet_zwei = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE );
			newSocket.receive(packet_zwei);

			//parse response
			DNS response = DNS.deserialize( packet_zwei.getData(), packet_zwei.getLength() );

			List<DNSResourceRecord> answers = response.getAnswers();
			List<DNSResourceRecord> authorities = response.getAuthorities(); // fuck da poLEASE

			List<DNSResourceRecord> aRecords = new LinkedList<DNSResourceRecord>();
			List<DNSResourceRecord> aaaaRecords = new LinkedList<DNSResourceRecord>();
			List<DNSResourceRecord> nsRecords = new LinkedList<DNSResourceRecord>();
			DNSResourceRecord 		cName = null;

			if( answers.size() >= 1 ){
				// check for a good answer

				for( DNSResourceRecord currAnswer : answers ){

					short answerType = currAnswer.getType();

					if( answerType == DNS.TYPE_A ){
						aRecords.add( currAnswer );
					} else if ( answerType == DNS.TYPE_AAAA ){
						aaaaRecords.add( currAnswer );
					} else if ( answerType == DNS.TYPE_NS ){
						nsRecords.add( currAnswer );
					} else if ( answerType == DNS.TYPE_CNAME ){
						cName = currAnswer;
					}

				}

				if( aRecords.size() >= 1 ){
					return aRecords.get(0).getName();
				}

				if( cName != null ){
					return ipAddressPlz( cName.getName(), nameserver );
				} else {
					System.out.print("Our dongers were too high!");
					throw new Exception();
				}

			}

			if( authorities.size() >= 1 ) {
				// if we're here, we had no good answers :(
				// this means we need to follow a new name server, or
				// ヽ༼ຈل͜ຈ༽ﾉ raise your dongers ヽ༼ຈل͜ຈ༽ﾉ.

				if( nsRecords.size() >= 1 ){
					return ipAddressPlz( domain, ipAddressPlz( nsRecords.get(0).getName(), ROOTSERVER ) );
				} else {
					System.out.println("Donger no donging.");
					throw new Exception();
				}
			}

			// if we made it here, we're fucked
			System.out.println("IP MESSED GOOFD");
			throw new Exception();

	
			// Send out the response
		} catch( Exception e ) {
			System.out.println( e ) ;
			e.printStackTrace();
		}

		return "8.8.8.8";
	}

	public static void processRequest( DNS request, DatagramSocket socket, String nameserver){

		System.out.println( request.toString() );

		// make sure it's a query (opcode 0)
		if( request.getOpcode() != 0 ){
			System.out.println("Opcode wrong.");
			return;
		}

		// make sure it's of type A, AAAA, CNAME, NS

		if( request.getQuestions().size() != 1 ){
			// wrong number of questions
			// YOU MUST ANSWER ME THESE QUESTIONS THREE (1 actually)
			return;
		}

		short type = request.getQuestions().get(0).getType();

		if(    type != DNS.TYPE_A 
			&& type != DNS.TYPE_AAAA 
			&& type != DNS.TYPE_NS 
			&& type != DNS.TYPE_CNAME ){ // type we don't care about
			System.out.println("Wrong type of request.");
			return;
		}

		try {
         // Convert the argument to ensure that is it valid
			int port = 53;

         // Construct the socket
			System.out.println(port + ", " + nameserver);
			DatagramSocket newSocket = new DatagramSocket( port, InetAddress.getByName(nameserver) );

           	// Create a packet
			DatagramPacket packet = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE );

			packet.setAddress(InetAddress.getByName(nameserver));

			packet.setData(request.serialize());
			newSocket.send( packet ) ;


           	// Receive a packet (blocking)
	       	DatagramPacket packet_zwei = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE );
			newSocket.receive(packet_zwei);

			//parse response
			DNS response = DNS.deserialize( packet_zwei.getData(), packet_zwei.getLength() );

			List<DNSResourceRecord> answers = response.getAnswers();
			List<DNSResourceRecord> authorities = response.getAuthorities(); // fuck da poLEASE

			List<DNSResourceRecord> aRecords = new LinkedList<DNSResourceRecord>();
			List<DNSResourceRecord> aaaaRecords = new LinkedList<DNSResourceRecord>();
			List<DNSResourceRecord> nsRecords = new LinkedList<DNSResourceRecord>();
			DNSResourceRecord 		cName = null;

			if( answers.size() >= 1 ){
				// check for a good answer

				for( DNSResourceRecord currAnswer : answers ){

					short answerType = currAnswer.getType();

					if( answerType == DNS.TYPE_A ){
						aRecords.add( currAnswer );
					} else if ( answerType == DNS.TYPE_AAAA ){
						aaaaRecords.add( currAnswer );
					} else if ( answerType == DNS.TYPE_NS ){
						nsRecords.add( currAnswer );
					} else if ( answerType == DNS.TYPE_CNAME ){
						cName = currAnswer;
					}

				}

				boolean good = type == DNS.TYPE_A 		&& aRecords.size() 		>= 1 
							|| type == DNS.TYPE_AAAA 	&& aaaaRecords.size() 	>= 1
							|| type == DNS.TYPE_NS 		&& nsRecords.size() 	>= 1
							|| type == DNS.TYPE_CNAME 	&& cName 				!= null;

				if( good ){
					// create a new response, properly add the answers section (usually includes CNAME for no reason.)
					byte[] payload = response.serialize();
					response.setAnswers( new LinkedList<DNSResourceRecord>() );
					response.setAuthorities( new LinkedList<DNSResourceRecord>() );

					// add answers
					if( type == DNS.TYPE_A ){
						response.setAnswers( aRecords );
					} else if ( type == DNS.TYPE_AAAA ){
						response.setAnswers( aaaaRecords );
					} else if ( type == DNS.TYPE_NS ){
						response.setAnswers( nsRecords );
					} 

					if ( cName != null ){
						response.addAnswer( cName );
					}

					socket.send( new DatagramPacket( payload, payload.length ) );
					return;

				} else {
					// need to recurse on a given CNAME
					// modify the request we have a tiny bit and send it for another victory lap!
					request.getQuestions().get(0).setName( cName.getName() );
					processRequest( request, socket, nameserver );
				}

			}

			if( authorities.size() >= 1 ) {
				// if we're here, we had no good answers :(
				// this means we need to follow a new name server, or
				// ヽ༼ຈل͜ຈ༽ﾉ raise your dongers ヽ༼ຈل͜ຈ༽ﾉ.
				processRequest( request, socket, ipAddressPlz( authorities.get(0).getName(), ROOTSERVER ) );
				return;
			}

			// if we got here, we REALLY MESSED UP
			System.out.println("TOO MANY MEMES.");
			throw new Exception();

			// Send out the response
		} catch( Exception e ) {
			System.out.println( e ) ;
			e.printStackTrace();
		}


		// try{
		// 	socket.send( new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE) ) ;
		// } catch( Exception e ){
		// 	e.printStackTrace();
		// 	return;
		// }
		

	}

	public static void main( String args[] ){
      // Check the arguments
		if( args.length != 4 ) {
			System.out.println( "usage: DatagramServer port" );
			return ;
		}

		ROOTSERVER = args[1];

		try {
         // Convert the argument to ensure that is it valid
			int port = 8053;

         // Construct the socket
			DatagramSocket socket = new DatagramSocket( port );

			System.out.println( "My body is Reggie - ROOT: " + ROOTSERVER );


			for( ;; ){
            	// Create a packet
				DatagramPacket packet = new DatagramPacket( new byte[PACKETSIZE], PACKETSIZE );

            	// Receive a packet (blocking)
				socket.receive( packet );

				DNS request = DNS.deserialize( packet.getData(), packet.getLength() );

				processRequest( request, socket, ROOTSERVER);

				socket.send( packet ) ;


			}  
		} catch( Exception e ) {
			System.out.println( e ) ;
			e.printStackTrace();
		}
	}
}
