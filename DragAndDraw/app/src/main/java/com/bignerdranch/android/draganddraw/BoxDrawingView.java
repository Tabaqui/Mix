package com.bignerdranch.android.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.02.2016.
 */
public class BoxDrawingView extends View {

    private static final String TAG = "BoxDrawingView";
    private static final int VIEW_ID = 42;
    private static final String SUPER_STATE = "superState";
    private static final String THIS_STATE = "thisState";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    private Bundle mBundle;

    public BoxDrawingView(Context context) {
        this(context, null);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setId(322123);
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle state = new Bundle();
        state.putParcelable(SUPER_STATE, superState);
        Log.i(TAG, "cb save invoke " + superState.toString());
        Log.i(TAG, "cb save invoke " + state.toString());
        if (!mBoxen.isEmpty()) {
           state.putParcelableArrayList(THIS_STATE, (ArrayList<? extends Parcelable>) mBoxen);
        }
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle ownState = (Bundle) state;
        List<Box> savedBoxen = ownState.getParcelableArrayList(THIS_STATE);
        mBoxen = savedBoxen;
        super.onRestoreInstanceState(ownState.getParcelable(SUPER_STATE));
        Log.i(TAG, "cb restore invoke " + ownState.getParcelable(SUPER_STATE).toString());
        Log.i(TAG, "cb restore invoke " + ownState.toString());
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }
        Log.i(TAG, action + " at x=" + current.x + ", y=" + current.y);
        return true;
    }
}
