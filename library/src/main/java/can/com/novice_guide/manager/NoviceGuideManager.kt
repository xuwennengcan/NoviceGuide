package can.com.novice_guide.manager

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import can.com.novice_guide.bean.NoviceGuideInfoBean
import can.com.novice_guide.widgets.NoviceGuideFloatingLayerView
import java.util.*

/**
 * Created by CAN on 19-2-14.
 * 新手引导管理类
 */
class NoviceGuideManager private constructor() {

    private val frameLayoutMaps = WeakHashMap<Activity, View>()

    companion object {
        private var instance: NoviceGuideManager? = null
            get() {
                if (field == null) {
                    field = NoviceGuideManager()
                }
                return field
            }

        fun get(): NoviceGuideManager {
            return instance!!
        }
    }

    //添加浮层
    fun addNoviceGuide(activity: Activity, map: WeakHashMap<View?, NoviceGuideInfoBean> , onClickListener : ((View)->Unit)? = null) {

        removeFloatingViewIfExit(activity)

        if (map.isEmpty()) {
            return
        } else {
            val keys: MutableSet<View?>? = map.keys
            if (keys != null) {
                for (i in keys) {
                    if (i == null || i.visibility == View.GONE) {
                        map.remove(i)
                    }
                }
            }
        }

        if (map.isEmpty())
            return

        val viewGroup = activity.window.decorView as ViewGroup
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val frameLayout = NoviceGuideFloatingLayerView(activity,map,onClickListener)
        frameLayout.layoutParams = layoutParams
        viewGroup.addView(frameLayout)
        frameLayoutMaps.put(activity, frameLayout)
    }

    //移除浮层
    fun removeFloatingViewIfExit(activity: Activity?) : Boolean {
        if(activity!=null&&!activity.isDestroyed&&frameLayoutMaps.containsKey(activity)){
            val viewGroup = activity.window.decorView as ViewGroup
            val frameLayout: View? = frameLayoutMaps[activity]
            if (frameLayout != null) {
                viewGroup.removeView(frameLayout)
                frameLayoutMaps.remove(activity)
                return true
            }
        }
        return false
    }

}