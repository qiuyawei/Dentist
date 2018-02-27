package com.example.xy.dentist.api;


import com.example.xy.dentist.bean.AppointBean;
import com.example.xy.dentist.bean.AppointuBean;
import com.example.xy.dentist.bean.AreaBean;
import com.example.xy.dentist.bean.BaseArrayListResult;
import com.example.xy.dentist.bean.BaseListResult;
import com.example.xy.dentist.bean.BaseResult;
import com.example.xy.dentist.bean.ClinicBean;
import com.example.xy.dentist.bean.DoctorDetailbean;
import com.example.xy.dentist.bean.DoctorInfoBean;
import com.example.xy.dentist.bean.Doctorbean;
import com.example.xy.dentist.bean.InfoBean;
import com.example.xy.dentist.bean.Meassagebean;
import com.example.xy.dentist.bean.PicBean;
import com.example.xy.dentist.bean.RecruitBean;
import com.example.xy.dentist.bean.ShopBean;
import com.example.xy.dentist.bean.StateBean;
import com.example.xy.dentist.bean.TimeSetBean;
import com.example.xy.dentist.bean.UserInfo;
import com.example.xy.dentist.bean.UserTypeBean;
import com.example.xy.dentist.bean.Userbean;
import com.example.xy.dentist.bean.VersionBean;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import rx.Observable;

/**
 * des:ApiService
 * Created by xsf
 * on 2016.06.15:47
 */
public interface ApiService {


    @GET("nc/video/list/{type}/n/{startPage}-10.html")
    Observable<Map<String, List<BaseResult>>> getVideoList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type,
            @Path("startPage") int startPage);

    //检查更新
    @FormUrlEncoded
    @POST(ApiConstants.checkVersion)
    Observable<BaseResult<VersionBean>>
    getAppVersion(@Field("user_token") String user_token);

    //登录
    @FormUrlEncoded
    @POST(ApiConstants.login)
    Observable<BaseResult<Userbean>>
    getLogin(@Field("phone") String phone, @Field("password") String password);

    //第三方登录
    @FormUrlEncoded
    @POST(ApiConstants.login)
    Observable<BaseResult<Userbean>>
    thirdLogin(@Field("type") String type, @Field("code") String code);

    //登录绑定
    @FormUrlEncoded
    @POST(ApiConstants.bindLogin)
    Observable<BaseResult<Userbean>>
    getBindLogin(@Field("phone") String phone, @Field("password") String password, @Field("type") String type, @Field("code") String code);

    //注册
    @FormUrlEncoded
    @POST(ApiConstants.register)
    Observable<BaseResult<Userbean>>
    register(@Field("password") String password, @Field("phone") String phone, @Field("phone_captcha") String phone_captcha, @Field("terminal") String terminal, @Field("code") String code);


    //获取消息
    @FormUrlEncoded
    @POST(ApiConstants.getMessage)
    Observable<BaseResult<BaseListResult<Meassagebean,String>>>
    getMessagesss(@Field("uid") String uid, @Field("states") String states);

    //忘记密码
    @FormUrlEncoded
    @POST(ApiConstants.forget_password)
    Observable<BaseResult<Userbean>>
    forget_password(@Field("new_password") String new_password, @Field("phone") String phone, @Field("phone_captcha") String phone_captcha);


    //发送手机验证码
    @FormUrlEncoded
    @POST(ApiConstants.captcha)
    Observable<BaseResult>
    captcha(@Field("phone") String phone);


    //我的信息
    @FormUrlEncoded
    @POST(ApiConstants.getMyInfo)
    Observable<BaseResult<InfoBean>>
    getMyInfo(@Field("user_token") String user_token);


    //修改我的信息
    @FormUrlEncoded
    @POST(ApiConstants.changeMyInfo)
    Observable<BaseResult>
    changeMyInfo(@Field("user_token") String user_token, @Field("name") String name, @Field("avatar") String avatar, @Field("age") String age);

    //修改头像
    @Multipart
    @POST(ApiConstants.changeMyAvatar)
    Observable<BaseResult<PicBean>>
    changeMyAvatar(@PartMap Map<String, RequestBody> map);

    @FormUrlEncoded
    @POST(ApiConstants.feedback)
    Observable<BaseResult>
    feedback(@Field("user_token") String user_token, @Field("content") String content);

    @FormUrlEncoded
    @POST(ApiConstants.date)
    Observable<BaseResult>
    appoint(@Field("user_token") String user_token, @Field("clinic_id") String clinic_id, @Field("doctor_id") String doctor_id,@Field("type") String type);

    @FormUrlEncoded
    @POST(ApiConstants.myDate)
    Observable<BaseResult<BaseListResult<AppointuBean,String>>>
    myDate(@Field("user_token") String user_token, @Field("page") String page, @Field("limit") String limit, @Field("type") String type);

    @FormUrlEncoded
    @POST(ApiConstants.judge)
    Observable<BaseResult>
    judge(@Field("user_token") String user_token, @Field("id") String id, @Field("comment") String comment, @Field("star") String star);

