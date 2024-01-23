package hr.ferit.leonmaderic.iotech

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hr.ferit.leonmaderic.iotech.data.DeviceViewModel
import hr.ferit.leonmaderic.iotech.data.RoomViewModel
import hr.ferit.leonmaderic.iotech.ui.HomeScreen
import hr.ferit.leonmaderic.iotech.ui.RoomScreen
import hr.ferit.leonmaderic.iotech.ui.theme.AddDeviceScreen
import hr.ferit.leonmaderic.iotech.ui.theme.AddRoomScreen

object Routes {
    const val SCREEN_ALL_ROOMS = "rooms"
    const val SCREEN_ROOM_DETAILS = "roomDetails/{roomId}"
    const val SCREEN_ADD_ROOM = "addRoom"
    const val SCREEN_ADD_DEVICE = "addDevice/{roomId}"
    fun getRoomDetailsPath(roomId: Any?) : String {
        if (roomId != null && roomId != -1) {
            return "roomDetails/$roomId"
        }
        return "roomDetails/0"
    }

    fun getAddDevicePath(roomId: Int?) : String {
        if (roomId != null && roomId != -1) {
            return "addDevice/$roomId"
        }
        return "addDevice/0"
    }
}


@Composable
fun NavigationController(
    roomViewModel: RoomViewModel,
    deviceViewModel: DeviceViewModel,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination =
    Routes.SCREEN_ALL_ROOMS) {
        composable(Routes.SCREEN_ALL_ROOMS) {
            HomeScreen(
                viewModel = roomViewModel,
                navigation = navController
            )
        }
        composable(
            Routes.SCREEN_ROOM_DETAILS,
            arguments = listOf(
                navArgument("roomId") {
                    type = NavType.IntType
                }
            )
        ) {backStackEntry ->
            backStackEntry.arguments?.getInt("roomId")?.let {
                RoomScreen(
                    viewModel = roomViewModel,
                    navigation = navController,
                    roomId = it
                )
            }
        }
        composable(Routes.SCREEN_ADD_ROOM) {
            AddRoomScreen(
                viewModel = roomViewModel,
                navigation = navController,
            )
        }
        composable(
            Routes.SCREEN_ADD_DEVICE,
            arguments = listOf(
                navArgument("roomId") {
                    type = NavType.IntType
                }
            )
        ) {backStackEntry ->
            backStackEntry.arguments?.getInt("roomId")?.let {
                AddDeviceScreen(
                    roomViewModel = roomViewModel,
                    deviceViewModel = deviceViewModel,
                    navigation = navController,
                    roomId = it
                )
            }
        }
    }
}