package com.lkk.locationnote.common;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.lkk.locationnote.common.log.Log;

public class BaseTabFragment extends BaseFragment {

    private static final String TAG = BaseTabFragment.class.getSimpleName();

    private OnTitleRightIconCallback mRightIconCallback;
    @DrawableRes
    private int mRightIconResId = -1;
    private View.OnClickListener mRightIconClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mRightIconCallback = (OnTitleRightIconCallback) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "Cast to callback fail, " + e.getMessage());
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mRightIconCallback != null) {
            mRightIconCallback.setRightIcon(mRightIconResId);
            mRightIconCallback.setRightIconClickListener(mRightIconClickListener);
        }
    }

    protected void setRightIconResId(@DrawableRes int resId) {
        mRightIconResId = resId;
    }

    protected void setRightIconClickListener(View.OnClickListener listener) {
        mRightIconClickListener = listener;
    }
}
