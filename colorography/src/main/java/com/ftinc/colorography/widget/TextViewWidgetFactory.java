package com.ftinc.colorography.widget;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ftinc.colorography.ColorStateUtils;


public class TextViewWidgetFactory implements WidgetFactory<TextView> {

    @Override
    public Class<TextView> getWidgetClass() {
        return TextView.class;
    }


    @Override
    public void applyTint(TextView view, @NonNull Context context, AttributeSet attrs, int tintColor) {
        view.setTextColor(tintColor);
        Drawable[] drawables = view.getCompoundDrawablesRelative();
        for (int i = 0; i < drawables.length; i++) {
            ColorStateUtils.tintDrawable(drawables[i], tintColor);
        }
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);
        view.setLinkTextColor(tintColor);
    }
}
