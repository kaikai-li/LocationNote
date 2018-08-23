package com.lkk.locationnote.common;

import android.support.annotation.DrawableRes;
import android.view.View;

public interface OnTitleRightIconCallback {
    void setRightIcon(@DrawableRes int resId);
    void setRightIconClickListener(View.OnClickListener listener);
}
