package app.dtse.hmsskeletondetection

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.huawei.hms.mlsdk.skeleton.MLJoint
import com.huawei.hms.mlsdk.skeleton.MLSkeleton
import java.util.*
import kotlin.math.abs

class SkeletonGraphic constructor(overlay: GraphicOverlay?, skeletons: List<MLSkeleton>?) :
    GraphicOverlay.Graphic(overlay) {
    private var skeletons: List<MLSkeleton>? = null

    private var circlePaint: Paint? = null

    private var linePaint: Paint? = null

    init {
        this.skeletons = skeletons
        circlePaint = Paint()
        circlePaint!!.color = Color.BLUE
        circlePaint!!.style = Paint.Style.FILL
        circlePaint!!.isAntiAlias = true
        linePaint = Paint()
        linePaint!!.color = Color.MAGENTA
        linePaint!!.style = Paint.Style.STROKE
        linePaint!!.strokeWidth = 10f
        linePaint!!.isAntiAlias = true
    }

    override fun draw(canvas: Canvas?) {
        for (i in skeletons!!.indices) {
            val skeleton = skeletons!![i]
            if (skeleton.joints == null) {
                continue
            }
            val paths: MutableList<Path?> = ArrayList()
            paths.add(
                getPath(
                    skeleton.getJointPoint(MLJoint.TYPE_HEAD_TOP),
                    skeleton.getJointPoint(MLJoint.TYPE_NECK)
                )
            )
            paths.add(
                getPath(
                    skeleton.getJointPoint(MLJoint.TYPE_NECK),
                    skeleton.getJointPoint(MLJoint.TYPE_LEFT_SHOULDER)
                )
            )
            paths.add(
                getPath(
                    skeleton.getJointPoint(MLJoint.TYPE_LEFT_SHOULDER),
                    skeleton.getJointPoint(MLJoint.TYPE_LEFT_ELBOW)
                )
            )
            paths.add(
                getPath(
                    skeleton.getJointPoint(MLJoint.TYPE_LEFT_ELBOW),
                    skeleton.getJointPoint(MLJoint.TYPE_LEFT_WRIST)
                )
            )
            paths.add(
                getPath(
                    skeleton.getJointPoint(MLJoint.TYPE_NECK),
                    skeleton.getJointPoint(MLJoint.TYPE_LEFT_HIP)
                )
            )
            paths.add(
                getPath(
                    skeleton.getJointPoint(MLJoint.TYPE_LEFT_HIP),
                    skeleton.getJointPoint(MLJoint.TYPE_LEFT_KNEE)
                )
            )
            paths.add(
                getPath(
                    skeleton.getJointPoint(MLJoint.TYPE_LEFT_KNEE),
                    skeleton.getJointPoint(MLJoint.TYPE_LEFT_ANKLE)
                )
            )
            paths.add(
                getPath(
                    skeleton.getJointPoint(MLJoint.TYPE_NECK),
                    skeleton.getJointPoint(MLJoint.TYPE_RIGHT_SHOULDER)
                )
            )
            paths.add(
                getPath(
                    skeleton.getJointPoint(MLJoint.TYPE_RIGHT_SHOULDER),
                    skeleton.getJointPoint(MLJoint.TYPE_RIGHT_ELBOW)
                )
            )
            paths.add(
                getPath(
                    skeleton.getJointPoint(MLJoint.TYPE_RIGHT_ELBOW),
                    skeleton.getJointPoint(MLJoint.TYPE_RIGHT_WRIST)
                )
            )
            paths.add(
                getPath(
                    skeleton.getJointPoint(MLJoint.TYPE_NECK),
                    skeleton.getJointPoint(MLJoint.TYPE_RIGHT_HIP)
                )
            )
            paths.add(
                getPath(
                    skeleton.getJointPoint(MLJoint.TYPE_RIGHT_HIP),
                    skeleton.getJointPoint(MLJoint.TYPE_RIGHT_KNEE)
                )
            )
            paths.add(
                getPath(
                    skeleton.getJointPoint(MLJoint.TYPE_RIGHT_KNEE),
                    skeleton.getJointPoint(MLJoint.TYPE_RIGHT_ANKLE)
                )
            )
            for (j in paths.indices) {
                if (paths[j] != null) {
                    canvas?.drawPath(paths[j]!!, linePaint!!)
                }
            }
            for (joint in skeleton.joints) {
                if (!(abs(joint.pointX - 0f) == 0f && abs(joint.pointY - 0f) == 0f)) {
                    canvas?.drawCircle(
                        translateX(joint.pointX),
                        translateY(joint.pointY), 12f, circlePaint!!
                    )
                }
            }
        }
    }

    private fun getPath(point1: MLJoint?, point2: MLJoint?): Path? {
        if (point1 == null || point2 == null) {
            return null
        }
        if (point1.pointX == 0f && point1.pointY == 0f) {
            return null
        }
        if (point2.pointX == 0f && point2.pointY == 0f) {
            return null
        }
        val path = Path()
        path.moveTo(
            translateX(point1.pointX),
            translateY(point1.pointY)
        )
        path.lineTo(translateX(point2.pointX), translateY(point2.pointY))
        return path
    }
}