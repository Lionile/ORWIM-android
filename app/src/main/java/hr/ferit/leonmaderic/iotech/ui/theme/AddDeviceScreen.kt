package hr.ferit.leonmaderic.iotech.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.ferit.leonmaderic.iotech.R
import hr.ferit.leonmaderic.iotech.data.Device
import hr.ferit.leonmaderic.iotech.data.DeviceViewModel
import hr.ferit.leonmaderic.iotech.data.RoomViewModel
import hr.ferit.leonmaderic.iotech.ui.DeviceCard
import hr.ferit.leonmaderic.iotech.ui.DeviceList
import hr.ferit.leonmaderic.iotech.ui.RoomTopBar
import hr.ferit.leonmaderic.iotech.ui.TopBar
import java.time.format.TextStyle


@Composable
fun AddDeviceScreen(
    roomViewModel: RoomViewModel,
    deviceViewModel: DeviceViewModel,
    navigation: NavController,
    roomId: Int,
){
    val rooms = roomViewModel.roomsData
    val devices = deviceViewModel.devicesData
    val scrollState = rememberLazyListState()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var deviceSelected: MutableState<Int> = remember { mutableStateOf(0) }
    var title = remember { mutableStateOf(TextFieldValue()) }

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
            RoomTopBar(room = rooms[roomId])
            DeviceSelection(devices = devices, deviceSelected = deviceSelected)
            Text(text = "Device name", modifier = Modifier.padding(start = 16.dp, bottom = 0.dp), fontWeight = FontWeight.Bold)
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
                    var device = Device(devices[deviceSelected.value].image, title.value.text)
                    roomViewModel.addDeviceToRoom(device, rooms[roomId])
                    navigation.popBackStack()
                }) {
                    Text(text = "Submit")
                }
            }
        }
    }
}


@Composable
fun DeviceSelection(
    devices: List<Device>,
    deviceSelected: MutableState<Int>
){
    var deviceListExpanded by remember { mutableStateOf(false)}

    DeviceCard(image = devices[deviceSelected.value].image, name = devices[deviceSelected.value].title) {
        deviceListExpanded = !deviceListExpanded
    }
    if(deviceListExpanded){
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            content = {
                items(devices.size){
                    DeviceCard(image = devices[it].image, name = devices[it].title) {
                        deviceSelected.value = it
                        deviceListExpanded = false
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