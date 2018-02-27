package com.example.xy.dentist.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.ShopBean;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.presenter.ShopPresenter;
import com.example.xy.dentist.utils.LogUtils;
import com.example.xy.dentist.utils.MyTimeUtils;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XY on 2017/10/12.
 */
public class ShopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private int type;
    private View itemView;
    private final ViewHolder mViewHolder;
    private ShopPresenter mPresenter;
    private ItemClickListener mListener;
    private ItemLongClickListener mLongClickListener;

    public ShopViewHolder(View itemView, final Context context, int type, ItemClickListener listener, ItemLongClickListener longClickListener) {
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
        ShopViewHolder imageViewHolder = new ShopViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shop, null), context, viewType, listener, longClickListener);
        return imageViewHolder;
    }

    public <T> void setData(ShopPresenter presenter, final T t, final int position) {
        this.mPresenter = presenter;
        if(t instanceof ShopBean){
            ShopBean bean= (ShopBean) t;
            mViewHolder.mTvName.setText(bean.name);
            mViewHolder.mTvMoney.setText("￥"+bean.price);

            LogUtils.print("pice",position+"="+bean.pic);
            ImageLoaderUtils.display(mContext, mViewHolder.mIvPic, ApiConstants.IMAGE_URL + bean.thumb);



        }



    }

    static class ViewHolder {
        @Bind(R.id.iv_pic)
        ImageView mIvPic;
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.tv_money)
        TextView mTvMoney;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
