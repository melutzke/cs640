// Screw you, javadocs
// My documentation is where it belongs
//
//
//
//
//


public class Iperfer {
    static void parse_args(String[] args, IperferConfig config) throws Exception {
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
                if (args.length != 7) {
                    //throw new ArgumentNumberException();
                    throw new Exception(argument_number_error);
                }
                //TODO: the repeated code here should be condensed
                switch (args[1]) {
                    case "-h":
                        config.set_host(args[2]);
                    break;
                    case "-p":
                        config.set_port(Integer.parseInt(args[2]));
                    break;
                    case "-t":
                        config.set_time(Integer.parseInt(args[2]));
                    break;
                    default:
                        throw new Exception(argument_format_error);
                }
                switch (args[3]) {
                    case "-h":
                        config.set_host(args[4]);
                    break;
                    case "-p":
                        config.set_port(Integer.parseInt(args[4]));
                    break;
                    case "-t":
                        config.set_time(Integer.parseInt(args[4]));
                    break;
                    default:
                        throw new Exception(argument_format_error);
                }
                switch (args[5]) {
                    case "-h":
                        config.set_host(args[6]);
                    break;
                    case "-p":
                        config.set_port(Integer.parseInt(args[6]));
                    break;
                    case "-t":
                        config.set_time(Integer.parseInt(args[6]));
                    break;
                    default:
                        throw new Exception(argument_format_error);
                }
            break;
            case "-s":
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
        IperferConfig config = new IperferConfig();
        //parse args and check for errors
        try {
            parse_args(args, config);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        return;
    }
}
