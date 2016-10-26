package findboom.android.com.findboom.dailog;

import com.qtcem.yexiu.R;
import com.qtcem.yexiu.adapter.PickerDialogAdapter;
import com.qtcem.yexiu.pickview.lib.WheelView;
import com.qtcem.yexiu.pickview.listener.OnItemSelectedListener;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public class PickerDialog extends PopupWindow {

	private String[] datas;
	private Context context;
	private View parent, view;
	private WheelView wheelView;
	private OnSelectItem onSelectItem;

	public PickerDialog(Context context, String[] datas, View parent) {
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
			wheelView = (WheelView) view.findViewById(R.id.wheel_view);
		}
		wheelView.setAdapter(new PickerDialogAdapter(datas));
		wheelView.setCyclic(false);
		wheelView.setTextSize(16);
		wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				// TODO Auto-generated method stub

				if (onSelectItem != null) {
					if (index == 0) {
						onSelectItem.onItemSelect("");
						return;
					}
					onSelectItem.onItemSelect(datas[index]);
				}
			}
		});
	}

	public void showPop() {
		this.setContentView(view);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.white)));
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
