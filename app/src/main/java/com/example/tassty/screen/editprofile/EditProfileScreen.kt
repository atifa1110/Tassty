package com.example.tassty.screen.editprofile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tassty.R
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CommonImage
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmailSection
import com.example.tassty.component.TextSection
import com.example.tassty.component.TextTransformationSection
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Orange500
import com.canhub.cropper.*
import com.example.tassty.component.CustomTwoColorText
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.LoadingOverlay
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.Transformations

@Composable
fun EditProfileScreen(
    onNavigateBack:() -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event->
            when(event){
                is EditProfileEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is EditProfileEvent.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    val cropImageLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            val croppedUri = result.uriContent
            viewModel.onImageSelected(croppedUri)
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri ?: return@rememberLauncherForActivityResult
            cropImageLauncher.launch(
                CropImageContractOptions(
                    uri = uri,
                    cropImageOptions = CropImageOptions(
                        guidelines = CropImageView.Guidelines.ON,
                        aspectRatioX = 1,
                        aspectRatioY = 1,
                        fixAspectRatio = true,
                        cropShape = CropImageView.CropShape.OVAL,
                        showIntentChooser = false,
                        activityTitle = "Crop Photo",
                        activityMenuIconColor = android.graphics.Color.WHITE,
                        activityMenuTextColor = android.graphics.Color.WHITE,
                        activityBackgroundColor = android.graphics.Color.BLACK,
                        toolbarColor = android.graphics.Color.BLACK,
                        toolbarBackButtonColor = android.graphics.Color.WHITE
                    )
                )
            )
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        EditProfileContent(
            uiState = uiState,
            onNameChanged = viewModel::onNameChange,
            onPhoneChanged = viewModel::onPhoneChange,
            onSaveChanged = viewModel::saveProfile,
            onEditPhotoClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onNavigateBack = onNavigateBack
        )

        LoadingOverlay(
            isLoading = uiState.isLoading,
            text = "Securely processing..."
        )
    }
}

@Composable
fun EditProfileContent(
    uiState: EditProfileUiState,
    onNameChanged:(String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onSaveChanged: () -> Unit,
    onEditPhotoClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold (
        containerColor = LocalCustomColors.current.background,
        topBar = {
            BackTopAppBar(onBackClick = onNavigateBack)
        },
        bottomBar = {
            Column(Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ButtonComponent(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true,
                    labelResId = R.string.save,
                    onClick = onSaveChanged
                )
            }
        }
    ){ paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            item(key = "header_section"){
                EditProfileHeader(
                    imageUrl = uiState.profileImage,
                    selectedLocalUri = uiState.selectedLocalUri,
                    onEditPhotoClick = onEditPhotoClick
                )
                Divider32()
            }
            item(key = "text_section"){
                Column (
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    TextSection(
                        label = "Full name",
                        leadingIcon = R.drawable.person,
                        text = uiState.name,
                        textError = "",
                        onTextChanged = onNameChanged
                    )

                    TextTransformationSection(
                        label = "Phone",
                        leadingIcon = R.drawable.phone,
                        placeholder = "Enter your phone number",
                        text = uiState.phone,
                        visualTransformation = Transformations.PhoneNumberTransformation(),
                        onTextChanged = onPhoneChanged
                    )

                    EmailSection(
                        enabled = false,
                        email = uiState.email,
                        emailError = "",
                        onEmailChanged = {}
                    )
                }
            }
        }
    }
}

@Composable
fun EditProfileHeader(
    modifier: Modifier = Modifier,
    imageUrl: String,
    selectedLocalUri: Uri?,
    onEditPhotoClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderTitleScreen(
            modifier = modifier.weight(1f),
            title = "Edit your \nprofile."
        )
        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            CommonImage(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape),
                imageUrl = selectedLocalUri?: imageUrl,
                name = "Profile Picture"
            )

            Surface(
                onClick = { onEditPhotoClick() },
                modifier = Modifier
                    .clip(CircleShape)
                    .size(32.dp),
                color = Orange500,
                tonalElevation = 2.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Edit Profile Picture",
                        tint = Neutral10,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun EditProfileLightPreview(){
//    TasstyTheme {
//        EditProfileContent(
//            uiState = EditProfileUiState(
//                name = "Atifa Fiorenza",
//                phone = "087878601919",
//                email = "atifafiorenza24@gmail.com"
//            ),
//            onPhoneChanged = {},
//            onNameChanged = {},
//            onSaveChanged = {},
//            onEditPhotoClick = {},
//            onNavigateBack = {}
//        )
//    }
//}

//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun EditProfileDarkPreview(){
//    TasstyTheme(darkTheme = true) {
//        EditProfileContent(
//            uiState = EditProfileUiState(
//                name = "Atifa Fiorenza",
//                phone = "087878601919",
//                email = "atifafiorenza24@gmail.com"
//            ),
//            onPhoneChanged = {},
//            onNameChanged = {},
//            onSaveChanged = {},
//            onEditPhotoClick = {},
//            onNavigateBack = {}
//        )
//    }
//}