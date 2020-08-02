package love.matrix.matrix

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions


class SplashActivity : AppCompatActivity() {

    private lateinit var imageLogo : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sp = getSharedPreferences("scope", 0)
        val editor = sp.edit()
        val random = (1..8).random()
        var count = sp.getInt("count", 0)
        if (count > 8 && random != 8) {
            setContentView(R.layout.activity_splash_short)
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)

               // runWithPermissions(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_CONTACTS) {
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in_short, R.anim.fade_out_short)
                    finish()
               // }
            }, 100)
        } else {
            editor.putInt("count", ++count).apply()
            setContentView(R.layout.activity_splash)
            imageLogo = findViewById(R.id.imageView3)
            val metric = DisplayMetrics()
            windowManager.defaultDisplay.getRealMetrics(metric)
            val size = (metric.heightPixels * 122.88f / 1000).toInt()
            imageLogo.layoutParams.height = size
            imageLogo.layoutParams.width = size

            val animatorSet = AnimatorSet()
            val animatorLeft =
                ObjectAnimator.ofFloat(imageLogo, "rotation", 0f, 720f).setDuration(2200)
            val animatorRight =
                ObjectAnimator.ofFloat(imageLogo, "rotation", 0f, -720f).setDuration(2200)
            animatorSet.play(animatorLeft).after(animatorRight)
            animatorSet.start()

            var flag = true
            imageLogo.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        flag = false
                        val intent = Intent(this, MainActivity::class.java)
                       // runWithPermissions(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_CONTACTS) {
                            startActivity(intent)
                            overridePendingTransition(R.anim.fade_in_short, R.anim.fade_out_short)
                            finish()
                       // }

                    }
                }
                true
            }

            Handler().postDelayed({
                if (flag) {
                    val intent = Intent(this, MainActivity::class.java)
                    //runWithPermissions(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_CONTACTS) {
                        startActivity(intent)
                        overridePendingTransition(R.anim.fade_in_short, R.anim.fade_out_short)
                        finish()
                    //}
                }
            }, 5200)
        }
    }

}
