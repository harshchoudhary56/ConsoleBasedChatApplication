import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultipleRequestServer {
   private ServerSocket serverSocket;
   
   public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        while(true) {
            new ClientHandler(serverSocket.accept()).start();
        }
   }
   public void stop() throws Exception{
        serverSocket.close();
   }

   private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in, console;
        Thread t1, t2;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
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
                        if(response != null) System.out.println("Client: " + response);
                        if(response != null && response.equals("exit")) {
                            System.out.println("Client terminated the chat");
                            System.exit(1);
                            // break;
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
        public void run() {
            startReading();
            startWriting();
        }
        
   }
   public static void main(String[] args) throws Exception {
        MultipleRequestServer mrs = new MultipleRequestServer();
        mrs.start(6666);
   }
}
