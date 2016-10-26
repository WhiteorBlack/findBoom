package findboom.android.com.findboom.adapter;

import android.text.TextUtils;

import java.util.List;

import findboom.android.com.findboom.widget.pickview.adapter.WheelAdapter;


public class PickerDialogAdapter implements WheelAdapter<String> {

	private List<String> datas;

	public PickerDialogAdapter(List<String> datas) {
		super();
		this.datas = datas;
	}

	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public String getItem(int index) {
		// TODO Auto-generated method stub
		return datas.get(index);
	}

	@Override
	public int indexOf(String o) {
		// TODO Auto-generated method stub
		int index = 0;
		for (int i = 0; i < datas.size(); i++) {
			if (TextUtils.equals(datas.get(i), o)) {
				index = i;
				break;
			}
		}
		return index;
	}

}
