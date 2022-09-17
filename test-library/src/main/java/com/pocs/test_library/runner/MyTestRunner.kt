package com.pocs.test_library.runner

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.LayoutInflaterCompat
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class MyTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }

    // ProgressBar로 인해 API 23에서 테스트가 무한 로딩하는 현상이 발생한다. 이를 막기위해 아래의 코드들을 도입한다.
    // https://proandroiddev.com/progressbar-animations-with-espresso-57f826102187
    override fun callActivityOnCreate(activity: Activity?, bundle: Bundle?) {
        if (activity is AppCompatActivity) {
            LayoutInflaterCompat.setFactory2(
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                NoAnimationInflaterFactory(activity.delegate)
            )
        }

        super.callActivityOnCreate(activity, bundle)
    }

    private class NoAnimationInflaterFactory(private val appCompatDelegate: AppCompatDelegate) :
        LayoutInflater.Factory2 {

        override fun onCreateView(
            parent: View?,
            name: String,
            context: Context,
            attrs: AttributeSet
        ): View? =
            when (name) {
                ProgressBar::class.java.simpleName -> {
                    StubCustomProgressButton(context, attrs)
                }
                else -> appCompatDelegate.createView(null, name, context, attrs)
            }

        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? =
            onCreateView(null, name, context, attrs)

        private class StubCustomProgressButton @JvmOverloads constructor(
            context: Context,
            attrs: AttributeSet,
            defStyleAttr: Int = 0
        ) : ProgressBar(context, attrs, defStyleAttr) {

            override fun startAnimation(animation: Animation) {
                // no-op
            }
        }
    }
}
