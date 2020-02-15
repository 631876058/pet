import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 这里就简单写一下客户端算了，
 * 其实它可以和server公用一些设计de的
 *
 * @author 郭豪豪
 * @date 2020-02-14
 **/
public class PetClient {

    private PrintWriter printWriter;

    private boolean shutdown = false;

    public Socket socket = null;



    public static void main(String[] args) throws IOException {
        PetClient petClient = new PetClient();
        petClient.startClient();
    }

    public synchronized void startClient() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 6666));
        socket.setTcpNoDelay(true);
        OutputStream outputStream = socket.getOutputStream();
        printWriter = new PrintWriter(outputStream);

        new Thread(() -> {
            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String data = null;
                while ((data = bufferedReader.readLine()) != null){
                    System.out.println(data);
                }
                socket.close();
            } catch (Exception ex) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ex.printStackTrace();
            }
        },"receiveServerDataThread").start();
    }

    public void sendMsg2Server(String data) {
        printWriter.println(data);
        printWriter.flush();
    }
}
