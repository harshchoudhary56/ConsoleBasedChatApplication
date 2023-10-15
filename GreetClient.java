import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GreetClient {
    private Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in, console;
    Thread t1, t2;

    public void startReading() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runnable r1 = () -> {
            try {
                while(true) {
                    String response = in.readLine();
                    if(response != null) System.out.println("Server: " + response);
                    if(response != null && response.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        System.exit(1);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        t1 = new Thread(r1); t1.start();
    }

    public void startWriting() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            console = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Runnable r2 = () -> {
            try {
                while(true) {
                    // System.out.print("Me: ");
                    String message = console.readLine();
                    out.println(message);
                    if(message != null && message.equals("exit"))
                        System.exit(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        t2 = new Thread(r2); t2.start();
    }

    public void startConnection(String ip, int port) throws Exception {
        clientSocket = new Socket(ip, port);
        startReading();
        startWriting();
        // out.println("Hello");
        
    }

    public void stopConnection() throws Exception {
        in.close();
        out.close();
        clientSocket.close();
    }
    public static void main(String[] args) throws Exception {
        GreetClient greetClient = new GreetClient();
        greetClient.startConnection("localhost", 6666);
        greetClient.stopConnection();
    }
}
