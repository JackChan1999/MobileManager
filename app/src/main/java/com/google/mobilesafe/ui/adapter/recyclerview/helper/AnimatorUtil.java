/*
 * Thanks to Niek Haarman for the original idea
 * https://github.com/nhaarman/ListViewAnimations/blob/master/lib-core/src/main/java/com/nhaarman/listviewanimations/util/AnimatorUtil.java
 *
 * ******************************************************************************
 *   Copyright (c) 2015 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */
package com.google.mobilesafe.ui.adapter.recyclerview.helper;

import android.animation.Animator;
import android.support.annotation.NonNull;

/**
 * ============================================================
 * 
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 
 * 作 者 : 陈冠杰
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2016-4-14 下午11:11:19
 * 
 * 描 述 ：
 * 
 * 
 * 修订历史 ：
 * 
 * ============================================================
 **/
public class AnimatorUtil {

    private AnimatorUtil() {
    }

    /**
     * Merges given Animators into one array.
     */
    @NonNull
    public static Animator[] concatAnimators(@NonNull final Animator[] animators, @NonNull final Animator alphaAnimator) {
        Animator[] allAnimators = new Animator[animators.length + 1];
        int i = 0;

        for (Animator animator : animators) {
            allAnimators[i] = animator;
            ++i;
        }
        allAnimators[allAnimators.length - 1] = alphaAnimator;
        return allAnimators;
    }

}