
package com.roomorama.caldroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.caldroid.R;


public class Sidebar extends View{

    public static interface   SideBarSelectListener
    {
        void onSelectIndex(int index);
    }
    protected TextView selectHintTextView;
    protected int selectIndex = 1;
    protected Paint paint;
    protected Paint selectPaint;
    protected Context context;
    protected String[] sections;
    protected  SideBarSelectListener sectionIndexer;
    protected int fontSizeInPx = 10;
    protected Integer pressBack  = R.drawable.sidebar_background_pressed_light;
    public void initBar(SideBarSelectListener sectionIndexer, String[] sections , boolean isLightMode, TextView selectHintTextView, int initSelectIndex)
    {
        this.sectionIndexer = sectionIndexer;
        this.sections =sections;
        this.selectHintTextView = selectHintTextView;
        this.selectIndex = initSelectIndex;
        init(isLightMode);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    public void setSelectIndex(int index)
    {
        selectIndex = index;
        postInvalidate();
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    public Sidebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        fontSizeInPx = context.getResources().getDimensionPixelSize(R.dimen.cell_text_size);
        init(true);
    }


    void  init(boolean isLightMode)
    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (isLightMode) {
            paint.setColor(getResources().getColor(R.color.text_diable_black));
        }
        else
        {
            paint.setColor(getResources().getColor(R.color.text_secondary_white));
            pressBack = R.drawable.sidebar_background_pressed_dark;
        }
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(fontSizeInPx);
        selectPaint = new Paint(paint);
        selectPaint.setColor(getResources().getColor(R.color.colorPrimary));
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (sections == null || sections.length == 0 )
            return;
        float center = getWidth() / 2;
        if (sections.length-1 == 0)
        {
            canvas.drawText(sections[0], center, getHeight() / 2, paint);
            return;
        }
        int height = (getHeight() - fontSizeInPx - fontSizeInPx /3) / (sections.length-1);
        for (int i=0; i< sections.length; i++)
        {
            int y = height * i;
            y+= fontSizeInPx;
            Paint drawePaint = paint;
            if (i == selectIndex)
            {
                drawePaint = selectPaint;
            }
            canvas.drawText(sections[i], center,  y, drawePaint);
        }
    }


    protected int sectionForPoint(float y) {
        int height = getHeight() / (sections.length);
        int index = (int) ((int)y / height);
        if(index < 0) {
            index = 0;
        }
        if(index > sections.length - 1){
            index = sections.length - 1;
        }
        return index;
    }

    protected void setHeaderTextAndScroll(MotionEvent event){
        if (sectionIndexer == null) {
            return;
        }
        int index = sectionForPoint(event.getY());
        selectIndex = index;
        selectHintTextView.setText(sections[selectIndex]);
        sectionIndexer.onSelectIndex(index);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:{
                setHeaderTextAndScroll(event);
                setBackgroundResource(pressBack);
                selectHintTextView.setVisibility(VISIBLE);
                return true;
            }
            case MotionEvent.ACTION_MOVE:{
                setHeaderTextAndScroll(event);
                selectHintTextView.setVisibility(VISIBLE);
                return true;
            }
            case MotionEvent.ACTION_UP:
                selectHintTextView.setVisibility(GONE);
                setBackgroundColor(Color.TRANSPARENT);
                return true;
            case MotionEvent.ACTION_CANCEL:
                selectHintTextView.setVisibility(GONE);
                setBackgroundColor(Color.TRANSPARENT);
                return true;
        }
        return super.onTouchEvent(event);
    }

}
