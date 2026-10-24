package com.example.data

import androidx.compose.ui.graphics.Color

enum class GroupType {
    ALL, BRANCH, TEAM, DM
}

enum class MessageKind {
    TEXT, POLL, IMAGE, VIDEO, AUDIO
}

data class PollOption(
    val label: String,
    val votes: Int
)

enum class DeliveryStatus {
    SENT, DELIVERED, READ, CONFIRMED
}

data class Message(
    val id: Int,
    val authorName: String?, // null if sent by current user ("You")
    val authorRole: String,
    val kind: MessageKind,
    val text: String = "",
    val time: String,
    val deliveryStatus: DeliveryStatus = DeliveryStatus.READ,
    val totalMembers: Int = 1,
    val seenCount: Int = 0,
    val requiresConfirm: Boolean = false,
    val confirmedCount: Int = 0,
    val isConfirmedByMe: Boolean = false,
    val pollQuestion: String = "",
    val pollOptions: List<PollOption> = emptyList(),
    val votedOptionIndex: Int = -1,
    val mediaCaption: String = "",
    val mediaHue: Float = 210f,
    val mediaDuration: String = "0:30",
    val audioDuration: String = "0:14",
    val isAudioPlaying: Boolean = false,
    val reactions: Map<String, Int> = emptyMap(),
    val userReaction: String? = null,
    val isFresh: Boolean = false
)

data class CommunityGroup(
    val id: Int,
    val name: String,
    val type: GroupType,
    val membersCount: Int,
    val messages: List<Message>,
    var unreadCount: Int = 0,
    var pinnedMessageId: Int? = null,
    val directPersonName: String? = null
)

data class StatusSlide(
    val text: String,
    val hue: Float
)

data class StatusUpdate(
    val id: Int,
    val authorName: String,
    val authorRole: String,
    val time: String,
    var isViewed: Boolean = false,
    val slides: List<StatusSlide>
)

data class AgendaItem(
    val time: String,
    val title: String
)

data class Meeting(
    val id: Int,
    val title: String,
    val day: String, // "Today", "Tomorrow", "This week"
    val time: String,
    val duration: String,
    val hostName: String,
    val scopeGroup: String,
    val agenda: List<AgendaItem>,
    var goingCount: Int,
    val invitedCount: Int,
    val isLive: Boolean = false,
    var userRsvp: String? = null, // "yes", "no", or null
    var isJoinedRoom: Boolean = false
)

data class Person(
    val id: Int,
    val name: String,
    val role: String,
    val branch: String,
    val team: String,
    val phone: String,
    val email: String,
    val status: String, // "Available", "In a meeting", "On the floor", "Away until 2 PM"
    val readPercentage: Int,
    val joinedYear: Int,
    val bio: String
)

data class Organization(
    val id: Int,
    val name: String,
    val shortName: String,
    val sector: String,
    val accentHex: String,
    val accentColor: Color,
    val totalPeople: Int,
    val groups: List<CommunityGroup>,
    val statuses: List<StatusUpdate>,
    val myStatus: StatusUpdate,
    val meetings: List<Meeting>,
    val directory: List<Person>,
    val description: String = "",
    val headquarters: String = "Chennai, India",
    val foundedYear: Int = 2014,
    val founderName: String = "Thatchina Moorthi",
    val founderEmail: String = "thatchinamoorthiopto@gmail.com",
    val logoPresetIndex: Int = 0,
    val customLogoUri: String? = null,
    val customCoverUri: String? = null
)

data class UserProfile(
    val email: String,
    val name: String,
    val role: String,
    val phone: String,
    val status: String,
    val bio: String,
    val photoPresetIndex: Int = -1, // -1 if custom or none
    val photoGradientHex: String? = null
)
