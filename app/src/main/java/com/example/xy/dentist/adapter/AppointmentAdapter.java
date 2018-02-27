package com.example.xy.dentist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.aspsine.irecyclerview.baseadapter.BaseReclyerViewAdapter;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.presenter.AppointmentPresenter;
import com.example.xy.dentist.viewholder.AppointmentViewHolder;

/**
 * Created by XY on 2017/10/11.
 */
public class AppointmentAdapter <T> extends BaseReclyerViewAdapter<T> {

    private Context mContext;
    private AppointmentPresenter mPresenter;
    private ItemClickListener listener;
    private ItemLongClickListener longClickListener;
    public   int flag;
    public AppointmentAdapter(Context context, AppointmentPresenter mPresenter,ItemClickListener listener,ItemLongClickListener longClickListener, int flag) {
        super(context);
        this.mContext = context;
        this.mPresenter = mPresenter;
        this.listener=listener;
        this.longClickListener=longClickListener;
        this.flag=flag;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return AppointmentViewHolder.create(mContext, viewType, listener, longClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof AppointmentViewHolder) {
            ((AppointmentViewHolder) holder).setData(mPresenter, get(position), position,flag);
        }
    }



}
