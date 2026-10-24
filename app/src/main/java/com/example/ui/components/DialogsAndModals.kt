package com.example.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.GroupType
import com.example.data.Organization
import com.example.data.UserProfile
import com.example.ui.theme.SangamTypography

@Composable
fun NewGroupDialog(
    organization: Organization,
    onDismiss: () -> Unit,
    onCreateGroup: (name: String, type: GroupType, welcomeMsg: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(GroupType.BRANCH) }
    var welcomeMsg by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("New branch or team", style = SangamTypography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Messages sent to the group reach every member", style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("NAME") },
                    placeholder = { Text("Salem Branch") },
                    modifier = Modifier.fillMaxWidth().testTag("new_group_name_input"),
                    shape = RoundedCornerShape(11.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = selectedType == GroupType.BRANCH,
                        onClick = { selectedType = GroupType.BRANCH },
                        label = { Text("Branch") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = selectedType == GroupType.TEAM,
                        onClick = { selectedType = GroupType.TEAM },
                        label = { Text("Team") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = welcomeMsg,
                    onValueChange = { welcomeMsg = it },
                    label = { Text("WELCOME MESSAGE (OPTIONAL)") },
                    placeholder = { Text("Welcome. This is where we share updates.") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(11.dp),
                    minLines = 2
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onCreateGroup(name, selectedType, welcomeMsg)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("create_group_submit_button")
                    ) {
                        Text("Create group")
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleMeetingDialog(
    organization: Organization,
    onDismiss: () -> Unit,
    onScheduleMeeting: (title: String, day: String, time: String, duration: String, scopeGroup: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf("Today") }
    var selectedTime by remember { mutableStateOf("17:00") }
    var selectedDuration by remember { mutableStateOf("45 min") }
    var selectedScope by remember { mutableStateOf(organization.groups.firstOrNull()?.name ?: "All Company") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("New meeting", style = SangamTypography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Invite a branch, team, or the whole company", style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("TITLE") },
                    placeholder = { Text("Weekly branch sync") },
                    modifier = Modifier.fillMaxWidth().testTag("new_meeting_title_input"),
                    shape = RoundedCornerShape(11.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Today", "Tomorrow", "This week").forEach { d ->
                        FilterChip(
                            selected = selectedDay == d,
                            onClick = { selectedDay = d },
                            label = { Text(d) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onScheduleMeeting(title, selectedDay, selectedTime, selectedDuration, selectedScope)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("schedule_meeting_submit_button")
                    ) {
                        Text("Schedule")
                    }
                }
            }
        }
    }
}

@Composable
fun StatusComposerDialog(
    organizationName: String,
    onDismiss: () -> Unit,
    onPostStatus: (text: String, hue: Float) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var selectedHue by remember { mutableStateOf(210f) }
    val colorSwatches = listOf(210f, 265f, 18f, 160f, 330f)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier.size(340.dp, 520.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.hsl(selectedHue, 0.52f, 0.34f),
                                Color.hsl((selectedHue + 36f) % 360f, 0.58f, 0.17f)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Visible to everyone in $organizationName",
                        style = SangamTypography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    OutlinedTextField(
                        value = text,
                        onValueChange = { if (it.length <= 120) text = it },
                        placeholder = { Text("Share an update with your teams", color = Color.White.copy(alpha = 0.65f), style = SangamTypography.headlineMedium) },
                        modifier = Modifier.fillMaxWidth().testTag("status_composer_text"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        textStyle = SangamTypography.headlineMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            colorSwatches.forEach { h ->
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(Color.hsl(h, 0.55f, 0.40f))
                                        .border(
                                            if (selectedHue == h) 2.dp else 0.dp,
                                            Color.White,
                                            CircleShape
                                        )
                                        .clickable { selectedHue = h }
                                )
                            }
                        }

                        Button(
                            onClick = {
                                if (text.isNotBlank()) {
                                    onPostStatus(text, selectedHue)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF10203A)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("post_status_submit_button")
                        ) {
                            Text("Post", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AccountMenuPopover(
    user: UserProfile,
    isDarkTheme: Boolean,
    isAutoStatusEnabled: Boolean = true,
    onMyProfileClick: () -> Unit,
    onToggleTheme: () -> Unit,
    onToggleAutoStatus: () -> Unit = {},
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(260.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3B5B8C)),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = user.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                    Text(initials.ifEmpty { "TM" }, color = Color.White, fontWeight = FontWeight.Bold)
                }

                Column {
                    Text(user.name, fontWeight = FontWeight.Bold, style = SangamTypography.bodyLarge)
                    Text(user.role, style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Text(
                text = "${user.email}\nSigned in to 5 organizations",
                style = SangamTypography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Divider(color = MaterialTheme.colorScheme.outline)

            OutlinedButton(
                onClick = onMyProfileClick,
                modifier = Modifier.fillMaxWidth().testTag("account_my_profile_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("My profile")
            }

            OutlinedButton(
                onClick = onToggleAutoStatus,
                modifier = Modifier.fillMaxWidth().testTag("account_toggle_auto_status_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(if (isAutoStatusEnabled) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked, contentDescription = null, modifier = Modifier.size(16.dp), tint = if (isAutoStatusEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isAutoStatusEnabled) "Auto change status: ON" else "Auto change status: OFF")
            }

            OutlinedButton(
                onClick = onToggleTheme,
                modifier = Modifier.fillMaxWidth().testTag("account_toggle_theme_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isDarkTheme) "Switch to light theme" else "Switch to dark theme")
            }

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().testTag("account_logout_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Log out")
            }
        }
    }
}

@Composable
fun ToastBar(
    message: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.onBackground,
        shadowElevation = 6.dp
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.background,
            style = SangamTypography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun CreateOrganizationDialog(
    onDismiss: () -> Unit,
    onCreateOrg: (name: String, shortName: String, sector: String, accentHex: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var shortName by remember { mutableStateOf("") }
    var sector by remember { mutableStateOf("") }
    var selectedHex by remember { mutableStateOf("#8E24AA") }

    val colorOptions = listOf(
        "#8E24AA" to "Purple (Tech)",
        "#D84315" to "Orange (Automotive)",
        "#00838F" to "Teal (Utility)",
        "#1F6FD0" to "Blue (Logistics)",
        "#C2285F" to "Crimson (IT & SaaS)",
        "#17824F" to "Emerald (Retail)"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Create New Company", style = SangamTypography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Set up a new organization workspace in Sangam", style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (shortName.isEmpty() && it.isNotBlank()) {
                            shortName = it.take(2).uppercase()
                        }
                    },
                    label = { Text("COMPANY NAME") },
                    placeholder = { Text("Apex AI Labs") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("create_org_name_input"),
                    shape = RoundedCornerShape(11.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = shortName,
                        onValueChange = { if (it.length <= 4) shortName = it.uppercase() },
                        label = { Text("SHORT CODE") },
                        placeholder = { Text("AX") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("create_org_code_input"),
                        shape = RoundedCornerShape(11.dp)
                    )

                    OutlinedTextField(
                        value = sector,
                        onValueChange = { sector = it },
                        label = { Text("SECTOR / INDUSTRY") },
                        placeholder = { Text("AI & Cloud · 4 offices") },
                        singleLine = true,
                        modifier = Modifier.weight(2f).testTag("create_org_sector_input"),
                        shape = RoundedCornerShape(11.dp)
                    )
                }

                Text("Theme accent color:", style = SangamTypography.labelLarge)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    colorOptions.take(5).forEach { (hex, _) ->
                        val parsedColor = try { Color(android.graphics.Color.parseColor(hex)) } catch (_: Exception) { Color.Gray }
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(parsedColor)
                                .border(if (selectedHex == hex) 3.dp else 0.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                .clickable { selectedHex = hex }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onCreateOrg(name, shortName.ifBlank { name.take(2).uppercase() }, sector.ifBlank { "Technology & Services" }, selectedHex)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("create_org_submit_button")
                    ) {
                        Text("Create Company", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddContactDialog(
    organization: Organization,
    onDismiss: () -> Unit,
    onAddContact: (name: String, role: String, branch: String, team: String, phone: String, email: String, status: String, bio: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf(organization.groups.firstOrNull { it.type == GroupType.BRANCH }?.name ?: "HQ Branch") }
    var team by remember { mutableStateOf(organization.groups.firstOrNull { it.type == GroupType.TEAM }?.name ?: "General Team") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Available") }
    var bio by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Add Person to ${organization.name}", style = SangamTypography.headlineSmall, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("FULL NAME") },
                    placeholder = { Text("Vijay Kumar") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_contact_name"),
                    shape = RoundedCornerShape(11.dp)
                )

                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("JOB TITLE / ROLE") },
                    placeholder = { Text("Senior Software Engineer") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_contact_role"),
                    shape = RoundedCornerShape(11.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = branch,
                        onValueChange = { branch = it },
                        label = { Text("BRANCH") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(11.dp)
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("PHONE") },
                        placeholder = { Text("+91 98000 12345") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(11.dp)
                    )
                }

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("SHORT BIO (OPTIONAL)") },
                    placeholder = { Text("Specialist in cloud engineering and product execution.") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(11.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onAddContact(name, role, branch, team, phone, email, status, bio)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("add_contact_submit_button")
                    ) {
                        Text("Add to Contacts", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CompanyProfileDialog(
    organization: Organization,
    onDismiss: () -> Unit,
    onContactFounder: (founderName: String) -> Unit,
    onUpdateCompanyDetails: ((description: String, hq: String, founder: String) -> Unit)? = null,
    onUpdateCompanyImages: ((logoPresetIndex: Int, customLogoUri: String?, customCoverUri: String?) -> Unit)? = null
) {
    var isEditingBio by remember { mutableStateOf(false) }
    var isEditingImages by remember { mutableStateOf(false) }
    var descriptionState by remember { mutableStateOf(organization.description.ifEmpty { "Official enterprise workspace on Sangam platform." }) }
    var hqState by remember { mutableStateOf(organization.headquarters) }
    var founderState by remember { mutableStateOf(organization.founderName) }
    
    var logoUriState by remember { mutableStateOf<String?>(organization.customLogoUri) }
    var coverUriState by remember { mutableStateOf<String?>(organization.customCoverUri) }
    var selectedLogoPreset by remember { mutableStateOf(organization.logoPresetIndex) }

    val logoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                logoUriState = uri.toString()
                onUpdateCompanyImages?.invoke(selectedLogoPreset, uri.toString(), coverUriState)
            }
        }
    )

    val coverPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                coverUriState = uri.toString()
                onUpdateCompanyImages?.invoke(selectedLogoPreset, logoUriState, uri.toString())
            }
        }
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Cover Image or Gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(organization.accentColor, organization.accentColor.copy(alpha = 0.5f))
                            )
                        )
                ) {
                    if (!coverUriState.isNullOrBlank()) {
                        AsyncImage(
                            model = coverUriState,
                            contentDescription = "Company Cover",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButton(
                            onClick = {
                                coverPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.45f))
                                .testTag("upload_cover_image_button")
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Upload Cover Image", tint = Color.White, modifier = Modifier.size(16.dp))
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.45f))
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Badge Overlapping Cover
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .offset(y = (-36).dp)
                                .size(76.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .border(3.5.dp, MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
                                .background(organization.accentColor)
                                .clickable {
                                    logoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                                .testTag("upload_company_logo_badge"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!logoUriState.isNullOrBlank()) {
                                AsyncImage(
                                    model = logoUriState,
                                    contentDescription = organization.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                when (selectedLogoPreset) {
                                    1 -> Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                                    2 -> Icon(Icons.Default.Diamond, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                                    3 -> Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                                    4 -> Icon(Icons.Default.Language, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                                    else -> Text(
                                        text = organization.shortName,
                                        color = Color.White,
                                        style = SangamTypography.headlineMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Camera overlay icon
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(2.dp)
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Change Logo", tint = Color.White, modifier = Modifier.size(12.dp))
                            }
                        }

                        Column(modifier = Modifier.padding(bottom = 6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(organization.name, style = SangamTypography.headlineMedium, fontWeight = FontWeight.Bold)
                                Icon(Icons.Default.Verified, contentDescription = "Verified Company", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                            }
                            Text(organization.sector, style = SangamTypography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // Company Branding & Image Options Action Row
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.Image, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                    Text("COMPANY LOGO & BRANDING", style = SangamTypography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                                TextButton(
                                    onClick = { isEditingImages = !isEditingImages },
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                                ) {
                                    Icon(if (isEditingImages) Icons.Default.Check else Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isEditingImages) "Done" else "Change Logo", fontSize = 12.sp)
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        logoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    modifier = Modifier.weight(1f).testTag("upload_company_logo_button"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Upload Logo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = {
                                        coverPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    modifier = Modifier.weight(1f).testTag("upload_company_cover_button"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Upload Cover", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            if (isEditingImages) {
                                Text("Select Preset Emblem Logo:", style = SangamTypography.labelMedium)
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    val presets = listOf(
                                        0 to "Short Initials",
                                        1 to "Shield",
                                        2 to "Diamond",
                                        3 to "Sparkle",
                                        4 to "Global"
                                    )
                                    presets.forEach { (idx, label) ->
                                        val isSel = selectedLogoPreset == idx
                                        FilterChip(
                                            selected = isSel,
                                            onClick = {
                                                selectedLogoPreset = idx
                                                logoUriState = null // reset to preset
                                                onUpdateCompanyImages?.invoke(idx, null, coverUriState)
                                            },
                                            label = { Text(label, fontSize = 11.sp) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Key Info Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AssistChip(
                            onClick = {},
                            label = { Text("HQ: ${organization.headquarters}") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp)) }
                        )
                        AssistChip(
                            onClick = {},
                            label = { Text("Est. ${organization.foundedYear}") },
                            leadingIcon = { Icon(Icons.Default.Business, contentDescription = null, modifier = Modifier.size(14.dp)) }
                        )
                    }

                    // About / Overview Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("COMPANY OVERVIEW & BIO", style = SangamTypography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                TextButton(
                                    onClick = { isEditingBio = !isEditingBio },
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                                ) {
                                    Icon(if (isEditingBio) Icons.Default.Check else Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isEditingBio) "Cancel" else "Edit Bio", fontSize = 12.sp)
                                }
                            }

                            if (!isEditingBio) {
                                Text(
                                    text = organization.description.ifEmpty { "Official enterprise workspace on Sangam platform." },
                                    style = SangamTypography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            } else {
                                OutlinedTextField(
                                    value = descriptionState,
                                    onValueChange = { descriptionState = it },
                                    label = { Text("COMPANY OVERVIEW / BIO") },
                                    modifier = Modifier.fillMaxWidth().testTag("edit_company_bio_input"),
                                    shape = RoundedCornerShape(10.dp),
                                    minLines = 3
                                )

                                OutlinedTextField(
                                    value = hqState,
                                    onValueChange = { hqState = it },
                                    label = { Text("HEADQUARTERS LOCATION") },
                                    modifier = Modifier.fillMaxWidth().testTag("edit_company_hq_input"),
                                    shape = RoundedCornerShape(10.dp),
                                    singleLine = true
                                )

                                Button(
                                    onClick = {
                                        onUpdateCompanyDetails?.invoke(descriptionState, hqState, founderState)
                                        isEditingBio = false
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("save_company_bio_button"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Save Overview & Bio", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Founder & Leadership Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF3B5B8C)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("TM", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }

                                Column {
                                    Text(organization.founderName, fontWeight = FontWeight.Bold, style = SangamTypography.bodyLarge)
                                    Text("Founder & Owner · ${organization.founderEmail}", style = SangamTypography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            IconButton(
                                onClick = {
                                    onContactFounder(organization.founderName)
                                    onDismiss()
                                },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .size(36.dp)
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = "Message Founder", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    // Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("${organization.totalPeople}", style = SangamTypography.headlineSmall, fontWeight = FontWeight.Bold)
                                Text("Total People", style = SangamTypography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("${organization.groups.size}", style = SangamTypography.headlineSmall, fontWeight = FontWeight.Bold)
                                Text("Communities", style = SangamTypography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("${organization.meetings.size}", style = SangamTypography.headlineSmall, fontWeight = FontWeight.Bold)
                                Text("Meetings", style = SangamTypography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    // Branches List
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("BRANCHES & LOCATIONS", style = SangamTypography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            organization.groups.filter { it.type == GroupType.BRANCH }.take(4).forEach { br ->
                                SuggestionChip(
                                    onClick = {},
                                    label = { Text("${br.name} (${br.membersCount})", fontSize = 11.sp) }
                                )
                            }
                        }
                    }

                    // Teams List
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("ACTIVE TEAMS", style = SangamTypography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            organization.groups.filter { it.type == GroupType.TEAM }.take(4).forEach { tm ->
                                SuggestionChip(
                                    onClick = {},
                                    label = { Text("${tm.name} (${tm.membersCount})", fontSize = 11.sp) }
                                )
                            }
                        }
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth().testTag("close_company_profile_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Close Profile", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveCallDialog(
    recipientName: String,
    isVideo: Boolean,
    onEndCall: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(true) }
    var isVideoOn by remember { mutableStateOf(isVideo) }
    var callSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            callSeconds++
        }
    }

    val minutes = callSeconds / 60
    val seconds = callSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Dialog(onDismissRequest = onEndCall) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1B2D)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.08f)
                ) {
                    Text(
                        text = if (isVideoOn) "● LIVE HD VIDEO CALL" else "● HD VOICE CALL",
                        color = Color(0xFF4CAF50),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFFFC107), Color(0xFFD84315))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val initials = recipientName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                    Text(initials.ifEmpty { "C" }, color = Color.White, style = SangamTypography.headlineMedium, fontWeight = FontWeight.Bold)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(recipientName, style = SangamTypography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(timeFormatted, style = SangamTypography.bodyLarge, color = Color.White.copy(alpha = 0.7f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mute Mic
                    IconButton(
                        onClick = { isMuted = !isMuted },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(if (isMuted) Color.White else Color.White.copy(alpha = 0.15f))
                            .testTag("call_mute_button")
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Mute",
                            tint = if (isMuted) Color.Black else Color.White
                        )
                    }

                    // Toggle Speaker
                    IconButton(
                        onClick = { isSpeakerOn = !isSpeakerOn },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(if (isSpeakerOn) Color.White else Color.White.copy(alpha = 0.15f))
                            .testTag("call_speaker_button")
                    ) {
                        Icon(
                            imageVector = if (isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = "Speaker",
                            tint = if (isSpeakerOn) Color.Black else Color.White
                        )
                    }

                    // Toggle Video
                    IconButton(
                        onClick = { isVideoOn = !isVideoOn },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(if (isVideoOn) Color.White else Color.White.copy(alpha = 0.15f))
                            .testTag("call_video_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isVideoOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                            contentDescription = "Video",
                            tint = if (isVideoOn) Color.Black else Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // End Call Red Button
                Button(
                    onClick = onEndCall,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("call_end_button")
                ) {
                    Icon(Icons.Default.CallEnd, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("End Call", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
