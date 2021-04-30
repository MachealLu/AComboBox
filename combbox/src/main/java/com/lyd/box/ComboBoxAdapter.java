package com.lyd.box;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.lyd.combbox.R;

import java.util.List;


public class ComboBoxAdapter<T> extends BaseAdapter {

    private final List<T> items;

    private ITextFormatter spinnerTextFormatter;
    private TextAlignment horizontalAlignment;

    private int textColor;
    private int backgroundSelector;

    int selectedIndex;

    ComboBoxAdapter(Context context, List<T> items, int textColor, int backgroundSelector,
                    ITextFormatter spinnerTextFormatter, TextAlignment horizontalAlignment) {

        this.spinnerTextFormatter = spinnerTextFormatter;
        this.backgroundSelector = backgroundSelector;
        this.horizontalAlignment = horizontalAlignment;
        this.textColor = textColor;
        this.items = items;
    }

    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        TextView textView;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.spinner_list_item, null);
            textView = (TextView) convertView.findViewById(R.id.text_view_spinner);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                textView.setBackground(ContextCompat.getDrawable(context, backgroundSelector));
            }
            convertView.setTag(new ViewHolder(textView));
        } else {
            textView = ((ViewHolder) convertView.getTag()).textView;
        }

        textView.setText(spinnerTextFormatter.format(getItem(position).toString()));
        textView.setTextColor(textColor);
        return convertView;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return items.size() - 1;
    }

    @Override
    public T getItem(int position) {
        if (position >= selectedIndex) {
            return items.get(position + 1);
        } else {
            return items.get(position);
        }
    }

    public T getItemAt(int position) {
        return items.get(position);
    }

    static class ViewHolder {
        TextView textView;
        ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }
}