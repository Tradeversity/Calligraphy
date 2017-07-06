package com.ftinc.colorography;

import android.view.View;

import com.ftinc.colorography.widget.AppCompatButtonWidgetFactory;
import com.ftinc.colorography.widget.AppCompatCheckBoxWidgetFactory;
import com.ftinc.colorography.widget.AppCompatEditTextWidgetFactory;
import com.ftinc.colorography.widget.AppCompatRadioButtonWidgetFactory;
import com.ftinc.colorography.widget.AppCompatRatingBarWidgetFactory;
import com.ftinc.colorography.widget.CardViewWidgetFactory;
import com.ftinc.colorography.widget.CollapsingToolbarLayoutWidgetFactory;
import com.ftinc.colorography.widget.DefaultWidgetFactory;
import com.ftinc.colorography.widget.FloatingActionButtonWidgetFactory;
import com.ftinc.colorography.widget.ProgressBarWidgetFactory;
import com.ftinc.colorography.widget.SwitchCompatWidgetFactory;
import com.ftinc.colorography.widget.TextViewWidgetFactory;
import com.ftinc.colorography.widget.WidgetFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by chris on 20/12/2013
 * Project: Calligraphy
 */
public class ColorographyConfig {

    private static final List<WidgetFactory<? extends View>> DEFAULT_FACTORIES = new ArrayList<>();

    static {
        DEFAULT_FACTORIES.add(new FloatingActionButtonWidgetFactory());
        DEFAULT_FACTORIES.add(new CollapsingToolbarLayoutWidgetFactory());
        DEFAULT_FACTORIES.add(new AppCompatButtonWidgetFactory());
        DEFAULT_FACTORIES.add(new AppCompatCheckBoxWidgetFactory());
        DEFAULT_FACTORIES.add(new AppCompatEditTextWidgetFactory());
        DEFAULT_FACTORIES.add(new AppCompatRadioButtonWidgetFactory());
        DEFAULT_FACTORIES.add(new AppCompatRatingBarWidgetFactory());
        DEFAULT_FACTORIES.add(new CardViewWidgetFactory());
        DEFAULT_FACTORIES.add(new ProgressBarWidgetFactory());
        DEFAULT_FACTORIES.add(new SwitchCompatWidgetFactory());
        DEFAULT_FACTORIES.add(new TextViewWidgetFactory());
        DEFAULT_FACTORIES.add(new DefaultWidgetFactory());
    }


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


    private final List<WidgetFactory<? extends View>> mWidgetFactories;


    protected ColorographyConfig(Builder builder) {
        mAttrId = builder.attrId;
        mCustomViewCreation = builder.customViewCreation;
        final List<WidgetFactory<? extends View>> tempList = new ArrayList<>(DEFAULT_FACTORIES);
        tempList.addAll(builder.mWidgetFactoryList);
        mWidgetFactories = Collections.unmodifiableList(tempList);
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


    public List<WidgetFactory<? extends View>> getWidgetFactories() {
        return mWidgetFactories;
    }


    public static class Builder {
        public static final int INVALID_ATTR_ID = -1;
        private boolean customViewCreation = true;
        private int attrId = R.attr.themeColor;

        private List<WidgetFactory<? extends View>> mWidgetFactoryList = new ArrayList<>();

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


        public Builder addCustomWidgetFactory(final WidgetFactory<? extends View> widgetFactory) {
            mWidgetFactoryList.add(widgetFactory);
            return this;
        }


        public ColorographyConfig build() {
            return new ColorographyConfig(this);
        }
    }
}
