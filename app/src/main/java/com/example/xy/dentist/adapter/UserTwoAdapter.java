package com.example.xy.dentist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.UserYearBean;
import com.example.xy.dentist.utils.LogUtils;
import com.google.gson.Gson;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XY on 2017/10/12.
 */
public class UserTwoAdapter extends BaseAdapter {

    private Context context;
    List<UserYearBean> data;

    public UserTwoAdapter(Context context, List<UserYearBean> data) {

        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public UserYearBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserYearBean bean = getItem(position);
        LogUtils.print("bean","UserTwoAdapter");
        ViewHolder hodler;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_user_two, null);
            hodler = new ViewHolder(convertView);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHolder) convertView.getTag();
        }
        if (bean != null) {
            hodler.mTvName.setText(bean.month+"月"+bean.day+"日  "+bean.start_time+"~"+bean.end_time);
            hodler.mTvRoom.setText("诊所："+bean.clinic_name);
            hodler.mTvDoc.setText("医生："+bean.doctor_name);
            hodler.mTvCases.setText("病例日志："+bean.content);

         //   ImageLoaderUtils.displayRound(context, hodler.mIvPic, R.mipmap.bna);


        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_line1)
        ImageView mIvLine1;
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_room)
        TextView mTvRoom;
        @Bind(R.id.tv_doc)
        TextView mTvDoc;
        @Bind(R.id.tv_cases)
        TextView mTvCases;
        @Bind(R.id.ll_content)
        LinearLayout mLlContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
