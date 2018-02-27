package com.example.xy.dentist.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.DoctorInfoBean;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.presenter.WorkbenchPresenter;
import com.example.xy.dentist.ui.doctor.activity.recruit.TimeSetActivity;
import com.example.xy.dentist.ui.doctor.activity.work.LogEvaluationActivity;
import com.example.xy.dentist.utils.DensityUtil;
import com.example.xy.dentist.utils.LogUtils;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XY on 2017/10/12.
 */
public class WorkbenchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private int type;
    private View itemView;
    private final ViewHolder mViewHolder;
    private WorkbenchPresenter mPresenter;
    private ItemClickListener mListener;
    private ItemLongClickListener mLongClickListener;
//    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public WorkbenchViewHolder(View itemView, final Context context, int type, ItemClickListener listener, ItemLongClickListener longClickListener) {
        super(itemView);
        this.itemView = itemView;
        this.type = type;
        this.mContext = context;
        mViewHolder = new ViewHolder(itemView);
        this.mListener = listener;
        this.mLongClickListener = longClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    /**
     * 点击监听
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(v, getPosition());
        }
    }

    /**
     * 长按监听
     */
    @Override
    public boolean onLongClick(View arg0) {
        if (mLongClickListener != null) {
            mLongClickListener.onItemLongClick(arg0, getPosition());
        }
        return true;
    }


    public static RecyclerView.ViewHolder create(Context context, int viewType, ItemClickListener listener, ItemLongClickListener longClickListener) {
        WorkbenchViewHolder imageViewHolder = new WorkbenchViewHolder(LayoutInflater.from(context).inflate(R.layout.item_workbench, null), context, viewType, listener, longClickListener);
        return imageViewHolder;
    }

    public <T> void setData(WorkbenchPresenter presenter, final T t, final int position, int flag) {
        this.mPresenter = presenter;
        //   mViewHolder.mTvAppo.setVisibility(flag ? View.VISIBLE : View.GONE);
        long half = (long) (0.5 * 60 * 60 * 1000);

        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");

        if (t instanceof DoctorInfoBean) {
            final DoctorInfoBean bean = (DoctorInfoBean) t;
            if (bean != null) {

                ImageLoaderUtils.displayRound(mContext, mViewHolder.mIvPic, ApiConstants.IMAGE_URL + bean.avatar);
                mViewHolder.mTvTitle.setText(bean.name);
                mViewHolder.mTvYear.setText(bean.age + "岁");
                mViewHolder.mTvPhone.setText(bean.phone);
//                Log.i("TAG", "tag=" + bean.tags);

                if (bean.tags != null) {
                    List<String> tag = Arrays.asList(bean.tags);
                    if (tag.size() == 1) {
                        if (!TextUtils.isEmpty(tag.get(0))) {
                            mViewHolder.mTvLab1.setText(tag.get(0));
                            mViewHolder.mTvLab1.setVisibility(View.VISIBLE);
                        }
                        mViewHolder.mTvLab2.setVisibility(View.GONE);
                        mViewHolder.mTvLab3.setVisibility(View.GONE);

                    } else if (tag.size() > 1) {
                        if (!TextUtils.isEmpty(tag.get(0))) {
                            mViewHolder.mTvLab1.setText(tag.get(0));
                            mViewHolder.mTvLab1.setVisibility(View.VISIBLE);
                        }
                        if (!TextUtils.isEmpty(tag.get(1))) {
                            mViewHolder.mTvLab2.setText(tag.get(1));
                            mViewHolder.mTvLab2.setVisibility(View.VISIBLE);
                        }
                        mViewHolder.mTvLab3.setVisibility(View.GONE);
                    } /*else if (tag.size() == 3) {
                        if (!TextUtils.isEmpty(tag.get(0))) {
                            mViewHolder.mTvLab1.setText(tag.get(0));
                            mViewHolder.mTvLab1.setVisibility(View.VISIBLE);
                        }
                        if (!TextUtils.isEmpty(tag.get(1))) {
                            mViewHolder.mTvLab2.setText(tag.get(1));
                            mViewHolder.mTvLab2.setVisibility(View.VISIBLE);
                        }
                        if (!TextUtils.isEmpty(tag.get(2))) {
                            mViewHolder.mTvLab3.setText(tag.get(2));
                            mViewHolder.mTvLab3.setVisibility(View.VISIBLE);
                        }
                    }*/

                }
            }


            switch (flag) {
                case 0:
                    mViewHolder.mTvAppo.setVisibility(View.GONE);
                    mViewHolder.mTvNeglect.setVisibility(View.GONE);

                    break;
                case 1:
                    mViewHolder.mTvAppo.setVisibility(View.VISIBLE);
                    mViewHolder.mTvNeglect.setVisibility(View.VISIBLE);

                    mViewHolder.mTvAppo.setText("设定时间");
                    mViewHolder.mTvAppo.setTextColor(mContext.getResources().getColor(R.color.white));
                    break;
                case 2:
                    mViewHolder.mTvAppo.setVisibility(View.INVISIBLE);
                    mViewHolder.mTvNeglect.setVisibility(View.VISIBLE);

                    if (TextUtils.isEmpty(bean.start_time)) {
                        mViewHolder.tv_time.setText(df3.format(Long.parseLong(bean.create_time) * 1000)+" (" +df1.format(Long.parseLong(bean.create_time) * 1000) + "~" + df1.format(Long.parseLong(bean.create_time) * 1000 + half)+")");
                    } else
//                    mViewHolder.mTvTime.setText("预约时间：" + bean.start_time + "~" + bean.end_time);
                        mViewHolder.tv_time.setText(bean.year + "-" + bean.month + "-" + bean.day + " (" + bean.start_time + "~" + bean.end_time + ")");

                    mViewHolder.mTvNeglect.setText("完成");
                    mViewHolder.mTvNeglect.setBackgroundResource(R.mipmap.line_appo);
                    break;
                case 3:
                    mViewHolder.mTvAppo.setVisibility(View.GONE);
                    mViewHolder.mTvNeglect.setVisibility(View.GONE);
                    if (TextUtils.isEmpty(bean.start_time)) {
                        mViewHolder.tv_time.setText(df3.format(Long.parseLong(bean.create_time) * 1000)+" (" +df1.format(Long.parseLong(bean.create_time) * 1000) + "~" + df1.format(Long.parseLong(bean.create_time) * 1000 + half)+")");
                    } else
//                    mViewHolder.mTvTime.setText("预约时间：" + bean.start_time + "~" + bean.end_time);
                        mViewHolder.tv_time.setText(bean.year + "-" + bean.month + "-" + bean.day + " (" + bean.start_time + "~" + bean.end_time + ")");                    mViewHolder.mTvAppo.setTextColor(Color.parseColor("#6F6F6F"));
                    mViewHolder.mTvAppo.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));

                    break;
                case -1:
                    mViewHolder.mTvAppo.setVisibility(View.GONE);
                    mViewHolder.mTvNeglect.setVisibility(View.GONE);
                    break;
            }


            mViewHolder.mTvAppo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mViewHolder.mTvAppo.getVisibility() == View.VISIBLE) {
                        Intent intent = new Intent(mContext, TimeSetActivity.class);
                        intent.putExtra("bean", bean);
                        mContext.startActivity(intent);
                    }

                }
            });

            mViewHolder.mTvNeglect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mViewHolder.mTvNeglect.getVisibility() == View.VISIBLE) {

                        if (mViewHolder.mTvNeglect.getText().toString().equals("忽略")) {
                            mPresenter.cancel(bean.id);
                        } else if (mViewHolder.mTvNeglect.getText().equals("完成")) {
                            String tag1 = "", tag2 = "";
                            List<String> tag = Arrays.asList(bean.tags);
                            LogUtils.print("tagSIze", tag.size());
                            if (tag.size() > 0) {
                                tag1 = tag.get(0);
                            }
                            if (tag.size() > 1) {
                                tag2 = tag.get(1);
                            }
                            Intent intent = new Intent(mContext, LogEvaluationActivity.class);
                            intent.putExtra("title", "填写日志评价");
                            intent.putExtra("id", bean.id);
                            intent.putExtra("DoctorInfoBean", bean);
                            intent.putExtra("tag1", tag1);
                            intent.putExtra("tag2", tag2);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });


        Log.i("TAG","flag"+flag);

        }

    }

    static class ViewHolder {
        @Bind(R.id.iv_pic)
        ImageView mIvPic;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_year)
        TextView mTvYear;
        @Bind(R.id.tv_phone)
        TextView mTvPhone;
        @Bind(R.id.tv_lab1)
        TextView mTvLab1;
        @Bind(R.id.tv_lab2)
        TextView mTvLab2;
        @Bind(R.id.tv_lab3)
        TextView mTvLab3;
        @Bind(R.id.tv_appo)
        TextView mTvAppo;
        @Bind(R.id.tv_neglect)
        TextView mTvNeglect;
        @Bind(R.id.tv_time)
        TextView tv_time;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
