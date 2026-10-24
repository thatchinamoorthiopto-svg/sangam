package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Organization
import com.example.data.SangamRepository
import com.example.ui.theme.SangamTypography

@Composable
fun LoginView(
    onLoginSuccess: (email: String, name: String) -> Unit,
    onDemoLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("thatchinamoorthiopto@gmail.com") }
    var password by remember { mutableStateOf("demo1234") }
    var name by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val sampleOrgs = remember { SangamRepository.createInitialOrganizations() }
    val configuration = LocalConfiguration.current
    val isWide = configuration.screenWidthDp >= 600

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 1000.dp)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isWide) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BrandSection(
                        sampleOrgs = sampleOrgs,
                        modifier = Modifier.weight(1f)
                    )
                    LoginFormCard(
                        isSignUp = isSignUp,
                        onSignUpChange = { isSignUp = it },
                        email = email,
                        onEmailChange = { email = it },
                        password = password,
                        onPasswordChange = { password = it },
                        name = name,
                        onNameChange = { name = it },
                        errorMessage = errorMessage,
                        onErrorMessageChange = { errorMessage = it },
                        onLoginSuccess = onLoginSuccess,
                        onDemoLogin = onDemoLogin,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BrandSection(
                        sampleOrgs = sampleOrgs,
                        modifier = Modifier.fillMaxWidth()
                    )
                    LoginFormCard(
                        isSignUp = isSignUp,
                        onSignUpChange = { isSignUp = it },
                        email = email,
                        onEmailChange = { email = it },
                        password = password,
                        onPasswordChange = { password = it },
                        name = name,
                        onNameChange = { name = it },
                        errorMessage = errorMessage,
                        onErrorMessageChange = { errorMessage = it },
                        onLoginSuccess = onLoginSuccess,
                        onDemoLogin = onDemoLogin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 480.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BrandSection(
    sampleOrgs: List<Organization>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "S",
                    color = Color.White,
                    style = SangamTypography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Sangam",
                style = SangamTypography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "PROTOTYPE",
                    style = SangamTypography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = "Every branch and team, one message away.",
            style = SangamTypography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "A company chat built for owners, managers and team leads. Send to everyone, one city or one team, and see who has read it.",
            style = SangamTypography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Organization Chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            sampleOrgs.take(3).forEach { org ->
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(org.accentColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = org.shortName,
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = org.name,
                            style = SangamTypography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BulletPoint("Send to everyone, one branch or one team")
            BulletPoint("See who has read and confirmed each message")
            BulletPoint("Status updates and meetings in the same place")
        }
    }
}

@Composable
private fun LoginFormCard(
    isSignUp: Boolean,
    onSignUpChange: (Boolean) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    errorMessage: String,
    onErrorMessageChange: (String) -> Unit,
    onLoginSuccess: (email: String, name: String) -> Unit,
    onDemoLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(vertical = 8.dp)
            .shadow(12.dp, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Segmented control
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (!isSignUp) MaterialTheme.colorScheme.surface else Color.Transparent)
                            .clickable {
                                onSignUpChange(false)
                                onErrorMessageChange("")
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sign in",
                            fontWeight = FontWeight.Bold,
                            style = SangamTypography.labelLarge,
                            color = if (!isSignUp) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (isSignUp) MaterialTheme.colorScheme.surface else Color.Transparent)
                            .clickable {
                                onSignUpChange(true)
                                onErrorMessageChange("")
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Create account",
                            fontWeight = FontWeight.Bold,
                            style = SangamTypography.labelLarge,
                            color = if (isSignUp) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Text(
                text = if (isSignUp) "Create your account" else "Welcome back",
                style = SangamTypography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            AnimatedVisibility(visible = isSignUp) {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("YOUR NAME") },
                    placeholder = { Text("Thatchina Moorthi") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_name_input"),
                    shape = RoundedCornerShape(11.dp)
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("EMAIL") },
                leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_email_input"),
                shape = RoundedCornerShape(11.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("PASSWORD") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_password_input"),
                shape = RoundedCornerShape(11.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = SangamTypography.bodySmall
                )
            }

            Button(
                onClick = {
                    if (email.isBlank() || password.length < 4) {
                        onErrorMessageChange("Please enter valid email and password.")
                    } else {
                        onLoginSuccess(email, name)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("login_submit_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (isSignUp) "Create account and continue" else "Sign in",
                    fontWeight = FontWeight.Bold,
                    style = SangamTypography.labelLarge
                )
            }

            if (!isSignUp) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Demo account",
                            fontWeight = FontWeight.Bold,
                            style = SangamTypography.labelLarge
                        )
                        Text(
                            text = "thatchinamoorthiopto@gmail.com · demo1234",
                            style = SangamTypography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(
                            onClick = onDemoLogin,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("demo_login_button")
                        ) {
                            Text("Sign in as Thatchina", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BulletPoint(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color(0xFF17824F),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text,
            style = SangamTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

