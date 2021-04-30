package com.lyd.box.box1;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.lyd.box.ITextFormatter;
import com.lyd.box.TextAlignment;
import com.lyd.combbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * For gridview and listview
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected Context mContext;
    private final List<T> mDatas;

    private int mLayoutResId;

    public BaseListAdapter(List<T> data) {
        this(0, data);
    }

    public BaseListAdapter(int layoutResId) {
        this(layoutResId, null);
    }

    private ITextFormatter spinnerTextFormatter;
    private TextAlignment horizontalAlignment;

    private int textColor;
    private int backgroundSelector;

    int selectedIndex;

    public BaseListAdapter(List<T> data, int textColor, int backgroundSelector,
                        ITextFormatter spinnerTextFormatter, TextAlignment horizontalAlignment) {
        this.spinnerTextFormatter = spinnerTextFormatter;
        this.backgroundSelector = backgroundSelector;
        this.horizontalAlignment = horizontalAlignment;
        this.textColor = textColor;

        this.mDatas = data == null ? new ArrayList<T>() : data;
    }

    public BaseListAdapter(int layoutResId, List<T> data) {
        this.mDatas = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
    }

    public void addAll(List<T> items) {
        this.mDatas.addAll(items);
        notifyDataSetChanged();
    }

    public void removeData(int location) {
        if (mDatas == null || mDatas.isEmpty()) {
            return;
        }
        mDatas.remove(location);
        notifyDataSetChanged();
    }

    public void updateData(int location, T item) {
        if (mDatas.isEmpty()) return;
        mDatas.set(location, item);
        notifyDataSetChanged();
    }

    public void addData(T item) {
        mDatas.add(item);
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public T getData(int position) {
        return mDatas.get(position);
    }

    public final void clear() {
        this.mDatas.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
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

    static class ViewHolder {
        TextView textView;
        ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }
}
