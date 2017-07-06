package com.ftinc.colorography;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.Field;


class ColorographyFactory {

    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE = new ThreadLocal<>();
    static final int[] DISABLED_STATE_SET = new int[]{-android.R.attr.state_enabled};
    static final int[] FOCUSED_STATE_SET = new int[]{android.R.attr.state_focused};
    static final int[] PRESSED_STATE_SET = new int[]{android.R.attr.state_pressed};
    static final int[] CHECKED_STATE_SET = new int[]{android.R.attr.state_checked};
    static final int[] UNPRESSED_UNFOCUSED_STATE_SET = new int[]{-android.R.attr.state_focused,-android.R.attr.state_pressed};
    static final int[] EMPTY_STATE_SET = new int[0];
    private static final int[] TEMP_ARRAY = new int[1];

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
        if (view != null && view.getTag(R.id.calligraphy_tag_id) != Boolean.TRUE) {
            onViewCreatedInternal(view, context, attrs);
            view.setTag(R.id.calligraphy_tag_id, Boolean.TRUE);
        }
        return view;
    }


    @SuppressLint("RestrictedApi")
    void onViewCreatedInternal(View view, final Context context, AttributeSet attrs) {
        int defaultColor = resolveDefaultColor(context, attrs);
        if (defaultColor != -1) {
            int themeColor = mThemeColor == -1 ? defaultColor : mThemeColor;
            if (view instanceof FloatingActionButton) {
                FloatingActionButton fab = (FloatingActionButton) view;
                fab.setBackgroundTintList(ColorStateList.valueOf(themeColor));
            }
            else if (view instanceof CollapsingToolbarLayout) {
                CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) view;
                ctl.setContentScrimColor(themeColor);
            }
            else if (view instanceof AppCompatButton) {
                AppCompatButton button = (AppCompatButton) view;
                ColorStateList originalTintList = ViewCompat.getBackgroundTintList(button);
                ColorStateList csl = createButtonColorStateList(context, themeColor, originalTintList);
                ViewCompat.setBackgroundTintList(button, csl);
            }
            else if (view instanceof AppCompatEditText) {
                AppCompatEditText editText = (AppCompatEditText) view;
                ColorStateList csl = createEditTextColorStateList(context, themeColor);
                ViewCompat.setBackgroundTintList(editText, csl);
                editText.setHighlightColor(themeColor);
                setCursorColor(editText, themeColor);
            }
            else if (view instanceof AppCompatCheckBox) {
                AppCompatCheckBox checkBox = (AppCompatCheckBox) view;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    checkBox.setButtonTintList(createCheckboxColorStateList(context, themeColor));
                }
                else {
                    checkBox.setSupportButtonTintList(createCheckboxColorStateList(context, themeColor));
                }
            }
            else if (view instanceof AppCompatRatingBar) {
                AppCompatRatingBar ratingBar = (AppCompatRatingBar) view;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ratingBar.setProgressTintList(ColorStateList.valueOf(themeColor));
                }
                else {
                    ratingBar.getProgressDrawable().setColorFilter(themeColor, PorterDuff.Mode.SRC_IN);
                }
            }
            else if (view instanceof SwitchCompat) {
                SwitchCompat sw = (SwitchCompat) view;
                sw.setThumbTintList(createSwitchThumbColorStateList(context, themeColor));
                sw.setTrackTintList(createSwitchTrackColorStateList(context, themeColor));
            }
            else if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setTextColor(themeColor);
                Drawable[] drawables = textView.getCompoundDrawablesRelative();
                for (int i = 0; i < drawables.length; i++) {
                    tintDrawable(drawables[i], themeColor);
                }
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);
            }
            else if (view instanceof CardView) {
                CardView cardView = (CardView) view;
                cardView.setCardBackgroundColor(themeColor);
            }
            else if (view instanceof ProgressBar) {
                ProgressBar progressBar = (ProgressBar) view;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    progressBar.setIndeterminateTintList(ColorStateList.valueOf(themeColor));
                }
                else {
                    progressBar.getIndeterminateDrawable().setColorFilter(themeColor, PorterDuff.Mode.SRC_IN);
                }
            }
            else {
                view.setBackgroundColor(themeColor);
            }
        }
    }


    private ColorStateList createCheckboxColorStateList(@NonNull final Context context,
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


    private ColorStateList createSwitchThumbColorStateList(@NonNull final Context context,
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


    private ColorStateList createSwitchTrackColorStateList(@NonNull final Context context,
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


    private ColorStateList createEditTextColorStateList(@NonNull final Context context,
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


    private ColorStateList createButtonColorStateList(@NonNull final Context context,
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


    public static int getThemeAttrColor(Context context, int attr) {
        TEMP_ARRAY[0] = attr;
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, null, TEMP_ARRAY);
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }


    public static float getThemeAttrAlpha(Context context, int attr) {
        TEMP_ARRAY[0] = attr;
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, null, TEMP_ARRAY);
        try {
            return a.getFloat(0, 1f);
        } finally {
            a.recycle();
        }
    }


    public static ColorStateList getThemeAttrColorStateList(Context context, int attr) {
        TEMP_ARRAY[0] = attr;
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, null, TEMP_ARRAY);
        try {
            return a.getColorStateList(0);
        } finally {
            a.recycle();
        }
    }


    public static int getDisabledThemeAttrColor(Context context, int attr) {
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


    static int getThemeAttrColor(Context context, int attr, float alpha) {
        final int color = getThemeAttrColor(context, attr);
        final int originalAlpha = Color.alpha(color);
        return ColorUtils.setAlphaComponent(color, Math.round(originalAlpha * alpha));
    }


    @ColorInt
    private int resolveDefaultColor(Context context, AttributeSet attrs) {
        return ColorographyUtils.pullDefaultColorFromView(context, attrs, mAttributeId);
    }

    private static void setCursorColor(EditText view, @ColorInt int color) {
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


    private static void tintDrawable(@Nullable Drawable drawable, @ColorInt int color) {
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, color);
        }
    }
}
