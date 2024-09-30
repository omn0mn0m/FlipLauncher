package com.omn0mn0m.fliplauncher

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.omn0mn0m.fliplauncher.ui.theme.FlipLauncherTheme

class ShortcutsActivity : ComponentActivity() {

    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val menuPackages = listOf(
            MenuPackage("Browser", "org.chromium.chrome"),
            MenuPackage("Camera", "com.tcl.camera"),
            MenuPackage("Contacts", "com.android.contacts"),
            MenuPackage("Gallery", "com.android.gallery3d"),
            MenuPackage("Messages", "com.android.mms"),
            MenuPackage("Music", "com.android.music"),
            MenuPackage("Phone", "com.android.dialer"),
            MenuPackage("Settings", "com.android.settings")
        )

        val toolPackages = listOf(
            MenuPackage("Calculator", "com.android.calculator2"),
            MenuPackage("Calendar", "com.android.calendar"),
            MenuPackage("Clock", "com.android.deskclock"),
            MenuPackage("Email", "com.android.email"),
            MenuPackage("File Manager", "com.jrdcom.filemanager"),
            MenuPackage("Note", "com.android.note"),
            MenuPackage("Recorder", "com.android.soundrecorder"),
        )

        setContent {
            FlipLauncherTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xCC11111b)
                ) { innerPadding ->
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 128.dp),
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        items(menuPackages) {
                            Shortcut(it)
                        }
                        items(toolPackages) {
                            Shortcut(it)
                        }
                    }
                }
            }
        }
        val decorMenuBarId = resources.getIdentifier("decor_menu_bar", "id", "android")
        val decorMenuBar: View? = findViewById(decorMenuBarId)
        decorMenuBar?.visibility = View.GONE

        window.decorView.apply {
            windowInsetsController?.hide(WindowInsets.Type.navigationBars())
        }
    }

    @Composable
    fun Shortcut(menuPackage: MenuPackage, modifier: Modifier = Modifier) {
        TextButton(
            onClick = {
                startActivity(
                    packageManager.getLaunchIntentForPackage(menuPackage.packageName)
                )
            },
            modifier = modifier
        ) {
            Text(menuPackage.name)
        }
    }
}

@Immutable
data class MenuPackage(
    val name: String,
    val packageName: String,
)
