// Screw you, javadocs
// My documentation is where it belongs
//
//
//
//
//



public class Iperfer {

    static int port;
    static int time;
    static String host;
    static boolean is_server;   
    static boolean is_client;

    static void set_port(int port) {
        this.port = port;
    }
    static void set_time(int time) {
        this.time = time;
    }
    static void set_host(String host) {
        this.host = host;
    }

    static void make_server() {
        is_server = true;
        is_client = false;
    }
    static void make_client() {
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
                    set_port(Integer.parseInt(args[i + 1]));
                    i++;
                break;
                case "-t":
                    set_time(Integer.parseInt(args[i + 1]));
                    i++;
                break;
                default:
                    throw new Exception(argument_format_error);
            }
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
