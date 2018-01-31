package com.example.java.eyepetizer.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import com.example.java.eyepetizer.R
import com.example.java.eyepetizer.utils.newIntent
import com.example.java.eyepetizer.utils.showToast
import org.jetbrains.anko.*

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermisson()

    }

    private fun requestPermisson() {
        val permissonList= ArrayList<String>()
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissonList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissonList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if(!permissonList.isEmpty()){
            ActivityCompat.requestPermissions(this, permissonList.toTypedArray(),1)
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
            SplashUI().setContentView(this)
            initView()
            setAnimation()
        }

    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==1) {
            if (grantResults.isNotEmpty()) {
                grantResults.forEachIndexed { index, i ->
                    if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                        showToast("${permissions[index]}权限被拒绝")
                    } else {
                        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        SplashUI().setContentView(this)
                        initView()
                        setAnimation()

                    }
                }
            }
        }
//        }else{
//            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
//            SplashUI().setContentView(this)
//            initView()
//            setAnimation()
//        }
    }


     fun setAnimation() {
        val alphaAnimation = AlphaAnimation(0.1f, 0.1f)
        alphaAnimation.duration=1000
        val scaleAnimation = ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF,0.5f)
        scaleAnimation.duration=1000
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(alphaAnimation)
        animationSet.addAnimation(scaleAnimation)
        animationSet.duration=1000
        find<ImageView>(R.id.iv_icon_splash).startAnimation(animationSet)
        animationSet.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                newIntent<MainActivity>()
                startActivity(intent)
                finish()
            }
        })

    }



    private fun initView() {
        val font:Typeface= Typeface.createFromAsset(this.assets,"fonts/Lobster-1.4.otf")
    }

    class SplashUI:AnkoComponent<SplashActivity>{
        override fun createView(ui: AnkoContext<SplashActivity>): View = with(ui){
            relativeLayout {
                backgroundResource=R.drawable.landing_background
                imageView(R.drawable.ic_action_focus_white_no_margin){
                    id=R.id.iv_icon_splash
                }.lparams(width=dip(61),height = dip(42)){
                    centerInParent()

                }
                textView("Eyepetizer"){
                    textColorResource=R.color.white
                    id=R.id.tv_name_english
                    textSize= 25f
                    typeface= Typeface.createFromAsset(context.assets,"fonts/Lobster-1.4.otf")
                }.lparams(width= wrapContent,height = wrapContent){
                    topMargin=dip(20)
                    centerInParent()
                    below(R.id.iv_icon_splash)
                }
                textView("开 眼"){
                    textColorResource=R.color.white
                    textSize= 18f
                }.lparams(width= wrapContent,height = wrapContent){
                    topMargin=dip(20)
                    centerInParent()
                    below(R.id.tv_name_english)
                }
                textView("Daily appetizers for your eyes，Bon eyepetit"){
                    id=R.id.tv_english_intro
                    textColorResource=R.color.white
                    typeface= Typeface.createFromAsset(context.assets,"fonts/Lobster-1.4.otf")
                }.lparams(width= wrapContent,height = wrapContent){
                    centerInParent()
                    bottomMargin=dip(16)
                    above(R.id.tv_intro)
                }
                textView("每日精选视频推介，让你大开眼界。"){
                    textColorResource=R.color.white
                    id=R.id.tv_intro
                }.lparams(width= wrapContent,height = wrapContent){
                    bottomMargin=dip(46)
                    alignParentBottom()
                    centerHorizontally()
                }

            }
        }

    }


}

