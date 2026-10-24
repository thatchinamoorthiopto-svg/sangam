package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ActiveCallState(
    val recipientName: String,
    val isVideo: Boolean,
    val startTime: Long = System.currentTimeMillis()
)

data class SangamUiState(
    val isLoggedIn: Boolean = false,
    val user: UserProfile = UserProfile(
        email = "thatchinamoorthiopto@gmail.com",
        name = "Thatchina Moorthi",
        role = "Founder & Owner",
        phone = "+91 98765 43210",
        status = "Available",
        bio = "Founder & Owner across tech, mechanics & utility platforms."
    ),
    val organizations: List<Organization> = emptyList(),
    val selectedOrgIndex: Int = 0,
    val selectedTab: String = "comm", // "comm", "status", "meet", "people"
    val selectedCommIndex: Int = 0,
    val selectedStatusIndex: Int = 0,
    val isMyStatusSelected: Boolean = false,
    val selectedMeetIndex: Int = 0,
    val selectedPeopleIndex: Int = 0,
    val isMyProfileSelected: Boolean = false,
    val isDetailOpenOnMobile: Boolean = false, // Single-pane mobile navigation
    val commFilter: String = "all", // "all", "branch", "team", "dm"
    val commQuery: String = "",
    val peopleQuery: String = "",
    val peopleGroupFilter: String = "",
    val isDarkTheme: Boolean = false,
    val showAccountMenu: Boolean = false,
    val showNewGroupDialog: Boolean = false,
    val showScheduleMeetingDialog: Boolean = false,
    val showStatusComposer: Boolean = false,
    val showCreateOrgDialog: Boolean = false,
    val showAddContactDialog: Boolean = false,
    val showCompanyProfileDialog: Boolean = false,
    val isAutoStatusEnabled: Boolean = true,
    val activeCall: ActiveCallState? = null,
    val toastMessage: String? = null
)

class SangamViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SangamUiState())
    val uiState: StateFlow<SangamUiState> = _uiState.asStateFlow()

    init {
        val initialOrgs = SangamRepository.createInitialOrganizations()
        _uiState.update { it.copy(organizations = initialOrgs) }
    }

    val currentOrg: Organization?
        get() = _uiState.value.organizations.getOrNull(_uiState.value.selectedOrgIndex)

    val currentGroup: CommunityGroup?
        get() = currentOrg?.groups?.getOrNull(_uiState.value.selectedCommIndex)

    val currentMeeting: Meeting?
        get() = currentOrg?.meetings?.getOrNull(_uiState.value.selectedMeetIndex)

    val currentPerson: Person?
        get() = currentOrg?.directory?.getOrNull(_uiState.value.selectedPeopleIndex)

    fun login(email: String, name: String) {
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                user = it.user.copy(email = email, name = name.ifBlank { "Thatchina Moorthi" })
            )
        }
        showToast("Welcome, ${name.ifBlank { "Thatchina" }}. Signed in as Founder & Owner.")
    }

    fun loginAsDemo() {
        _uiState.update { it.copy(isLoggedIn = true) }
        showToast("Welcome back, Thatchina Moorthi.")
    }

    fun selectOrg(index: Int) {
        _uiState.update {
            it.copy(
                selectedOrgIndex = index,
                selectedCommIndex = 0,
                selectedStatusIndex = 0,
                isMyStatusSelected = false,
                selectedMeetIndex = 0,
                selectedPeopleIndex = 0,
                isMyProfileSelected = false,
                isDetailOpenOnMobile = false,
                commFilter = "all",
                commQuery = "",
                peopleQuery = "",
                peopleGroupFilter = ""
            )
        }
    }

    fun selectTab(tab: String) {
        _uiState.update { it.copy(selectedTab = tab, isDetailOpenOnMobile = false) }
    }

    fun selectCommGroup(index: Int) {
        val org = currentOrg ?: return
        val groups = org.groups
        if (index in groups.indices) {
            groups[index].unreadCount = 0
            _uiState.update { it.copy(selectedCommIndex = index, isDetailOpenOnMobile = true) }
        }
    }

    fun selectStatus(index: Int, isMe: Boolean = false) {
        val org = currentOrg ?: return
        if (isMe) {
            _uiState.update { it.copy(isMyStatusSelected = true, isDetailOpenOnMobile = true) }
            if (org.myStatus.slides.isEmpty()) {
                _uiState.update { it.copy(showStatusComposer = true) }
            }
        } else {
            if (index in org.statuses.indices) {
                org.statuses[index].isViewed = true
                _uiState.update {
                    it.copy(selectedStatusIndex = index, isMyStatusSelected = false, isDetailOpenOnMobile = true)
                }
            }
        }
    }

    fun selectMeeting(index: Int) {
        val org = currentOrg ?: return
        if (index in org.meetings.indices) {
            _uiState.update { it.copy(selectedMeetIndex = index, isDetailOpenOnMobile = true) }
        }
    }

    fun selectPerson(index: Int, isMe: Boolean = false) {
        val org = currentOrg ?: return
        if (isMe) {
            _uiState.update { it.copy(isMyProfileSelected = true, isDetailOpenOnMobile = true) }
        } else {
            if (index in org.directory.indices) {
                _uiState.update {
                    it.copy(selectedPeopleIndex = index, isMyProfileSelected = false, isDetailOpenOnMobile = true)
                }
            }
        }
    }

    fun closeDetailOnMobile() {
        _uiState.update { it.copy(isDetailOpenOnMobile = false) }
    }

    fun setCommFilter(filter: String) {
        _uiState.update { it.copy(commFilter = filter) }
    }

    fun setCommQuery(query: String) {
        _uiState.update { it.copy(commQuery = query) }
    }

    fun setPeopleQuery(query: String) {
        _uiState.update { it.copy(peopleQuery = query) }
    }

    fun setPeopleGroupFilter(filter: String) {
        _uiState.update { it.copy(peopleGroupFilter = filter) }
    }

    fun sendMessage(
        kind: MessageKind,
        text: String,
        targetGroupIndex: Int,
        requiresConfirm: Boolean
    ) {
        val org = currentOrg ?: return
        val targetGroup = org.groups.getOrNull(targetGroupIndex) ?: return

        val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val newId = (100..99999).random()

        val newMsg = when (kind) {
            MessageKind.TEXT -> Message(
                id = newId,
                authorName = null,
                authorRole = "",
                kind = MessageKind.TEXT,
                text = text,
                time = timeStr,
                totalMembers = targetGroup.membersCount,
                seenCount = 0,
                requiresConfirm = requiresConfirm,
                confirmedCount = 0,
                isFresh = true
            )
            MessageKind.AUDIO -> Message(
                id = newId,
                authorName = null,
                authorRole = "",
                kind = MessageKind.AUDIO,
                text = "Voice message",
                audioDuration = "0:18",
                time = timeStr,
                totalMembers = targetGroup.membersCount,
                seenCount = 0,
                requiresConfirm = requiresConfirm,
                isFresh = true
            )
            MessageKind.IMAGE -> Message(
                id = newId,
                authorName = null,
                authorRole = "",
                kind = MessageKind.IMAGE,
                mediaCaption = text.ifBlank { "Photo from head office" },
                time = timeStr,
                mediaHue = (0..360).random().toFloat(),
                totalMembers = targetGroup.membersCount,
                seenCount = 0,
                requiresConfirm = requiresConfirm,
                isFresh = true
            )
            MessageKind.VIDEO -> Message(
                id = newId,
                authorName = null,
                authorRole = "",
                kind = MessageKind.VIDEO,
                mediaCaption = text.ifBlank { "Video message from head office" },
                time = timeStr,
                mediaDuration = "0:45",
                mediaHue = (0..360).random().toFloat(),
                totalMembers = targetGroup.membersCount,
                seenCount = 0,
                requiresConfirm = requiresConfirm,
                isFresh = true
            )
            MessageKind.POLL -> Message(
                id = newId,
                authorName = null,
                authorRole = "",
                kind = MessageKind.POLL,
                pollQuestion = text.ifBlank { "Quick poll: can everyone join the 4 PM call?" },
                pollOptions = listOf(
                    PollOption("Yes", 0),
                    PollOption("No", 0),
                    PollOption("Need more time", 0)
                ),
                time = timeStr,
                totalMembers = targetGroup.membersCount,
                seenCount = 0,
                isFresh = true
            )
        }

        val updatedMessages = targetGroup.messages + newMsg
        val updatedGroups = org.groups.mapIndexed { idx, grp ->
            if (idx == targetGroupIndex) grp.copy(messages = updatedMessages) else grp
        }
        val updatedOrgs = _uiState.value.organizations.mapIndexed { idx, o ->
            if (idx == _uiState.value.selectedOrgIndex) o.copy(groups = updatedGroups) else o
        }

        _uiState.update { it.copy(organizations = updatedOrgs) }

        // Start progressive read-receipt simulation for sent message
        simulateReadReceipts(targetGroupIndex, newId, targetGroup.membersCount, requiresConfirm)

        if (targetGroup.type == GroupType.DM) {
            simulateDmReply(targetGroupIndex, targetGroup.directPersonName ?: "Colleague")
        }
    }

    private fun simulateReadReceipts(
        groupIndex: Int,
        msgId: Int,
        totalMembers: Int,
        requiresConfirm: Boolean
    ) {
        viewModelScope.launch {
            // Stage 1: Sent -> Delivered (after 900ms)
            delay(900)
            if (_uiState.value.isAutoStatusEnabled) {
                updateMessageDeliveryStatus(groupIndex, msgId, DeliveryStatus.DELIVERED, seenCount = 1)
            }

            // Stage 2: Delivered -> Read (after 2100ms)
            delay(1200)
            val targetSeen = (totalMembers * 0.85).toInt().coerceAtLeast(1)
            if (_uiState.value.isAutoStatusEnabled) {
                updateMessageDeliveryStatus(groupIndex, msgId, DeliveryStatus.READ, seenCount = targetSeen)
            }

            // Stage 3: Read -> Confirmed (if required, after 3600ms)
            if (requiresConfirm) {
                delay(1500)
                if (_uiState.value.isAutoStatusEnabled) {
                    val confirmedCount = (targetSeen * 0.65).toInt().coerceAtLeast(1)
                    updateMessageDeliveryStatus(groupIndex, msgId, DeliveryStatus.CONFIRMED, seenCount = targetSeen, confirmedCount = confirmedCount)
                }
            }
        }
    }

    private fun updateMessageDeliveryStatus(
        groupIndex: Int,
        msgId: Int,
        status: DeliveryStatus,
        seenCount: Int,
        confirmedCount: Int = 0
    ) {
        val org = currentOrg ?: return
        val grp = org.groups.getOrNull(groupIndex) ?: return
        val updatedMsgs = grp.messages.map { m ->
            if (m.id == msgId) {
                m.copy(deliveryStatus = status, seenCount = seenCount, confirmedCount = if (confirmedCount > 0) confirmedCount else m.confirmedCount)
            } else m
        }
        val updatedGroups = org.groups.mapIndexed { i, g ->
            if (i == groupIndex) g.copy(messages = updatedMsgs) else g
        }
        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) o.copy(groups = updatedGroups) else o
        }
        _uiState.update { it.copy(organizations = updatedOrgs) }
    }

    private fun simulateDmReply(groupIndex: Int, personName: String) {
        viewModelScope.launch {
            delay(2200)
            val org = currentOrg ?: return@launch
            val grp = org.groups.getOrNull(groupIndex) ?: return@launch
            val person = org.directory.find { it.name == personName }
            val replies = listOf(
                "Understood, I will take care of it.",
                "Thanks. I will update you by evening.",
                "Noted. Sharing this with my team now.",
                "Got it. I will confirm shortly."
            )
            val replyText = replies.random()
            val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

            val replyMsg = Message(
                id = (100..99999).random(),
                authorName = personName,
                authorRole = person?.role ?: "Team Member",
                kind = MessageKind.TEXT,
                text = replyText,
                time = timeStr,
                totalMembers = 2,
                seenCount = 2
            )

            val updatedMsgs = grp.messages + replyMsg
            val updatedGroups = org.groups.mapIndexed { i, g ->
                if (i == groupIndex) g.copy(messages = updatedMsgs) else g
            }
            val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
                if (i == _uiState.value.selectedOrgIndex) o.copy(groups = updatedGroups) else o
            }
            _uiState.update { it.copy(organizations = updatedOrgs) }
        }
    }

    fun votePoll(messageId: Int, optionIndex: Int) {
        val org = currentOrg ?: return
        val grp = currentGroup ?: return
        val updatedMsgs = grp.messages.map { msg ->
            if (msg.id == messageId && msg.kind == MessageKind.POLL) {
                val newOpts = msg.pollOptions.mapIndexed { idx, opt ->
                    when (idx) {
                        optionIndex -> opt.copy(votes = opt.votes + 1)
                        msg.votedOptionIndex -> opt.copy(votes = (opt.votes - 1).coerceAtLeast(0))
                        else -> opt
                    }
                }
                msg.copy(pollOptions = newOpts, votedOptionIndex = optionIndex)
            } else msg
        }
        val updatedGroups = org.groups.mapIndexed { i, g ->
            if (i == _uiState.value.selectedCommIndex) g.copy(messages = updatedMsgs) else g
        }
        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) o.copy(groups = updatedGroups) else o
        }
        _uiState.update { it.copy(organizations = updatedOrgs) }
        showToast("Vote recorded")
    }

    fun confirmReceipt(messageId: Int) {
        val org = currentOrg ?: return
        val grp = currentGroup ?: return
        val updatedMsgs = grp.messages.map { msg ->
            if (msg.id == messageId) {
                val isConfirmed = !msg.isConfirmedByMe
                val newCount = if (isConfirmed) msg.confirmedCount + 1 else (msg.confirmedCount - 1).coerceAtLeast(0)
                msg.copy(isConfirmedByMe = isConfirmed, confirmedCount = newCount)
            } else msg
        }
        val updatedGroups = org.groups.mapIndexed { i, g ->
            if (i == _uiState.value.selectedCommIndex) g.copy(messages = updatedMsgs) else g
        }
        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) o.copy(groups = updatedGroups) else o
        }
        _uiState.update { it.copy(organizations = updatedOrgs) }
        showToast("Confirmation recorded. The sender can see this.")
    }

    fun toggleReaction(messageId: Int, emoji: String) {
        val org = currentOrg ?: return
        val grp = currentGroup ?: return
        val updatedMsgs = grp.messages.map { msg ->
            if (msg.id == messageId) {
                val currentReaction = msg.userReaction
                val updatedReactions = msg.reactions.toMutableMap()
                if (currentReaction == emoji) {
                    // Remove reaction
                    updatedReactions[emoji] = (updatedReactions[emoji] ?: 1) - 1
                    if ((updatedReactions[emoji] ?: 0) <= 0) updatedReactions.remove(emoji)
                    msg.copy(reactions = updatedReactions, userReaction = null)
                } else {
                    if (currentReaction != null) {
                        updatedReactions[currentReaction] = (updatedReactions[currentReaction] ?: 1) - 1
                        if ((updatedReactions[currentReaction] ?: 0) <= 0) updatedReactions.remove(currentReaction)
                    }
                    updatedReactions[emoji] = (updatedReactions[emoji] ?: 0) + 1
                    msg.copy(reactions = updatedReactions, userReaction = emoji)
                }
            } else msg
        }
        val updatedGroups = org.groups.mapIndexed { i, g ->
            if (i == _uiState.value.selectedCommIndex) g.copy(messages = updatedMsgs) else g
        }
        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) o.copy(groups = updatedGroups) else o
        }
        _uiState.update { it.copy(organizations = updatedOrgs) }
    }

    fun toggleAudioPlayback(messageId: Int) {
        val org = currentOrg ?: return
        val grp = currentGroup ?: return
        val updatedMsgs = grp.messages.map { msg ->
            if (msg.id == messageId && msg.kind == MessageKind.AUDIO) {
                msg.copy(isAudioPlaying = !msg.isAudioPlaying)
            } else msg
        }
        val updatedGroups = org.groups.mapIndexed { i, g ->
            if (i == _uiState.value.selectedCommIndex) g.copy(messages = updatedMsgs) else g
        }
        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) o.copy(groups = updatedGroups) else o
        }
        _uiState.update { it.copy(organizations = updatedOrgs) }
    }

    fun togglePinMessage(messageId: Int) {
        val org = currentOrg ?: return
        val grp = currentGroup ?: return
        val newPinned = if (grp.pinnedMessageId == messageId) null else messageId
        val updatedGroups = org.groups.mapIndexed { i, g ->
            if (i == _uiState.value.selectedCommIndex) g.copy(pinnedMessageId = newPinned) else g
        }
        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) o.copy(groups = updatedGroups) else o
        }
        _uiState.update { it.copy(organizations = updatedOrgs) }
        showToast(if (newPinned != null) "Pinned to the top of this group" else "Unpinned")
    }

    fun postStatus(text: String, hue: Float) {
        val org = currentOrg ?: return
        val mySlides = org.myStatus.slides + StatusSlide(text, hue)
        val updatedMyStatus = org.myStatus.copy(slides = mySlides)

        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) o.copy(myStatus = updatedMyStatus) else o
        }
        _uiState.update {
            it.copy(
                organizations = updatedOrgs,
                showStatusComposer = false,
                isMyStatusSelected = true,
                isDetailOpenOnMobile = true
            )
        }
        showToast("Status shared with everyone")
    }

    fun updateRsvp(meetingId: Int, rsvp: String) {
        val org = currentOrg ?: return
        val updatedMeets = org.meetings.map { m ->
            if (m.id == meetingId) {
                val wasGoing = m.userRsvp == "yes"
                val isGoing = rsvp == "yes"
                val newCount = if (isGoing && !wasGoing) m.goingCount + 1 else if (!isGoing && wasGoing) (m.goingCount - 1).coerceAtLeast(0) else m.goingCount
                m.copy(userRsvp = rsvp, goingCount = newCount)
            } else m
        }
        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) o.copy(meetings = updatedMeets) else o
        }
        _uiState.update { it.copy(organizations = updatedOrgs) }
        showToast(if (rsvp == "yes") "Marked as going" else "Host will see you cannot attend")
    }

    fun joinMeetingRoom(meetingId: Int) {
        val org = currentOrg ?: return
        val updatedMeets = org.meetings.map { m ->
            if (m.id == meetingId) {
                m.copy(isJoinedRoom = !m.isJoinedRoom)
            } else m
        }
        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) o.copy(meetings = updatedMeets) else o
        }
        _uiState.update { it.copy(organizations = updatedOrgs) }
        val m = updatedMeets.find { it.id == meetingId }
        showToast(if (m?.isJoinedRoom == true) "Connected to meeting room" else "Left the meeting")
    }

    fun createGroup(name: String, type: GroupType, welcomeMsg: String) {
        val org = currentOrg ?: return
        val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val membersCount = (8..45).random()
        val welcome = Message(
            id = (100..99999).random(),
            authorName = null,
            authorRole = "",
            kind = MessageKind.TEXT,
            text = welcomeMsg.ifBlank { "Welcome to $name. This group is for updates from head office." },
            time = timeStr,
            totalMembers = membersCount,
            seenCount = 1
        )
        val newGroup = CommunityGroup(
            id = (100..99999).random(),
            name = name,
            type = type,
            membersCount = membersCount,
            messages = listOf(welcome)
        )

        val updatedGroups = org.groups + newGroup
        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) o.copy(groups = updatedGroups) else o
        }
        _uiState.update {
            it.copy(
                organizations = updatedOrgs,
                selectedCommIndex = updatedGroups.lastIndex,
                isDetailOpenOnMobile = true,
                showNewGroupDialog = false
            )
        }
        showToast("$name created with $membersCount members")
    }

    fun scheduleMeeting(title: String, day: String, time: String, duration: String, scopeGroup: String) {
        val org = currentOrg ?: return
        val newMeeting = Meeting(
            id = (100..99999).random(),
            title = title,
            day = day,
            time = time,
            duration = duration,
            hostName = "You",
            scopeGroup = scopeGroup,
            agenda = listOf(
                AgendaItem(time, "Welcome and goals"),
                AgendaItem("Next", "Updates by branch"),
                AgendaItem("Last", "Actions and owners")
            ),
            goingCount = 1,
            invitedCount = 20
        )
        val updatedMeets = org.meetings + newMeeting
        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) o.copy(meetings = updatedMeets) else o
        }
        _uiState.update {
            it.copy(
                organizations = updatedOrgs,
                selectedMeetIndex = updatedMeets.lastIndex,
                isDetailOpenOnMobile = true,
                showScheduleMeetingDialog = false
            )
        }
        showToast("Meeting scheduled. Invitees are notified.")
    }

    fun openDirectMessageWith(personName: String) {
        val org = currentOrg ?: return
        val existingIndex = org.groups.indexOfFirst { it.type == GroupType.DM && it.directPersonName == personName }
        if (existingIndex >= 0) {
            _uiState.update {
                it.copy(
                    selectedTab = "comm",
                    selectedCommIndex = existingIndex,
                    isDetailOpenOnMobile = true
                )
            }
        } else {
            val person = org.directory.find { it.name == personName }
            val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val welcome = Message(
                id = (100..99999).random(),
                authorName = personName,
                authorRole = person?.role ?: "",
                kind = MessageKind.TEXT,
                text = "Hi ${_uiState.value.user.name.split(" ").firstOrNull() ?: "there"}, how can I help?",
                time = timeStr,
                totalMembers = 2,
                seenCount = 2
            )
            val newDmGroup = CommunityGroup(
                id = (100..99999).random(),
                name = personName,
                type = GroupType.DM,
                membersCount = 2,
                messages = listOf(welcome),
                directPersonName = personName
            )
            val updatedGroups = org.groups + newDmGroup
            val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
                if (i == _uiState.value.selectedOrgIndex) o.copy(groups = updatedGroups) else o
            }
            _uiState.update {
                it.copy(
                    organizations = updatedOrgs,
                    selectedTab = "comm",
                    selectedCommIndex = updatedGroups.lastIndex,
                    isDetailOpenOnMobile = true
                )
            }
        }
    }

    fun cycleDeliveryStatus(messageId: Int) {
        val org = currentOrg ?: return
        val grp = currentGroup ?: return
        var nextStatusStr = ""
        val updatedMsgs = grp.messages.map { msg ->
            if (msg.id == messageId) {
                val nextStatus = when (msg.deliveryStatus) {
                    DeliveryStatus.SENT -> DeliveryStatus.DELIVERED
                    DeliveryStatus.DELIVERED -> DeliveryStatus.READ
                    DeliveryStatus.READ -> DeliveryStatus.CONFIRMED
                    DeliveryStatus.CONFIRMED -> DeliveryStatus.SENT
                }
                nextStatusStr = when (nextStatus) {
                    DeliveryStatus.SENT -> "Sent (1 check mark)"
                    DeliveryStatus.DELIVERED -> "Delivered (2 gray check marks)"
                    DeliveryStatus.READ -> "Read (2 blue check marks)"
                    DeliveryStatus.CONFIRMED -> "Confirmed (Green shield check mark)"
                }
                msg.copy(deliveryStatus = nextStatus)
            } else msg
        }
        val updatedGroups = org.groups.mapIndexed { i, g ->
            if (i == _uiState.value.selectedCommIndex) g.copy(messages = updatedMsgs) else g
        }
        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) o.copy(groups = updatedGroups) else o
        }
        _uiState.update { it.copy(organizations = updatedOrgs) }
        if (nextStatusStr.isNotEmpty()) {
            showToast("Message status changed: $nextStatusStr")
        }
    }

    fun setShowCreateOrgDialog(show: Boolean) {
        _uiState.update { it.copy(showCreateOrgDialog = show) }
    }

    fun setShowCompanyProfileDialog(show: Boolean) {
        _uiState.update { it.copy(showCompanyProfileDialog = show) }
    }

    fun createOrganization(name: String, shortName: String, sector: String, accentHex: String) {
        val newId = (10..999).random()
        val parsedColor = try {
            androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(accentHex))
        } catch (_: Exception) {
            androidx.compose.ui.graphics.Color(0xFF3B5B8C)
        }
        val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val initialGroups = listOf(
            CommunityGroup(
                id = (1000..9999).random(),
                name = "All Company",
                type = GroupType.ALL,
                membersCount = 120,
                messages = listOf(
                    Message(
                        id = (10000..99999).random(),
                        authorName = "Thatchina Moorthi",
                        authorRole = "Founder & Owner",
                        kind = MessageKind.TEXT,
                        text = "Welcome to $name company portal! All team discussions start here.",
                        time = timeStr,
                        totalMembers = 120,
                        seenCount = 110,
                        deliveryStatus = DeliveryStatus.CONFIRMED
                    )
                )
            ),
            CommunityGroup(
                id = (1000..9999).random(),
                name = "HQ Branch",
                type = GroupType.BRANCH,
                membersCount = 45,
                messages = listOf(
                    Message(
                        id = (10000..99999).random(),
                        authorName = "Thatchina Moorthi",
                        authorRole = "Founder & Owner",
                        kind = MessageKind.TEXT,
                        text = "HQ Branch channel active.",
                        time = timeStr,
                        totalMembers = 45,
                        seenCount = 40,
                        deliveryStatus = DeliveryStatus.READ
                    )
                )
            )
        )
        val newOrg = Organization(
            id = newId,
            name = name,
            shortName = shortName.uppercase().take(3),
            sector = sector,
            accentHex = accentHex,
            accentColor = parsedColor,
            totalPeople = 120,
            groups = initialGroups,
            statuses = listOf(
                StatusUpdate((1000..9999).random(), "Thatchina Moorthi", "Founder & Owner", "just now", false, listOf(StatusSlide("$name officially created and ready!", 210f)))
            ),
            myStatus = StatusUpdate(0, "Thatchina Moorthi", "Founder & Owner", "just now", true, emptyList()),
            meetings = listOf(
                Meeting((1000..9999).random(), "$name Kickoff Sync", "Today", "5:00 PM", "30 min", "Thatchina Moorthi", "All Company", listOf(AgendaItem("5:00", "Company launch & goals")), 18, 25, true)
            ),
            directory = listOf(
                Person((1000..9999).random(), "Thatchina Moorthi", "Founder & Owner", "HQ Branch", "Leadership", "+91 98765 43210", "thatchinamoorthiopto@gmail.com", "Available", 99, 2026, "Founder & Owner of $name.")
            )
        )

        val updatedOrgs = _uiState.value.organizations + newOrg
        _uiState.update {
            it.copy(
                organizations = updatedOrgs,
                selectedOrgIndex = updatedOrgs.lastIndex,
                showCreateOrgDialog = false
            )
        }
        showToast("Company $name created successfully!")
    }

    fun setShowAddContactDialog(show: Boolean) {
        _uiState.update { it.copy(showAddContactDialog = show) }
    }

    fun addContact(
        name: String,
        role: String,
        branch: String,
        team: String,
        phone: String,
        email: String,
        status: String,
        bio: String
    ) {
        val org = currentOrg ?: return
        val newPerson = Person(
            id = (1000..9999).random(),
            name = name,
            role = role.ifBlank { "Team Member" },
            branch = branch.ifBlank { "Main Branch" },
            team = team.ifBlank { "General" },
            phone = phone.ifBlank { "+91 98000 00000" },
            email = email.ifBlank { "${name.lowercase().replace(" ", ".")}@company.com" },
            status = status.ifBlank { "Available" },
            readPercentage = (85..99).random(),
            joinedYear = 2026,
            bio = bio.ifBlank { "Team member at ${org.name}." }
        )
        val updatedDirectory = org.directory + newPerson
        val updatedOrgs = _uiState.value.organizations.mapIndexed { idx, o ->
            if (idx == _uiState.value.selectedOrgIndex) o.copy(directory = updatedDirectory) else o
        }
        _uiState.update {
            it.copy(
                organizations = updatedOrgs,
                selectedPeopleIndex = updatedDirectory.lastIndex,
                isMyProfileSelected = false,
                isDetailOpenOnMobile = true,
                showAddContactDialog = false
            )
        }
        showToast("$name added to contacts!")
    }

    fun saveUserProfile(name: String, role: String, phone: String, status: String, bio: String, photoPresetIndex: Int = -1) {
        val updatedName = name.ifBlank { _uiState.value.user.name }
        val updatedRole = role.ifBlank { _uiState.value.user.role }

        _uiState.update {
            it.copy(
                user = it.user.copy(
                    name = updatedName,
                    role = updatedRole,
                    phone = phone,
                    status = status,
                    bio = bio,
                    photoPresetIndex = photoPresetIndex
                )
            )
        }

        // Sync Thatchina Moorthi's directory entry across orgs
        val updatedOrgs = _uiState.value.organizations.map { org ->
            val updatedDir = org.directory.map { p ->
                if (p.email == _uiState.value.user.email || p.name == "Thatchina Moorthi") {
                    p.copy(name = updatedName, role = updatedRole, phone = phone, status = status, bio = bio)
                } else p
            }
            org.copy(directory = updatedDir)
        }
        _uiState.update { it.copy(organizations = updatedOrgs) }

        showToast("Profile saved successfully!")
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme, showAccountMenu = false) }
        showToast(if (_uiState.value.isDarkTheme) "Dark theme on" else "Light theme on")
    }

    fun toggleAccountMenu() {
        _uiState.update { it.copy(showAccountMenu = !it.showAccountMenu) }
    }

    fun setShowNewGroupDialog(show: Boolean) {
        _uiState.update { it.copy(showNewGroupDialog = show) }
    }

    fun setShowScheduleMeetingDialog(show: Boolean) {
        _uiState.update { it.copy(showScheduleMeetingDialog = show) }
    }

    fun setShowStatusComposer(show: Boolean) {
        _uiState.update { it.copy(showStatusComposer = show) }
    }

    fun showToast(msg: String) {
        _uiState.update { it.copy(toastMessage = msg) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun toggleAutoStatus() {
        val newState = !_uiState.value.isAutoStatusEnabled
        _uiState.update { it.copy(isAutoStatusEnabled = newState) }
        showToast(if (newState) "Auto change status enabled (Sent → Delivered → Read)" else "Auto status disabled (manual tap mode)")
    }

    fun updateCompanyDetails(description: String, headquarters: String, founderName: String) {
        val org = currentOrg ?: return
        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) {
                o.copy(
                    description = description.ifBlank { o.description },
                    headquarters = headquarters.ifBlank { o.headquarters },
                    founderName = founderName.ifBlank { o.founderName }
                )
            } else o
        }
        _uiState.update { it.copy(organizations = updatedOrgs) }
        showToast("Company profile details updated!")
    }

    fun updateCompanyImages(logoPresetIndex: Int, customLogoUri: String?, customCoverUri: String?) {
        val org = currentOrg ?: return
        val updatedOrgs = _uiState.value.organizations.mapIndexed { i, o ->
            if (i == _uiState.value.selectedOrgIndex) {
                o.copy(
                    logoPresetIndex = logoPresetIndex,
                    customLogoUri = customLogoUri ?: o.customLogoUri,
                    customCoverUri = customCoverUri ?: o.customCoverUri
                )
            } else o
        }
        _uiState.update { it.copy(organizations = updatedOrgs) }
        showToast("Company logo and branding updated!")
    }

    fun startCall(recipientName: String, isVideo: Boolean) {
        _uiState.update { it.copy(activeCall = ActiveCallState(recipientName, isVideo)) }
        showToast("Starting ${if (isVideo) "HD video call" else "voice call"} with $recipientName...")
    }

    fun endCall() {
        val call = _uiState.value.activeCall
        _uiState.update { it.copy(activeCall = null) }
        if (call != null) {
            showToast("Call with ${call.recipientName} ended")
        }
    }

    fun logout() {
        _uiState.update { it.copy(isLoggedIn = false, showAccountMenu = false, isDetailOpenOnMobile = false) }
    }
}
