package app.dtse.hmsskeletondetection

import com.huawei.hms.mlsdk.skeleton.MLJoint
import com.huawei.hms.mlsdk.skeleton.MLSkeleton
import java.util.ArrayList
import kotlin.math.abs

fun getValidSkeletons(results: List<MLSkeleton>?): List<MLSkeleton>? {
    val MIN_JOINT_SCORE = 0.3f

    if (results == null || results.isEmpty()) {
        return results
    }
    val mSkeletons: MutableList<MLSkeleton> = ArrayList()

    for (skeleton in results) {
        val mJoints: MutableList<MLJoint> = ArrayList()
        val joints: List<MLJoint> = skeleton.joints

        for (joint in joints) {
            // Check valid point.
            if (!(abs(joint.pointX - 0f).toInt() == 0 && abs(joint.pointY - 0f).toInt() == 0)
                && joint.score >= MIN_JOINT_SCORE) {
                mJoints.add(joint)
            } else {
                mJoints.add(MLJoint(0f, 0f, joint.type, 0f))
            }
        }
        val mSkeleton = MLSkeleton(mJoints)
        mSkeletons.add(mSkeleton)
    }
    return mSkeletons
}