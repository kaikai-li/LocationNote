package com.lkk.locationnote.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleView extends FrameLayout {

    private View mBackground;
    private TextView mTitle;
    private ImageView mLeftIcon;
    private TextView mLeftText;
    private ImageView mRightIcon;
    private TextView mRightText;
    private ImageView mRightIcon2;
    private TextView mRightText2;

    public TitleView(Context context) {
        super(context);
        initView();
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.title_view_layout,
                this, true);
        mBackground = rootView.findViewById(R.id.title_view);
        mTitle = rootView.findViewById(R.id.title);
        mLeftIcon = rootView.findViewById(R.id.title_left_icon);
        mLeftText = rootView.findViewById(R.id.title_left_text);
        mRightIcon = rootView.findViewById(R.id.title_right_icon);
        mRightText = rootView.findViewById(R.id.title_right_text);
        mRightIcon2 = rootView.findViewById(R.id.title_right_icon2);
        mRightText2 = rootView.findViewById(R.id.title_right_text2);
    }

    public void setBackground(int colorId) {
        if (colorId != -1) {
            mBackground.setBackgroundResource(colorId);
        } else {
            mBackground.setBackgroundResource(R.color.colorPrimary);
        }
    }

    public void setTitle(int resId) {
        if (resId != -1) {
            mTitle.setVisibility(VISIBLE);
            mTitle.setText(resId);
        } else {
            mTitle.setVisibility(GONE);
        }
    }

    public void setTitle(String titleStr) {
        if (!TextUtils.isEmpty(titleStr)) {
            mTitle.setVisibility(VISIBLE);
            mTitle.setText(titleStr);
        } else {
            mTitle.setVisibility(GONE);
        }
    }

    public void setLeftIcon(int resId) {
        if (resId != -1) {
            mLeftIcon.setVisibility(VISIBLE);
            mLeftIcon.setImageResource(resId);
        } else {
            mLeftIcon.setVisibility(GONE);
        }
    }

    public void setLeftIconClickListener(OnClickListener listener) {
        if (listener != null) {
            mLeftIcon.setOnClickListener(listener);
        }
    }

    public void setLeftText(int resId) {
        if (resId != -1) {
            mLeftText.setVisibility(VISIBLE);
            mLeftText.setText(resId);
        } else {
            mLeftText.setVisibility(GONE);
        }
    }

    public void setLeftText(String leftText) {
        if (!TextUtils.isEmpty(leftText)) {
            mLeftText.setVisibility(VISIBLE);
            mLeftText.setText(leftText);
        } else {
            mLeftText.setVisibility(GONE);
        }
    }

    public void setLeftTextClickListener(OnClickListener listener) {
        if (listener != null) {
            mLeftText.setOnClickListener(listener);
        }
    }

    public void setRightIcon(int resId) {
        if (resId != -1) {
            mRightIcon.setVisibility(VISIBLE);
            mRightIcon.setImageResource(resId);
        } else {
            mRightIcon.setVisibility(GONE);
        }
    }

    public void setRightIconClickListener(OnClickListener listener) {
        if (listener != null) {
            mRightIcon.setOnClickListener(listener);
        }
    }

    public void setRightText(int resId) {
        if (resId != -1) {
            mRightText.setVisibility(VISIBLE);
            mRightText.setText(resId);
        } else {
            mRightText.setVisibility(GONE);
        }
    }

    public void setRightText(String rightText) {
        if (!TextUtils.isEmpty(rightText)) {
            mRightText.setVisibility(VISIBLE);
            mRightText.setText(rightText);
        } else {
            mRightText.setVisibility(GONE);
        }
    }

    public void setRightTextClickListener(OnClickListener listener) {
        if (listener != null) {
            mRightText.setOnClickListener(listener);
        }
    }

    public void setRightIcon2(int resId) {
        if (resId != -1) {
            mRightIcon2.setVisibility(VISIBLE);
            mRightIcon2.setImageResource(resId);
        } else {
            mRightIcon2.setVisibility(GONE);
        }
    }

    public void setRightIcon2ClickListener(OnClickListener listener) {
        if (listener != null) {
            mRightIcon2.setOnClickListener(listener);
        }
    }

    public void setRightText2(int resId) {
        if (resId != -1) {
            mRightText2.setVisibility(VISIBLE);
            mRightText2.setText(resId);
        } else {
            mRightText2.setVisibility(GONE);
        }
    }

    public void setRightText2(String rightText2) {
        if (!TextUtils.isEmpty(rightText2)) {
            mRightText2.setVisibility(VISIBLE);
            mRightText2.setText(rightText2);
        } else {
            mRightText2.setVisibility(GONE);
        }
    }

    public void setRightText2ClickListener(OnClickListener listener) {
        if (listener != null) {
            mRightText2.setOnClickListener(listener);
        }
    }
}
