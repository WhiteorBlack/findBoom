package findboom.android.com.findboom.bean;/**
 * Created by Administrator on 2016/10/22.
 */

import java.util.List;

/**
 * author:${白曌勇} on 2016/10/22
 * TODO:
 */
public class Bean_AllConfig extends BaseBean {
    public AllConfig Data;

    public static class AllConfig {
        public CommonBoom CommonMineConfig;
        public TextBoom TextMineConfig;
        public PicBoom PicMineConfig;
        public RedBoom RedPackMineConfig;
        public GoldBoom GoldMineConfig;
        public Scan MineCleanerConfig;
    }

    public static class Scan {
        public int CleanRange;
        public String MineType;
    }

    public static class GoldBoom {
        public int MaxCount;
        public String ValidDays;
        public String MineType;
        public int BombRange;
    }

    public static class RedBoom {
        public int MaxCount;
        public int MinAmount;
        public int MaxAmount;
        public int BombRange;
        public int VisibleRange;
        public int CanRecRange;
        public String ArmDesc;
        public String ValidDays;
        public String MineType;
    }

    public static class PicBoom {
        public int PlusScore;
        public int MinusScore;
        public int BombRange;
        public String ValidDays;
        public String MineType;
        public String ArmDesc;
        public List<PicInfo> MinePics;
    }
    public static class PicInfo{
        public String PicUrl;
        public String PicTitle;
    }

    public static class TextBoom {
        public int PlusScore;
        public int MinusScore;
        public int BombRange;
        public String ValidDays;
        public String ArmDesc;
        public String MineType;
        public String[] Texts;
    }

    public static class CommonBoom {
        public int PlusScore;
        public int MinusScore;
        public int BombRange;
        public String ValidDays;
        public String MineType;
        public String ArmDesc;
    }
}
