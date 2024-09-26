package com.omn0mn0m.fliplauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.omn0mn0m.fliplauncher.ui.theme.FlipLauncherTheme

class ShortcutsActivity : ComponentActivity() {

    private val menuPackages = listOf(
        MenuPackage("Phone", "com.android.dialer"),
        MenuPackage("Messages", "com.android.mms"),
        MenuPackage("Contacts", "com.android.contacts"),
        MenuPackage("Gallery", "com.android.gallery3d"),
        MenuPackage("Music", "com.android.music"),
        MenuPackage("Camera", "com.tcl.camera"),
        MenuPackage("Browser", "org.chromium.chrome"),
        MenuPackage("Settings", "com.android.settings")
    )

    private val toolPackages = listOf(
        MenuPackage("Recorder", "com.android.soundrecorder"),
        MenuPackage("Calendar", "com.android.calendar"),
        MenuPackage("Clock", "com.android.deskclock"),
        MenuPackage("Note", "com.android.note"),
        MenuPackage("Calculator", "com.android.calculator2"),
        MenuPackage("Email", "com.android.email"),
        MenuPackage("File Manager", "com.jrdcom.filemanager"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageManager = packageManager
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
                            TextButton(onClick = {
                                startActivity(
                                    packageManager.getLaunchIntentForPackage(it.packageName)
                                )
                            }) {
                                Text(it.name)
                            }
                        }
                        items(toolPackages) {
                            TextButton(onClick = {
                                startActivity(
                                    packageManager.getLaunchIntentForPackage(it.packageName)
                                )
                            }) {
                                Text(it.name)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class MenuPackage(
    val name: String,
    val packageName: String,
)