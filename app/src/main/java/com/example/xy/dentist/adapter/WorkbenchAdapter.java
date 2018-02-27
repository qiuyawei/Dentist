package com.example.xy.dentist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.aspsine.irecyclerview.baseadapter.BaseReclyerViewAdapter;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.presenter.WorkbenchPresenter;
import com.example.xy.dentist.viewholder.WorkbenchViewHolder;

/**
 * Created by XY on 2017/10/12.
 */
public class WorkbenchAdapter<T> extends BaseReclyerViewAdapter<T> {

    private Context mContext;
    private WorkbenchPresenter mPresenter;
    private ItemClickListener listener;
    private ItemLongClickListener longClickListener;
    public  int flag;
    public WorkbenchAdapter(Context context, WorkbenchPresenter mPresenter,ItemClickListener listener,ItemLongClickListener longClickListener,int flag) {
        super(context);
        this.mContext = context;
        this.mPresenter = mPresenter;
        this.listener=listener;
        this.longClickListener=longClickListener;
        this.flag=flag;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return WorkbenchViewHolder.create(mContext, viewType, listener, longClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof WorkbenchViewHolder) {
            ((WorkbenchViewHolder) holder).setData(mPresenter, get(position), position,flag);
        }
    }

}
