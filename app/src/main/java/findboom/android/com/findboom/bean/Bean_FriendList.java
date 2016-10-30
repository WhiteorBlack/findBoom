package findboom.android.com.findboom.bean;/**
 * Created by Administrator on 2016/9/23.
 */

import java.util.List;

/**
 * author:${白曌勇} on 2016/9/23
 * TODO:
 */
public class Bean_FriendList {
    public String Msg;
    public String ExceptionMsg;
    public boolean Success;
    public List<Friend> Data;
    public static class Friend{
        public String UserId;
        public String FriendId;
        public String FriendNickName;
        public String FriendLevel;
        public String Avatar;
        public String EasemobId;
    }
}
