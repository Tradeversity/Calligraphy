package com.ftinc.colorography;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by chris on 20/12/2013
 * Project: Calligraphy
 * Modified by drew.heaver on 07/06/2017
 */
public final class ColorographyUtils {

    private ColorographyUtils() {
    }


    static int pullDefaultColorFromView(Context context, AttributeSet attrs, int[] attributeId) {
        if (attributeId == null || attrs == null)
            return -1;

        final String attributeName;
        try {
            attributeName = context.getResources().getResourceEntryName(attributeId[0]);
        } catch (Resources.NotFoundException e) {
            // invalid attribute ID
            return -1;
        }

        final int colorResourceId = attrs.getAttributeResourceValue(null, attributeName, -1);
        return colorResourceId > 0
                ? ContextCompat.getColor(context, colorResourceId)
                : attrs.getAttributeIntValue(null, attributeName, -1);
    }
}
