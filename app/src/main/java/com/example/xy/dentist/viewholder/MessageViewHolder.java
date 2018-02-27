package com.example.xy.dentist.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.Meassagebean;
import com.example.xy.dentist.bean.ShopBean;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.presenter.MessagePresenter;
import com.example.xy.dentist.presenter.ShopPresenter;
import com.example.xy.dentist.utils.LogUtils;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XY on 2017/10/12.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private View itemView;
    private final ViewHolder mViewHolder;
    private MessagePresenter mPresenter;
    private ItemClickListener mListener;
    private ItemLongClickListener mLongClickListener;

    public MessageViewHolder(View itemView, final Context context, int type, ItemClickListener listener, ItemLongClickListener longClickListener) {
        super(itemView);
        this.itemView = itemView;
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


    public static RecyclerView.ViewHolder create(Context context, int viewType, ItemClickListener listener, ItemLongClickListener longClickListener,ViewGroup parent) {
        LogUtils.print("create","create");

        MessageViewHolder imageViewHolder = new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message,parent, false), context, viewType, listener, longClickListener);
        return imageViewHolder;
    }

    public <T> void setData(MessagePresenter presenter, final T t, final int position) {
        this.mPresenter = presenter;
        if(t instanceof Meassagebean){
            Meassagebean bean= (Meassagebean) t;
            mViewHolder.tv_content.setText(bean.content);
            mViewHolder.tv_time.setText(new SimpleDateFormat("yyyy-MM-dd").format(Long.parseLong(bean.create_time)*1000));
//
            LogUtils.print("content",position+"="+bean.content);
//            ImageLoaderUtils.display(mContext, mViewHolder.mIvPic, ApiConstants.IMAGE_URL + bean.thumb);



        }



    }

    static class ViewHolder {

        @Bind(R.id.tv_content)
        TextView tv_content;

        @Bind(R.id.tv_time)
        TextView tv_time;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
