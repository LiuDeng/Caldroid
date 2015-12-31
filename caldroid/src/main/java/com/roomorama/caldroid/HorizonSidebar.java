
package com.roomorama.caldroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.caldroid.R;


public class HorizonSidebar extends Sidebar{



    private int textPadding = 5;
    private int textTopPadding = 0;

	public HorizonSidebar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
        fontSizeInPx = context.getResources().getDimensionPixelSize(R.dimen.cell_text_size);
        textPadding = context.getResources().getDimensionPixelSize(R.dimen.horizon_sidebar_padding);
        textTopPadding = context.getResources().getDimensionPixelSize(R.dimen.horizon_sidebar_top_padding);
        init(true);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
        if (sections == null || sections.length == 0 )
            return;
        int y = getHeight() / 2 + textTopPadding;


        int width = getWidth();
        width -= (2* textPadding) ;
        width = width/ (sections.length-1);
        for (int i=0; i< sections.length; i++)
        {
            int x = width * i;
            x += textPadding;
			Paint drawePaint = paint;
			if (i == selectIndex)
			{
				drawePaint = selectPaint;
			}
            canvas.drawText(sections[i], x,  y, drawePaint);
        }
	}

	@Override
	protected int sectionForPoint(float x) {
        int width = getWidth() / (sections.length);
		int index = (int) ((int)x / width);
		if(index < 0) {
			index = 0;
		}
		if(index > sections.length - 1){
			index = sections.length - 1;
		}
		return index;
	}

	@Override
	protected void setHeaderTextAndScroll(MotionEvent event){
		if (sectionIndexer == null) {
		        return;
	    }
        int index = sectionForPoint(event.getX());
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
			setBackgroundColor(Color.TRANSPARENT);
			selectHintTextView.setVisibility(GONE);
			return true;
		case MotionEvent.ACTION_CANCEL:
			selectHintTextView.setVisibility(GONE);
			return true;
		}
		return super.onTouchEvent(event);
	}

}
