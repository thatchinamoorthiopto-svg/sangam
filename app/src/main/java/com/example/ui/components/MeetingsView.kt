package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Meeting
import com.example.data.Organization
import com.example.ui.theme.SangamTypography

@Composable
fun MeetingsListPanel(
    organization: Organization,
    selectedMeetingIndex: Int,
    onSelectMeeting: (Int) -> Unit,
    onNewMeetingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = onNewMeetingClick,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("new_meeting_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("New meeting", style = SangamTypography.labelLarge)
        }

        Divider(color = MaterialTheme.colorScheme.outline)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val days = listOf("Today", "Tomorrow", "This week")
            days.forEach { day ->
                val dayMeetings = organization.meetings.mapIndexed { idx, m -> idx to m }.filter { it.second.day == day }
                if (dayMeetings.isNotEmpty()) {
                    item {
                        Text(day, style = SangamTypography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    itemsIndexed(dayMeetings) { _, (origIdx, meeting) ->
                        val isSelected = origIdx == selectedMeetingIndex

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onSelectMeeting(origIdx) }
                                .testTag("meeting_item_$origIdx"),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f) else Color.Transparent,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Column(modifier = Modifier.width(70.dp)) {
                                    Text(
                                        text = meeting.time,
                                        style = SangamTypography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = meeting.duration,
                                        style = SangamTypography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = meeting.title,
                                        fontWeight = FontWeight.Bold,
                                        style = SangamTypography.bodyLarge,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        if (meeting.isLive) {
                                            Surface(
                                                shape = CircleShape,
                                                color = Color(0xFFD8362F).copy(alpha = 0.15f)
                                            ) {
                                                Text(
                                                    text = "● Live now",
                                                    color = Color(0xFFD8362F),
                                                    style = SangamTypography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = meeting.scopeGroup,
                                            style = SangamTypography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
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

@Composable
fun MeetingDetailPane(
    meeting: Meeting?,
    onUpdateRsvp: (meetingId: Int, choice: String) -> Unit,
    onJoinRoom: (meetingId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (meeting == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Select a meeting to view details", style = SangamTypography.bodyLarge)
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (meeting.isLive) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFD8362F).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "● LIVE NOW",
                            color = Color(0xFFD8362F),
                            style = SangamTypography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Text(text = meeting.title, style = SangamTypography.displayMedium, fontWeight = FontWeight.Bold)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                        Text("${meeting.day} ${meeting.time} · ${meeting.duration}", style = SangamTypography.bodyMedium)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                        Text("Hosted by ${meeting.hostName}", style = SangamTypography.bodyMedium)
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (meeting.isLive) {
                        Button(
                            onClick = { onJoinRoom(meeting.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (meeting.isJoinedRoom) Color(0xFF17824F) else MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("join_meeting_button")
                        ) {
                            Icon(if (meeting.isJoinedRoom) Icons.Default.Check else Icons.Default.Videocam, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (meeting.isJoinedRoom) "In room" else "Join now")
                        }
                    }

                    OutlinedButton(
                        onClick = { onUpdateRsvp(meeting.id, "yes") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (meeting.userRsvp == "yes") Color(0xFF17824F) else Color.Transparent,
                            contentColor = if (meeting.userRsvp == "yes") Color.White else MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.testTag("rsvp_going_button")
                    ) {
                        Text(if (meeting.userRsvp == "yes") "Going" else "I am going")
                    }

                    OutlinedButton(
                        onClick = { onUpdateRsvp(meeting.id, "no") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (meeting.userRsvp == "no") Color(0xFFD8362F).copy(alpha = 0.15f) else Color.Transparent,
                            contentColor = if (meeting.userRsvp == "no") Color(0xFFD8362F) else MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.testTag("rsvp_decline_button")
                    ) {
                        Text(if (meeting.userRsvp == "no") "Declined" else "Cannot attend")
                    }
                }
            }
        }

        // Agenda Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Agenda", style = SangamTypography.titleLarge, fontWeight = FontWeight.Bold)

                meeting.agenda.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.time,
                            style = SangamTypography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.width(60.dp)
                        )
                        Text(
                            text = item.title,
                            style = SangamTypography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                }
            }
        }

        // Attendance Bar Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Who is coming", style = SangamTypography.titleLarge, fontWeight = FontWeight.Bold)

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${meeting.goingCount} of ${meeting.invitedCount} invited",
                        style = SangamTypography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                val pct = (meeting.goingCount.toFloat() / meeting.invitedCount.coerceAtLeast(1)).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = pct,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}
