# Sangam: Build Plan and UI/UX Spec

Company chat app for owners, managers and team leads. Messages go to the whole company, one branch or one team, and the sender sees who has read and confirmed.

Working name: **Sangam** (change freely). Status: clickable front-end prototype is done (`Sangam_final.html`). This plan covers turning it into a real app.

---

## 1. Problem and idea

- Owners and managers run teams through WhatsApp groups. Information goes owner > manager > team lead and often never reaches the last-level staff.
- Staff at the bottom feel disconnected, and the owner cannot tell who actually saw an announcement.
- Sangam is one app per organization with branches (by city) and teams (Digital, Accounts, etc). Messages can be sent to everyone, one branch or one team, with **read tracking and confirmation**.

**Main differentiator:** guaranteed reach. Seen-by counts, "confirm you have read this", pinned announcements. WhatsApp Communities and Slack do not show this per message.

**First customers:** multi-branch businesses: retail chains, clinics and hospitals, schools, logistics, distributors, IT offices.

---

## 2. Feature list

| Area | Feature | Prototype | Real app |
|---|---|---|---|
| Account | Sign in, create account | Done (demo only) | Phone OTP + email, per-organization roles |
| Organizations | Switch between 5 sample companies | Done | One account can belong to many orgs |
| Communities | Company, branches, teams, direct messages | Done (13 groups per org) | Admin creates, archives and edits |
| Chat | Text, photo, video, poll, confirm request | Done (photo/video are samples) | Real uploads, voice notes, files |
| Read tracking | Seen by X of Y, confirmed count | Simulated | Real delivery and read receipts, "who has not read" list |
| Pin | Pin message to top of a group | Done | Admin only |
| New group | Create branch or team with members | Done | Add and remove members, invite links |
| Status | Story-style updates, reply | Done | Expiry after 24h, audience control |
| Meetings | Today / tomorrow / this week, RSVP, new meeting | Done | Calendar sync, video room, reminders |
| People | Directory, filter by branch or team | Done (28 sample people) | Full staff import (CSV), search |
| Profile | Details, status, photo upload, avatar styles | Done | Cloud photo storage |
| Theme | Light and dark | Done | Same |

**Next features, in order of value:**
1. Branch dashboard: which branches have not confirmed an announcement.
2. Roles and permissions: staff can read but not post to the whole company.
3. Push notifications and an unread count on the app icon.
4. Message translation (Tamil, Hindi, Telugu, Kannada, English) and voice messages.
5. Shift and attendance check-in.

---

## 3. UI/UX specification (matches the prototype)

### 3.1 Layout

Framed app, rounded 22px, soft shadow, centered, max width 1400px.

```
Desktop (>820px)
+------+----------------+-----------------------------+
| Org  |  Panel (340px) |  Detail pane                |
| rail |  - Org name    |  Chat / Story / Meeting /   |
| 76px |  - Tab bar (4) |  Profile                    |
|      |  - Search/tools|                             |
| AR   |  - List        |                             |
| CF   |                |                             |
| ...  |                |                             |
| me   |                |                             |
+------+----------------+-----------------------------+

Phone (<=820px)
+---------------------------+
| S  AR CF BS KL NS     me  |  org strip (top)
|---------------------------|
| Org name                  |
| Search / filters          |
| List                      |
|---------------------------|
| Chats Status Meet People  |  bottom tab bar
+---------------------------+
Detail pane slides in from the right over the list, with a Back button.
```

### 3.2 Tabs (4)

1. **Communities**: search, "+" new group, filter chips (All, Branches, Teams, Direct). Groups listed under Company / Branches / Teams / Direct messages. Each row: icon tile, name, last message, time, unread badge.
2. **Status**: "My status" row with "+", Recent updates, Viewed. Avatar ring spins for unseen updates.
3. **Meetings**: "New meeting" button; groups Today, Tomorrow, This week. Live meetings show a blinking red "Live now".
4. **People**: search, filter by branch or team, "You" row on top, then the directory with a green / amber / grey status dot.

### 3.3 Detail screens

- **Chat**: header (icon, name, member count, "Read tracking on" pill), pinned banner, message list on a faint dot grid, composer.
  - My messages are right-aligned with a tinted bubble and a read-tracking line: eye icon, progress bar, "Seen by 1,103 of 1,240", "Confirmed 684".
  - Others' messages are left-aligned, name in accent colour (tap opens profile), role in grey.
  - Composer: "Send to" dropdown (any group), "Ask people to confirm" checkbox, photo, video and poll buttons, text field, send button.
