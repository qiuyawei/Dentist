package com.example.xy.dentist.widget.city;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.Api;
import com.example.xy.dentist.api.HostType;
import com.example.xy.dentist.bean.AddressModel;
import com.example.xy.dentist.bean.AreaBean;
import com.example.xy.dentist.bean.BaseArrayListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.jaydenxiao.common.baserx.RxManager;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.commonutils.ToastUitl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;


/**
 * CityPicker
 * put two WheelView into same Linearlayout
 *
 */
public class CityPicker extends LinearLayout  {
    private static final int REFRESH_VIEW = 0x001;

    private WheelView mProvincePicker;
    private WheelView mCityPicker;
    private WheelView mDistrictPicker;

    private int mCurrProvinceIndex = -1;
    private int mTemCityIndex = -1;

//    private AreaDataUtil mAreaDataUtil;
//    private ArrayList<String> mProvinceList = new ArrayList<String>();
//    private XmlResourceParser xmlResourceParser;

//    private ArrayList<String> province;                     //省
//    private ArrayList<HashMap<String,String>> city;         //市
//    private ArrayList<HashMap<String,String>> district;     //区

    /**
     * 所有省
     */
    protected ArrayList<String> mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName ="";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode ="";
    List<AddressModel> addressModelList;

    public RxManager mRxManage=new RxManager();

    public CityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        //getAreaInfo();

