package com.lyd.box;

import android.text.Spannable;
import android.text.SpannableString;

public class DefaultTextFormatter implements ITextFormatter {

    @Override
    public Spannable format(Object text) {
        return new SpannableString(text.toString());
    }
}
