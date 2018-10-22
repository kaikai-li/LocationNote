package com.lkk.locationnote.common.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

public class ImageLoaderUtil {

    public static void loadImage(Context context, Object url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().centerCrop())
                .transition(new DrawableTransitionOptions().crossFade(800))
                .into(imageView);
    }
}
