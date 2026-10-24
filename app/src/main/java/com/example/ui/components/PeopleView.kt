package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.data.Organization
import com.example.data.Person
import com.example.data.UserProfile
import com.example.ui.theme.SangamTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleListPanel(
    organization: Organization,
    selectedIndex: Int,
    isMyProfileSelected: Boolean,
    user: UserProfile,
    query: String,
    groupFilter: String,
    onQueryChange: (String) -> Unit,
    onGroupFilterChange: (String) -> Unit,
    onSelectPerson: (index: Int, isMe: Boolean) -> Unit,
    onAddContactClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search people and roles") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("people_search_input"),
                shape = RoundedCornerShape(11.dp),
                singleLine = true
            )
            if (onAddContactClick != null) {
                IconButton(
                    onClick = onAddContactClick,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .testTag("add_contact_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Add Contact",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Dropdown Filter
        var expandedGroupDropdown by remember { mutableStateOf(false) }
        Box {
            OutlinedButton(
                onClick = { expandedGroupDropdown = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(11.dp)
            ) {
                Text(
                    text = groupFilter.ifEmpty { "Everyone" },
                    modifier = Modifier.weight(1f),
                    style = SangamTypography.bodyMedium
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }

            DropdownMenu(
                expanded = expandedGroupDropdown,
                onDismissRequest = { expandedGroupDropdown = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Everyone") },
                    onClick = { onGroupFilterChange(""); expandedGroupDropdown = false }
                )
                organization.groups.forEach { grp ->
                    DropdownMenuItem(
                        text = { Text(grp.name) },
                        onClick = { onGroupFilterChange(grp.name); expandedGroupDropdown = false }
                    )
                }
            }
        }

        Divider(color = MaterialTheme.colorScheme.outline)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                Text("YOU", style = SangamTypography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectPerson(0, true) }
                        .testTag("my_profile_row"),
                    color = if (isMyProfileSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f) else Color.Transparent,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF3B5B8C)),
                            contentAlignment = Alignment.Center
                        ) {
                            val initials = user.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                            Text(initials.ifEmpty { "TM" }, color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Column {
                            Text(user.name, fontWeight = FontWeight.Bold, style = SangamTypography.bodyLarge)
                            Text("${user.role} · ${user.status}", style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                val count = organization.directory.size
                Text("DIRECTORY · $count OF $count", style = SangamTypography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            val filtered = organization.directory.mapIndexed { idx, p -> idx to p }.filter { (_, p) ->
                val matchesFilter = groupFilter.isEmpty() || p.branch == groupFilter || p.team == groupFilter
                val matchesQuery = query.isEmpty() || p.name.contains(query, ignoreCase = true) || p.role.contains(query, ignoreCase = true)
                matchesFilter && matchesQuery
            }

            itemsIndexed(filtered) { _, (origIdx, person) ->
                val isSelected = !isMyProfileSelected && origIdx == selectedIndex

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectPerson(origIdx, false) }
                        .testTag("person_item_$origIdx"),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f) else Color.Transparent,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF162338)),
                                contentAlignment = Alignment.Center
                            ) {
                                val initials = person.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                                Text(initials, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (person.status) {
                                            "Available" -> Color(0xFF17824F)
                                            "In a meeting" -> Color(0xFFD69A1B)
                                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                                    .align(Alignment.BottomEnd)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(person.name, fontWeight = FontWeight.Bold, style = SangamTypography.bodyLarge)
                            Text("${person.role} · ${person.branch}", style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PersonProfileDetailPane(
    person: Person?,
    organization: Organization,
    onSendMessage: (personName: String) -> Unit,
    onCopyPhone: (phone: String) -> Unit,
    onCopyEmail: (email: String) -> Unit,
    onStartCall: ((name: String, isVideo: Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (person == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Select a person to view profile", style = SangamTypography.bodyLarge)
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Cover Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(organization.accentColor, organization.accentColor.copy(alpha = 0.4f))
                    )
                )
        )

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Header Overlapping Cover
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .offset(y = (-36).dp)
                        .size(84.dp)
                        .clip(CircleShape)
                        .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape)
                        .background(Color(0xFF162338)),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = person.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                    Text(initials, color = Color.White, style = SangamTypography.displayMedium, fontWeight = FontWeight.Bold)
                }

                Column(modifier = Modifier.padding(bottom = 10.dp)) {
                    Text(person.name, style = SangamTypography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(person.role, style = SangamTypography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text(person.branch) })
                AssistChip(onClick = {}, label = { Text(person.team) })
                AssistChip(onClick = {}, label = { Text("● ${person.status}") })
            }

            // Action Buttons
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onSendMessage(person.name) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("profile_send_message_button")
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Send message")
                    }

                    FilledTonalButton(
                        onClick = { onStartCall?.invoke(person.name, false) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("profile_voice_call_button")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call")
                    }

                    FilledTonalButton(
                        onClick = { onStartCall?.invoke(person.name, true) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("profile_video_call_button")
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Video")
                    }
                }

                OutlinedButton(
                    onClick = { onCopyPhone(person.phone) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy phone (${person.phone})")
                }
            }

            // About Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("About", style = SangamTypography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(person.bio, style = SangamTypography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("${person.readPercentage}%", style = SangamTypography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("Messages read", style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("${person.joinedYear}", style = SangamTypography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("Joined", style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("2", style = SangamTypography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("Groups", style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Contact Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Contact", style = SangamTypography.titleLarge, fontWeight = FontWeight.Bold)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Phone", style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(person.phone, style = SangamTypography.bodyLarge, fontWeight = FontWeight.SemiBold)
                        }
                        OutlinedButton(onClick = { onCopyPhone(person.phone) }) { Text("Copy") }
                    }

                    Divider(color = MaterialTheme.colorScheme.outline)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Email", style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(person.email, style = SangamTypography.bodyLarge, fontWeight = FontWeight.SemiBold)
                        }
                        OutlinedButton(onClick = { onCopyEmail(person.email) }) { Text("Copy") }
                    }
                }
            }
        }
    }
}

