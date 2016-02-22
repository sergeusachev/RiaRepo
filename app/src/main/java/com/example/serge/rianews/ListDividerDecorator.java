package com.example.serge.rianews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ListDividerDecorator extends RecyclerView.ItemDecoration {

    private Drawable divider;

    public ListDividerDecorator(Context context) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            divider = context.getResources().getDrawable(R.drawable.line_divider, context.getTheme());
        } else {
            divider = context.getResources().getDrawable(R.drawable.line_divider);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
