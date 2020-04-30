package com.baidu.drawview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * TODO 优化
 */
public class DrawView extends LinearLayout {
    public static final int COLUMN_COUNT = 3;
    public static final int ROW_NUM = 3;
    public static final int CELLS_COUNT = COLUMN_COUNT * ROW_NUM;
    private static boolean DEBUG = BuildConfig.DEBUG;
    private List<Item> mList;
    private ActionListener mAnimListener;
    private List<View> mItemViewList;
    private int mHighLightIndex;
    private int mLastHighLightIndex;

    public DrawView(Context context) {
        super(context);
        onCreate();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    private void onCreate() {
        setOrientation(VERTICAL);
        this.mItemViewList = new ArrayList<>(CELLS_COUNT);
        for (int i = 0; i < ROW_NUM; i++) {
            LinearLayout currentLineContainer = buildLineContainer(getContext(), i);
            for (int j = 0; j < COLUMN_COUNT; j++) {
                View child = onCreateChildView(getContext(), i, j);
                mItemViewList.add(child);
                currentLineContainer.addView(child);
            }
            addView(currentLineContainer);
        }
    }

    public void updateHighLightIndex(int val) {
        mHighLightIndex = val;
        if (mItemViewList != null) {
            View view = null;
            for (int i = 0; i < mItemViewList.size(); i++) {
                view = mItemViewList.get(i);
                bindChildView(view, i);
            }
        }
        mLastHighLightIndex = mHighLightIndex;
        invalidate();
    }

    /**
     * TODO margin & padding
     *
     * @param context
     * @param rowIndex
     *
     * @return
     */
    private LinearLayout buildLineContainer(Context context, int rowIndex) {
        LinearLayout lineContainer = new LinearLayout(context);
        lineContainer.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams lineParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lineContainer.setLayoutParams(lineParams);
        lineParams.setMargins(0, 0, 0, 0);
        lineContainer.setPadding(0, 0, 0, 0);
        return lineContainer;
    }

    public void setList(List<Item> list) {
        if (mList == null) {
            mList = new ArrayList<>(list);
        } else {
            mList.clear();
            mList.addAll(list);
        }
        if (mItemViewList != null) {
            View view = null;
            for (int i = 0; i < mItemViewList.size(); i++) {
                view = mItemViewList.get(i);
                bindChildView(view, i);
            }
        }
        invalidate();
    }

    private Item getItem(int index) {
        if (mList == null || index < 0 || index > mList.size()) {
            return null;
        }
        return mList.get(index);
    }

    private View onCreateChildView(Context context, int rowIndex, int columnIndex) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_draw, null);
        return view;
    }

    private void bindChildView(View view, int position) {
        Item item = getItem(position);
        TextView descTv = view.findViewById(R.id.item_desc);
        ImageView imgView = view.findViewById(R.id.item_img);

        if (!TextUtils.isEmpty(item.descStr)) {
            descTv.setText(item.descStr);
        } else if (item.descStrId > 0) {
            descTv.setText(item.descStrId);
        }
        if (TextUtils.isEmpty(descTv.getText())) {
            descTv.setVisibility(View.INVISIBLE);
        } else {
            descTv.setVisibility(View.VISIBLE);
        }

        if (position == mHighLightIndex) {
            view.setBackground(getResources().getDrawable(item.bgDrawableIds));
            descTv.setTextColor(getResources().getColor(item.descStrColorIds));
            imgView.setBackground(getResources().getDrawable(item.imgDrawableIds));
        } else {
            view.setBackground(getResources().getDrawable(item.bgDrawableId));
            descTv.setTextColor(getResources().getColor(item.descStrColorId));
            imgView.setBackground(getResources().getDrawable(item.imgDrawableId));
        }
        view.setTag(position);
        view.setClickable(item.clickable);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) v.getTag();
                if (mAnimListener != null) {
                    mAnimListener.onClickAction(index);
                }
            }
        });

    }

    public void setAnimListener(ActionListener listener) {
        this.mAnimListener = listener;
    }

    public interface ActionListener {
        void onClickAction(int index);
    }

    public static class Item {
        @DrawableRes
        int bgDrawableId;
        @DrawableRes
        int bgDrawableIds;

        @DrawableRes
        int imgDrawableId;
        @DrawableRes
        int imgDrawableIds;

        @ColorRes
        int descStrColorId;
        @ColorRes
        int descStrColorIds;

        @StringRes
        int descStrId;
        String descStr;

        boolean clickable;

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == null) {
                return super.equals(obj);
            }
            Item item = (Item) obj;
            return item.bgDrawableId == bgDrawableId
                    && item.bgDrawableIds == bgDrawableIds
                    && item.imgDrawableId == imgDrawableId
                    && item.imgDrawableIds == imgDrawableIds
                    && item.descStrColorId == descStrColorId
                    && item.descStrColorIds == descStrColorIds
                    && item.descStrId == descStrId
                    && item.descStr == descStr
                    && item.clickable == clickable;
        }
    }
}
