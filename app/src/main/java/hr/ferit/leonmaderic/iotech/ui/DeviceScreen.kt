package hr.ferit.leonmaderic.iotech.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import hr.ferit.leonmaderic.iotech.R
import hr.ferit.leonmaderic.iotech.Routes
import hr.ferit.leonmaderic.iotech.data.RoomViewModel


@Composable
fun DeviceScreen(
    viewModel: RoomViewModel,
    navigation: NavController,
    roomId: Int,
    deviceId: Int,
){
    val scrollState = rememberLazyListState()
    var showDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    var context = LocalContext.current
    var rooms = viewModel.roomsData
    var devices = rooms[roomId].devices
    var device = devices[deviceId]

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
                Row(horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    IconButton(onClick = { showDialog.value = true }
                    ) {
                        Icon(
                            painterResource(id = R.drawable.delete),
                            contentDescription = "Delete device"
                        )
                    }
                }
                DeviceCard(image = device.image, name = device.title){}
            }
        }
        if (showDialog.value) {
            ConfirmationDialog(
                title = "Delete device",
                message = "Are you sure you want to delete this device?",
                onConfirm = {
                    navigation.popBackStack()
                    rooms[roomId].devices.removeAt(deviceId)
                    viewModel.updateRoom(rooms[roomId])
                },
                onDismiss = {
                    showDialog.value = false
                }
            )
        }
    }
}