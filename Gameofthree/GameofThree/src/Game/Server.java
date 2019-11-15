package Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {


    public static void main(String[] args) throws Exception {
        try (ServerSocket listener = new ServerSocket(1342)) {
            System.out.println("Server starts...");
            ExecutorService pool = Executors.newFixedThreadPool(100);
            while (true) {
                pool.execute(new FirstPlayer(listener.accept()));
            }
        }
    }

    private static class FirstPlayer implements Runnable {
        private Socket socket;

        FirstPlayer(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try {
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                int numToAdd = 0;
                while (true) {
                    try {
                        String dataFromClient = in.nextLine();
                        if (dataFromClient.contains("WINS")) {
                            //System.out.println("CLIENT PLAYER WINS");
                            break;
                        }

                        int clientNumber = Integer.parseInt(dataFromClient);
                        int mod = clientNumber % 3;

                        if(mod == 0) {
                            numToAdd = 0;
                        }
                        if(mod == 1) {
                            numToAdd = -1;
                        }
                        if(mod == 2) {
                            numToAdd = 1;
                        }

                        int dividedByThree = (clientNumber + numToAdd) / 3;
                        if (dividedByThree == 1) {
                            System.out.println("Server player WINS !!!!");
                            out.println("Server player WINS");
                            break;
                        } else {
                            System.out.println("Server sends to client"); 
                            out.println(dividedByThree);
                            out.println(numToAdd);
                            out.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                System.out.println("Error:" + socket);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}


