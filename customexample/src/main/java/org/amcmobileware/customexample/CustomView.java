package org.amcmobileware.customexample;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.TextView;

import org.amcmobileware.customview.R;

/**
 * Created by Arkadiusz Cieśliński on 25/09/16.
 * - acieslinski@gmail.com
 */
public class CustomView extends ViewGroup {
    private static final float STRIPE_WIDTH_RATIO = 2F / 3;
    private static final int STRIPE_PADDING = 10;

    private int mStripeColor;
    private TextView mTitleTextView;
    private TextView mContentTextView;

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.CustomViewStyleAttr);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, R.style.CustomViewStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CustomTheme_CustomView, defStyleAttr, defStyleRes);

        mStripeColor = typedArray.getColor(R.styleable.CustomTheme_CustomView_stripeColor,
                Color.BLACK);

        int titleTextViewStyle = typedArray.getResourceId(
                R.styleable.CustomTheme_CustomView_titleTextStyle,
                R.style.CustomViewStyle_TitleTextStyle);
        int contentTextViewStyle = typedArray.getResourceId(
                R.styleable.CustomTheme_CustomView_contentTextStyle,
                R.style.CustomViewStyle_ContentTextStyle);

        typedArray.recycle();

        mTitleTextView = new TextView(new ContextThemeWrapper(context, titleTextViewStyle));
        mContentTextView = new TextView(new ContextThemeWrapper(context, contentTextViewStyle));

        mTitleTextView.setSingleLine(true);

        addView(mTitleTextView);
        addView(mContentTextView);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int titleBottom = top + mTitleTextView.getMeasuredHeight();
        mTitleTextView.layout(left, top, right, titleBottom);

        int contentBottom = titleBottom + mContentTextView.getMeasuredHeight();
        mContentTextView.layout(left, titleBottom, right, contentBottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mTitleTextView.measure(widthMeasureSpec, heightMeasureSpec);
        mContentTextView.measure(widthMeasureSpec, heightMeasureSpec);

        setStripe(mStripeColor);
    }

    private void setStripe(int color) {
        int height = mTitleTextView.getMeasuredHeight();
        int width = (int) (height * STRIPE_WIDTH_RATIO);

        if (width > 0 && height > 0) {
            Bitmap.Config config = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);

            Paint paint = new Paint();
            paint.setColor(color);
            Canvas canvas = new Canvas(bitmap);

            canvas.drawRect(0, 0, width, height, paint);

            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            mTitleTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            mTitleTextView.setCompoundDrawablePadding(STRIPE_PADDING);
        }
    }
}
