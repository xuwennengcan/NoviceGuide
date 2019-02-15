package can.com.novice_guide.widgets

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import can.com.novice_guide.bean.NoviceGuideInfoBean
import can.com.novice_guide.enums.NoviceGuidePictureLocationType
import can.com.novice_guide.enums.NoviceGuideViewShapeType
import java.util.*

/**
 * Created by CAN on 19-2-14.
 * 新手引导-浮层View
 */

class NoviceGuideFloatingLayerView : FrameLayout {

    private val mBgColor = -0x4d000000 //背景色
    private val mInnerPaintColor = -0x7f000001 //内圈画笔颜色
    private val mOuterPaintColor = 0x4dffffff //外圈画笔颜色
    private val mTextPaintColor = 0xffffff //文字画笔颜色
    private var mInnerPaint = Paint() //内圈画笔
    private var mOuterPaint = Paint() //外圈画笔
    private var mTextPaint = Paint() //文字画笔

    private val mText = "跳过" //文字

    private val mInnerOuterPadding = 10//外圈与内圈的距离
    private val mHighLightRectPadding = 10//间距

    private var mMap: HashMap<View?, NoviceGuideInfoBean>? = null //数据

    constructor(context: Context) : super(context)

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.i("tag", "aaaaa")
    }

    constructor(context: Context, map: HashMap<View?, NoviceGuideInfoBean>) : super(context) {
        this.mMap = map
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
    private fun initTextPaint(paint: Paint) {
        paint.color = mTextPaintColor
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.textSize = 24.0f
        paint.strokeWidth = 3f

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
        paint.strokeWidth = 4f
        paint.isAntiAlias = true
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        //绘制虚线
        val dashPathEffect = DashPathEffect(floatArrayOf(12f, 4f), 1f)
        paint.pathEffect = dashPathEffect
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawText(mText, left.toFloat(), top.toFloat(), mTextPaint)

        val keys: MutableSet<View?>? = mMap?.keys

        if (keys != null) {
            for (view in keys) {
                val bean = mMap?.get(view)
                drawHighLightView(canvas, view, bean)

            }
        }

    }

    //绘制高亮view
    private fun drawHighLightView(canvas: Canvas?, view: View?, bean: NoviceGuideInfoBean?) {
        if (canvas == null || view == null || bean == null)
            return
        val highlightView = NoviceGuideHighlightView(view) //高亮view
        val innerRectF = highlightView.getRectF(mHighLightRectPadding) //高亮view的位置信息(内圈)
        val outerRectF = RectF(innerRectF.left - mInnerOuterPadding, innerRectF.top - mInnerOuterPadding,
                innerRectF.right + mInnerOuterPadding, innerRectF.bottom + mInnerOuterPadding)//高亮view的位置信息(外圈)
        if (bean.viewShapeType == NoviceGuideViewShapeType.CIRCLE) {//绘制圆
            canvas.drawCircle((innerRectF.right - innerRectF.left) / 2 + innerRectF.left,
                    (innerRectF.bottom - innerRectF.top) / 2 + innerRectF.top,
                    Math.max(innerRectF.width() / 2, innerRectF.height() / 2),
                    mInnerPaint)
            canvas.drawCircle((outerRectF.right - outerRectF.left) / 2 + outerRectF.left,
                    (outerRectF.bottom - outerRectF.top) / 2 + outerRectF.top,
                    Math.max(outerRectF.width() / 2, outerRectF.height() / 2),
                    mOuterPaint)
        } else if (bean.viewShapeType == NoviceGuideViewShapeType.ROUND) {//绘制椭圆
            canvas.drawRoundRect(innerRectF, highlightView.radius, highlightView.radius, mInnerPaint)
            canvas.drawRoundRect(outerRectF, highlightView.radius, highlightView.radius, mOuterPaint)
        }

        val bitmap = BitmapFactory.decodeResource(resources, bean.bitmapResource)
        if (bitmap != null && !bitmap.isRecycled) {

            when(bean.pictureLocationType){
                NoviceGuidePictureLocationType.LEFT->{
                    canvas.drawBitmap(bitmap, width-outerRectF.left, outerRectF.top, Paint())
                }
                NoviceGuidePictureLocationType.RIGHT->{
                    canvas.drawBitmap(bitmap, outerRectF.right, outerRectF.top, Paint())
                }
                NoviceGuidePictureLocationType.TOP->{
                    canvas.drawBitmap(bitmap, ((width-bitmap.width)/2).toFloat(), outerRectF.top - bitmap.height, Paint())
                }
                NoviceGuidePictureLocationType.BOTTOM->{
                    canvas.drawBitmap(bitmap, ((width-bitmap.width)/2).toFloat(), outerRectF.bottom, Paint())
                }
            }

            bitmap.recycle()
        }

    }

    private fun drawBitmap(canvas: Canvas?, view: View?, bean: NoviceGuideInfoBean?) {
        if (canvas == null || bean == null)
            return




    }


}
