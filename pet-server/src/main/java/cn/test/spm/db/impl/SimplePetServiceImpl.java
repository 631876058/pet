package cn.test.spm.db.impl;

import cn.test.spm.db.PetService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单的基于内存的宠物业务实现
 *
 * @author 郭豪豪
 * @date 2020-02-15
 **/
public class SimplePetServiceImpl implements PetService {
    /**
     * 用于存储宠物数据，注意多线程下的并发问题
     */
    public static final Map<String, AtomicInteger> petData = new ConcurrentHashMap<>();

    @Override
    public String getPet(String pet) {
        //用double check 来实现一下
        AtomicInteger adder = petData.get(pet);

        if (adder == null) {
            synchronized (petData) {
                adder = petData.get(pet);
                if (adder == null) {
                    adder = new AtomicInteger(0);
                    petData.put(pet, adder);
                }
            }
        }
        adder.incrementAndGet();
        return "OK";
    }

    @Override
    public String getRankList() {
        List<DataItem> dataBak = copy(petData);
        StringBuffer sb = new StringBuffer();

        //简单实现选择排序吧
        for (int i = 0; i < dataBak.size(); i++) {
            DataItem maxItem = dataBak.get(i);
            for (int j = i + 1; j < dataBak.size(); j++) {
                DataItem next = dataBak.get(j);
                if (maxItem.getNum() < next.getNum()) {
                    dataBak.set(i, next);
                    dataBak.set(j, maxItem);
                    maxItem = next;
                }
            }
            sb.append(maxItem.toString());
        }
        return sb.toString();
    }


    public List<DataItem> copy(Map<String, AtomicInteger> preData) {
        List<DataItem> dataItemList = new ArrayList<>();
        preData.forEach((k, v) -> {
            DataItem dataItem = new DataItem(k, v.intValue());
            dataItemList.add(dataItem);
        });
        return dataItemList;
    }

    public static class DataItem {

        public DataItem(String key, Integer num) {
            this.key = key;
            this.num = num;
        }

        private String key;
        private Integer num;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        @Override
        public String toString() {
            return key + ":" + num + "\n";
        }
    }
}
