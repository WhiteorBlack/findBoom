package findboom.android.com.findboom.bean;/**
 * Created by Administrator on 2016/9/23.
 */

import java.util.List;

/**
 * author:${白曌勇} on 2016/9/23
 * TODO:
 */
public class Bean_ShopList {
    public String Msg;
    public String ExceptionMsg;
    public boolean Success;
    public List<GoodsInfo> Data;

    public static class GoodsInfo {
        public String ArmTypeTxt;
        public float Price;
        public String Remark;
        public int ArmType;
        public String ArmInfoId;
    }
}
