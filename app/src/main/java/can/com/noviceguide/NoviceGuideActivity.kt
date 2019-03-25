package can.com.noviceguide

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import can.com.novice_guide.bean.NoviceGuideInfoBean
import can.com.novice_guide.enums.NoviceGuidePictureLocationType
import can.com.novice_guide.enums.NoviceGuideViewShapeType
import can.com.novice_guide.manager.NoviceGuideManager
import java.util.*

class NoviceGuideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novice_guide)
        val tv = findViewById(R.id.tv)
        val tv2 = findViewById(R.id.tv2)
        val tv3 = findViewById(R.id.tv3)
        val tv4 = findViewById(R.id.tv4)
        val tv5 = findViewById(R.id.tv5)
        val map = WeakHashMap<View?, NoviceGuideInfoBean>()
        val bean1 = NoviceGuideInfoBean(R.drawable.ic_novice_guide_click_look_program, NoviceGuidePictureLocationType.RIGHT, NoviceGuideViewShapeType.CIRCLE)
        val bean2 = NoviceGuideInfoBean(R.drawable.ic_novice_guide_click_enter_live_room, NoviceGuidePictureLocationType.BOTTOM, NoviceGuideViewShapeType.ROUND)
        val bean3 = NoviceGuideInfoBean(R.drawable.ic_novice_guide_click_enter_live_list, NoviceGuidePictureLocationType.TOP, NoviceGuideViewShapeType.ROUND)
        val bean4 = NoviceGuideInfoBean(R.drawable.ic_novice_guide_click_look_program,NoviceGuidePictureLocationType.LEFT,NoviceGuideViewShapeType.CIRCLE)
        var isShow = false
        tv.setOnClickListener {
            isShow = !isShow
            if (isShow) {
                map.put(tv, bean1)
                NoviceGuideManager.get().addNoviceGuide(this@NoviceGuideActivity, map){ _,_->
                    NoviceGuideManager.get().removeFloatingViewIfExit(this@NoviceGuideActivity)
                    isShow = false
                }
            }
        }

        tv2.setOnClickListener {
            isShow = !isShow
            if (isShow) {
                map.put(tv2, bean2)
                NoviceGuideManager.get().addNoviceGuide(this@NoviceGuideActivity, map){_,_->
                    NoviceGuideManager.get().removeFloatingViewIfExit(this@NoviceGuideActivity)
                    isShow = false
                }
            }
        }

        tv3.setOnClickListener {
            isShow = !isShow
            if (isShow) {
                map.put(tv3, bean3)
                NoviceGuideManager.get().addNoviceGuide(this@NoviceGuideActivity, map)
            }else
                Toast.makeText(this@NoviceGuideActivity,"这是view的点击事件",Toast.LENGTH_SHORT).show()
        }

        tv4.setOnClickListener {
            isShow = !isShow
            if(isShow){
                map.put(tv4,bean4)
                NoviceGuideManager.get().addNoviceGuide(this@NoviceGuideActivity, map)
            }else
                Toast.makeText(this@NoviceGuideActivity,"这是view的点击事件",Toast.LENGTH_SHORT).show()
        }

        tv5.setOnClickListener {
            isShow = !isShow
            if(isShow){
                map.put(tv, bean1)
                map.put(tv2, bean2)
                map.put(tv3, bean3)
                map.put(tv4,bean4)
                NoviceGuideManager.get().addNoviceGuide(this@NoviceGuideActivity, map)
            }
        }

        NoviceGuideManager.get().addNoviceGuide(this, map)
    }

}
