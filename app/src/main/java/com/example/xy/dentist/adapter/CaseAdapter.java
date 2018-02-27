package com.example.xy.dentist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.CaseBean;
import com.example.xy.dentist.tool.MyGridView;
import com.example.xy.dentist.utils.MyTimeUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XY on 2017/10/13.
 */
public class CaseAdapter extends BaseAdapter {

    private Context context;
    List<CaseBean> data;
    RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    int flag;
    public CaseAdapter(Context context, List<CaseBean> data, int flag) {

        this.context = context;
        this.data = data;
        this.flag=flag;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CaseBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CaseBean bean = getItem(position);
        ViewHolder hodler;
        if (convertView == null) {

            convertView = View.inflate(context, R.layout.item_case, null);
            hodler = new ViewHolder(convertView);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHolder) convertView.getTag();
        }
        if (bean != null) {

            hodler.mTvLable.setText(bean.name);
            System.out.println("---------" + MyTimeUtils.getTime());

         /*   if(position==getCount()-1||position==0){
                if (flag==0) {
                    if (position == 0) {
                        hodler.vv.setPadding(20, 0, 0, 0);
                    } else {
                        hodler.vv.setPadding(0, 0, 8, 0);
                    }
                }else{
                    if (position == 0) {
                        hodler.vv.setPadding(4, 0, 0, 0);
                    } else {
                        hodler.vv.setPadding(0, 0, 188, 0);
                    }
                }

            }else{
                hodler.vv.setPadding(0, 0, -10, 0);
            }*/
            if (!bean.isCheck) {
                hodler.mTvLable.setBackgroundResource(R.mipmap.case_uncheck);
                hodler.mTvLable.setTextColor(Color.parseColor("#A5A5A5"));
            } else {
                hodler.mTvLable.setBackgroundResource(R.mipmap.case_check);
                hodler.mTvLable.setTextColor(context.getResources().getColor(R.color.white));
            }

            if (bean.name.equals("1") && position == 7 || position == 23) {
                mParams.setMargins(0, 0, -10, 0);


            } else if (bean.name.equals("1") && position == 8 || position == 24) {
                mParams.setMargins(0, 0, 8, 0);

            } else {
                mParams.setMargins(0, 0, 8, 0);
            }
            if (bean.name.equals("Ⅰ") && position == 4 || position == 14) {
                mParams.setMargins(0, 0, -10, 0);

            } else if (bean.name.equals("Ⅰ") && position == 5 || position == 15) {
                mParams.setMargins(0, 0, 8, 0);

            } else {
                if(position==getCount()-1){
                    mParams.setMargins(8, 0,0, 0);
                }else{
                    mParams.setMargins(0, 0, 8, 0);
                }

            }


            if (flag==0){
                if(position>=16){
                    hodler.vv.setVisibility(View.GONE);
                }else{
                    hodler.vv.setVisibility(View.VISIBLE);
                }
                if(position==7||position==23){
                    hodler.vv_right.setVisibility(View.VISIBLE);
                }else{
                    hodler.vv_right.setVisibility(View.GONE);
                }

            }else if (flag==1){
                if(position>=10){
                    hodler.vv.setVisibility(View.GONE);
                }else{
                    hodler.vv.setVisibility(View.VISIBLE);
                }
                if(position==4||position==14){
                    hodler.vv_right.setVisibility(View.VISIBLE);
                }else{
                    hodler.vv_right.setVisibility(View.GONE);
                }
             //   hodler.mTvLable.setLayoutParams(mParams);
            }



        }

        return convertView;
    }

    public void updateItemData(MyGridView gv, int index) {
        if (gv == null) {
            return;
        }

        // 获取当前可以看到的item位置
        int visiblePosition = gv.getFirstVisiblePosition();
        // 如添加headerview后 firstview就是hearderview
        // 所有索引+1 取第一个view
        // View view = listview.getChildAt(index - visiblePosition + 1);
        // 获取点击的view
        View view = gv.getChildAt(index - visiblePosition);
        TextView mTvLable = (TextView) view.findViewById(R.id.tv_lable);
        // 获取mDataList.set(ids, item);更新的数据
        CaseBean bean = getItem(index);
        // 重新设置界面显示数据
        mTvLable.setText(bean.name);
        if (!bean.isCheck) {
            mTvLable.setBackgroundResource(R.mipmap.case_uncheck);
            mTvLable.setTextColor(Color.parseColor("#A5A5A5"));
        } else {
            mTvLable.setBackgroundResource(R.mipmap.case_check);
            mTvLable.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    static class ViewHolder {
        @Bind(R.id.tv_lable)
        TextView mTvLable;
        @Bind(R.id.vv)
        ImageView vv;
        @Bind(R.id.vv_right)
        ImageView vv_right;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
