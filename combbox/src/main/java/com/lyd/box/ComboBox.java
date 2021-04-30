package com.lyd.box;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.lyd.combbox.R;

import java.util.Arrays;
import java.util.List;

public class ComboBox extends AppCompatEditText {

    private static final int MAX_LEVEL = 10000;
    private static final int DEFAULT_ELEVATION = 16;
    private static final String INSTANCE_STATE = "instance_state";
    private static final String SELECTED_INDEX = "selected_index";
    private static final String IS_POPUP_SHOWING = "is_popup_showing";
    private static final String IS_ARROW_HIDDEN = "is_arrow_hidden";
    private static final String ARROW_DRAWABLE_RES_ID = "arrow_drawable_res_id";
    public static final int VERTICAL_OFFSET = 1;

    private int selectedIndex;
    private Drawable arrowDrawable;
    private PopupWindow popupWindow;
    private ListView listView;
    private ComboBoxAdapter adapter;
    private TextAlignment horizontalAlignment;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;
    private boolean isArrowHidden;
    private int textColor;
    private int backgroundSelector;
    private int arrowDrawableTint;
    private int displayHeight;
    private int parentVerticalOffset;
    private int dropDownListPaddingBottom;
    private @DrawableRes
    int arrowDrawableResId;

    private ITextFormatter spinnerTextFormatter = new DefaultTextFormatter();
    private ITextFormatter selectedTextFormatter = new DefaultTextFormatter();

    public ComboBox(Context context) {
        super(context);
        init(context, null);
    }

    public ComboBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ComboBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(SELECTED_INDEX, selectedIndex);
        bundle.putBoolean(IS_ARROW_HIDDEN, isArrowHidden);
        bundle.putInt(ARROW_DRAWABLE_RES_ID, arrowDrawableResId);
        if (popupWindow != null) {
            bundle.putBoolean(IS_POPUP_SHOWING, popupWindow.isShowing());
        }
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable savedState) {
        if (savedState instanceof Bundle) {
            Bundle bundle = (Bundle) savedState;
            selectedIndex = bundle.getInt(SELECTED_INDEX);

            if (adapter != null) {
                setTextInternal(adapter.getItemAt(selectedIndex).toString());
                adapter.setSelectedIndex(selectedIndex);
            }

            if (bundle.getBoolean(IS_POPUP_SHOWING)) {
                if (popupWindow != null) {
                    // Post the show request into the looper to avoid bad token exception
                    post(new Runnable() {
                        @Override
                        public void run() {
                            showDropDown();
                        }
                    });
                }
            }

            isArrowHidden = bundle.getBoolean(IS_ARROW_HIDDEN, false);
            arrowDrawableResId = bundle.getInt(ARROW_DRAWABLE_RES_ID);
            savedState = bundle.getParcelable(INSTANCE_STATE);
        }
        super.onRestoreInstanceState(savedState);
    }

    private void init(Context context, AttributeSet attrs) {

        Resources resources = getResources();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NiceSpinner);
//        int defaultPadding = resources.getDimensionPixelSize(R.dimen.one_and_a_half_grid_unit);

//        setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
//        setPadding(resources.getDimensionPixelSize(R.dimen.three_grid_unit), defaultPadding, defaultPadding,
//                defaultPadding);
        setClickable(true);

        backgroundSelector = typedArray.getResourceId(R.styleable.NiceSpinner_backgroundSelector, R.drawable.selector);
//        setBackgroundResource(backgroundSelector);
        textColor = typedArray.getColor(R.styleable.NiceSpinner_textTint, getDefaultTextColor(context));
//        setTextColor(textColor);

        listView = new ListView(context);

        listView.setId(getId());
        listView.setDivider(null);
        listView.setItemsCanFocus(true);
