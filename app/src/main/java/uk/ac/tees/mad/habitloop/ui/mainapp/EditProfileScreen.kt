package uk.ac.tees.mad.habitloop.ui.mainapp

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import uk.ac.tees.mad.habitloop.R
import uk.ac.tees.mad.habitloop.authentication.viewmodel.AuthViewmodel
import uk.ac.tees.mad.habitloop.ui.theme.poppinsFamily
import java.io.File
import java.io.FileOutputStream
import java.util.UUID



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EditProfileScreen(
    authViewmodel: AuthViewmodel,
    navController: NavHostController
){

    val context = LocalContext.current

    val currentUser by authViewmodel.currentUser.collectAsState()
    var updatedName by remember { mutableStateOf(currentUser?.name ?: "") }
    var showDialog by remember { mutableStateOf(false) }

    //Launching Gallery to select the picture
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri: Uri? ->
        uri?.let {
//            authViewmodel.updateProfileImage(uri)
        }
    }

    // Launcher for taking a picture with the camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
//            authViewmodel.updateProfileImage(bitmapToUri(context, bitmap))
        }
    }

    //Launcher for asking permission to access the camera.
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {isGranted->
            if (isGranted){
                cameraLauncher.launch(null)
            }else{
                Toast.makeText(context, "Camera Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    )

    //Launcher for asking permission to access the gallery.
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {isGranted->
            if (isGranted){
                galleryLauncher.launch("images/*")
            }else{
                Toast.makeText(context, "Gallery Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    )



    if (showDialog){
        ImageSelectionSource(
            onDismiss = { showDialog = false },
            onCameraClick = {
                cameraPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            },
            onGalleryClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    galleryLauncher.launch("image/*")
                } else {
                    galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    0.2f to Color(0xFFF7A6D0),
                    0.4f to Color(0xFF94bbe9),
                    1.0f to Color(0xFFeeaeca)
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Spacer(modifier = Modifier.weight(3f))
        GlideImage(
            modifier = Modifier
                .height(300.dp)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .border(
                    1.dp,
                    Color.DarkGray,
                    shape = CircleShape
                )
                .padding(1.dp)
                .clip(CircleShape),
            model = currentUser?.profilePictureUrl,
            contentDescription = "",
            failure = placeholder(painter = painterResource(R.drawable.avatar))
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.DarkGray,
                containerColor = Color.Transparent
            ),
            border = BorderStroke(1.dp, Color.Black),
            onClick = {
                showDialog = true
            }
        ) {
            Text(
                text = "Update Profile Picture",
                fontFamily = poppinsFamily,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.weight(1F))

        Text(
            text = "Edit Account Details",
            fontFamily = poppinsFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier.fillMaxWidth(0.85f),
            text = "Name",
            fontFamily = poppinsFamily,
            fontSize = 18.sp
        )
        Card(
            elevation = CardDefaults.elevatedCardElevation(10.dp)
        ){
            OutlinedTextField(
                value = updatedName,
                onValueChange = {
                    updatedName = it
                },
                modifier = Modifier.fillMaxWidth(0.88f),
                shape = RoundedCornerShape(15.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Name"
                    )
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.DarkGray,
                containerColor = Color.Transparent
            ),
            border = BorderStroke(1.dp, Color.Black),
            onClick = {
//                authViewmodel.updateNameAndUsername(updatedName, updatedUsername)
                navController.popBackStack()
            }
        ) {
            Text(
                text = "Update Profile",
                fontFamily = poppinsFamily,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.weight(10f))

    }
}

@Composable
fun ImageSelectionSource(
    onDismiss: ()->Unit,
    onCameraClick: ()->Unit,
    onGalleryClick: ()->Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Choose Image Source")
        },
        text = {
            Column {
                Button(
                    onClick = {
                        onCameraClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Take a Photo",
                        fontSize = 13.sp,
                        fontFamily = poppinsFamily
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onGalleryClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Choose from Gallery",
                        fontFamily = poppinsFamily,
                        fontSize = 13.sp
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}



fun bitmapToUri(context: Context, bt: Bitmap): Uri {
    val image = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
    val outStream = FileOutputStream(image)
    bt.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
    outStream.flush()
    outStream.close()
    return Uri.fromFile(image)
}
