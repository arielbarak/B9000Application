package com.example.b9000application.Helper;

import android.content.Context;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CircleProfile  extends CircleImageView {

    public CircleProfile(Context context) {
        super(context);
    }

    @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int  height = getMeasuredHeight();
            setMeasuredDimension(height, height);
        }

    }

