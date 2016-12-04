package findboom.android.com.findboom.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */

public class Bean_InviteList extends BaseBean {
    public List<InviteList> Data;

    public static class InviteList {
        public String FriendApplyId;
        public String FromUserId;
        public String FromUserINickName;
        public String ToUserId;
        public String ApplyMsg;
        public String SendTime;
        public String DealTime;
        public String DealResult;
        public String DealResultTxt;
        public String Avatar;
    }
}
