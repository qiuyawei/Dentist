package com.aspsine.irecyclerview.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.irecyclerview.R;
import com.aspsine.irecyclerview.RefreshTrigger;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by jaydenxiao on 16/9/7.
 */
public class AndroidVsIosHeaderView extends FrameLayout implements RefreshTrigger {
    private AnimationDrawable animationPull;
    private AnimationDrawable animationPullFan;
    private AnimationDrawable animationRefresh;
    private Context context;
    private ImageView header_img;
    private int[] pullAnimSrcs = new int[]{R.drawable.mt_pull,R.drawable.mt_pull01,R.drawable.mt_pull02,R.drawable.mt_pull03,R.drawable.mt_pull04,R.drawable.mt_pull05};
    private int[] refreshAnimSrcs = new int[]{R.drawable.mt_refreshing01,R.drawable.mt_refreshing02,R.drawable.mt_refreshing03,R.drawable.mt_refreshing04,R.drawable.mt_refreshing05,R.drawable.mt_refreshing06};
    private TextView tv_time,tv_state;
    private long mCurrentTime;
    private SimpleDateFormat mFormatter;
    private Date mDate;

    public AndroidVsIosHeaderView(Context context) {
        this(context, null);
    }

    public AndroidVsIosHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AndroidVsIosHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_irecyclerview_bat_vs_supper_refresh_header_view, this);
        if (pullAnimSrcs!=null) this.pullAnimSrcs = pullAnimSrcs;
        if (refreshAnimSrcs!=null) this.refreshAnimSrcs = refreshAnimSrcs;
        animationPull = new AnimationDrawable();
        animationPullFan = new AnimationDrawable();
        animationRefresh = new AnimationDrawable();
        for (int i=1;i< this.pullAnimSrcs.length;i++) {
            animationPull.addFrame(ContextCompat.getDrawable(context, this.pullAnimSrcs[i]),100);
            animationRefresh.setOneShot(true);
        }
        for (int i= this.pullAnimSrcs.length-1;i>=0;i--){
            animationPullFan.addFrame(ContextCompat.getDrawable(context, this.pullAnimSrcs[i]), 100);
            animationRefresh.setOneShot(true);
        }
        for (int src: this.refreshAnimSrcs) {
            animationRefresh.addFrame(ContextCompat.getDrawable(context, src),150);
            animationRefresh.setOneShot(false);
        }



    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        header_img = (ImageView)findViewById(R.id.meituan_header_img);
        tv_time= (TextView)findViewById(R.id.tv_time);
        tv_state= (TextView) findViewById(R.id.tv_state);
        if (pullAnimSrcs !=null&& pullAnimSrcs.length>0)
            header_img.setImageResource(pullAnimSrcs[0]);
    }

    @Override
    public void onStart(boolean automatic, int headerHeight, int finalHeight) {
        if (!automatic){
            header_img.setImageDrawable(animationPull);
            animationPull.start();
        }else {
            header_img.setImageDrawable(animationPullFan);
            animationPullFan.start();
        }
    }

    @Override
    public void onMove(boolean finished, boolean automatic, int dy) {

    }

    @Override
    public void onRefresh() {
        header_img.setImageDrawable(animationRefresh);

        animationRefresh.start();
        mCurrentTime = System.currentTimeMillis();
        mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mDate = new Date(mCurrentTime);
        tv_time.setText("最后更新时间" + mFormatter.format(mDate));

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        if (pullAnimSrcs !=null&& pullAnimSrcs.length>0)
            header_img.setImageResource(pullAnimSrcs[0]);
    }

    @Override
    public void onReset() {

    }
}
