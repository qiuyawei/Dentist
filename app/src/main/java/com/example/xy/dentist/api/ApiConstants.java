/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.example.xy.dentist.api;
//http://121.199.8.244:3501/user/userType?type=2&code=o8umu07-GgBUkssldIPS00aRi00o
public class ApiConstants {
    public static int SCREEN_WITH=0;
    public static final String NETEAST_HOST = "http://c.m.163.com/";

//http://121.199.8.244:3501/pic/20171117/17.34.22.497716.jpg
    public static final String SERVER_URL = "http://121.199.8.244:3501/";

    public static final String IMAGE_URL = "http://121.199.8.244:3501/pic/";
    public static final String checkVersion = "adminApi/getAndroidNumber";

    public static final String bindLogin = "user/bind";
    public static final String bindLoginDoctor = "doctor/bind";

    public static final String login = "user/login";
    public static final String register = "user/register";
    public static final String forget_password = "user/forget_password";
    public static final String captcha = "user/captcha";
    public static final String getMyInfo = "user/getMyInfo";
    public static final String changeMyInfo = "user/changeMyInfo";
    public static final String changeMyAvatar = "user/changeMyAvatar";
    public static final String feedback = "user/feedback";
    public static final String date = "user/date";
    public static final String myDate = "user/myDate";
    public static final String judge = "user/judge";
    public static final String doctor_login = "doctor/login";
    public static final String doctor_register = "doctor/register";
    public static final String doctor_forget_password = "doctor/forget_password";
    public static final String doctor_captcha = " doctor/captcha";
    public static final String doctor_getMyInfo = "doctor/getMyInfo";
    public static final String doctor_changeMyInfo = "doctor/changeMyInfo";
    public static final String doctor_changeMyAvatar = "doctor/changeMyAvatar";
    public static final String doctor_feedback = "doctor/feedback";
    public static final String doctor_date = "doctor/getUserList";
    public static final String doctor_getUserInfo = "doctor/getUserInfo";
    public static final String setTime = "doctor/setTime";
    public static final String doctor_cancel = "doctor/cancel";
    public static final String doctor_judge = "doctor/judge";
    public static final String doctor_getTimeList = "doctor/getTimeList";
    public static final String doctor_getTimeListNew = "doctor/getTimeListNew";

    public static final String recruit = "recruit/lists";
    public static final String recruit_info = "recruit/info";
    public static final String clinic = "clinic/lists";
    public static final String info = "clinic/info";
    public static final String clinic_detail = "clinic/doctorInfo";
    public static final String shop = "shop/lists";
    public static final String shop_info = "shop/info";
    public static final String shop_detail = "shop/detail/{id}";
    public static final String checkUserType = "home/checkUserType";
    public static final String checkUserType2 = "user/userType";

    public static final String tags = "home/tags";
    public static final String area = "home/area2";
    public static final String getMessage = "home/getPushLog";


    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {
            case HostType.NETEASE_DRIVER:
                host = SERVER_URL;
                break;

            case HostType.NEWS_DETAIL_HTML_PHOTO:
                host = "http://kaku.com/";
                break;
            default:
                host = "";
                break;
        }
        return host;
    }
}
