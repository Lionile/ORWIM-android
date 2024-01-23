package hr.ferit.leonmaderic.iotech

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import hr.ferit.leonmaderic.iotech.data.DeviceViewModel
import hr.ferit.leonmaderic.iotech.data.RoomViewModel
import hr.ferit.leonmaderic.iotech.ui.HomeScreen
import hr.ferit.leonmaderic.iotech.ui.theme.AddRoomScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val roomViewModel by viewModels<RoomViewModel>()
        val deviceViewModel by viewModels<DeviceViewModel>()
        setContent {
            NavigationController(roomViewModel = roomViewModel, deviceViewModel = deviceViewModel)
        }
    }
}