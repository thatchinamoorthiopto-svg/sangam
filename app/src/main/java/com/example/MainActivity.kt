package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.SangamViewModel
import com.example.ui.components.LoginView
import com.example.ui.screens.MainAppShell
import com.example.ui.theme.SangamTheme

class MainActivity : ComponentActivity() {

    private val viewModel: SangamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val currentOrg = viewModel.currentOrg

            SangamTheme(
                darkTheme = uiState.isDarkTheme,
                accentColor = currentOrg?.accentColor ?: com.example.ui.theme.AccentKaveri
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (!uiState.isLoggedIn) {
                        LoginView(
                            onLoginSuccess = { email, name -> viewModel.login(email, name) },
                            onDemoLogin = { viewModel.loginAsDemo() }
                        )
                    } else {
                        MainAppShell(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
