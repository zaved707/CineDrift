package com.zavedahmad.cineDrift.ui.settingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.zavedahmad.cineDrift.ui.theme.Nav3RecipesTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
fun SettingsScreen(backStack: SnapshotStateList<NavKey>, viewModel: SettingsViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val userInput by viewModel.userInput.collectAsStateWithLifecycle()
    val apiKeyInDB by viewModel.apiKeyInDB.collectAsStateWithLifecycle()
    var isThemeDialogActive by remember { mutableStateOf(false) }
    var isApiDialogueActive by remember { mutableStateOf(false) }
    val themeNow by viewModel.themeMode.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {

            TopAppBar(
                navigationIcon = { IconButton(onClick = { backStack.removeLastOrNull()  }) { Icon(
                    Icons.AutoMirrored.Default.ArrowBack , contentDescription = "go back")} },
                title = { Text("Settings") },

                colors = TopAppBarDefaults.topAppBarColors(

                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ), scrollBehavior = scrollBehavior
            )

        }) { innerPadding ->
        if (isApiDialogueActive) {
            Dialog(onDismissRequest = { isApiDialogueActive = false }) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .width(300.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            "Api Key",
                            modifier = Modifier.padding(start = 10.dp),
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        )

                        TextField(
                            modifier = Modifier.padding(vertical = 20.dp),
                            value = userInput,
                            onValueChange = { viewModel.setUserInput(it) })
                        Button(onClick = {
                            viewModel.setApiToDB()
                            isApiDialogueActive = false
                        }) { Text("set Api") }
                    }
                }
            }
        }
        if (isThemeDialogActive) {
            Dialog(onDismissRequest = { isThemeDialogActive = false }) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .width(300.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            "Theme Mode",
                            modifier = Modifier.padding(start = 10.dp),
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable(onClick = {
                                    viewModel.setTheme("system")

                                    isThemeDialogActive = false
                                })
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                onClick = null,
                                selected = themeNow?.value == "system"
                            )
                            Text("Follow System")
                        }
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable(onClick = {
                                    viewModel.setTheme("dark")
                                    isThemeDialogActive = false
                                })
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                onClick = null,
                                selected = themeNow?.value == "dark"
                            )
                            Text("Dark")
                        }
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable(onClick = {
                                    viewModel.setTheme("light")
                                    isThemeDialogActive = false
                                })
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                onClick = null,
                                selected = themeNow?.value == "light"
                            )
                            Text("Light")
                        }
                    }
                }

            }
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)

        ) {
//            Text("Enter Api ")
//
//            apiKeyInDB?.let { preference ->
//                Text(text = preference.value)
//            } ?: Text(text = "No preference found")
            SettingsItem(
                Icons.Default.DarkMode,
                title = "Theme Mode",
                description = themeNow?.value ?: "system",
                task = { isThemeDialogActive = true })
            SettingsItem(
                Icons.Default.Key,
                "Set Api", description =
                    apiKeyInDB?.value
            ) { isApiDialogueActive = true }
        }


    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, description: String?, task: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = { task() })
            .fillMaxWidth()
            .padding(20.dp),

        ) {
        Icon(
            modifier = Modifier.padding(10.dp),
            imageVector = icon,
            contentDescription = description
        )
        Spacer(Modifier.width(20.dp))
        Column {
            Text(
                title,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            description?.let {
                Text(
                    description,
                    style = TextStyle(fontWeight = FontWeight.ExtraLight, fontSize = 15.sp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Preview(device = "id:pixel_5", showSystemUi = true, showBackground = true)
@Composable
fun Preview() {
    Nav3RecipesTheme("system") {
        Row(
            modifier = Modifier
                .clickable(onClick = {})
                .fillMaxWidth()
                .padding(vertical = 20.dp)
                .background(Color.White),

            ) {
            Icon(
                modifier = Modifier.padding(10.dp),
                imageVector = Icons.Default.DarkMode,
                contentDescription = "switch theme"
            )
            Spacer(Modifier.width(20.dp))
            Column {
                Text(
                    "Theme Mode",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
                Text(
                    "Follow System",
                    style = TextStyle(fontWeight = FontWeight.ExtraLight, fontSize = 15.sp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}