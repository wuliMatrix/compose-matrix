
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.onCommit
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.core.composed
import androidx.ui.core.setContent
import androidx.ui.foundation.Box
import androidx.ui.foundation.Canvas
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Image
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.ImageAsset
import androidx.ui.graphics.asImageAsset
import androidx.ui.graphics.drawscope.drawCanvas
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.preferredHeight
import androidx.ui.layout.preferredHeightIn
import androidx.ui.layout.preferredWidth
import androidx.ui.res.loadImageResource
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import love.matrix.matrix.R

/**
 * 2020/7/9
 *
 * 组件来源： Learn-Jetpack-Compose-By-Example
 * https://github.com/vinaygaba/Learn-Jetpack-Compose-By-Example
 */

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun DisplayImagesComponent() {
    TitleComponent("Load image from the resource folder")
    LocalResourceImageComponent(R.drawable.lenna)

    TitleComponent("Load image from url using Picasso")
    NetworkImageComponentPicasso(
        url = "https://github.com/vinaygaba/CreditCardView/raw/master/images/Feature%20Image.png"
    )

    TitleComponent("Load image from url using Glide")
    NetworkImageComponentGlide(
        url = "https://github.com/vinaygaba/CreditCardView/raw/master/images/Feature%20Image.png"
    )

    TitleComponent("Image with rounded corners")
    ImageWithRoundedCorners(R.drawable.lenna)
}

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun LocalResourceImageComponent(@DrawableRes resId: Int) {
    // There are multiple methods available to load an image resource in Compose. However, it would
    // be advisable to use the loadImageResource method as it loads an image resource asynchronously
    val image = loadImageResource(resId)
    image.resource.resource?.let {
        // Image is a pre-defined composable that lays out and draws a given [ImageAsset].

        // You can think of Modifiers as implementations of the decorators pattern that are
        // used to modify the composable that its applied to. In this example, we configure the
        // Image composable to have a height of 200 dp.
        Image(asset = it, modifier = Modifier.preferredHeightIn(maxHeight = 200.dp))
    }
}

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun ImageWithRoundedCorners(@DrawableRes resId: Int) {
    // There are multiple methods available to load an image resource in Compose. However, it would
    // be advisable to use the loadImageResource method as it loads an image resource asynchronously
    val image = loadImageResource(resId)
    image.resource.resource?.let {
        // Box is a predefined convenience composable that allows you to apply common draw & layout
        // logic. In addition we also pass a few modifiers to it.

        // You can think of Modifiers as implementations of the decorators pattern that are
        // used to modify the composable that its applied to. In this example, we configure the
        // Box composable to have a height of 200dp, width of 200dp, alignment as center
        // and a custom draw modifier to clip the corners of the image.
        Box(
            modifier = 
                    Modifier.preferredHeight(200.dp) + Modifier.preferredWidth(200.dp)
                    + Modifier.RoundedCornerClipModifier(8.dp)
        ) {
            // Image is a pre-defined composable that lays out and draws a given [ImageAsset].
            Image(it)
        }
    }
}

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun NetworkImageComponentPicasso(url: String,
                                 modifier: Modifier = Modifier.fillMaxWidth() +
                                         Modifier.preferredHeightIn(maxHeight = 200.dp)) {
    // Source code inspired from - https://kotlinlang.slack.com/archives/CJLTWPH7S/p1573002081371500.
    // Made some minor changes to the code Leland posted.
    var image by state<ImageAsset?> { null }
    var drawable by state<Drawable?> { null }
    onCommit(url) {
        val picasso = Picasso.get()
        val target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                // TODO(lmr): we could use the drawable below
                drawable = placeHolderDrawable
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                drawable = errorDrawable
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                image = bitmap?.asImageAsset()
            }
        }
        picasso
            .load(url)
            .into(target)

        onDispose {
            image = null
            drawable = null
            picasso.cancelRequest(target)
        }
    }

    val theImage = image
    val theDrawable = drawable
    if (theImage != null) {
        // Box is a predefined convenience composable that allows you to apply common draw & layout
        // logic. In addition we also pass a few modifiers to it.

        // You can think of Modifiers as implementations of the decorators pattern that are
        // used to modify the composable that its applied to. In this example, we configure the
        // Box composable to have a max height of 200dp and fill out the entire available
        // width.
        Box(modifier = modifier,
            gravity = ContentGravity.Center
        ) {
            // Image is a pre-defined composable that lays out and draws a given [ImageAsset].
            Image(asset = theImage)
        }
    } else if (theDrawable != null) {
        Canvas(modifier = modifier) {
            drawCanvas { canvas, _ ->  
                theDrawable.draw(canvas.nativeCanvas)
            }
        }
    }
}

