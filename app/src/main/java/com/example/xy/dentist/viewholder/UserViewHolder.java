package com.example.xy.dentist.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.adapter.UserTwoAdapter;
import com.example.xy.dentist.bean.UserYear;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.presenter.UserPresenter;
import com.example.xy.dentist.tool.MyListView;
import com.example.xy.dentist.ui.patientside.activity.mine.CaseLogDetails;
import com.example.xy.dentist.utils.LogUtils;
import com.jaydenxiao.common.commonutils.FormatUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XY on 2017/10/12.
 */
public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private int type;
    private View itemView;
    private final ViewHolder mViewHolder;
    private UserPresenter mPresenter;
    private ItemClickListener mListener;
    private ItemLongClickListener mLongClickListener;

    public UserViewHolder(View itemView, final Context context, int type, ItemClickListener listener, ItemLongClickListener longClickListener) {
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
        UserViewHolder imageViewHolder = new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, null), context, viewType, listener, longClickListener);
        return imageViewHolder;
    }

    public <T> void setData(UserPresenter presenter, final T t, final int position) {
        this.mPresenter = presenter;
        List<String> data=new ArrayList<>();
        for (int i = 0; i <2 ; i++) {
            data.add("");
        }
        if(position%2==0){
            data.add("");
        }
        if(t instanceof UserYear){
            LogUtils.print("UserYear","UserYear");
            mViewHolder.mTvYear.setText(FormatUtil.checkValue(((UserYear) t).key));

            mViewHolder.mLv.setAdapter(new UserTwoAdapter(mContext,((UserYear) t).value));

            mViewHolder.mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String logid=((UserYear) t).value.get(position).id;
                    Intent intent=new Intent(mContext, CaseLogDetails.class);
                    intent.putExtra("logId",logid);
                    mContext.startActivity(intent);
                }
            });
        }




    }


    static class ViewHolder {
        @Bind(R.id.tv_year)
        TextView mTvYear;
        @Bind(R.id.fl_body)
        FrameLayout mFlBody;
        @Bind(R.id.lv)
        MyListView mLv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
