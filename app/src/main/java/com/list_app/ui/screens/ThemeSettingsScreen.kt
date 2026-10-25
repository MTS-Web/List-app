package com.list_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.list_app.data.model.AccentTheme
import com.list_app.ui.theme.AmberLightPrimary
import com.list_app.ui.theme.CoralLightPrimary
import com.list_app.ui.theme.CyanLightPrimary
import com.list_app.ui.theme.EmeraldLightPrimary
import com.list_app.ui.theme.IndigoLightPrimary
import com.list_app.ui.theme.LimeLightPrimary
import com.list_app.ui.theme.OceanLightPrimary
import com.list_app.ui.theme.PurpleLightPrimary
import com.list_app.ui.theme.RoseLightPrimary
import com.list_app.ui.theme.SunsetLightPrimary
import com.list_app.ui.theme.TealLightPrimary
import com.list_app.ui.theme.VioletLightPrimary
import com.list_app.ui.viewmodel.ListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsScreen(
    viewModel: ListViewModel,
    onBack: () -> Unit,
) {
    val themeConfig by viewModel.themeConfig.collectAsStateWithLifecycle()
    var showCustomColorPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dark Mode Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = if (themeConfig.isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column {
                            Text(
                                text = "Dark Theme",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = if (themeConfig.isDarkMode) "Enabled" else "Disabled",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = themeConfig.isDarkMode,
                        onCheckedChange = { viewModel.setDarkMode(it) }
                    )
                }
            }

            // Accent Theme Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Accent Color",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AccentTheme.entries.forEach { accent ->
                            val isSelected = themeConfig.customAccentColorHex == null && themeConfig.accentTheme == accent
                            val accentColor = getAccentPreviewColor(accent)

                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    viewModel.setCustomAccentColorHex(null)
                                    viewModel.setAccentTheme(accent)
                                },
                                label = { Text(accent.displayName) },
                                leadingIcon = {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(accentColor)
                                    )
                                },
                                trailingIcon = {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Custom Color Picker Button
                    OutlinedButton(
                        onClick = { showCustomColorPicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (themeConfig.customAccentColorHex != null) "Custom Color Active (Tap to Change)" else "Pick Custom Accent Color...")
                    }

                    if (themeConfig.customAccentColorHex != null) {
                        TextButton(
                            onClick = { viewModel.setCustomAccentColorHex(null) },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Reset to Preset Theme", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            // Theme Preview Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Theme Preview",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(onClick = {}) {
                            Text("Primary Action")
                        }
                        Badge {
                            Text("Badge", modifier = Modifier.padding(horizontal = 4.dp))
                        }
                    }
                }
            }
        }
    }

    if (showCustomColorPicker) {
        CustomColorPickerDialog(
            initialHex = themeConfig.customAccentColorHex ?: "#006A60",
            onDismiss = { showCustomColorPicker = false },
            onApply = { hex ->
                viewModel.setCustomAccentColorHex(hex)
                showCustomColorPicker = false
            }
        )
    }
}

@Composable
fun CustomColorPickerDialog(
    initialHex: String,
    onDismiss: () -> Unit,
    onApply: (String) -> Unit
) {
    val initialColor = runCatching { Color(initialHex.toColorInt()) }.getOrDefault(TealLightPrimary)
    var red by remember { mutableFloatStateOf(initialColor.red) }
    var green by remember { mutableFloatStateOf(initialColor.green) }
    var blue by remember { mutableFloatStateOf(initialColor.blue) }

    val currentColor = Color(red, green, blue)
    val hexString = String.format("#%02X%02X%02X", (red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt())

    val presetSwatches = listOf(
        "#E91E63", "#9C27B0", "#673AB7", "#3F51B5", "#2196F3",
        "#00BCD4", "#009688", "#4CAF50", "#8BC34A", "#FFEB3B",
        "#FF9800", "#FF5722", "#795548", "#607D8B", "#E91E63"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Custom Color Picker") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Color Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(currentColor)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = hexString,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (currentColor.luminance() > 0.5f) Color.Black else Color.White
                    )
                }

                // Preset Swatches Grid
                Text("Preset Swatches", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    presetSwatches.take(8).forEach { swatchHex ->
                        val swatchColor = Color(swatchHex.toColorInt())
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(swatchColor)
                                .clickable {
                                    red = swatchColor.red
                                    green = swatchColor.green
                                    blue = swatchColor.blue
                                }
                                .border(2.dp, if (currentColor == swatchColor) MaterialTheme.colorScheme.primary else Color.Transparent, CircleShape)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    presetSwatches.drop(8).take(8).forEach { swatchHex ->
                        val swatchColor = Color(swatchHex.toColorInt())
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(swatchColor)
                                .clickable {
                                    red = swatchColor.red
                                    green = swatchColor.green
                                    blue = swatchColor.blue
                                }
                                .border(2.dp, if (currentColor == swatchColor) MaterialTheme.colorScheme.primary else Color.Transparent, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Sliders
                Text("Red: ${(red * 255).toInt()}", style = MaterialTheme.typography.labelSmall)
                Slider(
                    value = red,
                    onValueChange = { red = it },
                    valueRange = 0f..1f
                )

                Text("Green: ${(green * 255).toInt()}", style = MaterialTheme.typography.labelSmall)
                Slider(
                    value = green,
                    onValueChange = { green = it },
                    valueRange = 0f..1f
                )

                Text("Blue: ${(blue * 255).toInt()}", style = MaterialTheme.typography.labelSmall)
                Slider(
                    value = blue,
                    onValueChange = { blue = it },
                    valueRange = 0f..1f
                )

                OutlinedTextField(
                    value = hexString,
                    onValueChange = { input ->
                        runCatching {
                            if (input.startsWith("#") && (input.length == 7 || input.length == 4)) {
                                val parsed = Color(input.toColorInt())
                                red = parsed.red
                                green = parsed.green
                                blue = parsed.blue
                            }
                        }
                    },
                    label = { Text("Hex Code") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onApply(hexString) }) {
                Text("Apply Custom Color")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun getAccentPreviewColor(accent: AccentTheme): Color = when (accent) {
    AccentTheme.TEAL -> TealLightPrimary
    AccentTheme.PURPLE -> PurpleLightPrimary
    AccentTheme.CORAL -> CoralLightPrimary
    AccentTheme.OCEAN -> OceanLightPrimary
    AccentTheme.SUNSET -> SunsetLightPrimary
    AccentTheme.EMERALD -> EmeraldLightPrimary
    AccentTheme.AMBER -> AmberLightPrimary
    AccentTheme.ROSE -> RoseLightPrimary
    AccentTheme.INDIGO -> IndigoLightPrimary
    AccentTheme.CYAN -> CyanLightPrimary
    AccentTheme.LIME -> LimeLightPrimary
    AccentTheme.VIOLET -> VioletLightPrimary
}
