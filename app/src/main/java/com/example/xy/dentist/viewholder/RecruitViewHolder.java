package com.example.xy.dentist.viewholder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.RecruitBean;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.presenter.EvaluatePresenter;
import com.example.xy.dentist.utils.GlobalParams;
import com.example.xy.dentist.utils.MyTimeUtils;
import com.example.xy.dentist.utils.PopUtil;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XY on 2017/10/12.
 */
public class RecruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private int type;
    private View itemView;
    private final ViewHolder mViewHolder;
    private EvaluatePresenter mPresenter;
    private ItemClickListener mListener;
    private ItemLongClickListener mLongClickListener;

    public RecruitViewHolder(View itemView, final Context context, int type, ItemClickListener listener, ItemLongClickListener longClickListener) {
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
        RecruitViewHolder imageViewHolder = new RecruitViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recruit2, null), context, viewType, listener, longClickListener);
        return imageViewHolder;
    }

    public <T> void setData(EvaluatePresenter presenter, final T t, final int position) {
        Log.i("TAG","RecruitHolder=");
        this.mPresenter = presenter;


        if(t instanceof RecruitBean){
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            final RecruitBean bean = (RecruitBean) t;
            if(bean!=null){
                mViewHolder.mTvPosi.setText(bean.name);
                mViewHolder.mTvCommp.setText(bean.company);
                mViewHolder.mTvMoney.setText("薪资："+bean.money);
                mViewHolder.mTvPhone.setText("联系电话："+bean.phone);
                mViewHolder.mTvTime.setText("发布时间："+ df.format(Long.parseLong(bean.create_time)*1000));
                Log.i("TAG","crr="+bean.create_time);

                mViewHolder.mTvCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopUtil.showView(mContext, PopUtil.getView(((Activity) mContext), "即将拨打电话",bean.phone, mContext, new PopUtil.onSelectFinishListener() {
                            @Override
                            public void onSelectFinish(String type) {
                                if (type.equals("yes")) {

                                    GlobalParams.callPhone(mContext, bean.phone);
                                }

                            }
                        }), ((Activity) mContext).getWindow(), mViewHolder.mTvPosi);
                    }
                });
            }

        }


    }



    static class ViewHolder {
        @Bind(R.id.tv_posi)
        TextView mTvPosi;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.tv_commp)
        TextView mTvCommp;
        @Bind(R.id.tv_money)
        TextView mTvMoney;
        @Bind(R.id.tv_phone)
        TextView mTvPhone;
        @Bind(R.id.tv_call)
        TextView mTvCall;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