/**
 * There is an ongoing issue with kapt and compose since it uses the IR backend. This causes the
 * app to not even compile if you are using kapt- https://issuetracker.google.com/issues/143232368.
 * For that reason, I am avoiding a dependency on the glide annotation processor. If you add that
 * dependency(or any other kapt related dependency for that matter), the app won't even compile.
 */
@Composable
fun NetworkImageComponentGlide(url: String, modifier: Modifier = Modifier.fillMaxWidth() +
        Modifier.preferredHeightIn(maxHeight = 200.dp)) {
    var image by state<ImageAsset?> { null }
    var drawable by state<Drawable?> { null }
    val context = ContextAmbient.current
    onCommit(url) {
        val glide = Glide.with(context)
        val target = object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(placeholder: Drawable?) {
                image = null
                drawable = placeholder
            }

            override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                image = bitmap.asImageAsset()
            }
        }
        glide
            .asBitmap()
            .load(url)
            .into(target)

        onDispose {
            image = null
            drawable = null
            glide.clear(target)
        }
    }

    val theImage = image
    val theDrawable = drawable
    if (theImage != null) {
        // Box is a predefined convenience composable that allows you to apply common draw & layout
        // logic. In addition we also pass a few modifiers to it.

        // You can think of Modifiers as implementations of the decorators pattern that are
        // used to modify the composable that its applied to. In this example, we configure the
        // Box composable to have a max height of 200dp and fill out the entire available
        // width.
        Box(modifier = modifier,
            gravity = ContentGravity.Center) {
            // Image is a pre-defined composable that lays out and draws a given [ImageAsset].
            Image(asset = theImage)
        }
    } else if (theDrawable != null) {
        Canvas(modifier = modifier) {
            drawCanvas { canvas, _ ->
                theDrawable.draw(canvas.nativeCanvas)
            }
        }
    }
}

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should
// think of composable functions to be similar to lego blocks - each composable function is in turn
// built up of smaller composable functions.
@Composable
fun TitleComponent(title: String) {
    // Text is a predefined composable that does exactly what you'd expect it to - display text on
    // the screen. It allows you to customize its appearance using style, fontWeight, fontSize, etc.
    Text(title, style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.W900,
        fontSize = 14.sp, color = Color.Black), modifier = Modifier.padding(16.dp) +
            Modifier.fillMaxWidth()
    )
}

// RoundedCornerClipModifier is a custom Modifier that is responsible for clipping and
// providing a rounder corner to the composable its applied to.
// Recommendation from -
// https://kotlinlang.slack .com/archives/CJLTWPH7S/p1589826323481600?thread_ts=1589821110.478100&cid=CJLTWPH7S
// TODO(vinaygaba) Add some more comments here
fun Modifier.RoundedCornerClipModifier(size :Dp): Modifier = composed {
    val shape = RoundedCornerShape(size)
    clip(shape)
}

/**
 * Android Studio lets you preview your composable functions within the IDE itself, instead of
 * needing to download the app to an Android device or emulator. This is a fantastic feature as you
 * can preview all your custom components(read composable functions) from the comforts of the IDE.
 * The main restriction is, the composable function must not take any parameters. If your composable
 * function requires a parameter, you can simply wrap your component inside another composable
 * function that doesn't take any parameters and call your composable function with the appropriate
 * params. Also, don't forget to annotate it with @Preview & @Composable annotations.
 */
@Preview("Load image stored in local resources folder")
@Composable
fun LocalResourceImageComponentPreview() {
    Column {
        LocalResourceImageComponent(R.drawable.lenna)
    }
}

@Preview("Load an image over the network using the Picasso library")
@Composable
fun NetworkImageComponentPicassoPreview() {
    NetworkImageComponentPicasso("https://github.com/vinaygaba/CreditCardView/raw/master/images/Feature%20Image.png")
}

@Preview("Load an image over the network using the Glide library")
@Composable
fun NetworkImageComponentGlidePreview() {
    NetworkImageComponentGlide("https://github.com/vinaygaba/CreditCardView/raw/master/images/Feature%20Image.png")
}

@Preview("Add round corners to an image")
@Composable
fun ImageWithRoundedCornersPreview() {
    ImageWithRoundedCorners(R.drawable.lenna)
}
