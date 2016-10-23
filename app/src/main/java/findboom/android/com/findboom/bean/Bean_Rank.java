package findboom.android.com.findboom.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class Bean_Rank extends BaseBean {
    public List<RankUser> Data;

    public static class RankUser{

        public String BombRankId;
        public String UserId;
        public String UserNickName;
        public String RankDate;
        public String BombCount;
        public String Avatar;
    }
}
