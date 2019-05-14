package can.com.novice_guide.widgets

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.view.MotionEvent
import android.view.View
import can.com.novice_guide.bean.NoviceGuideInfoBean
import can.com.novice_guide.enums.NoviceGuidePictureLocationType
import can.com.novice_guide.enums.NoviceGuideSkipTextPosition
import can.com.novice_guide.enums.NoviceGuideViewShapeType
import can.com.novice_guide.manager.NoviceGuideManager
import can.com.novice_guide.uitls.dp2px
import can.com.novice_guide.uitls.getRect
import can.com.novice_guide.uitls.rectF2Rect
import java.util.*

/**
 * Created by CAN on 19-2-14.
 * 新手引导-浮层View
 */

class NoviceGuideFloatingLayerView : View {

    private val mBgColor = -0x4d000000 //背景色
    private val mInnerPaintColor = -0x7f000001 //内圈画笔颜色
    private val mOuterPaintColor = -0x4c000001 //外圈画笔颜色
    private val mTextPaintColor = -0x00000001//文字画笔颜色
    private val mInnerPaint = Paint() //内圈画笔
    private val mOuterPaint = Paint() //外圈画笔
    private val mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG) //文字画笔

    private var mActivity: Activity? = null
    private var mRegionMap = WeakHashMap<View, Region>() //点击区域集合
    private var mMap: WeakHashMap<View?, NoviceGuideInfoBean>? = null //数据
    private var mTextRegion: Region? = null //text的点击区域

    private var mClickListener: ((View, NoviceGuideInfoBean) -> Unit?)? = null //重写的点击事件
    private var mOnSkipClickListener: (() -> Unit?)? = null //点击跳过的事件
    private var mCanTouchEmptyRegion = true //是否可以点击空白区域

    private var mSkipText = "跳过" //文字
    private var mSkipTextPosition = NoviceGuideSkipTextPosition.RIGHT_TOP //跳过文字的位置

    private val mInnerOuterPadding = 10//外圈与内圈的距离
    private val mHighLightRectPadding = 10//高亮view与原始view的间距
    private val mViewBitmapPadding = 20 //高亮view和图片间距

    constructor(context: Context) : super(context)

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mMap?.clear()
        mRegionMap.clear()
    }

    constructor(context: Activity, map: WeakHashMap<View?, NoviceGuideInfoBean>, onClickListener: ((View, NoviceGuideInfoBean) -> Unit)?) : super(context) {
        this.mMap = map
        this.mActivity = context
        this.mClickListener = onClickListener
        init()
    }

    //一些初始化工作
    private fun init() {
        setBackgroundColor(mBgColor)
        initInnerPaint(mInnerPaint)
        initOuterPaint(mOuterPaint)
        initTextPaint(mTextPaint)
    }

    //设置跳过的事件
    fun setOnSkipClickListener(onSkipClickListener: () -> Unit): NoviceGuideFloatingLayerView? {
        mOnSkipClickListener = onSkipClickListener
        return this
    }

    //设置跳过文字以及位置(默认位置右上角，文字“跳过”),位置参考 NoviceGuideSkipTextPosition
    fun setOnSkipText(skip: String, skipPosition: Int): NoviceGuideFloatingLayerView? {
        mSkipText = skip
        mSkipTextPosition = skipPosition
        return this
    }

    //设置点击其它空白区域是否响应跳过事件(默认true)
    fun setOnIsTouchEmptyRegion(isCanTouchEmptyRegion: Boolean): NoviceGuideFloatingLayerView? {
        mCanTouchEmptyRegion = isCanTouchEmptyRegion
        return this
    }

    //初始化文字画笔
    private fun initTextPaint(paint: TextPaint) {
        paint.color = mTextPaintColor
        paint.textSize = dp2px(context, 12).toFloat()
        paint.isAntiAlias = true
    }

    //初始化内圈画笔
    private fun initInnerPaint(paint: Paint) {
        paint.color = mInnerPaintColor
        paint.isAntiAlias = true
        //图像模式为清除图像模式,白色
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        //设置画笔遮罩滤镜,可以传入BlurMaskFilter或EmbossMaskFilter，前者为模糊遮罩滤镜而后者为浮雕遮罩滤镜
        //这个方法已经被标注为过时的方法了，如果你的应用启用了硬件加速，你是看不到任何阴影效果的
        paint.maskFilter = BlurMaskFilter(1f, BlurMaskFilter.Blur.SOLID)
        //关闭当前view的硬件加速
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        //保证调用onDraw()
        setWillNotDraw(false)
    }

    //初始化外圈画笔
    private fun initOuterPaint(paint: Paint) {
        paint.color = mOuterPaintColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.isAntiAlias = true
        //绘制虚线
        val dashPathEffect = DashPathEffect(floatArrayOf(20f, 6f), 1f)
        paint.pathEffect = dashPathEffect
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawText(canvas, mSkipText)

        val keys: MutableSet<View?>? = mMap?.keys

        if (keys != null) {
            for (view in keys) {
                val bean = mMap?.get(view)
                drawView(canvas, view, bean)
            }
        }

    }

    //绘制文本
    private fun drawText(canvas: Canvas?, text: String?) {
        if (text != null && canvas != null) {
            val rect = Rect()
            mTextPaint.getTextBounds(mSkipText, 0, mSkipText.length, rect)
            val textLocationX = if (mSkipTextPosition == NoviceGuideSkipTextPosition.LEFT_TOP)
                dp2px(context, 20)
            else
                width - rect.width() - dp2px(context, 15)
            val textLocationY = rect.height() + dp2px(context, 40)
            canvas.drawText(mSkipText, textLocationX.toFloat(), textLocationY.toFloat(), mTextPaint)
            mTextRegion = Region(getRect(textLocationX,
                    textLocationY,
                    textLocationX + rect.width(),
                    textLocationY + rect.height(),
                    dp2px(context, 10)))
        }
    }

    //绘制View相关
    private fun drawView(canvas: Canvas?, view: View?, bean: NoviceGuideInfoBean?) {
        if (canvas == null || view == null || bean == null)
            return

        //绘制高亮view
        val highlightView = NoviceGuideHighlightView(view, mHighLightRectPadding) //高亮view
        val innerRectF = highlightView.getRectF() //高亮view的位置信息(内圈)
        val outerRectF = RectF(innerRectF.left - mInnerOuterPadding, innerRectF.top - mInnerOuterPadding,
                innerRectF.right + mInnerOuterPadding, innerRectF.bottom + mInnerOuterPadding)//高亮view的位置信息(外圈)
        if (bean.viewShapeType == NoviceGuideViewShapeType.CIRCLE) {//绘制圆
            canvas.drawCircle((innerRectF.right - innerRectF.left) / 2 + innerRectF.left,
                    (innerRectF.bottom - innerRectF.top) / 2 + innerRectF.top,
                    Math.max(innerRectF.width() / 2, innerRectF.height() / 2), mInnerPaint)
            if (bean.needDrawOuter)
                canvas.drawCircle((outerRectF.right - outerRectF.left) / 2 + outerRectF.left,
                        (outerRectF.bottom - outerRectF.top) / 2 + outerRectF.top,
                        Math.max(outerRectF.width() / 2, outerRectF.height() / 2), mOuterPaint)
        } else if (bean.viewShapeType == NoviceGuideViewShapeType.ROUND) {//绘制椭圆
            canvas.drawRoundRect(innerRectF, highlightView.radius, highlightView.radius, mInnerPaint)
            if (bean.needDrawOuter)
                canvas.drawRoundRect(outerRectF, highlightView.radius, highlightView.radius, mOuterPaint)
        }

        //绘制bitmap
        val bitmap: Bitmap? = BitmapFactory.decodeResource(resources, bean.bitmapResource)
        if (bitmap != null && !bitmap.isRecycled) {
            when (bean.pictureLocationType) {
                NoviceGuidePictureLocationType.LEFT -> {
                    canvas.drawBitmap(bitmap, outerRectF.left - bitmap.width - mViewBitmapPadding,
                            (outerRectF.top + outerRectF.bottom - bitmap.height) / 2, Paint())
                }
                NoviceGuidePictureLocationType.RIGHT -> {
                    canvas.drawBitmap(bitmap, outerRectF.right + mViewBitmapPadding,
                            (outerRectF.top + outerRectF.bottom - bitmap.height) / 2, Paint())
                }
                NoviceGuidePictureLocationType.TOP -> {
                    canvas.drawBitmap(bitmap, ((width - bitmap.width) / 2).toFloat(),
                            outerRectF.top - bitmap.height - mViewBitmapPadding, Paint())
                }
                NoviceGuidePictureLocationType.BOTTOM -> {
                    canvas.drawBitmap(bitmap, ((width - bitmap.width) / 2).toFloat(),
                            outerRectF.bottom + mViewBitmapPadding, Paint())
                }
            }
            bitmap.recycle()
        }

        //点击区域收集
        if (!mRegionMap.containsKey(view))
            if (bean.needDrawOuter)
                mRegionMap.put(view, Region(rectF2Rect(outerRectF)))
            else
                mRegionMap.put(view, Region(rectF2Rect(innerRectF)))
    }

    //点击事件的处理
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                clickRegion(event.x.toInt(), event.y.toInt())
            }
        }
        return true
    }

    //处理点击的区域
    private fun clickRegion(x: Int, y: Int) {
        if (!mRegionMap.isEmpty()) {
            val keys: MutableSet<View?>? = mRegionMap.keys
            if (keys != null && !keys.isEmpty()) {
                val iterator = mRegionMap.keys.iterator()
                var isClick = false
                try {
                    iterator@ while (iterator.hasNext()) { //记得退出迭代器，防止报ConcurrentModificationException
                        val view = iterator.next()
                        val region = mRegionMap[view]
                        val infoBean = mMap?.get(view)
                        when (infoBean?.viewShapeType) {
                            NoviceGuideViewShapeType.CIRCLE -> { //点击圆
                                if (clickCircleRegion(view, infoBean, region, x, y)) {
                                    isClick = true
                                    break@iterator
                                }
                            }
                            NoviceGuideViewShapeType.ROUND -> { //点击矩形
                                if (clickRoundRegion(view, infoBean, region, x, y)) {
                                    isClick = true
                                    break@iterator
                                }
                            }
                        }
                    }
                } catch (exception: Exception) {
                }
                if (!isClick)
                    clickOther()
            }
        } else
            if (!clickSkip(x, y)) clickOther()
    }

    //处理矩形的区域
    private fun clickRoundRegion(view: View?, infoBean: NoviceGuideInfoBean, region: Region?, x: Int, y: Int): Boolean {
        return if (region != null && region.contains(x, y)) {
            if (mClickListener != null && view != null)
                mClickListener!!.invoke(view, infoBean)
            else {
                NoviceGuideManager.get().removeFloatingViewIfExit(mActivity)
                view?.performClick()
            }
            true
        } else
            clickSkip(x, y)
    }

    //处理圆的区域
    private fun clickCircleRegion(view: View?, infoBean: NoviceGuideInfoBean, region: Region?, x: Int, y: Int): Boolean {
        if (region != null) {
            val rect = region.bounds
            val diameter = Math.max(Math.abs(rect.right - rect.left), Math.abs(rect.bottom - rect.top)) //圆的直径
            val circleX = rect.left + Math.abs(rect.right - rect.left) / 2 //圆心x轴坐标
            val circleY = rect.top + Math.abs(rect.bottom - rect.top) / 2 //圆心y轴坐标
            val distanceX = Math.abs(x - circleX) //点击位置x坐标与圆心的距离
            val distanceY = Math.abs(y - circleY) //点击位置y坐标与圆心的距离
            val distance = Math.sqrt(Math.pow(distanceX.toDouble(), 2.0) + Math.pow(distanceY.toDouble(), 2.0)) //点击位置与圆心距离
            return if (distance <= diameter / 2) { //点击位置在圆内
                if (mClickListener != null && view != null)
                    mClickListener!!.invoke(view, infoBean)
                else {
                    NoviceGuideManager.get().removeFloatingViewIfExit(mActivity)
                    view?.performClick()
                }
                true
            } else
                clickSkip(x, y)
        } else
            return false
    }

    //处理跳过
    private fun clickSkip(x: Int, y: Int): Boolean {
        if (mTextRegion != null && mTextRegion!!.contains(x, y)) { //点击跳过
            mOnSkipClickListener?.invoke()
            NoviceGuideManager.get().removeFloatingViewIfExit(mActivity)
            return true
        }
        return false
    }

    //点击其它区域
    private fun clickOther() {
        if (mCanTouchEmptyRegion) {
            mOnSkipClickListener?.invoke()
            NoviceGuideManager.get().removeFloatingViewIfExit(mActivity)
        }
    }

}
