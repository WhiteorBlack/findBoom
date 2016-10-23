package findboom.android.com.findboom.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class Bean_MyBoomRecord extends BaseBean {
    public List<BoomInfo> Data;

    public static class BoomInfo{
        public String MineRecordId;
        public String MineUserNickName;
        public int MineType;
        public String MineTypeTxt;
        public String StatusTxt;
        public String Address;
        public String CreateTime;
    }
}
