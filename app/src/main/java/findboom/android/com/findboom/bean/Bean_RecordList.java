package findboom.android.com.findboom.bean;/**
 * Created by Administrator on 2016/10/6.
 */

import java.util.List;

/**
 * author:${白曌勇} on 2016/10/6
 * TODO:
 */
public class Bean_RecordList extends BaseBean {
    public List<RecordList> Data;
    public static class RecordList{
        public String UserScoreId;
        public String UserId;
        public int ScoreValue;
        public String GetTime;
        public String SourceType;
        public String SourceId;
        public String Remark;
    }
}
