package com.ftinc.colorography.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

public class CardViewWidgetFactory implements WidgetFactory<CardView> {

    @Override
    public Class<CardView> getWidgetClass() {
        return CardView.class;
    }


    @Override
    public void applyTint(CardView view, @NonNull Context context, AttributeSet attrs, int tintColor) {
        view.setCardBackgroundColor(tintColor);
    }
}