        getArea("0","Province");
//        xmlResourceParser = context.getResources().getXml(R.xml.province_data);

    }

    private void getArea(final String id, final String type) {

        mRxManage.add(area(id).subscribe(new Subscriber<BaseResult<BaseArrayListResult<AreaBean>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResult<BaseArrayListResult<AreaBean>> result) {
                if (result != null) {
                    try {
                        if (result.code == 200) {

                            if (!type.equals("City2")) {


                                switch (type) {
                                    case "Province":
                                        setPrvcinceData(result.data.list);
                                        getArea(result.data.list.get(0).id, "City");
                                        break;
                                    case "City":
                                        setCityData(result.data.list);
                                        getArea(result.data.list.get(0).id, "District");
                                        break;
                                    case "District":
                                        setDistrictData(result.data.list);
                                        break;

                                }

                            } else {

                                setDistrictData(result.data.list);

                            }


                        } else {
                            ToastUitl.showShort(result.message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
    }



    public  Observable<BaseResult<BaseArrayListResult<AreaBean>>> area(String id) {
        return  Api.getDefault(HostType.NETEASE_DRIVER).area(id).map(new Func1<BaseResult<BaseArrayListResult<AreaBean>>, BaseResult<BaseArrayListResult<AreaBean>>>() {
            @Override
            public BaseResult<BaseArrayListResult<AreaBean>> call(BaseResult<BaseArrayListResult<AreaBean>> baseArrayListResultBaseResult) {
                return baseArrayListResultBaseResult;
            }
        }).compose(RxSchedulers.<BaseResult<BaseArrayListResult<AreaBean>>>io_main());
    }

    public CityPicker(Context context) {
        this(context, null);
    }


    private void setPrvcinceData(ArrayList<AreaBean> data) {
        if(data.size()==0){
            return;
        }
        mProvincePicker.setData(data);
        mProvincePicker.setDefault(0);
        mProvincePicker.setEnable(true);


    }
    private void setCityData(ArrayList<AreaBean> data) {
        if(data.size()==0){
            return;
        }
        mCityPicker.setData(data);
        mCityPicker.setDefault(0);
        mCityPicker.setEnable(true);
    }

    private void setDistrictData(ArrayList<AreaBean> data) {
        if(data.size()==0){
            return;
        }
        mDistrictPicker.setData(data);
        mDistrictPicker.setDefault(0);
        mDistrictPicker.setEnable(true);
    }

    /*private void getAreaInfo() {

        List<ProvinceModel> provinceList = null;
        AssetManager asset = getContext().getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            /*//*//* 初始化默认选中的省、市、区
            if (provinceList!= null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                Log.d("省", mCurrentProviceName);
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList!= null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    Log.d("市", mCurrentCityName);
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            /*//*//*
            mProvinceDatas = new ArrayList<>();
            for (int i=0; i< provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas.add(provinceList.get(i).getName());
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j=0; j< cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k=0; k<districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }*/
    public void setData(List<AddressModel> addressModelList) {
        this.addressModelList=addressModelList;
        getAddressInfo();
    }

    private void getAddressInfo() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getContext().getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList!= null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                Log.d("省", mCurrentProviceName);
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList!= null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    Log.d("市", mCurrentCityName);
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new ArrayList<>();
            for (int i=0; i< provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas.add(provinceList.get(i).getName());
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j=0; j< cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k=0; k<districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    private ArrayList<String> getCity(String key){
        String[] s = mCitisDatasMap.get(key);
        ArrayList<String> list = new ArrayList<>();
        for (String value : s) {
            list.add(value);
        }
        return list;
    }

    private ArrayList<String> getDistrict(String key){
        String[] name = mDistrictDatasMap.get(key);
        ArrayList<String> list = new ArrayList<>();
        for (String value : name) {
            list.add(value);
        }
        return list;
    }
    //邮编
    private String getZipCode(String key) {
        return mZipcodeDatasMap.get(key);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);

        mProvincePicker = (WheelView) findViewById(R.id.province);
        mCityPicker = (WheelView) findViewById(R.id.city);
        mDistrictPicker = (WheelView) findViewById(R.id.area);

       /* mProvincePicker.setData(mProvinceDatas);
        mProvincePicker.setDefault(0);

        String defaultProvince = mProvinceDatas.get(0);
        String defaultCity = mCitisDatasMap.get(defaultProvince)[0];
        String defaultDistrict = mDistrictDatasMap.get(defaultCity)[0];
        String defaultZipCode = mZipcodeDatasMap.get(defaultDistrict);

        mCityPicker.setData(getCity(defaultProvince));
        mCityPicker.setDefault(1);

        mDistrictPicker.setData(getDistrict(defaultCity));
        mDistrictPicker.setDefault(1);*/

        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;


               /* if (mCurrProvinceIndex != id) {
                    mCurrProvinceIndex = id;

                    // get city names by province
                    ArrayList<String> citys = getCity(mProvinceDatas.get(id));
                    ArrayList<String> dis = getDistrict(citys.get(0));
                    if (citys.size() == 0) {
                        return;
                    }

                    mCityPicker.setData(citys);
                    mDistrictPicker.setData(dis);

                    if (citys.size() > 1) {
                        //if city is more than one,show start index == 1
                        mCityPicker.setDefault(1);
                        mDistrictPicker.setDefault(1);
                    } else {
                        mCityPicker.setDefault(0);
                        mDistrictPicker.setDefault(0);
                    }
                }*/

            }

            @Override
            public void selecting(int id, String text) {
            }
        });

        mCityPicker.setOnSelectListener(new WheelView.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                getArea(String.valueOf(id),"City2");
               /* if (mTemCityIndex != id) {
                    mTemCityIndex = id;
                    String selectCity = text;
                    if (selectCity == null || selectCity.equals(""))
                        return;

                    ArrayList<String> dis = getDistrict(selectCity);
                    mDistrictPicker.setData(dis);

                    if (dis.size() > 1) {
                        //if city is more than one,show start index == 1
                        mDistrictPicker.setDefault(1);
                    } else {
                        mDistrictPicker.setDefault(0);
                    }
                }*/
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mDistrictPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                String zipCode = getZipCode(text);
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

    }


    public ArrayList<String> getSelect(){

        ArrayList<String> list = new ArrayList<>();
        list.add(mProvincePicker.getSelectedText());
        list.add(mCityPicker.getSelectedText());
        list.add(mDistrictPicker.getSelectedText());
        return list;
    }


    public String getSelectid(){


        return mDistrictPicker.getSelectedId();
    }
    public String getSelectZip(){
        return getZipCode(mDistrictPicker.getSelectedText());
    }


}
