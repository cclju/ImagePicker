package com.lzy.imagepicker.adapter.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzy.imagepicker.adapter.CommonImageRecyclerAdapterWithHead;

public class GridSpacingItemWithHeadDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemWithHeadDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view); // item position

        CommonImageRecyclerAdapterWithHead adapterWithHead = (CommonImageRecyclerAdapterWithHead) parent.getAdapter();
        if (adapterWithHead == null || adapterWithHead.hasHeader(pos)) {
            return;
        }

        outRect.left = spacing;
        outRect.bottom = spacing;

//        int originPos = adapterWithHead.getOriginPosition(pos);
//        int column = originPos % spanCount; // item column
//
//        if (includeEdge) {
//            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
//            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
//
//            if (originPos < spanCount) { // top edge
//                outRect.top = spacing;
//            }
//            outRect.bottom = spacing; // item bottom
//        } else {
//            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
//            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
//            if (originPos >= spanCount) {
//                outRect.top = spacing; // item top
//            }
//        }
    }
}
