package com.ftinc.colorography;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import com.ftinc.colorography.widget.WidgetFactory;

import java.util.List;


class ColorographyFactory {

    /**
     * Use to match a view against a potential view id. Such as ActionBar title etc.
     *
     * @param view    not null view you want to see has resource matching name.
     * @param matches not null resource name to match against. Its not case sensitive.
     * @return true if matches false otherwise.
     */
    protected static boolean matchesResourceIdName(View view, String matches) {
        if (view.getId() == View.NO_ID) return false;
        final String resourceEntryName = view.getResources().getResourceEntryName(view.getId());
        return resourceEntryName.equalsIgnoreCase(matches);
    }

    private final int[] mAttributeId;
    private final int mThemeColor;


    public ColorographyFactory(int attributeId, int themeColor) {
        this.mAttributeId = new int[]{attributeId};
        this.mThemeColor = themeColor;
    }


    public View onViewCreated(View view, Context context, AttributeSet attrs) {
        if (view != null && view.getTag(R.id.colorography_tag_id) != Boolean.TRUE) {
            onViewCreatedInternal(view, context, attrs);
            view.setTag(R.id.colorography_tag_id, Boolean.TRUE);
        }
        return view;
    }


    @SuppressLint("RestrictedApi")
    void onViewCreatedInternal(View view, final Context context, AttributeSet attrs) {
        int defaultColor = resolveDefaultColor(context, attrs);
        if (defaultColor != -1) {
            int themeColor = mThemeColor == -1 ? defaultColor : mThemeColor;

            List<WidgetFactory<? extends View>> widgetFactories = ColorographyConfig.get().getWidgetFactories();
            for (int i = 0; i < widgetFactories.size(); i++) {
                WidgetFactory<? extends View> factory = widgetFactories.get(i);
                Class<? extends View> clazz = factory.getWidgetClass();
                if (clazz.isInstance(view)) {
                    apply(factory, view, context, attrs, themeColor);
                    break;
                }
            }
        }
    }


    private <T extends View> void apply(WidgetFactory<T> factory, View view, Context context, AttributeSet attrs, int tintColor) {
        Class<T> clazz = factory.getWidgetClass();
        T castedView = clazz.cast(view);
        factory.applyTint(castedView, context, attrs, tintColor);
    }


    @ColorInt
    private int resolveDefaultColor(Context context, AttributeSet attrs) {
        return ColorographyUtils.pullDefaultColorFromView(context, attrs, mAttributeId);
    }
}
