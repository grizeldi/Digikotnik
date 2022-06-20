package com.github.grizeldi.digikotnik;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class AngleView extends View {
    private static final float ARC_LINE_LENGTH_PERCENTAGE = 0.6f;

    private int mDisplayColor = Color.BLACK;

    private Paint linePaint;
    private float angle;

    public AngleView(Context context) {
        super(context);
        init(null, 0);
    }

    public AngleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AngleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.AngleView, defStyle, 0);

        mDisplayColor = a.getColor(R.styleable.AngleView_displayColor, mDisplayColor);

        a.recycle();

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5);
        linePaint.setColor(mDisplayColor);
    }

    // This mess should probably be optimized, so it doesn't allocate 3984579387 objects every call
    // but it works well enough for now.
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        float centerX = getWidth() / 2.0f;
        float centerY = getHeight() / 2.0f;
        int lineLength = contentWidth < contentHeight ? (int) (contentWidth / 2.0f) : (int) (contentHeight / 2.0);
        RectF phoneRect = new RectF(centerX - lineLength / 5, centerY - lineLength / 3, centerX + lineLength / 5, centerY + lineLength / 3);

        canvas.drawRoundRect(phoneRect, 5, 5, linePaint);
        canvas.drawLine(centerX, centerY, centerX, centerY - lineLength, linePaint);
        float transformedAngle = (float) -(angle - Math.PI / 2);
        canvas.drawLine(centerX, centerY, centerX - (float) (lineLength * Math.cos(transformedAngle)), centerY - (float) (lineLength * Math.sin(transformedAngle)), linePaint);
        canvas.drawArc(centerX - lineLength * ARC_LINE_LENGTH_PERCENTAGE,
                centerY - lineLength * ARC_LINE_LENGTH_PERCENTAGE,
                centerX + lineLength * ARC_LINE_LENGTH_PERCENTAGE,
                centerY + lineLength * ARC_LINE_LENGTH_PERCENTAGE,
                270,
                (float) -(angle / Math.PI * 180),
                false,
                linePaint);
    }

    public float getAngle() {
        return angle;
    }

    /**
     * Sets the angle to display.
     * @param angle Angle to display in radians.
     */
    public void setAngle(float angle) {
        this.angle = angle;
        invalidate();
    }
}