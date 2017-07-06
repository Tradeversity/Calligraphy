package com.ftinc.colorography.widget;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

import com.ftinc.colorography.ColorStateUtils;


public class SwitchCompatWidgetFactory implements WidgetFactory<SwitchCompat> {

    @Override
    public Class<SwitchCompat> getWidgetClass() {
        return SwitchCompat.class;
    }


    @Override
    public void applyTint(SwitchCompat view, @NonNull Context context, AttributeSet attrs, int tintColor) {
        view.setThumbTintList(ColorStateUtils.createSwitchThumbColorStateList(context, tintColor));
        view.setTrackTintList(ColorStateUtils.createSwitchTrackColorStateList(context, tintColor));
    }
}
