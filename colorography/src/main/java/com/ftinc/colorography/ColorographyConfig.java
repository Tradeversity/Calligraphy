package com.ftinc.colorography;

import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Created by chris on 20/12/2013
 * Project: Calligraphy
 */
public class ColorographyConfig {


    private static ColorographyConfig sInstance;

    /**
     * Set the default Calligraphy Config
     *
     * @param calligraphyConfig the config build using the builder.
     * @see ColorographyConfig.Builder
     */
    public static void initDefault(ColorographyConfig calligraphyConfig) {
        sInstance = calligraphyConfig;
    }

    /**
     * The current Calligraphy Config.
     * If not set it will create a default config.
     */
    public static ColorographyConfig get() {
        if (sInstance == null)
            sInstance = new ColorographyConfig(new Builder());
        return sInstance;
    }

    /**
     * Default Font Path Attr Id to lookup
     */
    private final int mAttrId;

    /**
     * Use Reflection to intercept CustomView inflation with the correct Context.
     */
    private final boolean mCustomViewCreation;


    protected ColorographyConfig(Builder builder) {
        mAttrId = builder.attrId;
        mCustomViewCreation = builder.customViewCreation;
    }


    public boolean isCustomViewCreation() {
        return mCustomViewCreation;
    }


    /**
     * @return the custom attrId to look for, -1 if not set.
     */
    public int getAttrId() {
        return mAttrId;
    }


    public static class Builder {
        public static final int INVALID_ATTR_ID = -1;
        private boolean customViewCreation = true;
        private int attrId = R.attr.fontPath;

        private Map<Class<? extends TextView>, Integer> mStyleClassMap = new HashMap<>();
        private Set<Class<?>> mHasTypefaceClasses = new HashSet<>();

        /**
         * This defaults to R.attr.fontPath. So only override if you want to use your own attrId.
         *
         * @param themeColorAttrId the custom attribute to look for color theming and the default color
         * @return this builder.
         */
        public Builder setThemeColorAttrId(int themeColorAttrId) {
            this.attrId = themeColorAttrId;
            return this;
        }


        /**
         * Due to the poor inflation order where custom views are created and never returned inside an
         * {@code onCreateView(...)} method. We have to create CustomView's at the latest point in the
         * overrideable injection flow.
         *
         * On HoneyComb+ this is inside the {@link android.app.Activity#onCreateView(android.view.View, String, android.content.Context, android.util.AttributeSet)}
         * Pre HoneyComb this is in the {@link android.view.LayoutInflater.Factory#onCreateView(String, android.util.AttributeSet)}
         *
         * We wrap base implementations, so if you LayoutInflater/Factory/Activity creates the
         * custom view before we get to this point, your view is used. (Such is the case with the
         * TintEditText etc)
         *
         * The problem is, the native methods pass there parents context to the constructor in a really
         * specific place. We have to mimic this in {@link ColorographyLayoutInflater#createCustomViewInternal(android.view.View, android.view.View, String, android.content.Context, android.util.AttributeSet)}
         * To mimic this we have to use reflection as the Class constructor args are hidden to us.
         *
         * We have discussed other means of doing this but this is the only semi-clean way of doing it.
         * (Without having to do proxy classes etc).
         *
         * Calling this will of course speed up inflation by turning off reflection, but not by much,
         * But if you want Calligraphy to inject the correct typeface then you will need to make sure your CustomView's
         * are created before reaching the LayoutInflater onViewCreated.
         */
        public Builder disableCustomViewInflation() {
            this.customViewCreation = false;
            return this;
        }

        /**
         * Add a custom style to get looked up. If you use a custom class that has a parent style
         * which is not part of the default android styles you will need to add it here.
         *
         * The Calligraphy inflater is unaware of custom styles in your custom classes. We use
         * the class type to look up the style attribute in the theme resources.
         *
         * So if you had a {@code MyTextField.class} which looked up it's default style as
         * {@code R.attr.textFieldStyle} you would add those here.
         *
         * {@code builder.addCustomStyle(MyTextField.class,R.attr.textFieldStyle}
         *
         * @param styleClass             the class that related to the parent styleResource. null is ignored.
         * @param styleResourceAttribute e.g. {@code R.attr.textFieldStyle}, 0 is ignored.
         * @return this builder.
         */
        public Builder addCustomStyle(final Class<? extends TextView> styleClass, final int styleResourceAttribute) {
            if (styleClass == null || styleResourceAttribute == 0) return this;
            mStyleClassMap.put(styleClass, styleResourceAttribute);
            return this;
        }


        public ColorographyConfig build() {
            return new ColorographyConfig(this);
        }
    }
}
