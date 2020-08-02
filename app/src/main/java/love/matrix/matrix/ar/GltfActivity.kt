package love.matrix.matrix.ar

import android.app.Activity
import android.app.ActivityManager
import android.content.Context

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.ArraySet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.gltfio.Animator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
//import kotlinx.android.synthetic.main.activity_main_face_ar.*
import com.google.ar.sceneform.rendering.Color
import love.matrix.matrix.R
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit


class GltfActivity : AppCompatActivity() {
    private lateinit var fab_capture : FloatingActionButton
    private lateinit var pb_capture : ProgressBar
    private lateinit var arFragment: ArFragment
    private lateinit var videoRecorder: VideoRecorder
    private var renderable: Renderable? = null
    private val animators: MutableSet<AnimationInstance> = ArraySet()

    private class AnimationInstance internal constructor(
        var animator: Animator,
        index: Int,
        var startTime: Long
    ) {
        var duration: Float = animator.getAnimationDuration(index)
        var index01: Int = index
    }

    private var nextColor = 0
    private val colors = listOf(
        Color(0F, 0F, 0F, 1F),
        Color(1F, 0F, 0F, 1F),
        Color(0F, 1F, 0F, 1F),
        Color(0F, 0F, 1F, 1F),
        Color(1F, 1F, 0F, 1F),
        Color(0F, 1F, 1F, 1F),
        Color(1F, 0F, 1F, 1F),
        Color(1F, 1F, 1F, 1F)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return
        }
        setContentView(R.layout.activity_main_face_ar)
        fab_capture = findViewById(R.id.fab_capture)
        pb_capture = findViewById(R.id.pb_capture)
        arFragment = supportFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment

        setViewsListener()
        loadModels()
        initVideoRecorder()
    }

    private fun initVideoRecorder() {
        videoRecorder = VideoRecorder(this)
        videoRecorder.setSceneView(arFragment.arSceneView)
    }

    private fun setViewsListener() {

        fab_capture.run {
            setOnClickListener {
                hideControls()
                takePhoto(this@GltfActivity, arFragment.arSceneView) {
                    showControls()
                }
            }
            setOnLongClickListener {
                hideControls()
                fab_capture.setImageResource(R.drawable.ar_stop)
                videoRecorder.toggleRecording()
                true
            }
            setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    val eventDuration = motionEvent.eventTime - motionEvent.downTime;
                    if (eventDuration > 500) {
                        fab_capture.setImageResource(R.drawable.ar_camera)
                        videoRecorder.stopRecording()
                        showControls()
                    }
                }
                false
            }
        }
    }

    private fun showControls() {
        pb_capture.visibility = GONE
        fab_capture.isClickable = true
    }

    private fun hideControls() {
        pb_capture.visibility = VISIBLE
        fab_capture.isClickable = false
    }

    private fun loadModels() {
        val weakActivity = WeakReference(this)
        ModelRenderable.builder()
            .setSource(
                this,
                Uri.parse(
                    "https://storage.googleapis.com/ar-answers-in-search-models/static/Tiger/model.glb"
                )
            )
            .setIsFilamentGltf(true)
            .build()
            .thenAccept { modelRenderable: ModelRenderable? ->
                val activity = weakActivity.get()
                if (activity != null) {
                    activity.renderable = modelRenderable
                }
            }
            .exceptionally {
                val toast =
                    Toast.makeText(this, "Unable to load Tiger renderable", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            }

        arFragment!!.setOnTapArPlaneListener { hitResult: HitResult, _: Plane?, _: MotionEvent? ->
            if (renderable == null) {
                return@setOnTapArPlaneListener
            }
            // Create the Anchor.
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment!!.arSceneView.scene)
            // Create the transformable model and add it to the anchor.
            val model = TransformableNode(arFragment!!.transformationSystem)
            model.setParent(anchorNode)
            model.renderable = renderable
            model.select()
            val filamentAsset = model.renderableInstance!!.filamentAsset
            if (filamentAsset!!.animator.animationCount > 0) {
                animators.add(AnimationInstance(filamentAsset.animator, 0, System.nanoTime()))
            }
            val color = colors[nextColor]
            nextColor++
            for (i in 0 until renderable!!.submeshCount) {
                val material = renderable!!.getMaterial(i)
                material.setFloat4("baseColorFactor", color)
            }
        }

        arFragment!!
            .arSceneView
            .scene
            .addOnUpdateListener {
                val time = System.nanoTime()
                for (animator in animators) {
                    animator.animator.applyAnimation(
                        animator.index01,
                        ((time - animator.startTime) / TimeUnit.SECONDS.toNanos(1)
                            .toDouble()).toFloat()
                                % animator.duration
                    )
                    animator.animator.updateBoneMatrices()
                }
            }
    }


    companion object {
        private val TAG = GltfActivity::class.java.simpleName
        private const val MIN_OPENGL_VERSION = 3.0

        /**
         * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
         * on this device.
         *
         *
         * Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
         *
         *
         * Finishes the activity if Sceneform can not run
         */
        fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Log.e(TAG, "Sceneform requires Android N or later")
                Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG)
                    .show()
                activity.finish()
                return false
            }
            val openGlVersionString =
                (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                    .deviceConfigurationInfo
                    .glEsVersion
            if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
                Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later")
                Toast.makeText(
                    activity,
                    "Sceneform requires OpenGL ES 3.0 or later",
                    Toast.LENGTH_LONG
                )
                    .show()
                activity.finish()
                return false
            }
            return true
        }
    }
}