- **Poll**: question, options as bars that fill to the vote percentage, tap to vote or change vote.
- **Video**: tap to play (progress bar animates).
- **Status viewer**: 9:15 card, segmented progress bars, auto-advance every 4.8s, tap right or left, reply box. My status shows "Seen by N people".
- **Status composer**: text plus 5 background colours.
- **Meeting detail**: title, time, host, audience, Join now (live only), I am going / Cannot attend, agenda with times, attendance bar.
- **Profile**: cover band, large avatar, name, role, branch / team / status chips, Send message, Copy phone, About, 3 stat tiles (messages read, joined, groups), contact rows with Copy, "Member of" chips that open the group.
- **My profile**: tap the avatar to upload a photo (cropped square, resized to 256px on device), or choose from 8 gradient avatar styles, or remove. Editable name, title, phone, status, about.

### 3.4 Design tokens

Light theme:

| Token | Value | Use |
|---|---|---|
| `--bg` | `#E9EEF4` | page background (cool grey with blue tint) |
| `--surface` | `#FFFFFF` | cards, panels |
| `--surface2` | `#F3F6FA` | inputs, rail |
| `--ink` | `#0F1B2D` | text |
| `--muted` | `#5A6A80` | secondary text |
| `--line` | `#DCE3EC` | borders |
| `--good` | `#17824F` | read tracking, success |
| `--live` | `#D8362F` | live meeting |

Dark theme: `--bg #080E18`, `--surface #101A2A`, `--surface2 #162338`, `--ink #E9EFF7`, `--muted #8FA1B8`, `--line #233450`.

**Accent changes per organization** (animates over 0.6s when switching):

| Organization | Accent |
|---|---|
| Arvind Retail Group (AR) | `#C94F0E` |
| CareFirst Hospitals (CF) | `#0B8A7C` |
| Brightpath Schools (BS) | `#6A4FD1` |
| Kaveri Logistics (KL) | `#1F6FD0` |
| Novatech Solutions (NS) | `#C2285F` |

Derived: accent-soft = accent 12% over surface; accent-line = accent 35% over line.

Typography:
- Display: **Bricolage Grotesque** 500/700 (headings, org name, big numbers).
- Body: **Instrument Sans** 400/500/600 (everything else).
- Base 15px / 1.45. Uppercase labels 11px with 0.09em letter spacing. Tabular numbers for counts and times.

Shape: radius 22 (app), 16 (cards), 12 (inputs, buttons), 99 (chips, badges). Icons are 1.8px stroke line icons, 18-20px.

### 3.5 Motion

| Where | Animation |
|---|---|
| App open | fade and rise, 0.6s |
| Login | card rises; org chips float up and down; headline accent cycles through org colours |
| Org switch | accent colour tween, list rows stagger in (30ms each) |
| Tab switch | white pill slides under active tab (spring), content fades |
| Chat open | messages stagger in (70ms each) |
| New message | pop-in scale |
| Read tracking | bar and counter climb every 0.65s after sending |
| Poll | bars grow to percentage (0.7s) |
| Status | spinning ring on unseen, progress bar fills per slide |
| Live meeting | blinking red dot |
| Unread badge | soft pulse |
| Profile cover | slow light drift |
| Toasts | slide up from bottom |
| Reduced motion | all animation effectively off |

### 3.6 Responsive and accessibility

- Works down to 360px width. Side gutter at least 16px. No horizontal page scroll.
- Visible focus rings, labels on all inputs, aria-labels on icon buttons.
- Colour is never the only signal (status has text, read tracking has numbers).
- Respects the phone's light or dark setting, with a manual toggle in the account menu.

---

## 4. Data model (for the real app)

```
Organization   id, name, sector, accent, logo
Member         id, orgId, userId, role (owner | manager | lead | staff), branchId, teamId, title, phone, status
User           id, name, email, phone, photoUrl, bio
Group          id, orgId, type (all | branch | team | dm), name, memberIds[], pinnedMessageId
Message        id, groupId, authorId, kind (text | image | video | poll | file), body, mediaUrl,
               requiresConfirm, createdAt
Receipt        messageId, userId, deliveredAt, readAt, confirmedAt
Poll           messageId, question, options[{label}], Vote{userId, optionIndex}
Status         id, authorId, slides[{text, color}], audience, expiresAt, StatusView{userId, viewedAt}
Meeting        id, groupId, hostId, title, start, durationMin, agenda[{time, text}], RSVP{userId, going}
```

