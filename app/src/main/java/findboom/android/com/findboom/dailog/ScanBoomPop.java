package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.widget.RadarView;

/**
 * Created by Administrator on 2016/11/14.
 */

public class ScanBoomPop extends BasePopupwind {
    private View view;
    private RadarView radarView;

    public ScanBoomPop(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.scan_pop, null);
        radarView = (RadarView) view.findViewById(R.id.radarView);
        this.setContentView(view);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void showPop(View parent) {
        super.showPop(parent);
        radarView.setSearching(true);
    }
}