//        listView.setPadding(8, 0,8,0);

        listView.setDivider(new ColorDrawable(0xffedecec));
        listView.setDividerHeight(1);

        //hide vertical and horizontal scrollbars
        listView.setVerticalScrollBarEnabled(true);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= selectedIndex && position < adapter.getCount()) {
                    position++;
                }

                selectedIndex = position;

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(parent, view, position, id);
                }

                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(parent, view, position, id);
                }

                adapter.setSelectedIndex(position);
                setTextInternal(adapter.getItemAt(position).toString());
                dismissDropDown();
            }
        });

        popupWindow = new PopupWindow(context);
        popupWindow.setContentView(listView);
        popupWindow.setOutsideTouchable(true);

        //TODO 设置触摸, 很重要
        popupWindow.setFocusable(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(DEFAULT_ELEVATION);
            popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.spinner_drawable));
        } else {
            popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.drop_down_shadow));
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!isArrowHidden) {
                    animateArrow(false);
                }
            }
        });

        isArrowHidden = typedArray.getBoolean(R.styleable.NiceSpinner_hideArrow, false);
        arrowDrawableTint = typedArray.getColor(R.styleable.NiceSpinner_arrowTint, Integer.MAX_VALUE);
        arrowDrawableResId = typedArray.getResourceId(R.styleable.NiceSpinner_arrowDrawable, R.drawable.arrow);
        dropDownListPaddingBottom =
                typedArray.getDimensionPixelSize(R.styleable.NiceSpinner_dropDownListPaddingBottom, 0);

        horizontalAlignment = TextAlignment.fromId(
                typedArray.getInt(R.styleable.NiceSpinner_popupTextAlignment, TextAlignment.CENTER.ordinal()));

        CharSequence[] entries = typedArray.getTextArray(R.styleable.NiceSpinner_entries);
        if (entries != null) {
            setDataSet(Arrays.asList(entries));
        }

        typedArray.recycle();

        measureDisplayHeight();
    }

    private void measureDisplayHeight() {
        displayHeight = getContext().getResources().getDisplayMetrics().heightPixels;
    }

    private int getParentVerticalOffset() {
        if (parentVerticalOffset > 0) {
            return parentVerticalOffset;
        }
        int[] locationOnScreen = new int[2];
        getLocationOnScreen(locationOnScreen);
        return parentVerticalOffset = locationOnScreen[VERTICAL_OFFSET];
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        arrowDrawable = initArrowDrawable(arrowDrawableTint);
        setRightDrawableOrHide(arrowDrawable);
    }

    private Drawable initArrowDrawable(int drawableTint) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), arrowDrawableResId);
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            if (drawableTint != Integer.MAX_VALUE && drawableTint != 0) {
                DrawableCompat.setTint(drawable, drawableTint);
            }
        }
        return drawable;
    }

    private void setRightDrawableOrHide(Drawable drawable) {
        if (!isArrowHidden && drawable != null) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    private int getDefaultTextColor(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme()
                .resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray typedArray = context.obtainStyledAttributes(typedValue.data,
                new int[]{android.R.attr.textColorPrimary});
        int defaultTextColor = typedArray.getColor(0, Color.BLACK);
        typedArray.recycle();
        return defaultTextColor;
    }


    public void setArrowDrawable(@DrawableRes @ColorRes int drawableId) {
        arrowDrawableResId = drawableId;
        arrowDrawable = initArrowDrawable(R.drawable.arrow);
        setRightDrawableOrHide(arrowDrawable);
    }

    public void setArrowDrawable(Drawable drawable) {
        arrowDrawable = drawable;
        setRightDrawableOrHide(arrowDrawable);
    }

    public void setTextInternal(String text) {
        if (selectedTextFormatter != null) {
            setText(selectedTextFormatter.format(text));
        } else {
            setText(text);
        }
        if (hasFocus()) {
            setSelection(text.length());
        }
    }

    public void setSelectedIndex(int position) {
        if (adapter != null) {
            if (position >= 0 && position <= adapter.getCount()) {
                adapter.setSelectedIndex(position);
                selectedIndex = position;
                setTextInternal(adapter.getItemAt(position).toString());
            } else {
                throw new IllegalArgumentException("Position must be lower than adapter count!");
            }
        }
    }

    public void addOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public <T> void setDataSet(List<T> list) {
        adapter = new ComboBoxAdapter<>(getContext(), list, textColor, backgroundSelector,
                spinnerTextFormatter, horizontalAlignment);
        setAdapterInternal(this.adapter);
    }

    private void setAdapterInternal(ComboBoxAdapter adapter) {
        // If the adapter needs to be settled again, ensure to reset the selected index as well
        selectedIndex = 0;
        listView.setAdapter(adapter);
        setTextInternal(adapter.getItemAt(selectedIndex).toString());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (isEnabled() && event.getAction() == MotionEvent.ACTION_DOWN) {//0123对应左上右下
        if (event.getAction() == MotionEvent.ACTION_DOWN) {//0123对应左上右下
            Drawable drawable = getCompoundDrawables()[2];
            if (drawable != null && event.getX() >= (getWidth() - drawable.getIntrinsicWidth() - getPaddingRight())) {
                onRightIconClicked();
            } else {
                //保证原来的输入功能
                return super.onTouchEvent(event);
            }
            return false;
        }
        return super.onTouchEvent(event);
    }

    private void onRightIconClicked() {
        if (!popupWindow.isShowing()) {
            showDropDown();
        } else {
            dismissDropDown();
        }

    }

    private void animateArrow(boolean shouldRotateUp) {
        int start = shouldRotateUp ? 0 : MAX_LEVEL;
        int end = shouldRotateUp ? MAX_LEVEL : 0;
        ObjectAnimator animator = ObjectAnimator.ofInt(arrowDrawable, "level", start, end);
        animator.setInterpolator(new LinearOutSlowInInterpolator());
        animator.start();
    }

    public void dismissDropDown() {
        if (!isArrowHidden) {
            animateArrow(false);
        }
        popupWindow.dismiss();
    }

    public void showDropDown() {
        if (!isArrowHidden) {
            animateArrow(true);
        }
        measurePopUpDimension();
        popupWindow.showAsDropDown(this);
    }

    private void measurePopUpDimension() {
        int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(
                displayHeight - getParentVerticalOffset() - getMeasuredHeight(),
                MeasureSpec.AT_MOST);
        listView.measure(widthSpec, heightSpec);
        popupWindow.setWidth(listView.getMeasuredWidth());
        popupWindow.setHeight(listView.getMeasuredHeight() - dropDownListPaddingBottom);
    }

    public void setTintColor(@ColorRes int resId) {
        if (arrowDrawable != null && !isArrowHidden) {
            DrawableCompat.setTint(arrowDrawable, ContextCompat.getColor(getContext(), resId));
        }
    }

    public void hideArrow() {
        isArrowHidden = true;
        setRightDrawableOrHide(arrowDrawable);
    }

    public void showArrow() {
        isArrowHidden = false;
        setRightDrawableOrHide(arrowDrawable);
    }

    public boolean isArrowHidden() {
        return isArrowHidden;
    }

    public void setDropDownListPaddingBottom(int paddingBottom) {
        dropDownListPaddingBottom = paddingBottom;
    }

    public int getDropDownListPaddingBottom() {
        return dropDownListPaddingBottom;
    }

    public void setSpinnerTextFormatter(ITextFormatter spinnerTextFormatter) {
        this.spinnerTextFormatter = spinnerTextFormatter;
    }

    public void setSelectedTextFormatter(ITextFormatter textFormatter) {
        this.selectedTextFormatter = textFormatter;
    }

    /************************************APIS***********************************/


    public <T> void addItem(T anObject) {

    }

    public <T> void insertItemAt(T anObject, int index) {
    }

    public <T> void removeltem(T anObject) {

    }

    void removeItemAt(int anlndex) {
    }    ///在下拉列表框中删除指定位置的对象项

    void removeAllItems() {
    }

    int getItemCount() {
        return 0;
    }

    Object getItemAt(int index) {
        return null;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    Object getSelectedltem() {
        return null;
    }
}
