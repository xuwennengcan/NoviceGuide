package can.com.novice_guide.uitls

import android.content.Context
import android.graphics.Rect
import android.graphics.RectF
import java.security.AccessController.getContext

/**
 * Created by CAN on 19-2-18.
 * 工具类
 */
fun dp2px(context: Context, dp: Int): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density + 0.5).toInt()
}

fun px2dp(context: Context, px: Int): Int {
    val density = context.resources.displayMetrics.density
    return (px / density + 0.5).toInt()
}

fun getRect(left:Int,top:Int,right:Int,bottom:Int,padding:Int) : Rect{
    return Rect(left-padding,top-padding,right+padding,bottom+padding)
}

fun rectF2Rect(rectF: RectF) : Rect{
    val rect = Rect()
    rect.left = rectF.left.toInt()
    rect.right = rectF.right.toInt()
    rect.top = rectF.top.toInt()
    rect.bottom = rectF.bottom.toInt()
    return rect
}