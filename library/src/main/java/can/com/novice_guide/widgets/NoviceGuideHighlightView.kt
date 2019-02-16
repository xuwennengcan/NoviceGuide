package can.com.novice_guide.widgets

import android.graphics.Rect
import android.graphics.RectF
import android.view.View

/**
 * Created by CAN on 19-2-14.
 * 高亮区域view的坐标信息
 */
internal class NoviceGuideHighlightView(//高亮的view
        private val mView: View? , private val mPadding : Int) {

    val radius: Float
        get() {
            if (mView == null) {
                throw IllegalArgumentException("the highlight view is null!")
            }
            return 50f
        }

    fun getRectF(): RectF {
        if (mView == null) {
            throw IllegalArgumentException("the highlight view is null!")
        }
        val mRectF = RectF()
        val rectView = getRect()
        mRectF.left = rectView.left.toFloat()
        mRectF.top = rectView.top.toFloat()
        mRectF.right = rectView.right.toFloat()
        mRectF.bottom = rectView.bottom.toFloat()
        return mRectF
    }

    fun getRect(): Rect {
        if (mView == null) {
            throw IllegalArgumentException("view can not be null .")
        }
        val result = Rect()
        val locations = IntArray(2)
        mView.getLocationOnScreen(locations)
        result.left = locations[0] - mPadding
        result.top = locations[1] - mPadding
        result.right = locations[0] + mView.measuredWidth + mPadding
        result.bottom = locations[1] + mView.measuredHeight + mPadding
        return result
    }


}