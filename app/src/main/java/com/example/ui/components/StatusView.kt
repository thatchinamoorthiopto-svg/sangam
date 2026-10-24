package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Organization
import com.example.data.StatusUpdate
import com.example.data.UserProfile
import com.example.ui.theme.SangamTypography

@Composable
fun StatusListPanel(
    organization: Organization,
    selectedStatusIndex: Int,
    isMyStatusSelected: Boolean,
    user: UserProfile,
    onSelectStatus: (index: Int, isMe: Boolean) -> Unit,
    onAddStatusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("My status", style = SangamTypography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

        // My Status Row
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .clickable { onSelectStatus(0, true) }
                .testTag("my_status_row"),
            color = if (isMyStatusSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f) else Color.Transparent,
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
                        .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .background(Color(0xFF3B5B8C)),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = user.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                    Text(initials.ifEmpty { "TM" }, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("My status", fontWeight = FontWeight.Bold, style = SangamTypography.bodyLarge)
                    Text(
                        text = if (organization.myStatus.slides.isNotEmpty()) "${organization.myStatus.slides.size} updates · visible to everyone" else "Add an update for your teams",
                        style = SangamTypography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onAddStatusClick,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add status", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                }
            }
        }

        Divider(color = MaterialTheme.colorScheme.outline)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val unseen = organization.statuses.mapIndexed { idx, s -> idx to s }.filter { !it.second.isViewed }
            val viewed = organization.statuses.mapIndexed { idx, s -> idx to s }.filter { it.second.isViewed }

            if (unseen.isNotEmpty()) {
                item {
                    Text("Recent updates (${unseen.size})", style = SangamTypography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                itemsIndexed(unseen) { _, (origIdx, s) ->
                    StatusItemRow(
                        status = s,
                        isSelected = !isMyStatusSelected && origIdx == selectedStatusIndex,
                        onClick = { onSelectStatus(origIdx, false) }
                    )
                }
            }

            if (viewed.isNotEmpty()) {
                item {
                    Text("Viewed (${viewed.size})", style = SangamTypography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                itemsIndexed(viewed) { _, (origIdx, s) ->
                    StatusItemRow(
                        status = s,
                        isSelected = !isMyStatusSelected && origIdx == selectedStatusIndex,
                        onClick = { onSelectStatus(origIdx, false) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusItemRow(
    status: StatusUpdate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f) else Color.Transparent,
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
                    .border(2.dp, if (status.isViewed) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary, CircleShape)
                    .background(Color(0xFF162338)),
                contentAlignment = Alignment.Center
            ) {
                val initials = status.authorName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                Text(initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(status.authorName, fontWeight = FontWeight.Bold, style = SangamTypography.bodyLarge)
                    Text(status.time, style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(
                    text = "${status.authorRole} · ${status.slides.size} update${if (status.slides.size > 1) "s" else ""}",
                    style = SangamTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun StatusDetailPane(
    organization: Organization,
    selectedStatusIndex: Int,
    isMyStatusSelected: Boolean,
    user: UserProfile,
    onBackClick: (() -> Unit)? = null,
    onAddStatusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeStatus = if (isMyStatusSelected) organization.myStatus else organization.statuses.getOrNull(selectedStatusIndex)

    if (activeStatus == null || activeStatus.slides.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(54.dp))
                Text("No status update to view", style = SangamTypography.headlineSmall)
                Button(onClick = onAddStatusClick) {
                    Text("Add a status update")
                }
            }
        }
        return
    }

    var currentSlideIndex by remember(activeStatus) { mutableStateOf(0) }
    val currentSlide = activeStatus.slides.getOrNull(currentSlideIndex) ?: activeStatus.slides.first()

    var replyText by remember { mutableStateOf("") }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(activeStatus, currentSlideIndex) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 4800, easing = LinearEasing)
        )
        if (currentSlideIndex < activeStatus.slides.lastIndex) {
            currentSlideIndex++
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        // Expanded Big Story Card Container
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.hsl(currentSlide.hue, 0.52f, 0.34f),
                                Color.hsl((currentSlide.hue + 36f) % 360f, 0.58f, 0.17f)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top Progress Bars & Header
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            activeStatus.slides.forEachIndexed { idx, _ ->
                                LinearProgressIndicator(
                                    progress = when {
                                        idx < currentSlideIndex -> 1f
                                        idx == currentSlideIndex -> progress.value
                                        else -> 0f
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(4.dp)
                                        .clip(CircleShape),
                                    color = Color.White,
                                    trackColor = Color.White.copy(alpha = 0.3f)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (onBackClick != null) {
                                IconButton(onClick = onBackClick) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                val name = if (isMyStatusSelected) user.name else activeStatus.authorName
                                val initials = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                                Text(initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }

                            Column {
                                Text(
                                    text = if (isMyStatusSelected) "My status" else activeStatus.authorName,
                                    fontWeight = FontWeight.Bold,
                                    style = SangamTypography.headlineSmall,
                                    color = Color.White
                                )
                                Text(
                                    text = if (isMyStatusSelected) "Visible to everyone" else "${activeStatus.authorRole} · ${activeStatus.time}",
                                    style = SangamTypography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    // Main Text
                    Text(
                        text = currentSlide.text,
                        style = SangamTypography.displayLarge,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        textAlign = TextAlign.Start
                    )

                    // Bottom Action or Reply Bar
                    if (isMyStatusSelected) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.RemoveRedEye, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Text(
                                text = "Seen by ${organization.totalPeople * 46 / 100} people",
                                style = SangamTypography.bodyLarge,
                                color = Color.White
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = replyText,
                                onValueChange = { replyText = it },
                                placeholder = { Text("Reply to ${activeStatus.authorName.split(" ").firstOrNull()}", color = Color.White.copy(alpha = 0.7f)) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("status_reply_input"),
                                shape = CircleShape,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.White,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                            IconButton(
                                onClick = { replyText = "" },
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send reply", tint = Color(0xFF10203A))
                            }
                        }
                    }
                }

                // Left/Right Tap Areas for Story Navigation
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                if (currentSlideIndex > 0) currentSlideIndex--
                            }
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                if (currentSlideIndex < activeStatus.slides.lastIndex) currentSlideIndex++
                            }
                    )
                }
            }
        }
    }
}
