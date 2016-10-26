package win99.com.slidelayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import win99.com.slidelayout.R;

/**
 * @author sanshu
 * @data 2016/10/16 下午10:17
 * @ToDo ${TODO}
 */

public class SlideLayout extends ViewGroup {

    private View mSlideView;
    private View mMainView;
    private int mDownX;
    private int mSlideWidth;//宽度
    private Scroller mScroller;
    private int mDownY;

    public SlideLayout(Context context) {
        this(context, null);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //这是构造函数里，优先于三方法执行
        init();
    }

    private void init() {
        //System.out.println("init");



        mScroller = new Scroller(getContext());




    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /* 获取子view*/
        mMainView = this.getChildAt(0);
        mSlideView = this.getChildAt(1);
        ////测量主界面的宽高
        mMainView.measure(widthMeasureSpec,heightMeasureSpec);
        //测量主界面的宽高
        mSlideWidth = mSlideView.getLayoutParams().width;
        mSlideView.measure(mSlideWidth,heightMeasureSpec);
        mMainView.findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("iv_back点击了");
            }
        });

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mMainView.layout(l, t, r, b);
        //设置位置
        mSlideView.layout(-mSlideView.getLayoutParams().width,t,0,b);

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY= (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                if (Math.abs(moveY - mDownY) < Math.abs(moveX-mDownX)) {
                    System.out.println("Y<X="+(moveY - mDownY)+"...."+(moveY - mDownY));
                    return true;
                }else{
                    return false;
                }

            default:
        }
        return false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.out.println("ACTION_DOWN");


                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("ACTION_MOVE");
                int moveX = (int) event.getX();

                int scrollX = getScrollX();
                System.out.println("scrollX="+scrollX);
                int diffX= mDownX - moveX;
                //将要移动位置
                int desX = scrollX + diffX;//
                if (desX > 0) {
                    scrollTo(0,0);
                }else if (desX < -mSlideWidth){
                    scrollTo(-mSlideWidth,0);
                }else{
                    scrollBy(diffX,0);
                }

                /*if (desX <= 0) {

                }*/
                mDownX=moveX;
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("ACTION_UP");
                int updateX = getScrollX();
                updatePosition(updateX);
                break;
            default:
        }

        return true;

    }

    //更新位置
    private void updatePosition(int updateX) {
        int startx=updateX;
        int starty=0;
        int dx=0;
        int dy=0;
        int duration=Math.abs(updateX) * 2;
        if (startx < -mSlideWidth/2) {
            //menu
            dx=-mSlideWidth -updateX;
        }else{
            //main
            dx=0-updateX;
        }
        mScroller.startScroll(startx,0,dx,dy,duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        //super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();
            scrollTo(currX,0);
            invalidate();
        }
    }
}