/*##################################医生端###############################################*/


    @FormUrlEncoded
    @POST(ApiConstants.doctor_login)
        //登陆
    Observable<BaseResult<Doctorbean>>
    doctor_login(@Field("phone") String phone, @Field("password") String password, @Field("type") String type, @Field("code") String code);


    //注册
    @FormUrlEncoded
    @POST(ApiConstants.doctor_register)
    Observable<BaseResult<Doctorbean>>
    doctor_register(@Field("password") String password, @Field("phone") String phone, @Field("phone_captcha") String phone_captcha, @Field("terminal") String terminal, @Field("type") String type, @Field("code") String code);


    //忘记密码
    @FormUrlEncoded
    @POST(ApiConstants.doctor_forget_password)
    Observable<BaseResult<Doctorbean>>
    doctor_forget_password(@Field("new_password") String new_password, @Field("phone") String phone, @Field("phone_captcha") String phone_captcha);


    //发送手机验证码
    @FormUrlEncoded
    @POST(ApiConstants.doctor_captcha)
    Observable<BaseResult>
    doctor_captcha(@Field("phone") String phone);


    //我的信息
    @FormUrlEncoded
    @POST(ApiConstants.doctor_getMyInfo)
    Observable<BaseResult<InfoBean>>
    doctor_getMyInfo(@Field("doctor_token") String doctor_token);


    //修改我的信息
    @FormUrlEncoded
    @POST(ApiConstants.doctor_changeMyInfo)
    Observable<BaseResult>
    doctor_changeMyInfo(@Field("doctor_token") String doctor_token, @Field("name") String name, @Field("avatar") String avatar, @Field("age") String age
            , @Field("introduce") String introduce , @Field("skill") String skill , @Field("resume") String resume,@Field("experience") String experice);

    //修改头像
    @Multipart
    @POST(ApiConstants.doctor_changeMyAvatar)
    Observable<BaseResult<PicBean>>
    doctor_changeMyAvatar(@PartMap Map<String, RequestBody> map);

    @FormUrlEncoded
    @POST(ApiConstants.doctor_feedback)
    Observable<BaseResult>
    doctor_feedback(@Field("doctor_token") String doctor_token, @Field("content") String content);


    @FormUrlEncoded
    @POST(ApiConstants.doctor_date)
    Observable<BaseResult<BaseListResult<DoctorInfoBean,String>>>
    doctor_date(@Field("doctor_token") String doctor_token, @Field("page") String page, @Field("limit") String limit, @Field("status") String status
            , @Field("name") String name, @Field("year") String year, @Field("month") String month, @Field("day") String day,
                @Field("start_time") String start_time, @Field("end_time") String end_time);

    @FormUrlEncoded
    @POST(ApiConstants.doctor_getUserInfo)
    Observable<BaseResult<BaseListResult<String,AppointBean>>>
    doctor_getUserInfo(@Field("doctor_token") String doctor_token, @Field("id") String id);

    @FormUrlEncoded
    @POST(ApiConstants.setTime)
    Observable<BaseResult>
    setTime(@Field("doctor_token") String doctor_token, @Field("id") String id, @Field("date_id") String date_id,@Field("data") String date);

    @FormUrlEncoded
    @POST(ApiConstants.doctor_cancel)
    Observable<BaseResult>
    doctor_cancel(@Field("doctor_token") String doctor_token, @Field("id") String id);

    @FormUrlEncoded
    @POST(ApiConstants.doctor_judge)
    Observable<BaseResult>
    doctor_judge(@Field("doctor_token") String doctor_token, @Field("id") String id, @Field("content") String content, @Field("tooth") String tooth, @Field("tags") String tags);


    @FormUrlEncoded
    @POST(ApiConstants.doctor_getTimeList)
    Observable<BaseResult<BaseListResult<TimeSetBean,String>>>
    doctor_getTimeList(@Field("doctor_token") String doctor_token);

    @FormUrlEncoded
    @POST(ApiConstants.doctor_getTimeListNew)
    Observable<BaseResult<BaseListResult<TimeSetBean,String>>>
    doctor_getTimeListNew(@Field("doctor_token") String doctor_token,@Field("date") String date);

    @FormUrlEncoded
    @POST(ApiConstants.recruit)
    Observable<BaseResult<BaseListResult<RecruitBean,String>>>
    recruit(@Field("doctor_token") String doctor_token, @Field("page") String page, @Field("limit") String limit, @Field("distict_id") String distict_id, @Field("time_desc") String time_desc);


    @FormUrlEncoded
    @POST(ApiConstants.recruit_info)
    Observable<BaseResult<BaseListResult<String,RecruitBean>>>
    recruit_info(@Field("doctor_token") String doctor_token, @Field("id") String id);


    @FormUrlEncoded
    @POST(ApiConstants.clinic)
    Observable<BaseResult<BaseListResult<ClinicBean,String>>>
    clinic(@Field("user_token") String user_token, @Field("page") String page, @Field("limit") String limit, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("distict_id") String distict_id);