Read tracking = count of `Receipt.readAt` divided by group size. The "who has not read" list is a query on missing receipts.

---

## 5. Recommended tech for the real build

- **App:** React or Next.js web app first (PWA, installable on phones), then a React Native or Flutter app if customers ask.
- **Backend:** Firebase (Auth with phone OTP, Firestore for realtime messages, Storage for photos and video, Cloud Messaging for push) is the fastest route. Supabase is a good alternative.
- **Security:** per-organization data rules so one company can never read another's data; role checks on posting to "everyone".
- **Pricing idea:** per organization per month by number of staff, with a free tier up to 10 people.

---

## 6. Roadmap

| Phase | Weeks | Goal |
|---|---|---|
| 0. Validate | 1-2 | Show the prototype to 5-10 owners. Ask: how do you reach staff today, would you pay for read tracking, would staff install another app? |
| 1. Pilot MVP | 3-8 | Real login, groups, text and image messages, read receipts, push notifications. One pilot business. |
| 2. Core | 9-14 | Polls, pinned messages, status, meetings, staff import by CSV, admin roles. |
| 3. Differentiators | 15-20 | Branch confirmation dashboard, translation, voice messages. |
| 4. Launch | 21+ | Pricing, onboarding, support, first 10 paying businesses. |

**Success check for the pilot:** at least 80% of staff read important announcements within 2 hours, and the owner stops using WhatsApp for official messages.

---

## 7. Paste-ready prompt for Google AI Studio (Build)

Attach `Sangam_final.html` as the reference, then paste this:

```
You are a senior product engineer. I have attached Sangam_final.html, a working single-file front-end prototype of a company chat app. Treat it as the exact UI/UX reference. Keep its layout, design tokens, typography, icons and animations.

GOAL
Turn it into a real, deployable web app (PWA) called "Sangam" for owners, managers and team leads of multi-branch businesses in India. Messages can be sent to the whole company, one branch or one team, and the sender can see who has read and confirmed each message.

KEEP FROM THE PROTOTYPE
- 3-column desktop layout (org rail, list panel, detail pane) and the phone layout (org strip on top, bottom tab bar, slide-over detail).
- Four tabs: Communities, Status, Meetings, People.
- Per-organization accent colour that animates when switching organizations.
- Light and dark themes with a toggle.
- Fonts: Bricolage Grotesque (headings) and Instrument Sans (body).
- Read tracking line under my messages: eye icon, progress bar, "Seen by X of Y", "Confirmed N".
- Polls, pinned message banner, "Ask people to confirm", direct messages, story-style status, meetings with RSVP, people directory, profile pages, profile photo upload and avatar styles.

BUILD
1. React + TypeScript + Vite (or Next.js), Tailwind or plain CSS using the same CSS variables as the prototype.
2. Firebase: Authentication (phone OTP and email), Firestore (realtime), Storage (photos, video), Cloud Messaging (push notifications).
3. Data model: Organization, Member (role: owner | manager | lead | staff), User, Group (all | branch | team | dm), Message, Receipt (deliveredAt, readAt, confirmedAt), Poll and Vote, Status and StatusView, Meeting and RSVP.
4. Real read receipts: mark a message as read when it scrolls into view; update the sender's "Seen by X of Y" in realtime; add a "Who has not read" list for managers.
5. Roles: only owners and managers can post to "Everyone" or a branch; leads can post to their team; staff can reply and confirm.
6. Admin screens: create and edit branches and teams, add members, import staff from CSV.
7. Profile photo: upload, crop square, resize to 256px, store in Firebase Storage, show everywhere the avatar appears.
8. Security rules so a user can only read data of organizations they belong to.
9. Replace all sample data with Firestore data, but keep a "Load demo data" button for demos.

QUALITY
- Responsive down to 360px, no horizontal scroll, visible focus states, aria-labels on icon buttons, respects prefers-reduced-motion.
- Empty states, loading states and error toasts for every screen.
- Clean folder structure and a README with setup steps (Firebase config, running locally, deploying).

Start by showing the folder structure and the Firestore schema, then build the app step by step.
```

---

## 8. Validation questions for owners

1. How do you share instructions with branch staff today? What goes wrong?
2. How often has an important message not reached someone? What did it cost?
3. Would you pay for knowing who has read each announcement? How much per month?
4. Would your staff install a new app, or must it work in the browser?
5. Which language do your frontline staff prefer?
