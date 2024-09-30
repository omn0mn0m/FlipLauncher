package com.omn0mn0m.fliplauncher

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omn0mn0m.fliplauncher.ui.theme.FlipLauncherTheme

class AppsActivity : ComponentActivity() {
    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlipLauncherTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xCC11111b)
                ) { innerPadding ->
                    val apps = getApps()

                    AppList(
                        apps = apps,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        val decorMenuBarId = resources.getIdentifier("decor_menu_bar", "id", "android")
        val decorMenuBar: View? = findViewById(decorMenuBarId)

        if (decorMenuBar != null) {
//            decorMenuBar.visibility = View.GONE
            val leftMenuId = resources.getIdentifier("menu_lsk", "id", "android")
            val centerMenuId = resources.getIdentifier("menu_csk", "id", "android")
            val rightMenuId = resources.getIdentifier("menu_rsk", "id", "android")

            val leftMenu: TextView? = findViewById(leftMenuId)
            val centerMenu: TextView? = findViewById(centerMenuId)
            val rightMenu: TextView? = findViewById(rightMenuId)

            leftMenu?.text = ""
            centerMenu?.text = getString(R.string.menu_bar_select)
            rightMenu?.text = getString(R.string.menu_bar_options)
        }
    }

    private fun getApps(): List<AppInfo> {
        val packageManager = packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfo: List<ResolveInfo> =
            packageManager.queryIntentActivities(mainIntent, 0)

        val apps = ArrayList<AppInfo>()

        for (info in resolveInfo) {
            val applicationInfo: ApplicationInfo = info.activityInfo.applicationInfo
            val label = applicationInfo.loadLabel(packageManager).toString()
            val icon = applicationInfo.loadIcon(packageManager)
            val launchIntent: Intent? =
                packageManager.getLaunchIntentForPackage(applicationInfo.packageName)
            apps.add(AppInfo(label, icon, launchIntent))
        }

        return apps.sortedBy{ it.name }
    }
}

data class AppInfo(
    val name: String,
    val icon: Drawable,
    val launchIntent: Intent?,
)

@Composable
fun AppIcon(appInfo: AppInfo) {
    val activity: ComponentActivity = LocalContext.current as ComponentActivity

    TextButton(
        onClick = { activity.startActivity(appInfo.launchIntent) },
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth(),
        content = {
//            Image(
//                appInfo.icon.toBitmap(config = Bitmap.Config.ARGB_8888).asImageBitmap(),
//                contentDescription = stringResource(R.string.test_app_name),
//                contentScale = ContentScale.Fit,
//                modifier = Modifier
//                    .size(32.dp)
//            )
            Text(
                appInfo.name,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    )
}

@Composable
fun AppList(apps: List<AppInfo>,  modifier: Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        for (app in apps) {
            AppIcon(app)
        }
    }
}