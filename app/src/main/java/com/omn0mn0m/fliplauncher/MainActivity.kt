package com.omn0mn0m.fliplauncher

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.omn0mn0m.fliplauncher.ui.theme.FlipLauncherTheme


class MainActivity : ComponentActivity() {

    private val tag = "MainActivity"
    private var showMenuBar: Boolean = false

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlipLauncherTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent
                ) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }

        val decorMenuBarId = resources.getIdentifier("decor_menu_bar", "id", "android")
        val decorMenuBar: View? = findViewById(decorMenuBarId)

        val leftMenuId = resources.getIdentifier("menu_lsk", "id", "android")
        val centerMenuId = resources.getIdentifier("menu_csk", "id", "android")
        val rightMenuId = resources.getIdentifier("menu_rsk", "id", "android")

        val leftMenu: TextView? = findViewById(leftMenuId)
        val centerMenu: TextView? = findViewById(centerMenuId)
        val rightMenu: TextView? = findViewById(rightMenuId)

        if (decorMenuBar != null) {
            leftMenu?.text = getString(R.string.menu_bar_notifications)
            centerMenu?.text = getString(R.string.menu_bar_apps)
            rightMenu?.text = getString(R.string.menu_bar_shortcuts)
        }

        if (showMenuBar) {
            decorMenuBar?.visibility = View.VISIBLE
            window.decorView.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        } else {
            decorMenuBar?.visibility = View.GONE
            window.decorView.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            }
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER -> {
                Log.d(tag, "DPAD_CENTER")
                try {
                    startActivity(
                        Intent()
                            .setClassName(
                                "com.android.launcher3",
                                "com.android.launcher3.allapps.AllAppsForTestActivity"
                            )
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)  // was 268468224
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                } catch (e: Exception) {
                    Log.e(tag, "com.android.launcher3 not found")
                    // use the slower in-app app list
                    startActivity(
                        Intent(this, AppsActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)  // was 268468224
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    )
                }
                true
            }

            KeyEvent.KEYCODE_DPAD_UP -> {
                Log.d(tag, "DPAD_UP")
                showMenuBar = !showMenuBar
                val decorMenuBarId = resources.getIdentifier("decor_menu_bar", "id", "android")
                val decorMenuBar: View? = findViewById(decorMenuBarId)

                if (showMenuBar) {
                    decorMenuBar?.visibility = View.VISIBLE
                    window.decorView.apply {
                        systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    }
                } else {
                    decorMenuBar?.visibility = View.GONE
                    window.decorView.apply {
                        systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    }
                }

                true
            }

            KeyEvent.KEYCODE_DPAD_DOWN -> {
                Log.d(tag, "DPAD_DOWN")
                try {
                    startActivity(
                        Intent()
                            .setClassName(
                                "com.android.systemui",
                                "com.tcl.recents.RecentsListActivity"
                            )
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)  // was 268468224
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    )
                } catch (e: Exception) {
                    Log.e(tag, "com.tcl.recents.RecentsListActivity not found")
                }
                true
            }

            KeyEvent.KEYCODE_DPAD_LEFT -> {
                Log.d(tag, "DPAD_LEFT")
                val split =
                    Settings.Global.getString(contentResolver, "keyshortcut_leftkey").split("-")
                startActivity(
                    Intent("android.intent.action.MAIN")
                        .addCategory("android.intent.category.LAUNCHER")
                        .setComponent(ComponentName(split[1], split[2]))
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)  // was 270532608
                )
                true
            }

            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                Log.d(tag, "DPAD_RIGHT")
                val split =
                    Settings.Global.getString(contentResolver, "keyshortcut_rightkey").split("-")
                startActivity(
                    Intent("android.intent.action.MAIN")
                        .addCategory("android.intent.category.LAUNCHER")
                        .setComponent(ComponentName(split[1], split[2]))
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)  // was 270532608
                )
                true
            }

            KeyEvent.KEYCODE_SOFT_LEFT -> {
                Log.d(tag, "DPAD_SOFT_LEFT")
                try {
                    startActivity(
                        Intent("com.tcl.notification")
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)  // was 268468224
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    )
                } catch (e: Exception) {
                    Log.e(tag, "com.tcl.notification not found")
                }
                true
            }

            KeyEvent.KEYCODE_SOFT_RIGHT -> {
                Log.d(tag, "DPAD_SOFT_RIGHT")
                startActivity(
                    Intent(this, ShortcutsActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                )
                true
            }

            else -> super.onKeyUp(keyCode, event)
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlipLauncherTheme {
        Greeting()
    }
}