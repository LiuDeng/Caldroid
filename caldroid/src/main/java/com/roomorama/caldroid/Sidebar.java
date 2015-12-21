
package com.roomorama.caldroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.caldroid.R;


public class Sidebar extends View{

    public static interface   SideBarSelectListener
    {
        void onSelectIndex(int index);
    }

	private Paint paint;
	private Context context;
    private String[] sections;
    private  SideBarSelectListener sectionIndexer;
    private int fontSizeInPx = 10;

    public void initBar(SideBarSelectListener sectionIndexer, String[] sections , boolean isLightMode)
    {
        this.sectionIndexer = sectionIndexer;
        this.sections =sections;
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
        }
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(fontSizeInPx);
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
            canvas.drawText(sections[i], center,  y, paint);
        }
	}
	
	private int sectionForPoint(float y) {
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
	
	private void setHeaderTextAndScroll(MotionEvent event){
		if (sectionIndexer == null) {
		        return;
	    }
        int index = sectionForPoint(event.getY());
        sectionIndexer.onSelectIndex(index);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:{
			setHeaderTextAndScroll(event);
			setBackgroundResource(R.drawable.sidebar_background_pressed);
			return true;
		}
		case MotionEvent.ACTION_MOVE:{
			setHeaderTextAndScroll(event);
			return true;
		}
		case MotionEvent.ACTION_UP:
			setBackgroundColor(Color.TRANSPARENT);
			return true;
		case MotionEvent.ACTION_CANCEL:
			setBackgroundColor(Color.TRANSPARENT);
			return true;
		}
		return super.onTouchEvent(event);
	}

}
