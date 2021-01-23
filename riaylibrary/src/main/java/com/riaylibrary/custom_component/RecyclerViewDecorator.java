package com.riaylibrary.custom_component;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewDecorator extends RecyclerView.ItemDecoration {

    private int itemOffset;

    public RecyclerViewDecorator(int itemOffset) {
        this.itemOffset = itemOffset;
    }

    public RecyclerViewDecorator(@NonNull Context context, @DimenRes int itemOffsetId) {

        this(context.getResources().getDimensionPixelSize(itemOffsetId));

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(itemOffset, itemOffset, itemOffset, itemOffset);
    }
}