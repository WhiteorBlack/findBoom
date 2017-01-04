package findboom.android.com.findboom.dailog;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.PickerDialogAdapter;
import findboom.android.com.findboom.bean.Bean_CityData;
import findboom.android.com.findboom.bean.DistrictModel;
import findboom.android.com.findboom.bean.ProvinceModel;
import findboom.android.com.findboom.utils.XmlParserHandler;
import findboom.android.com.findboom.widget.pickview.lib.WheelView;
import findboom.android.com.findboom.widget.pickview.listener.OnItemSelectedListener;


public class PickerCityDialog extends BasePopupwind {

    private List<String> datas;
    private Context context;
    private View parent, view;
    private WheelView wheelView;
    private WheelView wheelCity;
    private OnSelectItem onSelectItem;

    protected List<String> mProvinceDatas, cities; // 省级数据
    // key - 省 value - 市
    protected Map<String, List<String>> mCitisDatasMap = new HashMap<String, List<String>>();
    // key - 市 values - 区
    // 当前省的名称
    protected String mCurrentProviceName;
    // 当前市的名称
    protected String mCurrentCityName;


    public PickerCityDialog(Context context) {
        super(context);
        this.context = context;
        initView();
        initProvinceData();
    }

    private void initView() {
        // TODO Auto-generated method stub
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.picker_city_dialog, null);
            wheelView = (WheelView) view.findViewById(R.id.wheel_view_province);
            wheelCity = (WheelView) view.findViewById(R.id.wheel_view_city);
            view.findViewById(R.id.txt_cancle).setOnClickListener(this);
            view.findViewById(R.id.txt_confirm).setOnClickListener(this);
        }
        wheelView.setCyclic(false);
        wheelView.setTextSize(16);
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(int index) {
                // TODO Auto-generated method stub
                mCurrentProviceName = mProvinceDatas.get(index);
                upCity();
            }
        });

        wheelCity.setCyclic(false);
        wheelCity.setTextSize(16);
        wheelCity.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mCurrentCityName = cities.get(index);
            }
        });

        this.setContentView(view);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
    }

    @Override
    public void showPop(View parent) {
        this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_cancle:

                break;
            case R.id.txt_confirm:
                Bundle bundle = new Bundle();
                bundle.putString("city", mCurrentCityName);
                bundle.putString("pro", mCurrentProviceName);
                if (popInterfacer != null)
                    popInterfacer.OnConfirm(flag, bundle);
                break;
        }
        dismiss();
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

    private void upCity() {
        // TODO Auto-generated method stub

        cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new ArrayList();
        }
        mCurrentCityName = cities.get(0);
        wheelCity.setAdapter(new PickerDialogAdapter(cities));
    }

    private void initProvinceData() {
        // TODO Auto-generated method stub
        /**
         * 解析省市区的XML数据
         */

        List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("area.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            // */ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<String> cityList = provinceList.get(0)
                        .getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0);

                }
            }
            // */
            mProvinceDatas = new ArrayList();
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas.add(provinceList.get(i).getName());
                List<String> cityList = provinceList.get(i)
                        .getCityList();
                mCitisDatasMap.put(provinceList.get(i).getName(), cityList);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
        wheelView.setAdapter(new PickerDialogAdapter(mProvinceDatas));
        upCity();
    }
}
