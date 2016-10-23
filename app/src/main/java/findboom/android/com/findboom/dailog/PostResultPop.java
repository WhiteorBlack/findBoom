package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import findboom.android.com.findboom.R;


/**
 * 请求结果弹窗
 * 
 * @author Administrator
 *
 */
public class PostResultPop extends PopupWindow {

	private Context context;
	private TextView txtResult, txtInfo;
	private ImageView imgResult;
	private int iconId;
	private String result, info;
	private View view;
	private View parentView;

	/**
	 * 
	 * @param context
	 * @param iconId
	 *            如果结果中没有icon传-1
	 * @param result
	 *            如果不提示请求信息则传空
	 * @param info
	 */
	public PostResultPop(Context context, View parent, int iconId, String result, String info) {
		super();
		this.context = context;
		this.iconId = iconId;
		this.result = result;
		this.info = info;
		this.parentView = parent;
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.post_result_pop, null);
			view.getBackground().setAlpha(180);
			txtResult = (TextView) view.findViewById(R.id.txt_result);
			txtInfo = (TextView) view.findViewById(R.id.txt_result_info);
			imgResult = (ImageView) view.findViewById(R.id.img_notify_icon);
		}
		if (iconId == -1) {
			imgResult.setVisibility(View.GONE);
		} else {
			imgResult.setVisibility(View.VISIBLE);
			imgResult.setBackgroundResource(iconId);
		}

		if (TextUtils.isEmpty(result)) {
			txtResult.setVisibility(View.GONE);
		} else {
			txtResult.setVisibility(View.VISIBLE);
			txtResult.setText(result);
		}

		if (TextUtils.isEmpty(info)) {
			txtInfo.setVisibility(View.GONE);
		} else {
			txtInfo.setVisibility(View.VISIBLE);
			txtInfo.setText(info);
		}

	}

	public void showPop() {
		this.setContentView(view);
		this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(false);
		this.setAnimationStyle(R.style.goodsPopStyle);
		this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
		new CountDownTimer(2000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				dismiss();
			}
		}.start();
	}
}