//    诊所详情
    @FormUrlEncoded
    @POST(ApiConstants.info)
    Observable<BaseResult<BaseListResult<UserInfo,ClinicBean>>>
    info( @Field("id") String id, @Field("latitude") String latitude, @Field("longitude") String longitude);


    @FormUrlEncoded
    @POST(ApiConstants.clinic_detail)
    Observable<BaseResult<BaseListResult<String,DoctorDetailbean>>>
    clinic_detail(@Field("id") String id);


    @FormUrlEncoded
    @POST(ApiConstants.shop)
    Observable<BaseResult<BaseListResult<ShopBean,String>>>
    shop(@Field("doctor_token") String doctor_token, @Field("page") String page, @Field("limit") String limit);


    @FormUrlEncoded
    @POST(ApiConstants.shop_info)
    Observable<BaseResult<BaseListResult<String,ShopBean>>>
    shop_info(@Field("doctor_token") String doctor_token, @Field("id") String id);


    @POST(ApiConstants.shop_detail)
    Observable<BaseResult<BaseResult<String>>>
    shop_detail(@Path("id") String id);

    @FormUrlEncoded
    @POST(ApiConstants.checkUserType)
    Observable<BaseResult<UserTypeBean>>
    checkUserType(@Field("phone") String phone);

    @FormUrlEncoded
    @POST(ApiConstants.checkUserType2)
    Observable<BaseResult<UserTypeBean>>
    checkUserType2(@Field("code") String code,@Field("type") String type);

    @POST(ApiConstants.tags)
    Observable<BaseResult<BaseListResult<TimeSetBean,String>>>
    tags();



    //修改我的信息
    @FormUrlEncoded
    @POST(ApiConstants.doctor_changeMyInfo)
    Observable<BaseResult<StateBean>>
    status(@Field("doctor_token") String doctor_token, @Field("status") String status);

    //获取地区
    @FormUrlEncoded
    @POST(ApiConstants.area)
    Observable<BaseResult<BaseArrayListResult<AreaBean>>>
    area(@Field("id") String id);
}
