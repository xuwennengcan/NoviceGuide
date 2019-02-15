package can.com.novice_guide.enums;

import android.support.annotation.IntDef;

import static can.com.novice_guide.enums.NoviceGuideViewShapeType.CIRCLE;
import static can.com.novice_guide.enums.NoviceGuideViewShapeType.ROUND;

/**
 * Created by CAN on 19-2-15.
 * view的形状
 */
@IntDef({CIRCLE,ROUND})
public @interface NoviceGuideViewShapeType {
    int CIRCLE = 0; //圆
    int ROUND = 1; //椭圆
}
