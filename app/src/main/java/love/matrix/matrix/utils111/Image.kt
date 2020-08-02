package com.instagram.clone.utils111

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.Composable
import androidx.compose.onCommit
import androidx.compose.state
import androidx.ui.core.Constraints.Companion.Infinity
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.core.WithConstraints
import androidx.ui.foundation.Canvas
import androidx.ui.foundation.Image
import androidx.ui.graphics.ImageAsset
import androidx.ui.graphics.asImageAsset
import androidx.ui.graphics.drawscope.drawCanvas
import androidx.ui.layout.fillMaxSize

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.bumptech.glide.request.transition.Transition

@Composable
fun GlideImage(
    model: Any,
    customize: RequestBuilder<Bitmap>.() -> RequestBuilder<Bitmap> = { this }
) {
    WithConstraints {
        val image = state<ImageAsset?> { null }
        val drawable = state<Drawable?> { null }
        val context = ContextAmbient.current

        onCommit(model) {
            val target = object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    image.value = null
                    drawable.value = placeholder
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    image.value = resource.asImageAsset()
                }
            }

            val width =
                if (constraints.maxWidth in 1 until Infinity) {
                    constraints.maxWidth
                } else {
                    SIZE_ORIGINAL
                }

            val height =
                if (constraints.maxHeight in 1 until Infinity) {
                    constraints.maxHeight
                } else {
                    SIZE_ORIGINAL
                }

            val glide = Glide.with(context)
            glide
                .asBitmap()
                .load(model)
                .override(width, height)
                .let(customize)
                .into(target)

            onDispose {
                image.value = null
                drawable.value = null
                glide.clear(target)
            }
        }

        val theImage = image.value
        val theDrawable = drawable.value
        if (theImage != null) {
            Image(asset = theImage)
        } else if (theDrawable != null) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCanvas { canvas, _ ->
                theDrawable.draw(canvas.nativeCanvas)
            } }
        }
    }
}