package com.example.xy.dentist.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.jaydenxiao.common.commonutils.ToastUitl;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1]\\d{10}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	public static void callPhone(Context context, String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
		context.startActivity(intent);
	}

	public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/taxi";
	public static String getPhotoSavePath(){
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			ToastUitl.showShort("请检查SD卡是否存在");
			return "";

		}else{
			File rootFile = new File(ROOT_PATH);
			if (!rootFile.exists()){
				rootFile.mkdirs();
			}
		}


		return ROOT_PATH;
	}

	public static void callKeFu(Context context) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4001680288"));
		context.startActivity(intent);
	}

	public static int getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}else if (birthDay.getTime() > cal.getTimeInMillis()){
			return -1;
		}


		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) age--;
			} else {
				age--;
			}
		}
		System.out.println("age:" + age);
		return age;
	}

	public static boolean isEmail(String email){
		String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
//		logger.info(m.matches()+"---");
		return m.matches();
	}


}
