package com.amlogic.toolkit.infocollection.widgets;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Wenjie.Chen on 2017/10/17.
 */

public class FontManager {
    private static Typeface typeface = null;

    public static Typeface getTypeface(Context context) {
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), "msyhtj.ttf");
        }
        return typeface;
    }
}
