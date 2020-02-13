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
        OutputStream outputStream = socket.getOutputStream();
        printWriter = new PrintWriter(outputStream);

        new Thread(() -> {
            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                //在2048个字符内，必须出现OK，否则就出事故了
                char[] buff = new char[2048];
                int size = 0;
                StringBuffer sb = new StringBuffer();
                //这里简单用OK拆个包吧
                while(!shutdown) {
                    while ((size = reader.read(buff)) > 0) {
                        for (int i = 0; i < size; i++) {
                            //出现OK字样后，将sb里面整体拿出，作为一个业务数据，并创建新的sb来接受另外的数据
                            if (buff[i] == 'O' && buff[i + 1] == 'K') {
                                sb.append("OK");
                                String data = sb.toString();
                                System.out.println(data);
                                sb = new StringBuffer();
                            }
                            sb.append(buff[i]);
                        }
                        //处理完buff之后，重新创建一个buff
                        buff = new char[2048];
                    }
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
