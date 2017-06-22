/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView.ScaleType;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;

public class RotateLoadingLayout extends LoadingLayout {
    int[] resIds = new int[]{
            R.drawable.dropdown_anim_00,
            R.drawable.dropdown_anim_01,
            R.drawable.dropdown_anim_02,
            R.drawable.dropdown_anim_03,
            R.drawable.dropdown_anim_04,
            R.drawable.dropdown_anim_05,
            R.drawable.dropdown_anim_06,
            R.drawable.dropdown_anim_07,
            R.drawable.dropdown_anim_08,
            R.drawable.dropdown_anim_09,
            R.drawable.dropdown_anim_10,
    };

    /**
     * 如果声明了自有属性
     *
     * @param context
     * @param mode
     * @param scrollDirection
     * @param attrs
     */
    public RotateLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);

    }

    /**
     * 对自有属性中旋转动画的中心点赋值
     *
     * @param imageDrawable
     */
    public void onLoadingDrawableSet(Drawable imageDrawable) {
        //NO-OP()
    }

    /**
     * 回调方法
     * 只要处于下来动作，该方法不断调用
     *
     * @param scaleOfLayout
     */
    protected void onPullImpl(float scaleOfLayout) {
//		float angle;//角度
//		if (mRotateDrawableWhilePulling) {
//			angle = scaleOfLayout * 90f;
//		} else {
//			angle = Math.max(0f, Math.min(180f, scaleOfLayout * 360f - 180f));
//		}
//
//		mHeaderImageMatrix.setRotate(angle, mRotationPivotX, mRotationPivotY);
//		mHeaderImage.setImageMatrix(mHeaderImageMatrix);
        Log.d("TAG", "onPullImpl: " + scaleOfLayout);
        int idx = (int) (scaleOfLayout * 10) + 1;
        if (idx <= 10) {
            //根据原始图片的大小创建一个缩小的图片
            int id = resIds[idx];
            Bitmap src = BitmapFactory.decodeResource(getResources(), id);
            int width = src.getWidth() * idx / 10;
            int height = src.getHeight() * idx / 10;
            Bitmap dest = Bitmap.createScaledBitmap(src, width, height, true);
            mHeaderImage.setImageBitmap(dest);
        } else {
            int resId = resIds[10];
            mHeaderImage.setImageResource(resId);
        }
//        //另一种思路:利用原始图片创建一个ScaleDrawable
//        int id = resIds[idx];
//        Drawable drawable = getResources().getDrawable(id);
//        drawable.setLevel(100);
//        ScaleDrawable sd = new ScaleDrawable(drawable, Gravity.CENTER, 0.5f, 0.5f);
//        mHeaderImage.setImageDrawable(sd);


    }

    /**
     * 回调方法
     * 当下拉到一定位置松手后，进入到刷新状态时被回调
     * 原生：让ImageView播放一个无限旋转的旋转补间动画
     * 咱们：让ImageView播放一个吃包子的帧动画
     */
    @Override
    protected void refreshingImpl() {
//		mHeaderImage.startAnimation(mRotateAnimation);
        mHeaderImage.setImageResource(R.drawable.refreshing_anim);
        Drawable drawable = mHeaderImage.getDrawable();
        ((AnimationDrawable) drawable).start();

    }

    /**
     * 回调方法
     * 当刷新完毕后，头部缩起时调用
     * 咱们：没有必要让帧动画停止
     */
    @Override
    protected void resetImpl() {
//		mHeaderImage.clearAnimation();
//		resetImageRotation();
    }

    private void resetImageRotation() {
//		if (null != mHeaderImageMatrix) {
//			mHeaderImageMatrix.reset();
//			mHeaderImage.setImageMatrix(mHeaderImageMatrix);
//		}
    }

    @Override
    protected void pullToRefreshImpl() {
        // NO-OP
    }

    @Override
    protected void releaseToRefreshImpl() {
        // NO-OP
    }

    /**
     * 返回默认的图片
     *
     * @return
     */
    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.dropdown_anim_00;
    }

}
