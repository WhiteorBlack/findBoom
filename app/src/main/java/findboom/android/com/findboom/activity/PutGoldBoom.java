package findboom.android.com.findboom.activity;/**
 * Created by Administrator on 2016/10/22.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.BaseBean;
import findboom.android.com.findboom.bean.Bean_AllConfig;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.dailog.BandPhonePop;
import findboom.android.com.findboom.dailog.ChangePayPwdPop;
import findboom.android.com.findboom.dailog.CheckPhoto;
import findboom.android.com.findboom.dailog.ConfrimPwdPop;
import findboom.android.com.findboom.dailog.PostResultPop;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.interfacer.PopInterfacer;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import okhttp3.Call;
import findboom.android.com.findboom.R;

/**
 * author:${白曌勇} on 2016/10/22
 * TODO:寻宝雷
 */
public class PutGoldBoom extends TakePhotoActivity implements PopInterfacer {
    private ImageView takePhoto;
    private EditText edtCount, edtRemark;
    private TextView txtType, txtIntro, txtDistance, txtTime, txtReduceRecord, txtAddRecord;
    private int type = 3;
    private Context context;
    private String configString;
    private Bean_AllConfig.GoldBoom redBoom;
    private int rang = 50;
    private String picPath;
    private Bundle bundle;
    private CheckPhoto checkPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.put_gold_boom_pop);
        context = this;
        initView();
        initData();
    }

    private Bean_AllConfig bean_config;

    private void initData() {
        bundle = getIntent().getBundleExtra("data");
        configString = bundle.getString("config");
        if (!TextUtils.isEmpty(configString)) {
            bean_config = new Gson().fromJson(configString, Bean_AllConfig.class);
            redBoom = bean_config.Data.GoldMineConfig;
            rang = redBoom.CanRecRange;
            txtDistance.setText(redBoom.VisibleRange + "m");
            txtReduceRecord.setText(rang + "m");
        } else getAllConfig();
    }

    /**
     * 获取所有军火配置信息
     */
    private void getAllConfig() {
        PostTools.getData(context, CommonUntilities.CONFIG_URL + "GetArmsConfig", new HashMap<String, String>(), new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                configString = response;
                initData();
            }
        });
    }

    private void initView() {
        takePhoto = (ImageView) findViewById(R.id.img_take_photo);
        txtAddRecord = (TextView) findViewById(R.id.txt_add_record);
        txtDistance = (TextView) findViewById(R.id.txt_boom_distance);
        txtIntro = (TextView) findViewById(R.id.txt_boom_intro);
        txtReduceRecord = (TextView) findViewById(R.id.txt_reduce_record);
        txtTime = (TextView) findViewById(R.id.txt_boom_time);
        txtType = (TextView) findViewById(R.id.txt_type);
        edtCount = (EditText) findViewById(R.id.edt_count);
        edtRemark = (EditText) findViewById(R.id.edt_remark);
    }

    String remark = "", count = "0";

    public void boomClick(View v) {
        FindBoomApplication.getInstance().playClickSound();
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.btn_confirm:
                remark = edtRemark.getText().toString();
                count = edtCount.getText().toString();
                if (TextUtils.isEmpty(count)) {
                    Tools.toastMsg(context, "请输入优惠个数");
                    return;
                }
                putGoldBoom();
                break;
            case R.id.img_take_photo:
                if (TextUtils.isEmpty(picPath))
                    takePhoto();
                else {
                    if (checkPhoto == null)
                        checkPhoto = new CheckPhoto(context);
                    checkPhoto.setPhoto(picPath);
                    checkPhoto.setPopInterfacer(this, 0);
                    checkPhoto.showPop(txtAddRecord);
                }
                break;

        }
    }

    private void takePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), "/findBoom/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        final Uri imageUri = Uri.fromFile(file);
        CharSequence[] items = {"手机相册", "手机拍照"};
        final TakePhoto takePhoto = getTakePhoto();
//        final CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
        new AlertDialog.Builder(this).setTitle("选择照片").setCancelable(true).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        takePhoto.onPickFromGallery();
                        break;
                    case 1:
                        takePhoto.onPickFromCapture(imageUri);
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();

    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        picPath = result.getImage().getPath();
        Glide.with(context).load(picPath).into(takePhoto);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    /**
     * 放置红包雷
     */
    private void putGoldBoom() {
        Map<String, String> params = new HashMap<>();
        params.put("Provice", bundle.getString("pro"));
        params.put("City", bundle.getString("city"));
        params.put("Area", bundle.getString("area"));
        params.put("Street", bundle.getString("street"));
        params.put("Longitude", bundle.getString("lng"));
        params.put("Latitude", bundle.getString("lat"));
        params.put("MineType", "4");
        params.put("Remark", remark);
        params.put("PicUrl", Tools.convertIconToString(picPath));
        params.put("Count", count);
        params.put("PicTitle", "寻宝雷");
        PostTools.postData(context, CommonUntilities.MINE_URL + "AddGoldMine", params, new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (TextUtils.isEmpty(response))
                    return;
                BaseBean baseBean = new Gson().fromJson(response, BaseBean.class);
                if (baseBean != null && baseBean.Success) {
                    setResult(RESULT_OK, new Intent().putExtra("success", true));
                    finish();
                } else
                    new PostResultPop(context, txtAddRecord, R.drawable.icon_error, baseBean.Msg, "").showPop();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                Tools.debug(e.toString());
            }
        });
    }

    @Override
    public void OnDismiss(int flag) {

    }

    @Override
    public void OnConfirm(int flag, Bundle bundle) {

    }

    @Override
    public void OnCancle(int flag) {
        picPath = null;
        Glide.with(context).load(R.mipmap.icon_take_photo).into(takePhoto);
    }
}
