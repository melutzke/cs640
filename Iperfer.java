// Screw you, javadocs
// My documentation is where it belongs
//
//
//
//
//

import java.net.Socket;
//import java.net.SocketAddress;
import java.net.ServerSocket;
import java.io.InputStream;

public class Iperfer {

    static int port;
    static int time;
    static String host;
    static boolean is_server;

    static void set_port(int arg_port) throws Exception {
        //if( arg_port < 1024 || arg_port > 65535 ){
        //    throw new Exception("Error: port number must be in the range 1024 to 65535");
        //}
        port = arg_port;
    }

    static void set_time(int arg_time) {
        time = arg_time;
    }

    static void set_host(String arg_host) {
        host = arg_host;
    }

    static void make_server() {
        is_server = true;
    }

    static void make_client() {
        is_server = false;
    }

    static void parse_args(String[] args) throws Exception {
        String argument_number_error = "Error: missing or additional arguments";
        String argument_format_error = "Error: incorrect format of arguments";
        // the next test will result in an out of bounds error if this test
        // is not done.
        if (args.length == 0) {
            //throw new ArgumentNumberException();
            throw new Exception(argument_number_error);
        }

        for(int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-c":
                    make_client();
                    if(args.length != 7){
                        throw new Exception(argument_number_error);
                    }
                break;
                case "-s":
                    make_server();
                    if(args.length != 3){
                        throw new Exception(argument_number_error);
                    }
                break;
                case "-h":
                    set_host(args[i + 1]);
                    i++;
                break;
                case "-p":
                    try{
                        set_port(Integer.parseInt(args[i + 1]));
                        i++;
                    } catch( NumberFormatException e ){
                        throw new Exception(argument_format_error);
                    }
                break;
                case "-t":
                    try{
                        set_time(Integer.parseInt(args[i + 1]));
                        i++;
                    } catch( Exception e ){
                        throw new Exception(argument_format_error);
                    }
                break;
                default:
                    throw new Exception(argument_format_error);
            }
        }
    }

    public static void run_server() {

        ServerSocket server_socket = null;
        Socket client_socket = null;
        byte[] byte_array = new byte[1000];

        while( true ){

            double bytes_read = 0;

            try {

                server_socket = new ServerSocket( port );
                client_socket = server_socket.accept();
                int read = 0;

                while( read != -1 ){
                    InputStream client_in = client_socket.getInputStream();
                    read = client_socket.getInputStream().read(byte_array, 0, 1000);

                    if( read >= 0){
                        bytes_read += read;
                    }
                }

                server_socket.close();
                System.out.println("kilobytes read: " + bytes_read / 1000.0);

            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

    }

    public static void run_client() {
        byte[] send_bytes = new byte[1000];
        long start = System.nanoTime();
        try {
            Socket server_socket = new Socket(host, port);
            while( ( System.nanoTime() - start ) / 1000000000L < time ){
                server_socket.getOutputStream().write( send_bytes );
            }
            System.out.println("Done sending! :D :D :D");
            server_socket.close();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void main (String[] args) throws Exception{
        //parse args and check for errors
        try {
            parse_args(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        if( is_server ){
            run_server();
        } else {
            run_client();
        }

    }
}
