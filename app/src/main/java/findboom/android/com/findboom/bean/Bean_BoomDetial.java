package findboom.android.com.findboom.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class Bean_BoomDetial extends BaseBean {
    public BoomDetial Data;


    public static class BoomDetial {
        public String MineRecordId;
        public String UserId;
        public String Provice;
        public String City;
        public String Area;
        public String Street;
        public String Address;
        public String Longitude;
        public String Latitude;
        public String MineType;
        public String MineTypeTxt;
        public String CreateTime;
        public String ExpireTime;
        public String Remark;
        public int Status; //状态 0未触发 1已触发 -1已删除
        public String PlusScore; //炸人增加积分数
        public String MinusScore;  //被炸减少积分数
        public String BombTime;
        public String BombUserId;
        public int BombType;  //触发类型 0：被动触发 1：扫雷器触发
        public String Text;
        public String PicUrl;
        public String MineUserNickName;
        public String PicTitle;
        public boolean IsHaveBombSuit;
        public int BombRange;
        public String BombTypeTxt;  //触发类型 0：被动触发 1：扫雷器触发
        public String StatusTxt;  //状态 0未触发 1已触发 -1已删除
        public String UserNickName;
        public String BombUserNickName;
        public String RedPackText;
        public int Count;
        public int LeftCount;
        public String TotalAmount;
        public List<RedRecord> RedPackReciveRecords;
        public List<GoldRecord> GoldReciveRecords;
    }
}
