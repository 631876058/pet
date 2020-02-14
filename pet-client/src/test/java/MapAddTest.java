import com.sun.org.apache.xalan.internal.res.XSLTErrorResources;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author 郭豪豪
 * @date 2020-02-14
 **/
public class MapAddTest implements Runnable {

    private static Map<String,Integer> map = new HashMap<>();

    private static CountDownLatch latch = new CountDownLatch(100);

    public static void main(String args[]) throws InterruptedException {
        for (int i = 0; i < 100 ; i++) {
            Thread thread = new Thread(new MapAddTest());
            thread.start();
        }
        latch.await();
        map.forEach((k,v)->{
            System.out.println(k+":"+v);
        });
    }

    @Override
    public void run() {
        String[] strList = {"a","b","c"};
        for(int i = 0 ; i < 1000 ; i++){
            for(String str : strList){
                Thread.yield();
                synchronized (str){
                    Integer integer = map.get(str);
                    Thread.yield();
                    map.put(str,integer == null ? 1 : integer+1);
                }
            }
        }
        latch.countDown();
    }
}
