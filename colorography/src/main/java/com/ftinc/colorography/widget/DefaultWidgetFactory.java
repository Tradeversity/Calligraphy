package com.ftinc.colorography.widget;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;


public class DefaultWidgetFactory implements WidgetFactory<View> {

    @Override
    public Class<View> getWidgetClass() {
        return View.class;
    }


    @Override
    public void applyTint(View view, @NonNull Context context, AttributeSet attrs, int tintColor) {
        view.setBackgroundColor(tintColor);
    }
}
