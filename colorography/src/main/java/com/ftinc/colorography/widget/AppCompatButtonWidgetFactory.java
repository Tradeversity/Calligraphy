package com.ftinc.colorography.widget;


import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.ftinc.colorography.ColorStateUtils;


public class AppCompatButtonWidgetFactory implements WidgetFactory<AppCompatButton> {

    @Override
    public Class<AppCompatButton> getWidgetClass() {
        return AppCompatButton.class;
    }


    @Override
    public void applyTint(AppCompatButton view, @NonNull Context context, AttributeSet attrs, int tintColor) {
        ColorStateList originalTintList = ViewCompat.getBackgroundTintList(view);
        ColorStateList csl = ColorStateUtils.createButtonColorStateList(context, tintColor, originalTintList);
        ViewCompat.setBackgroundTintList(view, csl);
    }
}
