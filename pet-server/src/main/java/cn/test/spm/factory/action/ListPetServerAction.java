package cn.test.spm.factory.action;

import cn.test.spm.db.PetDataBase;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 宠物列表处理器
 *
 * @author 郭豪豪
 * @date 2020-02-13
 **/
public class ListPetServerAction extends AbstractPetServerAction {

    public ListPetServerAction(String socketId, Socket socket, String data) {
        super(socketId, socket, data);
    }

    @Override
    protected void action(Socket socket, String data) {
        String sort = sort();
        sendMsg2Client(sort+"OK");
    }

    /**
     * 需要将此时的数据保存一份快照
     *
     * @return
     */
    public String sort() {
        List<DataItem> dataBak = copy(PetDataBase.petData);
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
