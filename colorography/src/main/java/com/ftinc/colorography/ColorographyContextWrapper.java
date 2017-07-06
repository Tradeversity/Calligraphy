package com.ftinc.colorography;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by chris on 19/12/2013
 * Project: Calligraphy
 */
public class ColorographyContextWrapper extends ContextWrapper {

    private ColorographyLayoutInflater mInflater;

    private final int mAttributeId;
    private final ThemeColorProvider mThemeColorProvider;

    /**
     * Uses the default configuration from {@link ColorographyConfig}
     *
     * Remember if you are defining default in the
     * {@link ColorographyConfig} make sure this is initialised before
     * the activity is created.
     *
     * @param base ContextBase to Wrap.
     * @return ContextWrapper to pass back to the activity.
     */
    public static ContextWrapper wrap(Context base, ThemeColorProvider colorProvider) {
        return new ColorographyContextWrapper(base, colorProvider);
    }


    /**
     * Get the Calligraphy Activity Fragment Instance to allow callbacks for when views are created.
     *
     * @param activity The activity the original that the ContextWrapper was attached too.
     * @return Interface allowing you to call onActivityViewCreated
     */
    static ColorographyActivityFactory get(Activity activity) {
        if (!(activity.getLayoutInflater() instanceof ColorographyLayoutInflater)) {
            throw new RuntimeException("This activity does not wrap the Base Context! See CalligraphyContextWrapper.wrap(Context)");
        }
        return (ColorographyActivityFactory) activity.getLayoutInflater();
    }

    /**
     * Uses the default configuration from {@link ColorographyConfig}
     *
     * Remember if you are defining default in the
     * {@link ColorographyConfig} make sure this is initialised before
     * the activity is created.
     *
     * @param base ContextBase to Wrap
     */
    ColorographyContextWrapper(Context base, ThemeColorProvider colorProvider) {
        super(base);
        mAttributeId = ColorographyConfig.get().getAttrId();
        mThemeColorProvider = colorProvider;
    }

    /**
     * Override the default AttributeId, this will always take the custom attribute defined here
     * and ignore the one set in {@link ColorographyConfig}.
     *
     * Remember if you are defining default in the
     * {@link ColorographyConfig} make sure this is initialised before
     * the activity is created.
     *
     * @param base        ContextBase to Wrap
     * @param attributeId Attribute to lookup.
     */
    public ColorographyContextWrapper(Context base, int attributeId, ThemeColorProvider colorProvider) {
        super(base);
        mAttributeId = attributeId;
        mThemeColorProvider = colorProvider;
    }


    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = new ColorographyLayoutInflater(LayoutInflater.from(getBaseContext()), this, mAttributeId, mThemeColorProvider.getThemeColor(), false);
            }
            return mInflater;
        }
        return super.getSystemService(name);
    }

}
