package com.ftinc.colorography.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;

public class CollapsingToolbarLayoutWidgetFactory implements WidgetFactory<CollapsingToolbarLayout> {

    @Override
    public Class<CollapsingToolbarLayout> getWidgetClass() {
        return CollapsingToolbarLayout.class;
    }


    @Override
    public void applyTint(CollapsingToolbarLayout view, @NonNull Context context, AttributeSet attrs, int tintColor) {
        view.setContentScrimColor(tintColor);
    }
}
