package can.com.novice_guide.widgets;

import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by CAN on 19-2-14.
 * 高亮区域view
 */
class NoviceGuideHighlightView {

    //高亮的view
    private View mView;

    private RectF rectF;

    NoviceGuideHighlightView(View mView) {
        this.mView = mView;
    }

    float getRadius() {
        if (mView == null) {
            throw new IllegalArgumentException("the highlight view is null!");
        }
        return 60 ;
    }

    float getXRadius(){
        if (mView == null) {
            throw new IllegalArgumentException("the highlight view is null!");
        }
        return mView.getWidth()/2;
    }

    float getYRadius(){
        if (mView == null) {
            throw new IllegalArgumentException("the highlight view is null!");
        }
        return mView.getHeight()/2;
    }

    RectF getRectF(int padding) {
        if (mView == null) {
            throw new IllegalArgumentException("the highlight view is null!");
        }
        if (rectF == null) {
            rectF = new RectF();
            Rect locationInView = getLocationInView(padding,mView);
            rectF.left = locationInView.left ;
            rectF.top = locationInView.top ;
            rectF.right = locationInView.right  ;
            rectF.bottom = locationInView.bottom ;
        }
        return rectF;
    }


    private static Rect getLocationInView(int padding , View child) {
        if (child == null ) {
            throw new IllegalArgumentException("view can not be null .");
        }
        Rect result = new Rect();
        int[] locations = new int[2];
        child.getLocationOnScreen(locations);
        result.left = locations[0]-padding;
        result.top = locations[1]-padding;
        result.right = locations[0] + child.getMeasuredWidth()+padding;
        result.bottom = locations[1] + child.getMeasuredHeight()+padding;
        return result;
    }


}