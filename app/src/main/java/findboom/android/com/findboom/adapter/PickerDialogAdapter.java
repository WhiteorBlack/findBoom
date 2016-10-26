package findboom.android.com.findboom.adapter;

import com.qtcem.yexiu.pickview.adapter.WheelAdapter;

import android.text.TextUtils;

public class PickerDialogAdapter implements WheelAdapter<String> {

	private String[] datas;

	public PickerDialogAdapter(String[] datas) {
		super();
		this.datas = datas;
	}

	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return datas.length;
	}

	@Override
	public String getItem(int index) {
		// TODO Auto-generated method stub
		return datas[index];
	}

	@Override
	public int indexOf(String o) {
		// TODO Auto-generated method stub
		int index = 0;
		for (int i = 0; i < datas.length; i++) {
			if (TextUtils.equals(datas[i], o)) {
				index = i;
				break;
			}
		}
		return index;
	}

}
