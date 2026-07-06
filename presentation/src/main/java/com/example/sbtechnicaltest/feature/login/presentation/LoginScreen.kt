package com.example.sbtechnicaltest.feature.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sbtechnicaltest.presentation.R
import com.example.sbtechnicaltest.design.SBTechnicalTestTheme
import com.example.sbtechnicaltest.design.StudentBeansAccent
import com.example.sbtechnicaltest.design.StudentBeansBackground
import com.example.sbtechnicaltest.design.StudentBeansHintText
import com.example.sbtechnicaltest.design.StudentBeansPrimaryText
import com.example.sbtechnicaltest.design.StudentBeansSecondaryText
import com.example.sbtechnicaltest.design.StudentBeansSurface

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = StudentBeansBackground,
    ) { innerPadding ->
        LoginContent(
            uiState = uiState,
            onUsernameChanged = onUsernameChanged,
            onPasswordChanged = onPasswordChanged,
            onLoginClicked = onLoginClicked,
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    contentPadding: PaddingValues,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .imePadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            item {
                Column {
                    Spacer(modifier = Modifier.height(80.dp))

                    Text(
                        text = stringResource(R.string.login_title),
                        color = StudentBeansPrimaryText,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 32.sp,
                    )

                    Text(
                        text = stringResource(R.string.login_subtitle),
                        color = StudentBeansSecondaryText,
                        fontSize = 17.sp,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(top = 8.dp),
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    LoginTextField(
                        value = uiState.username,
                        onValueChange = onUsernameChanged,
                        placeholder = stringResource(R.string.login_email_label),
                        isError = uiState.usernameErrorResId != null,
                        errorMessage = uiState.usernameErrorResId?.let { stringResource(it) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                        ),
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    LoginTextField(
                        value = uiState.password,
                        onValueChange = onPasswordChanged,
                        placeholder = stringResource(R.string.login_password_label),
                        isError = uiState.passwordErrorResId != null,
                        errorMessage = uiState.passwordErrorResId?.let { stringResource(it) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { onLoginClicked() },
                        ),
                    )
                }
            }

            item {
                Button(
                    onClick = onLoginClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StudentBeansAccent,
                        contentColor = Color.White,
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.login_button),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean,
    errorMessage: String?,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        placeholder = {
            Text(
                text = placeholder,
                color = StudentBeansHintText,
                fontSize = 18.sp,
            )
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = StudentBeansPrimaryText,
            fontSize = 18.sp,
            lineHeight = 22.sp,
        ),
        singleLine = true,
        isError = isError,
        supportingText = errorMessage?.let { error ->
            {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = StudentBeansSurface,
            unfocusedContainerColor = StudentBeansSurface,
            errorContainerColor = StudentBeansSurface,
            focusedBorderColor = StudentBeansAccent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = StudentBeansAccent,
            focusedPlaceholderColor = StudentBeansHintText,
            unfocusedPlaceholderColor = StudentBeansHintText,
        ),
    )
}

@Preview(
    name = "Login - Default",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun LoginScreenPreview() {
    LoginScreenPreviewContent(uiState = LoginUiState())
}

@Preview(
    name = "Login - Validation Errors",
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun LoginScreenValidationErrorsPreview() {
    LoginScreenPreviewContent(
        uiState = LoginUiState(
            usernameErrorResId = R.string.login_username_or_email_required_error,
            passwordErrorResId = R.string.login_password_required_error,
        ),
    )
}

@Composable
private fun LoginScreenPreviewContent(
    uiState: LoginUiState,
) {
    SBTechnicalTestTheme {
        LoginScreen(
            uiState = uiState,
            onUsernameChanged = {},
            onPasswordChanged = {},
            onLoginClicked = {},
        )
    }
}
