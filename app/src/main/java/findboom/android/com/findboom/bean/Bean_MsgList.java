package findboom.android.com.findboom.bean;/**
 * Created by Administrator on 2016/10/6.
 */

import java.util.List;

/**
 * author:${白曌勇} on 2016/10/6
 * TODO:
 */
public class Bean_MsgList extends BaseBean {
    public List<MsgList> Data;
    public static class MsgList{
        public String UserMsgId;
        public String UserId;
        public String MsgContent;
        public String HasRead;
        public String SendTime;
    }
}
