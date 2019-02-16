package can.com.novice_guide.widgets

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.provider.CalendarContract
import android.text.TextPaint
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import can.com.novice_guide.bean.NoviceGuideInfoBean
import can.com.novice_guide.enums.NoviceGuidePictureLocationType
import can.com.novice_guide.enums.NoviceGuideViewShapeType
import can.com.novice_guide.uitls.NoviceGuideManager
import java.util.*

/**
 * Created by CAN on 19-2-14.
 * 新手引导-浮层View
 */

class NoviceGuideFloatingLayerView : FrameLayout {

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

    private var mText = "跳过" //文字

    private val mInnerOuterPadding = 10//外圈与内圈的距离
    private val mHighLightRectPadding = 10//高亮view与原始view的间距
    private val mViewBitmapPadding = 20 //高亮view和图片间距

    constructor(context: Context) : super(context)

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mActivity = null
        mMap?.clear()
        mRegionMap.clear()
    }

    constructor(context: Activity, map: WeakHashMap<View?, NoviceGuideInfoBean>) : super(context) {
        this.mMap = map
        this.mActivity = context
        init()
    }

    //一些初始化工作
    private fun init() {
        setBackgroundColor(mBgColor)
        initInnerPaint(mInnerPaint)
        initOuterPaint(mOuterPaint)
        initTextPaint(mTextPaint)
    }

    //初始化文字画笔
    private fun initTextPaint(paint: TextPaint) {
        paint.color = mTextPaintColor
        paint.textSize = 36.0f
        paint.isAntiAlias = true
    }

    //初始化内圈画笔
    private fun initInnerPaint(paint: Paint) {
        paint.color = mInnerPaintColor
        paint.isAntiAlias = true
        //图像模式为清除图像模式
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

        drawText(canvas,mText)

        val keys: MutableSet<View?>? = mMap?.keys

        if (keys != null) {
            for (view in keys) {
                val bean = mMap?.get(view)
                drawView(canvas, view, bean)
            }
        }

    }

    //绘制文本
    private fun drawText(canvas:Canvas?,text : String?){
        if(text!=null&&canvas != null){
            canvas.drawText(mText,100f, 100f, mTextPaint)
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
            canvas.drawCircle((outerRectF.right - outerRectF.left) / 2 + outerRectF.left,
                    (outerRectF.bottom - outerRectF.top) / 2 + outerRectF.top,
                    Math.max(outerRectF.width() / 2, outerRectF.height() / 2), mOuterPaint)
        } else if (bean.viewShapeType == NoviceGuideViewShapeType.ROUND) {//绘制椭圆
            canvas.drawRoundRect(innerRectF, highlightView.radius, highlightView.radius, mInnerPaint)
            canvas.drawRoundRect(outerRectF, highlightView.radius, highlightView.radius, mOuterPaint)
        }

        //绘制bitmap
        val bitmap : Bitmap? = BitmapFactory.decodeResource(resources, bean.bitmapResource)
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
        mRegionMap.put(view, Region(highlightView.getRect()))
    }

    //点击事件的处理
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x.toInt()
                val y = event.y.toInt()
                if (mRegionMap.size > 0) {
                    val keys: MutableSet<View?>? = mRegionMap.keys
                    if (keys != null) {
                        for (view in keys) {
                            if (view != null) {
                                val region = mRegionMap[view]
                                if (region != null && region.contains(x, y)) {
                                    if (mActivity != null)
                                        NoviceGuideManager.get().removeFloatingViewIfExit(mActivity!!)
                                    view.performClick()
                                }
                            }
                        }
                    }
                }
            }
        }
        return true
    }

}
