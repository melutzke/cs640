// Screw you, javadocs
// My documentation is where it belongs
//
//
//
//
//



public class Iperfer {

    int port;
    int time;
    String host;
    boolean is_server;   
    boolean is_client;

    void set_port(int port) {
        this.port = port;
    }
    void set_time(int time) {
        this.time = time;
    }
    void set_host(String host) {
        this.host = host;
    }

    void make_server() {
        is_server = true;
        is_client = false;
    }
    void make_client() {
        is_server = false;
        is_client = true;
    }

    static void parse_args(String[] args) throws Exception {
        String argument_number_error = "Error: missing or additional arguments";
        String argument_format_error = "Error: incorrect format of arguments";
        String port_number_error = "Error: port number must be in the range 1024 to 65535";
        // the next test will result in an out of bounds error if this test
        // is not done.
        if (args.length == 0) {
            //throw new ArgumentNumberException();
            throw new Exception(argument_number_error);
        }
        // Determine the number of arguments
        switch (args[0]) {
            case "-c":
                make_client();
                if (args.length != 7) {
                    //throw new ArgumentNumberException();
                    throw new Exception(argument_number_error);
                }
                //TODO: the repeated code here should be condensed
                switch (args[1]) {
                    case "-h":
                        set_host(args[2]);
                    break;
                    case "-p":
                        set_port(Integer.parseInt(args[2]));
                    break;
                    case "-t":
                        set_time(Integer.parseInt(args[2]));
                    break;
                    default:
                        throw new Exception(argument_format_error);
                }
                switch (args[3]) {
                    case "-h":
                        set_host(args[4]);
                    break;
                    case "-p":
                        set_port(Integer.parseInt(args[4]));
                    break;
                    case "-t":
                        set_time(Integer.parseInt(args[4]));
                    break;
                    default:
                        throw new Exception(argument_format_error);
                }
                switch (args[5]) {
                    case "-h":
                        set_host(args[6]);
                    break;
                    case "-p":
                        set_port(Integer.parseInt(args[6]));
                    break;
                    case "-t":
                        set_time(Integer.parseInt(args[6]));
                    break;
                    default:
                        throw new Exception(argument_format_error);
                }
            break;
            case "-s":
                make_server();
                if (args.length != 3) {
                    //throw new ArgumentNumberException();
                    throw new Exception(argument_number_error);
                }
            break;
            default:
                //throw new ArgumentFormatException();
                throw new Exception(argument_format_error);
        }
        return;
    }

    public static void main (String[] args) {
        //parse args and check for errors
        try {
            parse_args(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        return;
    }
}
