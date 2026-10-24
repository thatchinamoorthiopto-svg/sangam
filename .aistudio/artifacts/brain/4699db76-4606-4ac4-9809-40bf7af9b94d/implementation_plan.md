# Sangam - Company Chat & Multi-Branch Communication App

Sangam is a multi-branch organization communication platform for mobile Android (built in Jetpack Compose). It enables owners, managers, and team leads across businesses (retail, healthcare, education, logistics, IT) to broadcast announcements to the whole company, specific city branches, or teams—complete with real-time read receipts ("Seen by X of Y"), confirmation requests, story-style status updates, polls, and meeting RSVPs.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The app faithfully implements the attached `Sangam_final.html` reference and 8 mockup UI screenshots. All design tokens, animations, custom typography, organization accent color switching, and components are mapped directly into native Jetpack Compose.

- **Confirmed Design & Architecture**:
  - **Dynamic Theme & Org Accents**: Smooth transition between 5 featured organizations (Arvind Retail Group `#C94F0E`, CareFirst Hospitals `#0B8A7C`, Brightpath Schools `#6A4FD1`, Kaveri Logistics `#1F6FD0`, Novatech Solutions `#C2285F`) altering active UI accent colors.
  - **Adaptive Shell & Navigation**: 3-pane rail layout on larger screens / tablets and a clean bottom tab bar (`Communities`, `Status`, `Meetings`, `People`) with top org rail strip on mobile.
  - **Core Capabilities**: Real-time read tracking progress indicators, confirmation action buttons, interactive polls with percentage fill bars, pinned announcements banner, story/status updates with auto-advance and color gradients, meeting RSVP with attendance bars, directory with status dots, direct messages, and editable member profiles with photo selection and gradient avatar options.
  - **Persistence & Demo Mode**: Full reactive in-memory state repository pre-loaded with rich realistic sample data across 5 organizations, with interactive state mutation (posting messages, voting on polls, confirming read receipts, posting status updates, RSVPing for meetings, creating new branches/teams, and saving user profile).

---

## 1. Overview & Core Concept

- **What It Does**: Sangam guarantees information reach across distributed organizations. Senders broadcast messages to `All Company`, specific `Branches` (cities), or `Teams` (departments), and monitor real-time read receipts and explicit staff confirmations.
- **Target Audience**: Business owners, regional managers, branch managers, and frontline staff in retail, healthcare, schools, logistics, and IT.
- **Key Value**: Eliminates the "lost message" problem in large WhatsApp groups or Slack channels by providing explicit per-message read tracking ("Seen by 1,103 of 1,240") and "Confirm you have read this" action triggers.

---

## 2. User Experience & Visual Design

### 2.1 Color Tokens & Theming
- **Page Background (`--bg`)**: Light `#E9EEF4` / Dark `#080E18`
- **Surface (`--surface`)**: Light `#FFFFFF` / Dark `#101A2A`
- **Secondary Surface (`--surface2`)**: Light `#F3F6FA` / Dark `#162338`
- **Ink / Text (`--ink`)**: Light `#0F1B2D` / Dark `#E9EFF7`
- **Muted Text (`--muted`)**: Light `#5A6A80` / Dark `#8FA1B8`
- **Line / Border (`--line`)**: Light `#DCE3EC` / Dark `#233450`
- **Success / Read Tracking (`--good`)**: Light `#17824F` / Dark `#3CC184`
- **Live / Alert (`--live`)**: Light `#D8362F` / Dark `#FF6B63`
- **Dynamic Organization Accents**:
  - Arvind Retail Group (AR): `#C94F0E`
  - CareFirst Hospitals (CF): `#0B8A7C`
  - Brightpath Schools (BS): `#6A4FD1`
  - Kaveri Logistics (KL): `#1F6FD0`
  - Novatech Solutions (NS): `#C2285F`

### 2.2 Navigation & Layout Structure
1. **Login & Registration Screen**:
   - Welcome banner with organization badge chips and value propositions.
   - Segmented tab control (`Sign in` / `Create account`).
   - One-tap "Sign in as Arjun" demo button.
