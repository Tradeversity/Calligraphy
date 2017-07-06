package com.ftinc.colorography.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.AttributeSet;

public class AppCompatRatingBarWidgetFactory implements WidgetFactory<AppCompatRatingBar> {

    @Override
    public Class<AppCompatRatingBar> getWidgetClass() {
        return AppCompatRatingBar.class;
    }


    @Override
    public void applyTint(AppCompatRatingBar view, @NonNull Context context, AttributeSet attrs, int tintColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setProgressTintList(ColorStateList.valueOf(tintColor));
        }
        else {
            view.getProgressDrawable().setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        }
    }
}
