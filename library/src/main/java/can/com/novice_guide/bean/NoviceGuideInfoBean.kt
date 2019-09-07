package can.com.novice_guide.bean

/**
 * Created by CAN on 19-2-15.
 * 新手引导信息类
 * @param bitmapResource 绘制的图片资源
 * @param pictureLocationType 绘制的图片位置
 * @param viewShapeType 绘制的高亮区域的形状
 * @param needDrawOuter 是否绘制外圈
 * @param viewResource 绘制高亮区域为自定义资源
 * @param viewResourcePadding 绘制高亮区域的间距
 * @param highLightRectPadding 高亮的间距
 * @param bitmapIsCenter 绘制的图片是否居中
 */
class NoviceGuideInfoBean(val bitmapResource: Int,
                          val pictureLocationType: Int,
                          val viewShapeType: Int,
                          val needDrawOuter: Boolean = true,
                          val viewResource: Int? = null,
                          val viewResourcePadding: Int = 0,
                          val highLightRectPadding: Int? = null,
                          val bitmapIsCenter: Boolean = false)
