package findboom.android.com.findboom.bean;/**
 * Created by Administrator on 2016/9/27.
 */

import java.util.List;

/**
 * author:${白曌勇} on 2016/9/27
 * TODO:
 */
public class Bean_UserArm extends BaseBean{
    public List<UserArm> Data;
    public static class UserArm{
        public String UserArmsId;
        public String UserId;
        public String Remark;
        public int Count;
        public String ArmTypeTxt;
        public int ArmType;
    }
}
