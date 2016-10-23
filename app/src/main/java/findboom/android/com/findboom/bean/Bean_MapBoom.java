package findboom.android.com.findboom.bean;/**
 * Created by Administrator on 2016/10/6.
 */

import java.util.List;

/**
 * author:${白曌勇} on 2016/10/6
 * TODO:
 */
public class Bean_MapBoom extends BaseBean {
   public List<MapBoom> Data;

    public static class MapBoom {
        public String MineRecordId;
        public String UserId;
        public double Longitude;
        public double Latitude;
        public String ExpireTime;
        public int BombRange;
        public int MineType;
    }
}
