package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.PickerDialogAdapter;
import findboom.android.com.findboom.widget.pickview.lib.WheelView;
import findboom.android.com.findboom.widget.pickview.listener.OnItemSelectedListener;


public class PickerCityDialog extends PopupWindow {

    private List<String> datas;
    private Context context;
    private View parent, view;
    private WheelView wheelView;
    private WheelView wheelCity;
    private OnSelectItem onSelectItem;

    public PickerCityDialog(Context context, List<String> datas, View parent) {
        super();
        this.datas = datas;
        this.context = context;
        this.parent = parent;
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.picker_dialog, null);
            wheelView = (WheelView) view.findViewById(R.id.wheel_view_province);
            wheelCity = (WheelView) view.findViewById(R.id.wheel_view_city);
        }
        wheelView.setAdapter(new PickerDialogAdapter(datas));
        wheelView.setCyclic(false);
        wheelView.setTextSize(16);
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                // TODO Auto-generated method stub
                if (onSelectItem != null) {
                    onSelectItem.onItemSelect(datas.get(index));
                }
            }
        });

        this.setContentView(view);
    }

    public void showPop() {
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    public void setCurrentPos(int pos) {
        if (wheelView != null) {
            wheelView.setCurrentItem(pos);
        }
    }

    public OnSelectItem getOnSelectItem() {
        return onSelectItem;
    }

    public void setOnSelectItem(OnSelectItem onSelectItem) {
        this.onSelectItem = onSelectItem;
    }

    public interface OnSelectItem {
        void onItemSelect(String selectString);
    }
}
