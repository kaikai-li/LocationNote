package com.lkk.locationnote.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    private Unbinder mUnbinder;
    private OnTitleRightIconCallback mRightIconCallback;
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

    protected void setRightIconResId(int resId) {
        mRightIconResId = resId;
    }

    protected void setRightIconClickListener(View.OnClickListener listener) {
        mRightIconClickListener = listener;
    }
}
