package findboom.android.com.findboom.activity;/**
 * Created by Administrator on 2016/10/22.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.asytask.PostTools;
import findboom.android.com.findboom.bean.Bean_AllConfig;
import findboom.android.com.findboom.dailog.SelectBoomPic;
import findboom.android.com.findboom.dailog.SelectBoomText;
import findboom.android.com.findboom.dailog.SelectBoomType;
import findboom.android.com.findboom.interfacer.PopInterfacer;
import findboom.android.com.findboom.interfacer.PostCallBack;
import findboom.android.com.findboom.utils.CommonUntilities;
import findboom.android.com.findboom.utils.Tools;
import findboom.android.com.findboom.widget.gallery.ViewPaperAdapter;
import findboom.android.com.findboom.widget.gallery.ZoomOutPageTransformer;

import static android.R.attr.type;

/**
 * author:${白曌勇} on 2016/10/22
 * TODO:
 */
public class PutPicBoom extends Activity implements PopInterfacer {

    private ViewPager viewPager;
    private int[] picRes = new int[]{
            R.mipmap.boom_cry, R.mipmap.boom_bingo, R.mipmap.boom_hello, R.mipmap.boom_cry, R.mipmap.boom_laugh
    };

    private List<View> views;
    private String configString;
    private Bean_AllConfig bean_config;
    private Bean_AllConfig.PicBoom picBoom;
    private String picUrl;
    private int range=50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.put_pic_boom_pop);
        initView();
        initData();
    }

    private void initData() {
        int wide= (int) (Tools.getScreenWide(this)*0.3);
        int height= (int) (wide*2.1);
        views = new ArrayList<>();
        configString = getIntent().getStringExtra("config");
        if (!TextUtils.isEmpty(configString)) {
            bean_config = new Gson().fromJson(configString, Bean_AllConfig.class);
            picBoom = bean_config.Data.PicMineConfig;
            if (picBoom != null && picBoom.MinePics != null && picBoom.MinePics.size() > 0) {
                for (int i = 0; i < picBoom.MinePics.size(); i++) {
                    range=picBoom.BombRange;
                    View view = LayoutInflater.from(this).inflate(R.layout.imageview, null);
                    ImageView imgPic = (ImageView) view.findViewById(R.id.imageview);
                    LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) imgPic.getLayoutParams();
                    params.width=wide;
                    params.height=height;
                    params.setMargins((int)(wide*0.2),(int)(wide*0.1),(int)(wide*0.1),(int)(wide*0.1));
                    imgPic.setLayoutParams(params);
                    Glide.with(this).load(picBoom.MinePics.get(i).PicUrl).into(imgPic);
                    views.add(view);
                }
                viewPager.setAdapter(new ViewPaperAdapter(views));
            }
            viewPager.setCurrentItem(2);
        } else getAllConfig();

    }

    /**
     * 获取所有军火配置信息
     */
    private void getAllConfig() {
        PostTools.getData(this, CommonUntilities.CONFIG_URL + "GetArmsConfig", new HashMap<String, String>(), new PostCallBack() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                configString = response;
                initData();
            }
        });
    }


    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                picUrl=picBoom.MinePics.get(position).PicUrl;
            }
        });
        viewPager.setPageMargin((int) (-Tools.getScreenWide(this)*0.5));
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }


    public void boomClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.btn_confirm:
                Bundle bundle = new Bundle();
                bundle.putString("type",  "2");
                bundle.putString("imgUrl", picUrl);
                bundle.putString("imgInfo", "");
                bundle.putString("text", "");
                bundle.putInt("rang", range);
                setResult(RESULT_OK, new Intent().putExtra("data", bundle));
                finish();
                break;

        }
    }

    @Override
    public void OnDismiss(int flag) {

    }

    @Override
    public void OnConfirm(int flag, Bundle bundle) {

        switch (flag) {
        }


    }

    @Override
    public void OnCancle(int flag) {

    }

}
