package hr.ferit.leonmaderic.iotech.ui

import android.app.AlertDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import hr.ferit.leonmaderic.iotech.R
import hr.ferit.leonmaderic.iotech.Routes
import hr.ferit.leonmaderic.iotech.data.Device
import hr.ferit.leonmaderic.iotech.data.Room
import hr.ferit.leonmaderic.iotech.data.RoomViewModel

@Composable
fun RoomScreen(
    viewModel: RoomViewModel,
    navigation: NavController,
    roomId: Int,
){
    val rooms = viewModel.roomsData
    val scrollState = rememberLazyListState()
    var showDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    var context = LocalContext.current

    Column {
        TopBar(R.drawable.back_arrow, navigation){
            navigation.popBackStack()
        }
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.DarkGray)
        ){
            item{
                RoomTopBar(room = rooms[roomId], showDialog)
                DeviceList(devices = rooms[roomId].devices, navigation = navigation, roomId = roomId){clickedIndex ->
                    navigation.navigate(Routes.getDeviceDetailsPath(roomId, clickedIndex))
                }
            }
        }
    }
    if (showDialog.value) {
        ConfirmationDialog(
            title = "Delete Room",
            message = "Are you sure you want to delete this room?",
            onConfirm = {
                navigation.popBackStack()
                viewModel.deleteRoom(context, rooms[roomId])
            },
            onDismiss = {
                showDialog.value = false
            }
        )
    }
}



@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss.invoke() },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Warning, contentDescription = "Warning")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title)
            }
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm.invoke()
                    onDismiss.invoke()
                }
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss.invoke() }
            ) {
                Text(text = "Cancel")
            }
        }
    )
}



@Composable
fun RoomTopBar(
    room: Room,
    showDialog: MutableState<Boolean>
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
    ){
        Image(painter = rememberAsyncImagePainter(model = room.image),
            contentDescription = "room image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Gray),
                modifier = Modifier.padding(5.dp)
            ){
                Text(text = room.title, fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(3.dp))
            }

        }
        Row(horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
        ){
            IconButton(onClick = { showDialog.value = true }
            ) {
                Icon(
                    painterResource(id = R.drawable.delete),
                    contentDescription = "Delete room"
                )
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.DarkGray
                    ),
                    startY = 380f,
                    endY = 500f
                )
            )
        )
    }
}



@Composable
fun RoomTopBar(room: Room){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
    ){
        Image(painter = rememberAsyncImagePainter(model = room.image),
            contentDescription = "living room image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Gray),
                modifier = Modifier.padding(5.dp)
            ){
                Text(text = room.title, fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(3.dp))
            }

        }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.DarkGray
                    ),
                    startY = 380f,
                    endY = 500f
                )
            )
        )
    }
}



@Composable
fun DeviceList(
    devices: List<Device>,
    navigation: NavController,
    roomId: Int,
    onClick: (Int) -> Unit,
){
    Column{
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(5.dp)
                .padding(bottom = 0.dp)
        ){
            IconButton(onClick = { navigation.navigate(Routes.getAddDevicePath(roomId)) }
            ) {
                Icon(
                    painterResource(id = R.drawable.add),
                    contentDescription = "Add device"
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom
            ){
                Text(text = "Devices", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                Text(text = "(${devices.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            content = {
                items(devices.size){
                    DeviceCard(image = devices[it].image, name = devices[it].title) {
                        onClick(it)
                    }
                }
            },
            modifier = Modifier
                .heightIn(max = 10000.dp) // to remove possible infinite constraint
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        )
    }
}



@Composable
fun DeviceCard(
    image: String,
    name: String,
    onClick: () -> Unit,
){
    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .height(150.dp)
            .fillMaxSize()
    ){

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Gray),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxSize()
                .clickable {
                    onClick()
                },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ){
                Image(
                    painter = rememberAsyncImagePainter(model = image),
                    contentDescription = name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .weight(5f)
                )
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(2f),
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}