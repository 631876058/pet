package cn.test.spm.db;

/**
 * 操作宠物列表
 *
 * @author 郭豪豪
 * @date 2020-02-15
 **/
public interface PetService {

    /**
     * 用户来领取宠物
     *
     * @param pet
     * @return
     */
    String getPet(String pet);


    /**
     * 获取宠物领养排行榜
     *
     * @return
     */
    String getRankList();
}
