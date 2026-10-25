package com.list_app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.list_app.R
import com.list_app.data.model.ListItem
import com.list_app.data.model.UserList
import com.list_app.ui.viewmodel.ListViewModel
import com.list_app.util.ImageUtils
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetailScreen(
    listId: String,
    viewModel: ListViewModel,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val currentListState by viewModel.getListById(listId).collectAsStateWithLifecycle()
    val itemsList by viewModel.getItemsForList(listId).collectAsStateWithLifecycle()

    var showAddItemDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ListItem?>(null) }
    var itemToDelete by remember { mutableStateOf<ListItem?>(null) }

    var editingListDetails by remember { mutableStateOf<UserList?>(null) }
    var deleteListConfirmed by remember { mutableStateOf(false) }

    var showListOptionsMenu by remember { mutableStateOf(false) }

    // Cover Photo Picker Launcher
    val coverPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val localPath = ImageUtils.copyUriToInternalStorage(context, uri)
            viewModel.updateListCoverUri(listId, localPath)
        }
    }

    val currentList = currentListState
    val itemCount = itemsList.size

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentList?.title ?: "List Details",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (currentList != null) {
                        // Item count badge on the right
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = "$itemCount ${if (itemCount == 1) "item" else "items"}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            modifier = Modifier.padding(end = 4.dp)
                        )

                        // Overflow menu button
                        IconButton(onClick = { showListOptionsMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "List Options"
                            )
                        }

                        DropdownMenu(
                            expanded = showListOptionsMenu,
                            onDismissRequest = { showListOptionsMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Change Cover Photo") },
                                leadingIcon = {
                                    Icon(Icons.Default.PhotoCamera, contentDescription = null)
                                },
                                onClick = {
                                    showListOptionsMenu = false
                                    coverPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Edit List Details") },
                                leadingIcon = {
                                    Icon(Icons.Default.Edit, contentDescription = null)
                                },
                                onClick = {
                                    showListOptionsMenu = false
                                    editingListDetails = currentList
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete List") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                },
                                onClick = {
                                    showListOptionsMenu = false
                                    deleteListConfirmed = true
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddItemDialog = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Item", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        if (currentList == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(top = 8.dp, bottom = 88.dp)
            ) {
                // Empty items state
                if (itemsList.isEmpty()) {
                    item {
                        EmptyItemsView(
                            onAddItemClick = { showAddItemDialog = true }
                        )
                    }
                } else {
                    items(
                        items = itemsList,
                        key = { listItem -> listItem.id }
                    ) { item ->
                        ListItemCard(
                            item = item,
                            onItemClick = {
                                if (item.webUrl.isNotBlank()) {
                                    ImageUtils.launchWebUrl(context, item.webUrl)
                                }
                            },
                            onEditClick = { editingItem = item },
                            onDeleteClick = { itemToDelete = item }
                        )
                    }
                }
            }
        }
    }

    // Add Item Dialog
    if (showAddItemDialog) {
        CreateOrEditItemDialog(
            item = null,
            onDismiss = { showAddItemDialog = false },
            onConfirm = { name, description, price, imageUrl, webUrl ->
                viewModel.addItemToList(
                    listId = listId,
                    name = name,
                    description = description,
                    price = price,
                    imageUrl = imageUrl,
                    webUrl = webUrl
                )
                showAddItemDialog = false
            }
        )
    }

    // Edit Item Dialog
    editingItem?.let { targetItem ->
        CreateOrEditItemDialog(
            item = targetItem,
            onDismiss = { editingItem = null },
            onConfirm = { name, description, price, imageUrl, webUrl ->
                viewModel.updateItem(
                    targetItem.copy(
                        name = name,
                        description = description,
                        price = price,
                        imageUrl = imageUrl,
                        webUrl = webUrl
                    )
                )
                editingItem = null
            }
        )
    }

    // Delete Item Confirmation
    itemToDelete?.let { targetItem ->
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Delete Item") },
            text = { Text("Are you sure you want to delete '${targetItem.name}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteItem(targetItem.id)
                        itemToDelete = null
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit List Details Dialog
    editingListDetails?.let { targetList ->
        CreateOrEditListDialog(
            userList = targetList,
            onDismiss = { editingListDetails = null },
            onConfirm = { title, description, coverUri ->
                viewModel.updateList(
                    targetList.copy(
                        title = title,
                        description = description,
                        coverImageUri = coverUri
                    )
                )
                editingListDetails = null
            }
        )
    }

    // Delete List Confirmation
    if (deleteListConfirmed && (currentList != null)) {
        AlertDialog(
            onDismissRequest = { deleteListConfirmed = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Delete List") },
            text = { Text("Are you sure you want to delete '${currentList.title}' and all its items?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteList(currentList.id)
                        deleteListConfirmed = false
                        onBack()
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteListConfirmed = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ListItemCard(
    item: ListItem,
    onItemClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val context = LocalContext.current
    var showItemMenu by remember { mutableStateOf(false) }

    val formattedPrice = remember(item.price) {
        if (item.price > 0.0) {
            val format = NumberFormat.getCurrencyInstance(Locale.US)
            format.format(item.price)
        } else {
            ""
        }
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onItemClick),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Item Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (!item.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imageUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.placeholder),
                        error = painterResource(R.drawable.placeholder),
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Name, Price, Description, Web URL
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (item.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (formattedPrice.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = formattedPrice,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Options Menu
            Box {
                IconButton(onClick = { showItemMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Item Options"
                    )
                }

                DropdownMenu(
                    expanded = showItemMenu,
                    onDismissRequest = { showItemMenu = false }
                ) {
                    if (item.webUrl.isNotBlank()) {
                        DropdownMenuItem(
                            text = { Text("Open Web Link") },
                            leadingIcon = {
                                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
                            },
                            onClick = {
                                showItemMenu = false
                                ImageUtils.launchWebUrl(context, item.webUrl)
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("Edit Item") },
                        leadingIcon = {
                            Icon(Icons.Default.Edit, contentDescription = null)
                        },
                        onClick = {
                            showItemMenu = false
                            onEditClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Item") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            showItemMenu = false
                            onDeleteClick()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyItemsView(
    onAddItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingBag,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No items in this list yet",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tap the button below to add your first item!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAddItemClick) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add Item")
        }
    }
}

@Composable
fun CreateOrEditItemDialog(
    item: ListItem?,
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String, price: Double, imageUrl: String?, webUrl: String) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(item?.name ?: "") }
    var description by remember { mutableStateOf(item?.description ?: "") }
    var priceText by remember { mutableStateOf(if (item != null && item.price > 0.0) item.price.toString() else "") }
    var webUrl by remember { mutableStateOf(item?.webUrl ?: "") }
    var imageUrl by remember { mutableStateOf(item?.imageUrl) }
    var imageUrlInput by remember { mutableStateOf("") }
    var showUrlInput by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUrl = ImageUtils.copyUriToInternalStorage(context, uri)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (item == null) "Add New Item" else "Edit Item") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Price (e.g. 19.99)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    minLines = 2,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = webUrl,
                    onValueChange = { webUrl = it },
                    label = { Text("Website / Purchase Link") },
                    placeholder = { Text("https://example.com") },
                    singleLine = true,
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Language, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Photo Selection
                Text(
                    text = "Item Photo",
                    style = MaterialTheme.typography.labelLarge
                )

                if (!imageUrl.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Item Photo Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        IconButton(
                            onClick = { imageUrl = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                .size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove Image",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Gallery")
                    }

                    OutlinedButton(
                        onClick = { showUrlInput = !showUrlInput },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (showUrlInput) "Hide URL" else "Image URL")
                    }
                }

                if (showUrlInput) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = imageUrlInput,
                            onValueChange = { imageUrlInput = it },
                            label = { Text("Image URL") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = {
                                if (imageUrlInput.isNotBlank()) {
                                    imageUrl = imageUrlInput.trim()
                                    imageUrlInput = ""
                                    showUrlInput = false
                                }
                            }
                        ) {
                            Text("Set")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val parsedPrice = priceText.toDoubleOrNull() ?: 0.0
                        onConfirm(
                            name.trim(),
                            description.trim(),
                            parsedPrice,
                            imageUrl,
                            webUrl.trim()
                        )
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text(if (item == null) "Add Item" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
