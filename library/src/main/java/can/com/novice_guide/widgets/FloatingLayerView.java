package can.com.novice_guide.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import can.com.novice_guide.enums.NoviceGuideType;
import can.com.novice_guide.uitls.NoviceGuideManager;

/**
 * Created by CAN on 19-2-14.
 * 浮层View
 */

public class FloatingLayerView extends FrameLayout {

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //释放bitmap

    }

    public FloatingLayerView(@NonNull Context context) {
        super(context);
    }


    public FloatingLayerView(Context context, HashMap<View,HashMap<Integer,NoviceGuideType>> map){
        super(context);

    }



}
