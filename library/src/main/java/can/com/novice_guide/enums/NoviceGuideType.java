package can.com.novice_guide.enums;

import android.support.annotation.IntDef;

import static can.com.novice_guide.enums.NoviceGuideType.BOTTOM;
import static can.com.novice_guide.enums.NoviceGuideType.LEFT;
import static can.com.novice_guide.enums.NoviceGuideType.RIGHT;
import static can.com.novice_guide.enums.NoviceGuideType.TOP;

/**
 * Created by CAN on 19-2-14.
 *
 */
@IntDef({LEFT,RIGHT,TOP,BOTTOM})
public @interface NoviceGuideType {
    int LEFT = 0;
    int RIGHT = 1;
    int TOP = 2;
    int BOTTOM = 3;
}
