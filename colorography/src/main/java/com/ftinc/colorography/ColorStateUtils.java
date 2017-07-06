package com.ftinc.colorography;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

public final class ColorStateUtils {

    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE = new ThreadLocal<>();
    private static final int[] DISABLED_STATE_SET = new int[]{-android.R.attr.state_enabled};
    private static final int[] FOCUSED_STATE_SET = new int[]{android.R.attr.state_focused};
    private static final int[] PRESSED_STATE_SET = new int[]{android.R.attr.state_pressed};
    private static final int[] CHECKED_STATE_SET = new int[]{android.R.attr.state_checked};
    private static final int[] UNPRESSED_UNFOCUSED_STATE_SET = new int[]{
            -android.R.attr.state_focused,-android.R.attr.state_pressed };
    private static final int[] EMPTY_STATE_SET = new int[0];
    private static final int[] TEMP_ARRAY = new int[1];


    private ColorStateUtils() {
    }


    public static ColorStateList createCheckboxColorStateList(@NonNull final Context context,
                                                        @ColorInt final int baseColor) {
        final int[][] states = new int[3][];
        final int[] colors = new int[3];
        int i = 0;

        final int colorSwitchThumbNormal = getThemeAttrColor(context, R.attr.colorControlNormal);
        final int disabledColor = getDisabledThemeAttrColor(context, R.attr.colorButtonNormal);

        states[i] = DISABLED_STATE_SET;
        colors[i] = disabledColor;
        i++;

        states[i] = CHECKED_STATE_SET;
        colors[i] = baseColor;
        i++;

        states[i] = EMPTY_STATE_SET;
        colors[i] = colorSwitchThumbNormal;
        i++;

        return new ColorStateList(states, colors);
    }


    public static ColorStateList createSwitchThumbColorStateList(@NonNull final Context context,
                                                           @ColorInt final int baseColor) {
        final int[][] states = new int[3][];
        final int[] colors = new int[3];
        int i = 0;

        final int colorSwitchThumbNormal = getThemeAttrColor(context, R.attr.colorSwitchThumbNormal);
        final int disabledColor = getDisabledThemeAttrColor(context, R.attr.colorButtonNormal);

        states[i] = DISABLED_STATE_SET;
        colors[i] = disabledColor;
        i++;

        states[i] = CHECKED_STATE_SET;
        colors[i] = baseColor;
        i++;

        states[i] = EMPTY_STATE_SET;
        colors[i] = colorSwitchThumbNormal;
        i++;

        return new ColorStateList(states, colors);
    }


    public static ColorStateList createSwitchTrackColorStateList(@NonNull final Context context,
                                                           @ColorInt final int baseColor) {
        final int[][] states = new int[3][];
        final int[] colors = new int[3];
        int i = 0;

        final int foregroundColor = getThemeAttrColor(context, android.R.attr.colorForeground);
        final int disabledColor = ColorUtils.setAlphaComponent(foregroundColor , (int)(.1f * 255));
        final int checkedColor = ColorUtils.setAlphaComponent(baseColor, (int)(.3f * 255));
        final int emptyColor = ColorUtils.setAlphaComponent(foregroundColor, (int)(.3f * 255));

        states[i] = DISABLED_STATE_SET;
        colors[i] = disabledColor;
        i++;

        states[i] = CHECKED_STATE_SET;
        colors[i] = checkedColor;
        i++;

        states[i] = EMPTY_STATE_SET;
        colors[i] = emptyColor;
        i++;

        return new ColorStateList(states, colors);
    }


    public static ColorStateList createEditTextColorStateList(@NonNull final Context context,
                                                        @ColorInt final int baseColor) {
        final int[][] states = new int[3][];
        final int[] colors = new int[3];
        int i = 0;

        final int colorControlNormal = getThemeAttrColor(context, R.attr.colorControlNormal);
        final int disabledColor = getDisabledThemeAttrColor(context, R.attr.colorButtonNormal);

        states[i] = DISABLED_STATE_SET;
        colors[i] = disabledColor;
        i++;

        states[i] = UNPRESSED_UNFOCUSED_STATE_SET;
        colors[i] = colorControlNormal;
        i++;

        states[i] = EMPTY_STATE_SET;
        colors[i] = baseColor;
        i++;

        return new ColorStateList(states, colors);
    }


