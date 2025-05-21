package com.lzh.tripplan

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
        // 设置内容在状态栏下方
        window.decorView.findViewById<ViewGroup>(android.R.id.content)
            .setOnApplyWindowInsetsListener { view, windowInsets ->
                val statusBarHeight = windowInsets.systemWindowInsetTop
                view.setPadding(0, statusBarHeight, 0, 0)
                windowInsets.consumeSystemWindowInsets()
            }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}