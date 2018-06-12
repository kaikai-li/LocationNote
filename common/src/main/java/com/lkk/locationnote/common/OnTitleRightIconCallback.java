package com.lkk.locationnote.common;

import android.view.View;

public interface OnTitleRightIconCallback {
    void setRightIcon(int resId);
    void setRightIconClickListener(View.OnClickListener listener);
}
