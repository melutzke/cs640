public class IperferConfig {
    boolean is_server;
    int port;
    int time;
    String host;
    String error;

    //constructor.  I hope I'm doing this right.
    public IperferConfig(boolean is_server, String host, int port, int time) {
        this.is_server = is_server;
        this.host = host;
        this.port = port;
        this.time = time;
    }

    public IperferConfig() {
        this.is_server = false;
        this.host = "";
        this.port = -1;
        this.time = -1;
    }

    public void set_port(int port) {
        this.port = port;
    }

    public void set_time(int time) {
        this.time = time;
    }

    public void set_host(String host) {
        this.host = host;
    }

    public void set_mode(String mode) {
        String newmode = mode.toLowerCase();
        switch (newmode) {
            case "c":
            case "-c":
            case "client":
            case "--client":
                this.is_server = false;
                break;
            case "s":
            case "-s":
            case "server":
            case "--server":
                this.is_server = true;
                break;
            default:
                this.error = "FUCK.";
        }
    }
}
