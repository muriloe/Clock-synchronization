package timeserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class TimeServer {


    public static void main(String[] args) throws IOException {
         ServerSocket server = new ServerSocket(12345);
         
         while (true) {
            Socket cliente = server.accept();
            System.out.println("---------------------Novo cliente - >localaddr:" + cliente.getLocalAddress() + " inet" + cliente.getInetAddress());

            Thread thread = new Thread(new Listener(cliente));
            thread.start();
        }
         
    }
    
}
