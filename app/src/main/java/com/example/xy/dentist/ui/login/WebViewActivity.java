package com.example.xy.dentist.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.example.xy.dentist.R;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import butterknife.Bind;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class WebViewActivity extends BaseActivity {
    @Bind(R.id.progress_bar)
    SmoothProgressBar progressBar;
    @Bind(R.id.web_view)
    WebView web_view;
    @Bind(R.id.video_view)
    FrameLayout videoview;
    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    String url = "http://www.baidu.com";

    private int flag=-1;
    private WebChromeClient.CustomViewCallback myCallback;
    private View xCustomView;
    private WebChromeClient.CustomViewCallback 	xCustomViewCallback;
    public static void launch(Activity from,String title,String url){
        Intent intent = new Intent(from,WebViewActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("url",url);
        from.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mNtb.setTitleText("详情");
        mNtb.setRightImagVisibility(false);
        mNtb.setLeftImagSrc(R.mipmap.back);
        String title = getIntent().getStringExtra("title");
        url= getIntent().getStringExtra("url");

    }

    @Override
    protected void initData() {

        WebSettings webSettings = web_view.getSettings();
        webSettings.setBuiltInZoomControls(true);// 隐藏缩放按钮
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//排版适应屏幕
        webSettings.setUseWideViewPort(true);//可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);//保存表单数据
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);// 启用地理定位
        webSettings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
        webSettings.setDomStorageEnabled(true);


        webSettings.setAllowFileAccess(true); // 允许访问文件

        webSettings.setPluginState(WebSettings.PluginState.ON);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        if(url.contains("http")){

            web_view.loadUrl(url);
        }else{
            web_view.loadDataWithBaseURL("http://avatar.csdn.net",url, "text/html", "UTF-8", null);

        }
        web_view.setWebChromeClient(new WebChromeClient() {


            //播放网络视频全屏时会调用的方法
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {


                fullScreen();
                web_view.setVisibility(View.GONE);

                if (xCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }

                videoview.addView(view);
                xCustomView = view;
                xCustomViewCallback = callback;
                videoview.setVisibility(View.VISIBLE);
/*
                if (myCallback != null) {
                    myCallback.onCustomViewHidden();
                    myCallback = null;
                    return;
                }
                mOriginalOrientation = getRequestedOrientation();
                long id = Thread.currentThread().getId();
                Log.v("WidgetChromeClient", "rong debug in showCustomView Ex: " + id);

                ViewGroup parent = (ViewGroup) web_view.getParent();
                String s = parent.getClass().getName();
                Log.v("WidgetChromeClient", "rong debug Ex: " + s);
                parent.removeView(web_view);
                parent.addView(view);
                parent.bringToFront();
                myView = view;
                myCallback = callback;
                super.onShowCustomView(view, callback);*/
            }


            @Override
            public void onHideCustomView() {


                long id = Thread.currentThread().getId();
                Log.v("WidgetChromeClient", "rong debug in hideCustom Ex: " + id);


                //   fullScreen();

                if (xCustomView == null)//不是全屏播放状态
                    return;

                // Hide the custom view.
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                xCustomView.setVisibility(View.GONE);

                // Remove the custom view from its container.
                videoview.removeView(xCustomView);
                xCustomView = null;
                videoview.setVisibility(View.GONE);
                xCustomViewCallback.onCustomViewHidden();

                web_view.setVisibility(View.VISIBLE);


                /*if (myView != null) {

                    if (myCallback != null) {
                        myCallback.onCustomViewHidden();
                        myCallback = null;
                    }

                    ViewGroup parent = (ViewGroup) myView.getParent();
                    parent.removeView(myView);
                    parent.addView(web_view);
                    myView = null;


                }

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                web_view.getSettings().setBuiltInZoomControls(true);

                super.onHideCustomView();*/

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (newProgress >= 100) {
                    progressBar.setVisibility(View.GONE);
                }
                progressBar.setProgress(newProgress);
            }

        });

        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                Log.i("onPageStarted", "错误");
                web_view.setVisibility(View.GONE);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i("onPageStarted", "onPage               Started" + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i("onPageFinished", "onPageFinished>>>>>>" + url);
                web_view.setVisibility(View.VISIBLE);

                super.onPageFinished(view, url);
            }


        });
    }

    @Override
    protected void initListener() {
        mNtb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inCustomView()) {
                    hideCustomView();
                }else {
                    //退出时加载空网址防止退出时还在播放视频
                    web_view.loadUrl("about:blank");
                    WebViewActivity.this.finish();
                    Log.i("testwebview", "===>>>2");
                }
                finish();
            }
        });

    }


    private void fullScreen() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            flag=0;
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            flag=1;
        }
    }

    @Override
    protected void onDestroy() {

        DestoryWebview();
        super.onDestroy();


    }

    private void DestoryWebview() {
        if (web_view != null) {
            ViewGroup parent = (ViewGroup) web_view.getParent();
            if (parent != null) {
                parent.removeView(web_view);
            }
            web_view.removeAllViews();
            web_view.destroy();
            web_view = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inCustomView()) {
                hideCustomView();
                return true;
            }else {
                //退出时加载空网址防止退出时还在播放视频
                web_view.loadUrl("about:blank");
                WebViewActivity.this.finish();
                Log.i("testwebview", "===>>>2");
            }
        }
        return true;
    }


    /**
     * 判断是否是全屏
     * @return
     */
    public boolean inCustomView() {
        return (xCustomView != null);
    }
    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {

        if (myCallback != null) {
            myCallback.onCustomViewHidden();
            myCallback = null ;
        }
    }

}
