# NoviceGuide
自定义新手引导
### 引入
    implementation 'com.github.xuwennengcan:NoviceGuide:1.0.0'
### 使用
    val map = WeakHashMap<View?, NoviceGuideInfoBean>()
    val bean = NoviceGuideInfoBean(R.drawable.click_look, NoviceGuidePictureLocationType.RIGHT, NoviceGuideViewShapeType.CIRCLE)
    map.put(view,bean)
    NoviceGuideManager.get().addNoviceGuide(this@NoviceGuideActivity, map)
### 效果图
    
