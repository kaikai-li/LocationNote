package com.lkk.locationnote.note.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;

import com.lkk.locationnote.common.BaseActivity;
import com.lkk.locationnote.common.TitleView;
import com.lkk.locationnote.note.R;
import com.lkk.locationnote.note.R2;

import butterknife.BindView;

public class AddLocationActivity extends BaseActivity {

    public static final String EXTRA_NEW_LOCATION = "EXTRA_NEW_LOCATION";

    @BindView(R2.id.add_location_title)
    TitleView mTitleView;
    @BindView(R2.id.add_location_edit_view)
    TextInputEditText mNewLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        initTitleView();
    }

    private void initTitleView() {
        mTitleView.setTitle(R.string.add_location);
        mTitleView.setLeftIcon(R.drawable.ic_arrow_back);
        mTitleView.setLeftIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitleView.setRightText(R.string.done);
        mTitleView.setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newLocation = mNewLocation.getText().toString();
                if (TextUtils.isEmpty(newLocation)) {
                    showSnackbar(mNewLocation, R.string.content_empty);
                    return;
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_NEW_LOCATION, newLocation);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
