import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by massivCode on 2015-12-26.
 */
public class Server {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(7777);

            while(true) {
                Socket socket = serverSocket.accept();
                Thread thread = new ServerThread(socket);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
