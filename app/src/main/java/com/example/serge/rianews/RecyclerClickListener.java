package com.example.serge.rianews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class RecyclerClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private GestureDetector.OnGestureListener gestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            };

    public RecyclerClickListener(Context context) {
        gestureDetector = new GestureDetector(context, gestureListener);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (gestureDetector.onTouchEvent(e)) {
            View clickedChild = rv.findChildViewUnder(e.getX(), e.getY());
            if (clickedChild != null && !clickedChild.dispatchTouchEvent(e)) {
                int clickedPosition = rv.getChildPosition(clickedChild);
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    onItemClick(rv, clickedChild, clickedPosition);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public abstract void onItemClick(RecyclerView recyclerView, View itemView, int position);
}
