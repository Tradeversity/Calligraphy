package com.ftinc.colorography.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import com.ftinc.colorography.ColorStateUtils;

public class AppCompatCheckBoxWidgetFactory implements WidgetFactory<AppCompatCheckBox> {

    @Override
    public Class<AppCompatCheckBox> getWidgetClass() {
        return AppCompatCheckBox.class;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void applyTint(AppCompatCheckBox view, @NonNull Context context, AttributeSet attrs, int tintColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setButtonTintList(ColorStateUtils.createCheckboxColorStateList(context, tintColor));
        }
        else {
            view.setSupportButtonTintList(ColorStateUtils.createCheckboxColorStateList(context, tintColor));
        }
    }
}
