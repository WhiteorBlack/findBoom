package findboom.android.com.findboom.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.activity.FindBoomDetial;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_BoomDetial;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;

/**
 * Created by Administrator on 2016/9/30.
 */
public class NormalBoomFragment extends Fragment {
    private View view;
    private TextView txtRemark, txtInfo;
    private ImageView imgPhoto;
    private String boomId;
    private boolean isMine = true;
    private int type;

    public void setInfo(String id, int type, boolean isMine) {
        this.isMine = isMine;
        this.boomId = id;
        this.type = type;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getBoomInfo();
    }

    private Bean_BoomDetial bean_BoomDetial;

    private void getBoomInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("mineRecordId", boomId);
        PostTools.getData(getContext(), CommonUntilities.USER_URL + "GetMineInfo", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                bean_BoomDetial = new Gson().fromJson(response, Bean_BoomDetial.class);
                if (bean_BoomDetial != null && bean_BoomDetial.Success) {
                    setData();
                }
            }
        });
    }

    private void setData() {
        ((FindBoomDetial) getActivity()).setState(bean_BoomDetial.Data.BombTypeTxt, bean_BoomDetial.Data.StatusTxt);
        txtRemark.setText(TextUtils.isEmpty(bean_BoomDetial.Data.PicUrl) ? bean_BoomDetial.Data.Text : bean_BoomDetial.Data.PicTitle);
        txtInfo.setText("");
        txtInfo.append(bean_BoomDetial.Data.BombTime);
        if (isMine) {

            txtInfo.append(Tools.getSpanString(getContext(), bean_BoomDetial.Data.MineUserNickName, R.color.content_yellow));
            txtInfo.append(" 在" + Tools.getSpanString(getContext(), bean_BoomDetial.Data.Address, R.color.content_yellow) + " 踩到了我埋的雷,");
            if (bean_BoomDetial.Data.IsHaveBombSuit) {
                txtInfo.append("但是被他的防弹衣成功防御,不加积分。");
            } else
                txtInfo.append("减少对方 " + Tools.getSpanString(getContext(), bean_BoomDetial.Data.MinusScore + "积分", R.color.content_yellow) + "为我增加" +
                        Tools.getSpanString(getContext(), bean_BoomDetial.Data.PlusScore + "积分", R.color.content_yellow) + "。");
        } else {
            txtInfo.append(" 我在" + Tools.getSpanString(getContext(), bean_BoomDetial.Data.Address, R.color.content_yellow) + " 踩到了" +
                    Tools.getSpanString(getContext(), bean_BoomDetial.Data.MineUserNickName, R.color.content_yellow) + " 埋的雷");
            if (bean_BoomDetial.Data.IsHaveBombSuit) {
                txtInfo.append(",但是被防弹衣成功防御。");
            } else
                txtInfo.append("减少我 " + Tools.getSpanString(getContext(), bean_BoomDetial.Data.MinusScore + "积分", R.color.content_yellow) + "为对方增加" +
                        Tools.getSpanString(getContext(), bean_BoomDetial.Data.PlusScore + "积分", R.color.content_yellow) + "。");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.normal_boom_fragment, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        txtInfo = (TextView) view.findViewById(R.id.txt_boom_info);
        txtRemark = (TextView) view.findViewById(R.id.txt_boom_text);
        imgPhoto = (ImageView) view.findViewById(R.id.img_photo);
    }
}
