package can.com.novice_guide.uitls

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import can.com.novice_guide.enums.NoviceGuideType
import can.com.novice_guide.widgets.FloatingLayerView
import java.util.*

/**
 * Created by CAN on 19-2-14.
 * 新手引导管理类
 */
class NoviceGuideManager {

    private val frameLayoutMaps = WeakHashMap<Activity, FloatingLayerView>()

    companion object {
        var instance: NoviceGuideManager? = null
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
    fun addNoviceGuide(activity: Activity, map : HashMap<View,HashMap<Int,NoviceGuideType>> ) {
        if(map.isEmpty()){
            return
        }else{
            val mIsAllGone = true

            val keys : MutableSet<View> = map.keys
            val values : MutableCollection<HashMap<Int, NoviceGuideType>> = map.values

            for(i in 0..keys.size){

            }

            for(i in keys){
                if(i.visibility==View.GONE){
                    map.remove(i)
                }
            }

        }

        if(map.isEmpty())
            return


        val viewGroup = activity.window.decorView as ViewGroup
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val frameLayout = FloatingLayerView(activity,map)

        frameLayout.layoutParams = layoutParams
        viewGroup.addView(frameLayout)
        frameLayoutMaps.put(activity, frameLayout)
    }

}