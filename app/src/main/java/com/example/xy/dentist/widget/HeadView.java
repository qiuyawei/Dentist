package com.example.xy.dentist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xy.dentist.R;
import com.example.xy.dentist.api.ApiConstants;
import com.example.xy.dentist.bean.UserInfo;
import com.jaydenxiao.common.commonutils.FormatUtil;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;

/**
 * Created by XY on 2017/9/1.
 */
public class HeadView extends LinearLayout {
    private ImageView iv_head;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_year;

    public HeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public HeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadView(Context context) {
        this(context, null);
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.head_layout, null);
        iv_head = (ImageView) view.findViewById(R.id.iv_head);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_year = (TextView) view.findViewById(R.id.tv_year);
        addView(view);
    }


    /**
     * 设置基本信息
     *
     * @param info
     */
    public void setData(UserInfo info) {
        if (info != null) {
            tv_name.setText(FormatUtil.checkValue(info.name));
            tv_year.setText(FormatUtil.checkValue(info.age + "岁"));
            tv_phone.setText(FormatUtil.checkValue(info.phone));
            ImageLoaderUtils.displayRound(getContext(), iv_head, ApiConstants.IMAGE_URL + info.avatar);
        }
    }
}
