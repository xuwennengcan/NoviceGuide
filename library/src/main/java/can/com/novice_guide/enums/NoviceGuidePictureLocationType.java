package can.com.novice_guide.enums;

import android.support.annotation.IntDef;

import static can.com.novice_guide.enums.NoviceGuidePictureLocationType.BOTTOM;
import static can.com.novice_guide.enums.NoviceGuidePictureLocationType.LEFT;
import static can.com.novice_guide.enums.NoviceGuidePictureLocationType.RIGHT;
import static can.com.novice_guide.enums.NoviceGuidePictureLocationType.TOP;

/**
 * Created by CAN on 19-2-14.
 * view对应图片位置
 */
@IntDef({LEFT,RIGHT,TOP,BOTTOM})
public @interface NoviceGuidePictureLocationType {
    int LEFT = 0; //左
    int RIGHT = 1; //右
    int TOP = 2; //上
    int BOTTOM = 3; //下
}
