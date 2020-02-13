package cn.test.spm.db;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 宠物数据存储
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class PetDataBase {

    /**
     * 用于存储宠物数据，注意多线程下的并发问题
     */
    public static final Map<String, AtomicInteger> petData = new ConcurrentHashMap<>(16);

}
