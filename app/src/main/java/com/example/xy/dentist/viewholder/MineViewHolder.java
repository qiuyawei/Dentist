package com.example.xy.dentist.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.bean.TabBean;
import com.example.xy.dentist.listener.ItemClickListener;
import com.example.xy.dentist.listener.ItemLongClickListener;
import com.example.xy.dentist.presenter.UserPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XY on 2017/10/12.
 */
public class MineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;
    private int type;
    private View itemView;
    private final ViewHolder mViewHolder;
    private UserPresenter mPresenter;
    private ItemClickListener mListener;
    private ItemLongClickListener mLongClickListener;

    public MineViewHolder(View itemView, final Context context, int type, ItemClickListener listener, ItemLongClickListener longClickListener) {
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
        MineViewHolder imageViewHolder = new MineViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mine, null), context, viewType, listener, longClickListener);
        return imageViewHolder;
    }

    public <T> void setData(UserPresenter presenter, final T t, final int position) {
        this.mPresenter = presenter;
        if(t instanceof TabBean){
            mViewHolder.mTvTitle.setText(((TabBean) t).key);
            mViewHolder.mTvContent.setText(((TabBean) t).value);


        }
       /* switch (position){
            case 0:
                mViewHolder.mTvTitle.setText("医院介绍");
                mViewHolder.mTvContent.setText("上海交通大学口腔临床硕士\n"+"中华口腔医学会儿童口腔医学会员\n"+"口腔主治医师");

                break;
            case 1:
                mViewHolder.mTvTitle.setText("擅长");
                mViewHolder.mTvContent.setText("“医师，掌医之政令。”《敦煌变文集·欢喜国王缘》：“便唤医师寻妙药，即求方术拟案（安）魂。”《元典章·礼部五·医学》：“各处有司广设学校，为医师者，命一通晓经书良医主之。”中国古代很早就出现了医师的形象。随着历史的发展");

                break;
            case 2:
                mViewHolder.mTvTitle.setText("履历");
                mViewHolder.mTvContent.setText("中国医师协会是经国家民政部登记注册，由执业医师、执业助理医师及单位会员自愿组成的全国性、行业性、非营利性的群众团体，是国家一级协会，是独立的法人社团。 本会的宗旨是发挥行业服务、协调、自律、维权、监督、管理作用，团结和组织全国医师遵守国家宪法、法律、法规和政策，弘扬以德为本，救死扶伤人道主义的职业道德，努力提高医疗水平和服务质量，维护医师的合法权益，为我国人民的健康和社会主义建设服务");

                break;
        }*/

        mViewHolder.mLlInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, position);
            }
        });


    }

    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.ll_info)
        LinearLayout mLlInfo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