2. **App Shell Architecture**:
   - **Top / Left Org Rail**: Quick switcher between organizations with colored badges and active indicator pills. Account profile avatar button at bottom/top.
   - **Navigation Tabs (4)**: `Communities`, `Status`, `Meetings`, `People` with animated pill selection background.
   - **Left Panel List**: Search bar, category filter chips (`All`, `Branches`, `Teams`, `Direct`), grouped lists with unread badges, status rings, and meeting time badges.
   - **Main Detail Pane**:
     - *Communities / Chat View*: Header with member count and "Read tracking on" pill; pinned message banner; scrollable message list with right-aligned user messages (green/blue tinted) showing progress bar + read count, and left-aligned incoming messages; poll cards with voting bars; media/video cards; composer with "Send to" scope dropdown, "Ask people to confirm" checkbox, image/video/poll buttons, and send button.
     - *Status Story View*: 9:15 vertical gradient card, segmented progress bars, auto-advance, tap left/right navigation, reply box, view counter for user status, and status composer with color theme swatches.
     - *Meeting Detail View*: Live status pill, title, time/date/host, "Join now" / "I am going" / "Cannot attend" RSVP buttons, agenda timeline, and attendee progress bar.
     - *People Directory & Profile*: User row + staff directory filtered by branch/team; detailed profile card with cover banner, statistics, contact copy buttons, and "Member of" chips.
     - *My Profile Editor*: Photo upload button, preset avatar styles, editable name, title, phone, status, and bio.

---

## 3. Key Product Decisions & Trade-Offs

1. **Jetpack Compose Native Construction**:
   - Recreate every CSS visual detail (rounded corners `22.dp`, custom colors, typography, progress bars, segmented tabs, elevation shadows) using pure Compose `Modifier` primitives and `MaterialTheme`.
2. **State Management**:
   - Centralized `SangamViewModel` holding `StateFlow<SangamUiState>` managing active organization, active tab, selected message thread, search filters, poll votes, message acknowledgements, status updates, meeting RSVPs, and user profile data.
3. **Edge-to-Edge & System Insets**:
   - Native `enableEdgeToEdge()` with proper `WindowInsets.safeDrawing` and `WindowInsets.navigationBars` padding to ensure no clipping on notches or Android gesture bars.

---

## 4. Technical Architecture & Data Strategy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                            Sangam Android App                               │
├───────────────────────────────┬─────────────────────────────────────────────┤
│        SangamActivity         │               ViewModel Layer               │
│  (Window Insets, Edge2Edge)   │             (SangamViewModel)               │
├───────────────────────────────┴─────────────────────────────────────────────┤
│                             UiState Repository                              │
│       ┌───────────────────────┬───────────────────────┬───────────────────┐ │
│       │ Organizations & Groups│ Messages & Receipts  │ User & Profiles   │ │
│       └───────────────────────┴───────────────────────┴───────────────────┘ │
├─────────────────────────────────────────────────────────────────────────────┤
│                             Jetpack Compose UI                              │
│  ┌──────────────┐  ┌────────────────┐  ┌───────────────┐  ┌──────────────┐ │
│  │  LoginScreen │  │ CommunitiesView│  │  StatusViewer │  │ MeetingDetail│ │
│  └──────────────┘  └────────────────┘  └───────────────┘  └──────────────┘ │
│  ┌──────────────┐  ┌────────────────┐  ┌───────────────┐  ┌──────────────┐ │
│  │ PeopleScreen │  │ ProfileEditView│  │ NewGroupModal │  │ AccountPopup │ │
│  └──────────────┘  └────────────────┘  └───────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Component Structure
- `MainActivity.kt`: Entry point configuring edge-to-edge and launching `SangamApp`.
- `ui/theme/Theme.kt`, `Color.kt`, `Type.kt`: Color schemes, organizational accent colors, typography, and Material 3 shapes.
- `data/SangamModels.kt`: Data classes for `Organization`, `CommunityGroup`, `Message`, `PollOption`, `StatusUpdate`, `Meeting`, `User`, `Person`.
- `data/SangamRepository.kt`: Pre-populated data generator and state container.
- `ui/SangamViewModel.kt`: Reactive ViewModel with `StateFlow` and actions for sending messages, toggling confirm receipts, voting on polls, adding story status updates, updating RSVPs, creating new groups, and saving profile changes.
- `ui/screens/LoginScreen.kt`: Sign-in / sign-up flow with demo login button.
- `ui/screens/MainAppShell.kt`: Responsive layout containing Org Rail, Panel List, and Detail Pane.
- `ui/components/CommunitiesView.kt`: Chat screen, message bubbles, read receipt indicators, poll cards, video preview, and composer.
- `ui/components/StatusView.kt`: Story viewer with auto-advance, progress segments, and status creation dialog.
- `ui/components/MeetingsView.kt`: Meeting detail with agenda, live badge, and RSVP controls.
- `ui/components/PeopleView.kt`: Staff directory, profile view, and avatar style editor.