@Composable
fun MyProfileEditorPane(
    user: UserProfile,
    organization: Organization,
    onSaveProfile: (name: String, role: String, phone: String, status: String, bio: String, photoPresetIndex: Int) -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var nameState by remember { mutableStateOf(user.name) }
    var roleState by remember { mutableStateOf(user.role) }
    var phoneState by remember { mutableStateOf(user.phone) }
    var statusState by remember { mutableStateOf(user.status) }
    var bioState by remember { mutableStateOf(user.bio) }
    var selectedPreset by remember { mutableStateOf(user.photoPresetIndex) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedPreset = 0
            onShowToast("Selected profile picture from device")
        }
    }

    val presetGradients = remember {
        listOf(
            Brush.linearGradient(listOf(Color(0xFF0B8A7C), Color(0xFF1F6FD0))),
            Brush.linearGradient(listOf(Color(0xFFC94F0E), Color(0xFFC2285F))),
            Brush.linearGradient(listOf(Color(0xFF6A4FD1), Color(0xFFC2285F))),
            Brush.linearGradient(listOf(Color(0xFF1F6FD0), Color(0xFF6A4FD1))),
            Brush.linearGradient(listOf(Color(0xFF17824F), Color(0xFF0B8A7C))),
            Brush.linearGradient(listOf(Color(0xFFD69A1B), Color(0xFFC94F0E))),
            Brush.linearGradient(listOf(Color(0xFF0F1B2D), Color(0xFF3B5B8C))),
            Brush.linearGradient(listOf(Color(0xFFC2285F), Color(0xFF6A4FD1)))
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(Brush.horizontalGradient(listOf(organization.accentColor, organization.accentColor.copy(alpha = 0.4f))))
        )

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .offset(y = (-36).dp)
                        .size(84.dp)
                        .clip(CircleShape)
                        .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape)
                        .background(if (selectedPreset >= 0) presetGradients[selectedPreset] else Brush.linearGradient(listOf(Color(0xFF3B5B8C), Color(0xFF1F6FD0)))),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = nameState.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                    Text(initials.ifEmpty { "TM" }, color = Color.White, style = SangamTypography.displayMedium, fontWeight = FontWeight.Bold)
                }

                Column(modifier = Modifier.padding(bottom = 10.dp)) {
                    Text(nameState, style = SangamTypography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(roleState, style = SangamTypography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Photo Preset Style Picker Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Profile photo", style = SangamTypography.titleLarge, fontWeight = FontWeight.Bold)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("upload_photo_button")
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Upload photo")
                        }
                        OutlinedButton(
                            onClick = { selectedPreset = -1; onShowToast("Photo removed") },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Remove photo")
                        }
                    }

                    Text("Or choose a gradient avatar style:", style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        presetGradients.forEachIndexed { idx, brush ->
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(brush)
                                    .border(
                                        if (selectedPreset == idx) 3.dp else 0.dp,
                                        if (selectedPreset == idx) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable { selectedPreset = idx },
                                contentAlignment = Alignment.Center
                            ) {
                                val initials = nameState.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                                Text(initials.ifEmpty { "TM" }, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Edit Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Edit details", style = SangamTypography.titleLarge, fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = nameState,
                        onValueChange = { nameState = it },
                        label = { Text("NAME") },
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_name"),
                        shape = RoundedCornerShape(11.dp)
                    )

                    OutlinedTextField(
                        value = roleState,
                        onValueChange = { roleState = it },
                        label = { Text("TITLE") },
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_role"),
                        shape = RoundedCornerShape(11.dp)
                    )

                    OutlinedTextField(
                        value = phoneState,
                        onValueChange = { phoneState = it },
                        label = { Text("PHONE") },
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_phone"),
                        shape = RoundedCornerShape(11.dp)
                    )

                    OutlinedTextField(
                        value = statusState,
                        onValueChange = { statusState = it },
                        label = { Text("STATUS") },
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_status"),
                        shape = RoundedCornerShape(11.dp)
                    )

                    OutlinedTextField(
                        value = bioState,
                        onValueChange = { bioState = it },
                        label = { Text("ABOUT") },
                        modifier = Modifier.fillMaxWidth().testTag("edit_profile_bio"),
                        shape = RoundedCornerShape(11.dp),
                        minLines = 3
                    )

                    Button(
                        onClick = {
                            onSaveProfile(nameState, roleState, phoneState, statusState, bioState, selectedPreset)
                        },
                        modifier = Modifier.fillMaxWidth().testTag("save_profile_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save profile", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