    public static ColorStateList createButtonColorStateList(@NonNull final Context context,
                                                      @ColorInt final int baseColor,
                                                      @Nullable final ColorStateList tint) {
        final int[][] states = new int[4][];
        final int[] colors = new int[4];
        int i = 0;

        final int colorControlHighlight = getThemeAttrColor(context, R.attr.colorControlHighlight);
        final int disabledColor = getDisabledThemeAttrColor(context, R.attr.colorButtonNormal);

        // Disabled state
        states[i] = DISABLED_STATE_SET;
        colors[i] = tint == null ? disabledColor : tint.getColorForState(states[i], 0);
        i++;

        states[i] = PRESSED_STATE_SET;
        colors[i] = ColorUtils.compositeColors(colorControlHighlight,
                tint == null ? baseColor : tint.getColorForState(states[i], 0));
        i++;

        states[i] = FOCUSED_STATE_SET;
        colors[i] = ColorUtils.compositeColors(colorControlHighlight,
                tint == null ? baseColor : tint.getColorForState(states[i], 0));
        i++;

        // Default enabled state
        states[i] = EMPTY_STATE_SET;
        colors[i] = tint == null ? baseColor : tint.getColorForState(states[i], 0);
        i++;

        return new ColorStateList(states, colors);
    }


    private static int getThemeAttrColor(Context context, int attr) {
        TEMP_ARRAY[0] = attr;
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, null, TEMP_ARRAY);
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }


    private static float getThemeAttrAlpha(Context context, int attr) {
        TEMP_ARRAY[0] = attr;
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, null, TEMP_ARRAY);
        try {
            return a.getFloat(0, 1f);
        } finally {
            a.recycle();
        }
    }


    private static ColorStateList getThemeAttrColorStateList(Context context, int attr) {
        TEMP_ARRAY[0] = attr;
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, null, TEMP_ARRAY);
        try {
            return a.getColorStateList(0);
        } finally {
            a.recycle();
        }
    }


    private static int getDisabledThemeAttrColor(Context context, int attr) {
        final ColorStateList csl = getThemeAttrColorStateList(context, attr);
        if (csl != null && csl.isStateful()) {
            // If the CSL is stateful, we'll assume it has a disabled state and use it
            return csl.getColorForState(DISABLED_STATE_SET, csl.getDefaultColor());
        } else {
            // Else, we'll generate the color using disabledAlpha from the theme

            final TypedValue tv = getTypedValue();
            // Now retrieve the disabledAlpha value from the theme
            context.getTheme().resolveAttribute(android.R.attr.disabledAlpha, tv, true);
            final float disabledAlpha = tv.getFloat();

            return getThemeAttrColor(context, attr, disabledAlpha);
        }
    }


    private static TypedValue getTypedValue() {
        TypedValue typedValue = TL_TYPED_VALUE.get();
        if (typedValue == null) {
            typedValue = new TypedValue();
            TL_TYPED_VALUE.set(typedValue);
        }
        return typedValue;
    }


    private static int getThemeAttrColor(Context context, int attr, float alpha) {
        final int color = getThemeAttrColor(context, attr);
        final int originalAlpha = Color.alpha(color);
        return ColorUtils.setAlphaComponent(color, Math.round(originalAlpha * alpha));
    }


    public static void setCursorColor(EditText view, @ColorInt int color) {
        try {
            // Get the cursor resource id
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(view);

            // Get the editor
            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(view);

            // Get the drawable and set a color filter
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {drawable, drawable};

            // Set the drawables
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }


    public static void tintDrawable(@Nullable Drawable drawable, @ColorInt int color) {
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, color);
        }
    }
}
