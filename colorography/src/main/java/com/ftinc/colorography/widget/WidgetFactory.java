package com.ftinc.colorography.widget;


import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;


public interface WidgetFactory<T extends View> {

    Class<T> getWidgetClass();
    void applyTint(T view, @NonNull Context context, AttributeSet attrs, @ColorInt int tintColor);
}
