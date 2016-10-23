package findboom.android.com.findboom.bean;/**
 * Created by Administrator on 2016/9/20.
 */

/**
 * author:${白曌勇} on 2016/9/20
 * TODO:
 */
public class Bean_UserInfo {
    public boolean Success;
    public String Msg;
    public String ExceptionMsg;
    public GameUser Data;

    public static class GameUser {
        public String GameUserId;
        public String PhoneNumber; //手机号;
        public String OpenId;
        public String PassWord;
        public String NickName;
        public String Avatar;
        public String BirthDay;
        public String Province;
        public String City;
        public String Age;
        public String Area;
        public String Profession;
        public String UserProfile;
        public String PhoneBrand;
        public String UserLevel;
        public String UserScore;
        public String UserBalance;
        public String RedPackBalance;
        public String ReferUserId;
        public String ReferUserCount;
        public boolean IsAdminUser;
        public String Token;
        public String TokenExpireTime;
        public String CreateTime;
        public String BindTime;
        public String EasemobId;
        public String EasemobPwd;
        public String RegistrationId;
        public String UserSource;
        public String LastGiveTime;
        public String Status;
        public String PayPassWord;
        public String StatusTxt;
    }
}
