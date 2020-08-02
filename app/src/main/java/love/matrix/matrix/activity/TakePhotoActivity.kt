package love.matrix.matrix.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import love.matrix.matrix.R
import love.matrix.matrix.ui.common.MatrixTheme
import love.matrix.matrix.ui.photo.PhotoScreen

class TakePhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // setContentView(R.layout.activity_splash_short)
        setContent {
            MatrixTheme {
                PhotoScreen()
            }
        }
    }

}

