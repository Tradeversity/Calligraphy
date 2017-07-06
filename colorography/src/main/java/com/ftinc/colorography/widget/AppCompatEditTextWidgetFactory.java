package com.ftinc.colorography.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.ftinc.colorography.ColorStateUtils;

public class AppCompatEditTextWidgetFactory implements WidgetFactory<AppCompatEditText> {

    @Override
    public Class<AppCompatEditText> getWidgetClass() {
        return AppCompatEditText.class;
    }


    @Override
    public void applyTint(AppCompatEditText view, @NonNull Context context, AttributeSet attrs, int tintColor) {
        ColorStateList csl = ColorStateUtils.createEditTextColorStateList(context, tintColor);
        ViewCompat.setBackgroundTintList(view, csl);
        view.setHighlightColor(tintColor);
        ColorStateUtils.setCursorColor(view, tintColor);
    }
}
