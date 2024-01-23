package hr.ferit.leonmaderic.iotech.ui.theme

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.ferit.leonmaderic.iotech.R
import hr.ferit.leonmaderic.iotech.data.Room
import hr.ferit.leonmaderic.iotech.data.RoomViewModel
import hr.ferit.leonmaderic.iotech.ui.TopBar
import java.io.ByteArrayOutputStream
import java.io.File

@Composable
fun AddRoomScreen(
    viewModel: RoomViewModel,
    navigation: NavController,
){
    var imageUri: MutableState<Uri?> = remember { mutableStateOf<Uri?>(null) }
    var title = remember { mutableStateOf(TextFieldValue()) }
    val scrollState = rememberLazyListState()
    val context = LocalContext.current

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        state = scrollState,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
    ){
        item{
            TopBar(R.drawable.back_arrow, navigation){
                navigation.popBackStack()
            }
            PickImageFromGallery(imageUri)

            Text(text = "Title", modifier = Modifier.padding(start = 16.dp, bottom = 0.dp), fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = title.value,
                onValueChange = {
                    title.value = it
                },
                label = { Text("Enter text") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally,
               modifier = Modifier
                   .fillMaxWidth()
            ){
                Button(onClick = {
                    viewModel.addRoom(context, imageUri.value, title.value.text, listOf())
                    navigation.popBackStack()
                }) {
                    Text(text = "Submit")
                }
            }
        }
    }
}




@Composable
fun PickImageFromGallery(
    imageUri: MutableState<Uri?> = remember { mutableStateOf<Uri?>(null) },
    bitmap: MutableState<Bitmap?> = remember { mutableStateOf<Bitmap?>(null) }
) {
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri.value = uri
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        imageUri.value?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
                Log.d("Add room", "Image filepath: ${getFileNameFromUri(LocalContext.current,uri = it)}")
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxSize()
                        .padding(20.dp)
                )
            }
        }?: run {// If no Image is chosen, display basic empty room
            Image(
                painterResource(id = R.drawable.empty_room),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxSize()
                    .padding(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Pick Image")
        }
    }

}


fun getFileNameFromUri(context: Context, uri: Uri): String? {
    val contentResolver: ContentResolver = context.contentResolver

    // Query to get the display name (filename) of the file
    val cursor = contentResolver.query(uri, null, null, null, null, null)

    cursor?.use {
        if (it.moveToFirst()) {
            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1) {
                var filename = it.getString(displayNameIndex) // full filename

                // cut off file extention
                val file = File(filename)
                val dotIndex = file.name.lastIndexOf('.')

                // Check if a dot was found and if it's not the first character in the name
                return if (dotIndex > 0) {
                    file.name.substring(0, dotIndex)
                } else {
                    // No extension or dot is the first character
                    file.name
                }
            }
        }
    }

    return null
}