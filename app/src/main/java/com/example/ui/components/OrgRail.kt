package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Organization
import com.example.data.UserProfile
import com.example.ui.theme.SangamTypography

@Composable
fun OrgRail(
    organizations: List<Organization>,
    selectedOrgIndex: Int,
    user: UserProfile,
    onSelectOrg: (Int) -> Unit,
    onAccountClick: () -> Unit,
    onCreateOrgClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.width(76.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // App Logo Badge
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFC107)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "S",
                        color = Color(0xFF10203A),
                        style = SangamTypography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Org Items
                organizations.forEachIndexed { index, org ->
                    val isSelected = index == selectedOrgIndex
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.08f else 0.95f,
                        animationSpec = tween(durationMillis = 200),
                        label = "org_scale"
                    )
                    val indicatorHeight by animateDpAsState(
                        targetValue = if (isSelected) 28.dp else 0.dp,
                        animationSpec = tween(durationMillis = 200),
                        label = "indicator_height"
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Left active pill indicator
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(indicatorHeight)
                                .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .scale(scale)
                                .size(46.dp)
                                .clip(if (isSelected) CircleShape else RoundedCornerShape(15.dp))
                                .background(org.accentColor)
                                .clickable { onSelectOrg(index) }
                                .testTag("org_badge_$index"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!org.customLogoUri.isNullOrBlank()) {
                                coil.compose.AsyncImage(
                                    model = org.customLogoUri,
                                    contentDescription = org.name,
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Text(
                                    text = org.shortName,
                                    color = Color.White,
                                    style = SangamTypography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Create Organization Button
                if (onCreateOrgClick != null) {
                    IconButton(
                        onClick = onCreateOrgClick,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .testTag("create_org_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Company",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // User Profile Avatar
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3B5B8C))
                    .clickable { onAccountClick() }
                    .testTag("user_avatar_button"),
                contentAlignment = Alignment.Center
            ) {
                val initials = user.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                Text(
                    text = initials.ifEmpty { "TM" },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
