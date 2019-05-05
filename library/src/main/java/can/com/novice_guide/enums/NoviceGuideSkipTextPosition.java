package can.com.novice_guide.enums;

import android.support.annotation.IntDef;

import static can.com.novice_guide.enums.NoviceGuideSkipTextPosition.LEFT_TOP;
import static can.com.novice_guide.enums.NoviceGuideSkipTextPosition.RIGHT_TOP;

/**
 * Created by CAN on 19-5-5.
 * view对应图片位置
 */
@IntDef({LEFT_TOP,RIGHT_TOP})
public @interface NoviceGuideSkipTextPosition {
    int LEFT_TOP = 0; //左上
    int RIGHT_TOP = 1; //右上
}
