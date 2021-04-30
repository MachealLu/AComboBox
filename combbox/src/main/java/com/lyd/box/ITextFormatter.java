package com.lyd.box;

import android.text.Spannable;

public interface ITextFormatter<T> {
    Spannable format(T text);
}
