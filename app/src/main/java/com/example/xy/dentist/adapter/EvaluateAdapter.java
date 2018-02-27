package com.example.xy.dentist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.aspsine.irecyclerview.baseadapter.BaseReclyerViewAdapter;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.presenter.QueryPresenter;
import com.example.xy.dentist.viewholder.EvaluateViewHolder;


/**
 * Created by XY on 2017/9/11.
 */
public class EvaluateAdapter<T> extends BaseReclyerViewAdapter<T> {

    private Context mContext;
    private QueryPresenter mPresenter;
    private  ItemClickListener listener;
    private ItemLongClickListener longClickListener;

    public EvaluateAdapter(Context context, QueryPresenter mPresenter,ItemClickListener listener,ItemLongClickListener longClickListener) {
        super(context);
        this.mContext = context;
        this.mPresenter = mPresenter;
        this.listener=listener;
        this.longClickListener=longClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return EvaluateViewHolder.create(mContext, viewType,listener,longClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof EvaluateViewHolder) {
            ((EvaluateViewHolder) holder).setData(mPresenter, get(position), position);
        }
    }
}
