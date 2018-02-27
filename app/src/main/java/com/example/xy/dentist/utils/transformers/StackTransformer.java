package com.example.xy.dentist.utils.transformers;

import android.view.View;

/**
 * ViewPager叠加式翻页
 * Created by Wallace on 2016/2/18.
 */
public class StackTransformer extends ABaseTransformer{
    @Override
    protected void onTransform(View view, float position) {
        view.setTranslationX(position < 0 ? 0f : -view.getWidth() * position);
    }
}
