package net.cloudapp.callme.hots3;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * http://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private Context context;
    private int divideOffset;

    public SpacesItemDecoration(Context context, int space, int divideOffset) {
        this.space = space;
        this.context = context;
        this.divideOffset = divideOffset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space / divideOffset;
        outRect.right = space / divideOffset;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if(parent.getChildAdapterPosition(view) == 0 || (parent.getChildAdapterPosition(view) == 1
                && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE))
            outRect.top = space;
    }
}