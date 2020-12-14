package app.dtse.hmsskeletondetection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.huawei.hmf.tasks.Task
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.skeleton.MLSkeleton
import com.huawei.hms.mlsdk.skeleton.MLSkeletonAnalyzer
import com.huawei.hms.mlsdk.skeleton.MLSkeletonAnalyzerFactory
import com.huawei.hms.mlsdk.skeleton.MLSkeletonAnalyzerSetting
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var graphicOverlay: GraphicOverlay? = null
    private var TAG = MainActivity::class.java.simpleName
    private var analyzer: MLSkeletonAnalyzer? = null
    private var frame: MLFrame? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        graphicOverlay = findViewById(R.id.skeleton_previewOverlay)

        buttonNormalDetect.setOnClickListener {
            graphicOverlay?.clear()
            initFrame(MLSkeletonAnalyzerSetting.TYPE_NORMAL)
        }

        buttonYogaDetect.setOnClickListener {
            graphicOverlay?.clear()
            initFrame(MLSkeletonAnalyzerSetting.TYPE_YOGA)
        }
    }

    private fun initAnalyzer(analyzerType: Int) {
        val setting = MLSkeletonAnalyzerSetting.Factory()
            .setAnalyzerType(analyzerType)
            .create()
        analyzer = MLSkeletonAnalyzerFactory.getInstance().getSkeletonAnalyzer(setting)

        imageSkeletonDetectAsync()
    }

    private fun initFrame(type: Int) {
        val originBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.sport)

        val maxHeight = (imageView.parent as View).height
        val targetWidth = (imageView.parent as View).width

        // Update bitmap size

        val scaleFactor = (originBitmap.width.toFloat() / targetWidth.toFloat())
            .coerceAtLeast(originBitmap.height.toFloat() / maxHeight.toFloat())

        val resizedBitmap = Bitmap.createScaledBitmap(
            originBitmap,
            (originBitmap.width / scaleFactor).toInt(),
            (originBitmap.height / scaleFactor).toInt(),
            true
        )

        frame = MLFrame.fromBitmap(resizedBitmap)
        initAnalyzer(type)
    }

    private fun imageSkeletonDetectAsync() {
        val task: Task<List<MLSkeleton>>? = analyzer?.asyncAnalyseFrame(frame)
        task?.addOnSuccessListener { results ->

            // Detection success.
            val skeletons: List<MLSkeleton>? = getValidSkeletons(results)
            if (skeletons != null && skeletons.isNotEmpty()) {
                graphicOverlay?.clear()
                val skeletonGraphic = SkeletonGraphic(graphicOverlay, results)
                graphicOverlay?.add(skeletonGraphic)

            } else {
                Log.e(TAG, "async analyzer result is null.")
            }
        }?.addOnFailureListener { /* Result failure. */ }
    }

    private fun stopAnalyzer() {
        if (analyzer != null) {
            try {
                analyzer?.stop()
            } catch (e: IOException) {
                Log.e(TAG, "Failed for analyzer: " + e.message)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAnalyzer()
    }
}