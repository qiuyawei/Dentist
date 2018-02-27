package com.example.xy.dentist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.TimeSetBean;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.utils.MyTimeUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XY on 2017/10/13.
 */
public class TimeSetAdapter extends BaseAdapter {

    private Context context;
    List<TimeSetBean> data;
    int flag;

    public TimeSetAdapter(Context context, List<TimeSetBean> data, int flag) {

        this.context = context;
        this.data = data;
        this.flag=flag;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public TimeSetBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TimeSetBean bean = getItem(position);
        ViewHolder hodler;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_timeset, null);
            hodler = new ViewHolder(convertView);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHolder) convertView.getTag();
        }
        if (bean != null) {

          if(!bean.isCheck){
          //  if(bean.disabled.equals("1")){
                if(flag==0) {
                    hodler.mTvLable.setText(bean.start_time+"-"+bean.end_time);
                    hodler.mTvLable.setPadding(0,0,0,0);
                    if(bean.disabled!=null&&bean.disabled.equals("0")){
                        hodler.mTvLable.setBackgroundResource(R.mipmap.time_en);
                        hodler.mTvLable.setTextColor(Color.parseColor("#A5A5A5"));
                    }else if(bean.disabled!=null&&bean.disabled.equals("1")){
                        hodler.mTvLable.setBackgroundResource(R.mipmap.time_enable);
                        hodler.mTvLable.setEnabled(false);
                        hodler.mTvLable.setTextColor(context.getResources().getColor(R.color.white));
                    }
                    /*if (MyTimeUtils.com(bean.start_time, MyTimeUtils.getTime()) < 0) {
                        hodler.mTvLable.setBackgroundResource(R.mipmap.time_enable);
                        hodler.mTvLable.setTextColor(context.getResources().getColor(R.color.white));
                    } else {
                        hodler.mTvLable.setBackgroundResource(R.mipmap.time_en);
                        hodler.mTvLable.setTextColor(Color.parseColor("#A5A5A5"));
                    }*/
                }else  if(flag==1){
                    hodler.mTvLable.setText(bean.name);
                    hodler.mTvLable.setPadding(10,10,10,10);
                    hodler.mTvLable.setBackgroundResource(R.drawable.gray_bg);
                    hodler.mTvLable.setTextColor(context.getResources().getColor(R.color.white));
                }else  if(flag==2){
                    hodler.mTvLable.setText(bean.name);
                    hodler.mTvLable.setPadding(10,10,10,10);
                    hodler.mTvLable.setBackgroundResource(R.drawable.orange_bg);
                    hodler.mTvLable.setTextColor(context.getResources().getColor(R.color.white));
                }
            }else{
                hodler.mTvLable.setTextColor(context.getResources().getColor(R.color.white));
                if(flag==0){
                    hodler.mTvLable.setText(bean.start_time+"-"+bean.end_time);
//                    hodler.mTvLable.setText(bean.year+"-"+bean.month+"-"+bean.day+" ("+bean.start_time + " - " +bean.end_time+")");
                    // hodler.mTvLable.setBackgroundResource(R.mipmap.time_en);
                    hodler.mTvLable.setBackgroundResource(R.mipmap.time_choose);
                    hodler.mTvLable.setPadding(0,0,0,0);
                }else if(flag==1){
                    hodler.mTvLable.setText(bean.name);
                    hodler.mTvLable.setBackgroundResource(R.drawable.purple_bg);
                    hodler.mTvLable.setPadding(10,10,10,10);
                }else  if(flag==2){
                    hodler.mTvLable.setText(bean.name);
                    hodler.mTvLable.setPadding(10,10,10,10);
                    hodler.mTvLable.setBackgroundResource(R.drawable.orange_bg);
                    hodler.mTvLable.setTextColor(context.getResources().getColor(R.color.white));
                }
            }

            if(bean.disabled==null){
                LogUtils.print("Nulll","dsiable");
            }else {
                if(bean.disabled.equals("1")){
                    hodler.mTvLable.setPadding(10,10,10,10);
                    hodler.mTvLable.setBackgroundResource(R.mipmap.time_enable);
                    hodler.mTvLable.setTextColor(context.getResources().getColor(R.color.white));
                }
            }






        }

        return convertView;
    }

    public void setData(int position,boolean isSelect) {
       /* for (int i = 0; i < data.size(); i++) {
            if(position!=i){

            data.get(i).isCheck=false;
            }
        }*/
        data.get(position).isCheck=isSelect;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.tv_lable)
        TextView mTvLable;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
