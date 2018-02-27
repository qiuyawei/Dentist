package com.jaydenxiao.common.commonutils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jaydenxiao.common.R;

import java.io.File;

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        RequestOptions options = new RequestOptions();
        options.error(error).placeholder(placeholder);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void display(Context context, ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void displaySmallPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();

        Glide.with(context).load(url)
                .apply(options)
                .into(imageView);
    }

    public static void displayBigPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .format(DecodeFormat.PREFER_ARGB_8888);

        Glide.with(context).load(url)
                .apply(options)
                .into(imageView);
    }

    public static void display(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();
        Glide.with(context).load(url)
                .apply(options)
                .into(imageView);
    }

    public static void displayRound(Context context, ImageView imageView, String url) {

        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        RequestOptions requestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.toux2);
//        Glide.with(context).load(Constant.IMAGE_API + newLogo).apply(requestOptions).into(iv_photo);

        Glide.with(context).load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    public static void displayRound(Context context, ImageView imageView, int resId) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        RequestOptions requestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.toux2);

        Glide.with(context).load(resId)
                .apply(requestOptions).into(imageView);
    }

}
