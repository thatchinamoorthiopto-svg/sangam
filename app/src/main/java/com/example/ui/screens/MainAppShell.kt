package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GroupType
import com.example.ui.SangamViewModel
import com.example.ui.components.*
import com.example.ui.theme.SangamTypography
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppShell(
    viewModel: SangamViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentOrg = viewModel.currentOrg
    val clipboardManager = LocalClipboardManager.current

    val configuration = LocalConfiguration.current
    val isWideScreen = configuration.screenWidthDp > 820

    // System Back Handler for Single-Pane Mobile Detail view
    if (!isWideScreen && uiState.isDetailOpenOnMobile) {
        BackHandler {
            viewModel.closeDetailOnMobile()
        }
    }

    // Toast Dismiss Timer
    LaunchedEffect(uiState.toastMessage) {
        if (uiState.toastMessage != null) {
            delay(2800)
            viewModel.clearToast()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        if (currentOrg != null) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Left Org Rail (if wide screen)
                if (isWideScreen) {
                    OrgRail(
                        organizations = uiState.organizations,
                        selectedOrgIndex = uiState.selectedOrgIndex,
                        user = uiState.user,
                        onSelectOrg = { viewModel.selectOrg(it) },
                        onAccountClick = { viewModel.toggleAccountMenu() },
                        onCreateOrgClick = { viewModel.setShowCreateOrgDialog(true) }
                    )
                }

                // Main Content Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    // Mobile Top Bar (only shown when not inside a mobile detail chat screen)
                    if (!isWideScreen && !uiState.isDetailOpenOnMobile) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    uiState.organizations.forEachIndexed { idx, org ->
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(if (idx == uiState.selectedOrgIndex) CircleShape else RoundedCornerShape(12.dp))
                                                .background(org.accentColor)
                                                .clickable { viewModel.selectOrg(idx) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(org.shortName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF3B5B8C))
                                        .clickable { viewModel.toggleAccountMenu() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    val initials = uiState.user.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                                    Text(initials.ifEmpty { "TM" }, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Organization Title Header & Tab Bar (only shown when not inside a mobile detail screen)
                    if (isWideScreen || !uiState.isDetailOpenOnMobile) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { viewModel.setShowCompanyProfileDialog(true) }
                                        .padding(vertical = 2.dp)
                                        .testTag("company_name_header"),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(currentOrg.name, style = SangamTypography.headlineMedium, fontWeight = FontWeight.Bold)
                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                color = currentOrg.accentColor.copy(alpha = 0.15f),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, currentOrg.accentColor.copy(alpha = 0.4f))
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    Icon(Icons.Default.Info, contentDescription = "View Company Profile", tint = currentOrg.accentColor, modifier = Modifier.size(13.dp))
                                                    Text("Company Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = currentOrg.accentColor)
                                                }
                                            }
                                        }
                                        Text("${currentOrg.sector} · ${currentOrg.totalPeople} people", style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                // 4 Tabs Row with big, bold selection pills and high contrast
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        val tabs = listOf(
                                            "comm" to ("Communities" to Icons.Default.Chat),
                                            "status" to ("Status" to Icons.Default.Circle),
                                            "meet" to ("Meetings" to Icons.Default.CalendarMonth),
                                            "people" to ("People" to Icons.Default.People)
                                        )

                                        tabs.forEach { (key, pair) ->
                                            val (label, icon) = pair
                                            val isSelected = uiState.selectedTab == key
                                            val animatedBg by androidx.compose.animation.animateColorAsState(
                                                targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                                animationSpec = androidx.compose.animation.core.tween(200),
                                                label = "tab_bg"
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(11.dp))
                                                    .background(animatedBg)
                                                    .clickable { viewModel.selectTab(key) }
                                                    .padding(vertical = 10.dp, horizontal = 4.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = icon,
                                                        contentDescription = label,
                                                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Text(
                                                        text = label,
                                                        style = SangamTypography.titleMedium,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        maxLines = 1,
                                                        softWrap = false,
                                                        overflow = TextOverflow.Ellipsis,
                                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Content View (Responsive Single-Pane vs 2-Pane)
                    if (!isWideScreen) {
                        // Mobile Layout: Show List OR Detail Pane
                        if (!uiState.isDetailOpenOnMobile) {
                            when (uiState.selectedTab) {
                                "comm" -> CommunitiesListPanel(
                                    groups = currentOrg.groups,
                                    selectedIndex = uiState.selectedCommIndex,
                                    filter = uiState.commFilter,
                                    query = uiState.commQuery,
                                    onFilterChange = { viewModel.setCommFilter(it) },
                                    onQueryChange = { viewModel.setCommQuery(it) },
                                    onSelectGroup = { viewModel.selectCommGroup(it) },
                                    onNewGroupClick = { viewModel.setShowNewGroupDialog(true) },
                                    modifier = Modifier.fillMaxSize()
                                )
                                "status" -> StatusListPanel(
                                    organization = currentOrg,
                                    selectedStatusIndex = uiState.selectedStatusIndex,
                                    isMyStatusSelected = uiState.isMyStatusSelected,
                                    user = uiState.user,
                                    onSelectStatus = { idx, isMe -> viewModel.selectStatus(idx, isMe) },
                                    onAddStatusClick = { viewModel.setShowStatusComposer(true) },
                                    modifier = Modifier.fillMaxSize()
                                )
                                "meet" -> MeetingsListPanel(
                                    organization = currentOrg,
                                    selectedMeetingIndex = uiState.selectedMeetIndex,
                                    onSelectMeeting = { viewModel.selectMeeting(it) },
                                    onNewMeetingClick = { viewModel.setShowScheduleMeetingDialog(true) },
                                    modifier = Modifier.fillMaxSize()
                                )
                                "people" -> PeopleListPanel(
                                    organization = currentOrg,
                                    selectedIndex = uiState.selectedPeopleIndex,
                                    isMyProfileSelected = uiState.isMyProfileSelected,
                                    user = uiState.user,
                                    query = uiState.peopleQuery,
                                    groupFilter = uiState.peopleGroupFilter,
                                    onQueryChange = { viewModel.setPeopleQuery(it) },
                                    onGroupFilterChange = { viewModel.setPeopleGroupFilter(it) },
                                    onSelectPerson = { idx, isMe -> viewModel.selectPerson(idx, isMe) },
                                    onAddContactClick = { viewModel.setShowAddContactDialog(true) },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        } else {
                            // Mobile Detail Screen
                            when (uiState.selectedTab) {
                                "comm" -> CommunitiesDetailPane(
                                    group = viewModel.currentGroup,
                                    allGroups = currentOrg.groups,
                                    onBackClick = { viewModel.closeDetailOnMobile() },
                                    onSendMessage = { kind, text, grpIdx, confirm -> viewModel.sendMessage(kind, text, grpIdx, confirm) },
                                    onVotePoll = { msgId, optIdx -> viewModel.votePoll(msgId, optIdx) },
                                    onConfirmReceipt = { msgId -> viewModel.confirmReceipt(msgId) },
                                    onToggleReaction = { msgId, emoji -> viewModel.toggleReaction(msgId, emoji) },
                                    onToggleAudioPlayback = { msgId -> viewModel.toggleAudioPlayback(msgId) },
                                    onTogglePin = { msgId -> viewModel.togglePinMessage(msgId) },
                                    onToggleDeliveryStatus = { msgId -> viewModel.cycleDeliveryStatus(msgId) },
                                    onPersonClick = { name -> viewModel.openDirectMessageWith(name) },
                                    onStartCall = { name, isVideo -> viewModel.startCall(name, isVideo) },
                                    modifier = Modifier.fillMaxSize()
                                )
                                "status" -> StatusDetailPane(
                                    organization = currentOrg,
                                    selectedStatusIndex = uiState.selectedStatusIndex,
                                    isMyStatusSelected = uiState.isMyStatusSelected,
                                    user = uiState.user,
                                    onBackClick = { viewModel.closeDetailOnMobile() },
                                    onAddStatusClick = { viewModel.setShowStatusComposer(true) },
                                    modifier = Modifier.fillMaxSize()
                                )
                                "meet" -> MeetingDetailPane(
                                    meeting = viewModel.currentMeeting,
                                    onUpdateRsvp = { id, choice -> viewModel.updateRsvp(id, choice) },
                                    onJoinRoom = { id -> viewModel.joinMeetingRoom(id) },
                                    modifier = Modifier.fillMaxSize()
                                )
                                "people" -> {
                                    if (uiState.isMyProfileSelected) {
                                        MyProfileEditorPane(
                                            user = uiState.user,
                                            organization = currentOrg,
                                            onSaveProfile = { name, role, phone, status, bio, photoIdx ->
                                                viewModel.saveUserProfile(name, role, phone, status, bio, photoIdx)
                                            },
                                            onShowToast = { viewModel.showToast(it) },
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    } else {
                                        PersonProfileDetailPane(
                                            person = viewModel.currentPerson,
                                            organization = currentOrg,
                                            onSendMessage = { name -> viewModel.openDirectMessageWith(name) },
                                            onCopyPhone = { phone ->
                                                clipboardManager.setText(AnnotatedString(phone))
                                                viewModel.showToast("Copied $phone")
                                            },
                                            onCopyEmail = { email ->
                                                clipboardManager.setText(AnnotatedString(email))
                                                viewModel.showToast("Copied $email")
                                            },
                                            onStartCall = { name, isVideo -> viewModel.startCall(name, isVideo) },
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Wide Screen Layout (2-Pane Side by Side)
                        Row(modifier = Modifier.fillMaxSize()) {
                            when (uiState.selectedTab) {
                                "comm" -> {
                                    CommunitiesListPanel(
                                        groups = currentOrg.groups,
                                        selectedIndex = uiState.selectedCommIndex,
                                        filter = uiState.commFilter,
                                        query = uiState.commQuery,
                                        onFilterChange = { viewModel.setCommFilter(it) },
                                        onQueryChange = { viewModel.setCommQuery(it) },
                                        onSelectGroup = { viewModel.selectCommGroup(it) },
                                        onNewGroupClick = { viewModel.setShowNewGroupDialog(true) },
                                        modifier = Modifier.width(340.dp)
                                    )
                                    Divider(modifier = Modifier.fillMaxHeight().width(1.dp), color = MaterialTheme.colorScheme.outline)
                                    CommunitiesDetailPane(
                                        group = viewModel.currentGroup,
                                        allGroups = currentOrg.groups,
                                        onSendMessage = { kind, text, grpIdx, confirm -> viewModel.sendMessage(kind, text, grpIdx, confirm) },
                                        onVotePoll = { msgId, optIdx -> viewModel.votePoll(msgId, optIdx) },
                                        onConfirmReceipt = { msgId -> viewModel.confirmReceipt(msgId) },
                                        onToggleReaction = { msgId, emoji -> viewModel.toggleReaction(msgId, emoji) },
                                        onToggleAudioPlayback = { msgId -> viewModel.toggleAudioPlayback(msgId) },
                                        onTogglePin = { msgId -> viewModel.togglePinMessage(msgId) },
                                        onToggleDeliveryStatus = { msgId -> viewModel.cycleDeliveryStatus(msgId) },
                                        onPersonClick = { name -> viewModel.openDirectMessageWith(name) },
                                        onStartCall = { name, isVideo -> viewModel.startCall(name, isVideo) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                "status" -> {
                                    StatusListPanel(
                                        organization = currentOrg,
                                        selectedStatusIndex = uiState.selectedStatusIndex,
                                        isMyStatusSelected = uiState.isMyStatusSelected,
                                        user = uiState.user,
                                        onSelectStatus = { idx, isMe -> viewModel.selectStatus(idx, isMe) },
                                        onAddStatusClick = { viewModel.setShowStatusComposer(true) },
                                        modifier = Modifier.width(340.dp)
                                    )
                                    Divider(modifier = Modifier.fillMaxHeight().width(1.dp), color = MaterialTheme.colorScheme.outline)
                                    StatusDetailPane(
                                        organization = currentOrg,
                                        selectedStatusIndex = uiState.selectedStatusIndex,
                                        isMyStatusSelected = uiState.isMyStatusSelected,
                                        user = uiState.user,
                                        onAddStatusClick = { viewModel.setShowStatusComposer(true) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                "meet" -> {
                                    MeetingsListPanel(
                                        organization = currentOrg,
                                        selectedMeetingIndex = uiState.selectedMeetIndex,
                                        onSelectMeeting = { viewModel.selectMeeting(it) },
                                        onNewMeetingClick = { viewModel.setShowScheduleMeetingDialog(true) },
                                        modifier = Modifier.width(340.dp)
                                    )
                                    Divider(modifier = Modifier.fillMaxHeight().width(1.dp), color = MaterialTheme.colorScheme.outline)
                                    MeetingDetailPane(
                                        meeting = viewModel.currentMeeting,
                                        onUpdateRsvp = { id, choice -> viewModel.updateRsvp(id, choice) },
                                        onJoinRoom = { id -> viewModel.joinMeetingRoom(id) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                "people" -> {
                                    PeopleListPanel(
                                        organization = currentOrg,
                                        selectedIndex = uiState.selectedPeopleIndex,
                                        isMyProfileSelected = uiState.isMyProfileSelected,
                                        user = uiState.user,
                                        query = uiState.peopleQuery,
                                        groupFilter = uiState.peopleGroupFilter,
                                        onQueryChange = { viewModel.setPeopleQuery(it) },
                                        onGroupFilterChange = { viewModel.setPeopleGroupFilter(it) },
                                        onSelectPerson = { idx, isMe -> viewModel.selectPerson(idx, isMe) },
                                        onAddContactClick = { viewModel.setShowAddContactDialog(true) },
                                        modifier = Modifier.width(340.dp)
                                    )
                                    Divider(modifier = Modifier.fillMaxHeight().width(1.dp), color = MaterialTheme.colorScheme.outline)
                                    if (uiState.isMyProfileSelected) {
                                        MyProfileEditorPane(
                                            user = uiState.user,
                                            organization = currentOrg,
                                            onSaveProfile = { name, role, phone, status, bio, photoIdx ->
                                                viewModel.saveUserProfile(name, role, phone, status, bio, photoIdx)
                                            },
                                            onShowToast = { viewModel.showToast(it) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    } else {
                                        PersonProfileDetailPane(
                                            person = viewModel.currentPerson,
                                            organization = currentOrg,
                                            onSendMessage = { name -> viewModel.openDirectMessageWith(name) },
                                            onCopyPhone = { phone ->
                                                clipboardManager.setText(AnnotatedString(phone))
                                                viewModel.showToast("Copied $phone")
                                            },
                                            onCopyEmail = { email ->
                                                clipboardManager.setText(AnnotatedString(email))
                                                viewModel.showToast("Copied $email")
                                            },
                                            onStartCall = { name, isVideo -> viewModel.startCall(name, isVideo) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Account Menu Popover
        if (uiState.showAccountMenu) {
            AccountMenuPopover(
                user = uiState.user,
                isDarkTheme = uiState.isDarkTheme,
                isAutoStatusEnabled = uiState.isAutoStatusEnabled,
                onMyProfileClick = {
                    viewModel.selectTab("people")
                    viewModel.selectPerson(0, isMe = true)
                    viewModel.toggleAccountMenu()
                },
                onToggleTheme = { viewModel.toggleTheme() },
                onToggleAutoStatus = { viewModel.toggleAutoStatus() },
                onLogout = { viewModel.logout() },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = if (isWideScreen) 76.dp else 12.dp, bottom = 12.dp)
            )
        }

        // Dialog Modals
        if (uiState.showNewGroupDialog && currentOrg != null) {
            NewGroupDialog(
                organization = currentOrg,
                onDismiss = { viewModel.setShowNewGroupDialog(false) },
                onCreateGroup = { name, type, welcome -> viewModel.createGroup(name, type, welcome) }
            )
        }

        if (uiState.showScheduleMeetingDialog && currentOrg != null) {
            ScheduleMeetingDialog(
                organization = currentOrg,
                onDismiss = { viewModel.setShowScheduleMeetingDialog(false) },
                onScheduleMeeting = { title, day, time, duration, scope ->
                    viewModel.scheduleMeeting(title, day, time, duration, scope)
                }
            )
        }

        if (uiState.showStatusComposer && currentOrg != null) {
            StatusComposerDialog(
                organizationName = currentOrg.name,
                onDismiss = { viewModel.setShowStatusComposer(false) },
                onPostStatus = { text, hue -> viewModel.postStatus(text, hue) }
            )
        }

        if (uiState.showCreateOrgDialog) {
            CreateOrganizationDialog(
                onDismiss = { viewModel.setShowCreateOrgDialog(false) },
                onCreateOrg = { name, shortName, sector, hex ->
                    viewModel.createOrganization(name, shortName, sector, hex)
                }
            )
        }

        if (uiState.showAddContactDialog && currentOrg != null) {
            AddContactDialog(
                organization = currentOrg,
                onDismiss = { viewModel.setShowAddContactDialog(false) },
                onAddContact = { name, role, branch, team, phone, email, status, bio ->
                    viewModel.addContact(name, role, branch, team, phone, email, status, bio)
                }
            )
        }

        if (uiState.showCompanyProfileDialog && currentOrg != null) {
            CompanyProfileDialog(
                organization = currentOrg,
                onDismiss = { viewModel.setShowCompanyProfileDialog(false) },
                onContactFounder = { founderName ->
                    viewModel.openDirectMessageWith(founderName)
                },
                onUpdateCompanyDetails = { desc, hq, founder ->
                    viewModel.updateCompanyDetails(desc, hq, founder)
                },
                onUpdateCompanyImages = { logoPresetIndex, customLogoUri, customCoverUri ->
                    viewModel.updateCompanyImages(logoPresetIndex, customLogoUri, customCoverUri)
                }
            )
        }

        // Active Call Modal
        uiState.activeCall?.let { call ->
            ActiveCallDialog(
                recipientName = call.recipientName,
                isVideo = call.isVideo,
                onEndCall = { viewModel.endCall() }
            )
        }

        // Toast Message Bar
        AnimatedVisibility(
            visible = uiState.toastMessage != null,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            uiState.toastMessage?.let { msg ->
                ToastBar(message = msg)
            }
        }
    }
}
