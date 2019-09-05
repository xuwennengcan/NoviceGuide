package can.com.novice_guide.bean

/**
 * Created by CAN on 19-2-15.
 * 新手引导信息类
 */
class NoviceGuideInfoBean(val bitmapResource: Int,
                          val pictureLocationType: Int,
                          val viewShapeType: Int,
                          val needDrawOuter: Boolean = true,
                          val viewResource: Int? = null,
                          val viewResourcePadding: Int = 0,
                          val highLightRectPadding: Int? = null)
