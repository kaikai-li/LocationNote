package com.lkk.locationnote.common;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    private Unbinder mUnbinder;
    private OnTitleRightIconCallback mRightIconCallback;
    @DrawableRes
    private int mRightIconResId = -1;
    private View.OnClickListener mRightIconClickListener;

    public void setRightIconCallback(OnTitleRightIconCallback callback) {
        mRightIconCallback = callback;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
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

    protected void showSnackbar(View view, String message) {
        if (view == null || TextUtils.isEmpty(message)) {
            return;
        }
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    protected void showSnackbar(View view, @StringRes int resId) {
        if (view == null) {
            return;
        }
        showSnackbar(view, getString(resId));
    }
}
