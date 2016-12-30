package findboom.android.com.findboom.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */

public class Bean_GoldList extends BaseBean {
    public List<GoldList> Data;
    public static class GoldList{
        public String Id;
        public String ConfigId;
        public String GoldId;
        public int GoldAmount;
        public float Price;
        public String Status;
        public String StatusTxt;
    }
}
