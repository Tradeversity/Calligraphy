package com.ftinc.colorography.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ProgressBarWidgetFactory implements WidgetFactory<ProgressBar> {

    @Override
    public Class<ProgressBar> getWidgetClass() {
        return ProgressBar.class;
    }


    @Override
    public void applyTint(ProgressBar view, @NonNull Context context, AttributeSet attrs, int tintColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setIndeterminateTintList(ColorStateList.valueOf(tintColor));
        }
        else {
            view.getIndeterminateDrawable().setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        }
    }
}
