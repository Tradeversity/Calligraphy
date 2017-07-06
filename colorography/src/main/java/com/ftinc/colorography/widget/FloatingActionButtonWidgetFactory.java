package com.ftinc.colorography.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

public class FloatingActionButtonWidgetFactory implements WidgetFactory<FloatingActionButton> {

    @Override
    public Class<FloatingActionButton> getWidgetClass() {
        return FloatingActionButton.class;
    }


    @Override
    public void applyTint(FloatingActionButton view, @NonNull Context context, AttributeSet attrs, int tintColor) {
        view.setBackgroundTintList(ColorStateList.valueOf(tintColor));
    }
}
