package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.SangamTypography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunitiesListPanel(
    groups: List<CommunityGroup>,
    selectedIndex: Int,
    filter: String,
    query: String,
    onFilterChange: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onSelectGroup: (Int) -> Unit,
    onNewGroupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Search & Plus Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search branches and teams", style = SangamTypography.bodyMedium) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("comm_search_input"),
                shape = RoundedCornerShape(11.dp),
                singleLine = true
            )
            IconButton(
                onClick = onNewGroupClick,
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .testTag("add_group_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New group",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Filter Chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val filterOptions = listOf("all" to "All", "branch" to "Branches", "team" to "Teams", "dm" to "Direct")
            filterOptions.forEach { (key, label) ->
                val selected = filter == key
                FilterChip(
                    selected = selected,
                    onClick = { onFilterChange(key) },
                    label = { Text(label, style = SangamTypography.labelLarge) },
                    shape = CircleShape,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_chip_$key")
                )
            }
        }

        Divider(color = MaterialTheme.colorScheme.outline)

        // Group List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val filteredGroups = groups.mapIndexed { idx, grp -> idx to grp }.filter { (_, grp) ->
                val matchesFilter = when (filter) {
                    "branch" -> grp.type == GroupType.BRANCH
                    "team" -> grp.type == GroupType.TEAM
                    "dm" -> grp.type == GroupType.DM
                    else -> true
                }
                val matchesQuery = query.isEmpty() || grp.name.contains(query, ignoreCase = true)
                matchesFilter && matchesQuery
            }

            itemsIndexed(filteredGroups) { _, (originalIndex, grp) ->
                val isSelected = originalIndex == selectedIndex
                val lastMsg = grp.messages.lastOrNull()

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectGroup(originalIndex) }
                        .testTag("group_item_$originalIndex"),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f) else Color.Transparent,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Icon Tile
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(13.dp))
                                .background(
                                    when (grp.type) {
                                        GroupType.ALL -> MaterialTheme.colorScheme.primary
                                        GroupType.BRANCH -> MaterialTheme.colorScheme.primaryContainer
                                        GroupType.TEAM -> MaterialTheme.colorScheme.surfaceVariant
                                        GroupType.DM -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (grp.type) {
                                    GroupType.ALL -> Icons.Default.Public
                                    GroupType.BRANCH -> Icons.Default.LocationOn
                                    GroupType.TEAM -> Icons.Default.Groups
                                    GroupType.DM -> Icons.Default.Person
                                },
                                contentDescription = null,
                                tint = if (grp.type == GroupType.ALL) Color.White else MaterialTheme.colorScheme.primary
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = grp.name,
                                    fontWeight = FontWeight.Bold,
                                    style = SangamTypography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = lastMsg?.time ?: "",
                                    style = SangamTypography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when (lastMsg?.kind) {
                                        MessageKind.TEXT -> lastMsg.text
                                        MessageKind.AUDIO -> "Voice message (${lastMsg.audioDuration})"
                                        MessageKind.POLL -> "Poll: ${lastMsg.pollQuestion}"
                                        MessageKind.IMAGE -> "Photo: ${lastMsg.mediaCaption}"
                                        MessageKind.VIDEO -> "Video: ${lastMsg.mediaCaption}"
                                        else -> ""
                                    },
                                    style = SangamTypography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )

                                if (grp.unreadCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = grp.unreadCount.toString(),
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunitiesDetailPane(
    group: CommunityGroup?,
    allGroups: List<CommunityGroup>,
    onBackClick: (() -> Unit)? = null,
    onSendMessage: (kind: MessageKind, text: String, targetGroupIndex: Int, requiresConfirm: Boolean) -> Unit,
    onVotePoll: (messageId: Int, optionIndex: Int) -> Unit,
    onConfirmReceipt: (messageId: Int) -> Unit,
    onToggleReaction: (messageId: Int, emoji: String) -> Unit,
    onToggleAudioPlayback: (messageId: Int) -> Unit,
    onTogglePin: (messageId: Int) -> Unit,
    onToggleDeliveryStatus: ((messageId: Int) -> Unit)? = null,
    onPersonClick: (String) -> Unit,
    onStartCall: ((recipientName: String, isVideo: Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (group == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Select a group to start messaging", style = SangamTypography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }

    var textInput by remember { mutableStateOf("") }
    var selectedTargetIndex by remember { mutableStateOf(allGroups.indexOf(group)) }
    var askConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(group) {
        selectedTargetIndex = allGroups.indexOf(group)
    }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val dotColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Chat Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (onBackClick != null) {
                        IconButton(onClick = onBackClick, modifier = Modifier.testTag("chat_back_button")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                when (group.type) {
                                    GroupType.ALL -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.primaryContainer
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (group.type) {
                                GroupType.ALL -> Icons.Default.Public
                                GroupType.BRANCH -> Icons.Default.LocationOn
                                GroupType.TEAM -> Icons.Default.Groups
                                GroupType.DM -> Icons.Default.Person
                            },
                            contentDescription = null,
                            tint = if (group.type == GroupType.ALL) Color.White else MaterialTheme.colorScheme.primary
                        )
                    }

                    Column {
                        Text(
                            text = group.name,
                            style = SangamTypography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = when (group.type) {
                                GroupType.ALL -> "Whole company · ${group.membersCount} members"
                                GroupType.BRANCH -> "Branch · ${group.membersCount} members"
                                GroupType.TEAM -> "Team · ${group.membersCount} members"
                                GroupType.DM -> "Direct message"
                            },
                            style = SangamTypography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { onStartCall?.invoke(group.name, false) },
                        modifier = Modifier.testTag("chat_header_voice_call_button")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Voice Call", tint = MaterialTheme.colorScheme.primary)
                    }

                    IconButton(
                        onClick = { onStartCall?.invoke(group.name, true) },
                        modifier = Modifier.testTag("chat_header_video_call_button")
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        // Pinned Banner
        val pinnedMsg = group.messages.find { it.id == group.pinnedMessageId }
        if (pinnedMsg != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val idx = group.messages.indexOf(pinnedMsg)
                        if (idx >= 0) {
                            coroutineScope.launch { listState.animateScrollToItem(idx) }
                        }
                    },
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.PushPin, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Text(
                        text = "Pinned · ${pinnedMsg.text.ifBlank { pinnedMsg.mediaCaption }}",
                        style = SangamTypography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Messages List with Dot Grid pattern
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .drawBehind {
                    val step = 18.dp.toPx()
                    var x = 0f
                    while (x < size.width) {
                        var y = 0f
                        while (y < size.height) {
                            drawCircle(dotColor, radius = 1.dp.toPx(), center = Offset(x, y))
                            y += step
                        }
                        x += step
                    }
                }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(group.messages) { idx, msg ->
                MessageBubbleItem(
                    message = msg,
                    onVotePoll = { optIdx -> onVotePoll(msg.id, optIdx) },
                    onConfirmReceipt = { onConfirmReceipt(msg.id) },
                    onToggleReaction = { emoji -> onToggleReaction(msg.id, emoji) },
                    onToggleAudio = { onToggleAudioPlayback(msg.id) },
                    onTogglePin = { onTogglePin(msg.id) },
                    onToggleDeliveryStatus = { onToggleDeliveryStatus?.invoke(msg.id) },
                    onAuthorClick = { name -> onPersonClick(name) },
                    isPinned = group.pinnedMessageId == msg.id
                )
            }
        }

        // Composer
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Send to:", style = SangamTypography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    // Target Group Dropdown
                    var expandedDropdown by remember { mutableStateOf(false) }
                    Box {
                        Surface(
                            shape = RoundedCornerShape(9.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                            modifier = Modifier.clickable { expandedDropdown = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = allGroups.getOrNull(selectedTargetIndex)?.name ?: group.name,
                                    style = SangamTypography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        }

                        DropdownMenu(
                            expanded = expandedDropdown,
                            onDismissRequest = { expandedDropdown = false }
                        ) {
                            allGroups.forEachIndexed { index, grp ->
                                DropdownMenuItem(
                                    text = { Text(grp.name, style = SangamTypography.bodyMedium) },
                                    onClick = {
                                        selectedTargetIndex = index
                                        expandedDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Checkbox(
                            checked = askConfirm,
                            onCheckedChange = { askConfirm = it },
                            modifier = Modifier.testTag("ask_confirm_checkbox")
                        )
                        Text("Ask people to confirm", style = SangamTypography.bodyMedium)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onSendMessage(MessageKind.AUDIO, "", selectedTargetIndex, askConfirm) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = "Voice note", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    IconButton(
                        onClick = { onSendMessage(MessageKind.IMAGE, textInput, selectedTargetIndex, askConfirm); textInput = "" },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = "Send Photo", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    IconButton(
                        onClick = { onSendMessage(MessageKind.VIDEO, textInput, selectedTargetIndex, askConfirm); textInput = "" },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = "Send Video", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    IconButton(
                        onClick = { onSendMessage(MessageKind.POLL, textInput, selectedTargetIndex, false); textInput = "" },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(Icons.Default.Poll, contentDescription = "Create Poll", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Write a message...") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    IconButton(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                onSendMessage(MessageKind.TEXT, textInput, selectedTargetIndex, askConfirm)
                                textInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(13.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .testTag("send_message_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubbleItem(
    message: Message,
    onVotePoll: (Int) -> Unit,
    onConfirmReceipt: () -> Unit,
    onToggleReaction: (emoji: String) -> Unit,
    onToggleAudio: () -> Unit,
    onTogglePin: () -> Unit,
    onToggleDeliveryStatus: (() -> Unit)? = null,
    onAuthorClick: (String) -> Unit,
    isPinned: Boolean
) {
    val isMe = message.authorName == null
    var showEmojiPicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Surface(
                shape = if (isMe) RoundedCornerShape(topStart = 18.dp, topEnd = 4.dp, bottomStart = 18.dp, bottomEnd = 18.dp)
                else RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 18.dp),
                color = if (isMe) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f) else MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isMe) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.widthIn(max = 480.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (!isMe && !message.authorName.isNullOrEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = message.authorName,
                                fontWeight = FontWeight.Bold,
                                style = SangamTypography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable { onAuthorClick(message.authorName) }
                            )
                            Text(
                                text = message.authorRole,
                                style = SangamTypography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    when (message.kind) {
                        MessageKind.TEXT -> {
                            Text(
                                text = message.text,
                                style = SangamTypography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        MessageKind.AUDIO -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                IconButton(
                                    onClick = onToggleAudio,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(
                                        imageVector = if (message.isAudioPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = "Play voice note",
                                        tint = Color.White
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth().height(18.dp)
                                    ) {
                                        listOf(8, 14, 22, 12, 18, 28, 16, 22, 10, 24, 18, 12).forEach { h ->
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(h.dp)
                                                    .clip(CircleShape)
                                                    .background(if (message.isAudioPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                                            )
                                        }
                                    }
                                    Text(
                                        text = message.audioDuration,
                                        style = SangamTypography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        MessageKind.POLL -> {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.Poll, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                    Text(text = message.pollQuestion, fontWeight = FontWeight.Bold, style = SangamTypography.bodyLarge)
                                }

                                val totalVotes = message.pollOptions.sumOf { it.votes }.coerceAtLeast(1)
                                message.pollOptions.forEachIndexed { optIdx, opt ->
                                    val pct = (opt.votes.toFloat() / totalVotes * 100).toInt()
                                    val isVoted = message.votedOptionIndex == optIdx

                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { onVotePoll(optIdx) },
                                        shape = RoundedCornerShape(10.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        border = if (isVoted) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
                                    ) {
                                        Box {
                                            val fillFraction = opt.votes.toFloat() / totalVotes
                                            Box(
                                                modifier = Modifier
                                                    .matchParentSize()
                                                    .fillMaxWidth(fillFraction)
                                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                            )
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(text = opt.label, style = SangamTypography.bodyMedium, fontWeight = if (isVoted) FontWeight.Bold else FontWeight.Normal)
                                                Text(text = "$pct%", style = SangamTypography.labelLarge, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                                Text(
                                    text = "${message.pollOptions.sumOf { it.votes }} votes · ${if (message.votedOptionIndex >= 0) "you voted" else "tap to vote"}",
                                    style = SangamTypography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        MessageKind.IMAGE -> {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                color = Color(0xFF1F6FD0).copy(alpha = 0.7f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Image, contentDescription = null, tint = Color.White, modifier = Modifier.size(42.dp))
                                }
                            }
                            Text(text = message.mediaCaption, style = SangamTypography.bodyMedium)
                        }
                        MessageKind.VIDEO -> {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                color = Color(0xFF0B8A7C).copy(alpha = 0.8f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(54.dp))
                                }
                            }
                            Text(text = message.mediaCaption, style = SangamTypography.bodyMedium)
                        }
                    }

                    // Read Receipt Bar for user sent messages
                    if (isMe && message.totalMembers > 1) {
                        val pct = (message.seenCount.toFloat() / message.totalMembers).coerceIn(0f, 1f)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.RemoveRedEye, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                            Box(
                                modifier = Modifier
                                    .width(84.dp)
                                    .height(5.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(pct)
                                        .background(Color(0xFF17824F))
                                )
                            }
                            Text(
                                text = "Seen by ${message.seenCount} of ${message.totalMembers}",
                                style = SangamTypography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (message.requiresConfirm) {
                                Text(
                                    text = "· Confirmed ${message.confirmedCount}",
                                    style = SangamTypography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // Confirm button for incoming messages with requiresConfirm
                    if (!isMe && message.requiresConfirm) {
                        Button(
                            onClick = onConfirmReceipt,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (message.isConfirmedByMe) Color(0xFF17824F) else MaterialTheme.colorScheme.surface,
                                contentColor = if (message.isConfirmedByMe) Color.White else MaterialTheme.colorScheme.primary
                            ),
                            border = if (!message.isConfirmedByMe) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = if (message.isConfirmedByMe) "Confirmed" else "Confirm you have read this", style = SangamTypography.labelLarge)
                        }
                    }

                    // Footer Timestamp & Status Badge & Pin Action
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onToggleDeliveryStatus?.invoke() }
                                .padding(vertical = 2.dp, horizontal = 2.dp)
                        ) {
                            Text(
                                text = message.time,
                                style = SangamTypography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (isMe) {
                                val popScale by animateFloatAsState(
                                    targetValue = when (message.deliveryStatus) {
                                        DeliveryStatus.READ -> 1.3f
                                        DeliveryStatus.CONFIRMED -> 1.2f
                                        DeliveryStatus.DELIVERED -> 1.1f
                                        DeliveryStatus.SENT -> 1.0f
                                    },
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMediumLow
                                    ),
                                    label = "pop_spec"
                                )

                                when (message.deliveryStatus) {
                                    DeliveryStatus.SENT -> {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Sent",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                            modifier = Modifier.scale(popScale).size(15.dp)
                                        )
                                    }
                                    DeliveryStatus.DELIVERED -> {
                                        Icon(
                                            imageVector = Icons.Default.DoneAll,
                                            contentDescription = "Delivered",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                            modifier = Modifier.scale(popScale).size(15.dp)
                                        )
                                    }
                                    DeliveryStatus.READ -> {
                                        Icon(
                                            imageVector = Icons.Default.DoneAll,
                                            contentDescription = "Read",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.scale(popScale).size(15.dp)
                                        )
                                    }
                                    DeliveryStatus.CONFIRMED -> {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                                            modifier = Modifier
                                                .scale(popScale)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFF17824F).copy(alpha = 0.15f))
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Verified,
                                                contentDescription = "Confirmed",
                                                tint = Color(0xFF17824F),
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Text(
                                                text = "Confirmed",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF17824F)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { showEmojiPicker = !showEmojiPicker },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddReaction,
                                    contentDescription = "React",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            IconButton(
                                onClick = onTogglePin,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PushPin,
                                    contentDescription = "Pin message",
                                    tint = if (isPinned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Quick Emoji Reaction Picker Bar
            if (showEmojiPicker) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("👍", "❤️", "🔥", "👏", "😂", "🙏").forEach { emoji ->
                            Text(
                                text = emoji,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable {
                                        onToggleReaction(emoji)
                                        showEmojiPicker = false
                                    }
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }

            // Reactions Display Badge Bar
            if (message.reactions.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    message.reactions.forEach { (emoji, count) ->
                        Surface(
                            shape = CircleShape,
                            color = if (message.userReaction == emoji) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.clickable { onToggleReaction(emoji) }
                        ) {
                            Text(
                                text = "$emoji $count",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
