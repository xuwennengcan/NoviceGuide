package can.com.noviceguide

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import can.com.novice_guide.bean.NoviceGuideInfoBean
import can.com.novice_guide.enums.NoviceGuidePictureLocationType
import can.com.novice_guide.enums.NoviceGuideViewShapeType
import can.com.novice_guide.uitls.NoviceGuideManager
import java.util.*

class NoviceGuideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novice_guide)
        val tv = findViewById(R.id.tv)
        val tv2 = findViewById(R.id.tv2)
        val map = HashMap<View?, NoviceGuideInfoBean>()
        val bean1 = NoviceGuideInfoBean(R.drawable.click_look,NoviceGuidePictureLocationType.RIGHT,NoviceGuideViewShapeType.CIRCLE)
        val bean2 = NoviceGuideInfoBean(R.drawable.enter_live,NoviceGuidePictureLocationType.BOTTOM,NoviceGuideViewShapeType.ROUND)
        map.put(tv, bean1)
        map.put(tv2, bean2)
        NoviceGuideManager.get().addNoviceGuide(this, map)
    }

    override fun onPause() {
        super.onPause()
        NoviceGuideManager.get().removeFloatingViewIfExit(this)
    }
}
