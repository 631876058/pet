import cn.test.spm.PetManageServer;
import org.junit.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import static java.lang.Thread.sleep;

/**
 * 测试服务端
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class PetManagerServerTest {

    @Test
    public void testPetManagerTest() throws IOException {
        PetManageServer.main(null);
    }

    @Test
    public void testConnectPetServer() throws IOException, InterruptedException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1",6666));
//        socket.setTcpNoDelay(true);
//        for (int i = 0; i < 100 ; i++) {
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println("GET:dog");
//        printWriter.flush();
        printWriter.println("LIST");
        //将内存的数据，冲刷到TCP缓存通道
        printWriter.flush();
//        }
        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String s = null;
        while((s = reader.readLine()) != null){
            System.out.println(s);
        }
    }

}
