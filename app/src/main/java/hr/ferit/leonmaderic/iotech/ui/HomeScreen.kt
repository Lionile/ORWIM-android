package hr.ferit.leonmaderic.iotech.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import hr.ferit.leonmaderic.iotech.R
import hr.ferit.leonmaderic.iotech.Routes
import hr.ferit.leonmaderic.iotech.data.Room
import hr.ferit.leonmaderic.iotech.data.RoomViewModel


@Composable
fun HomeScreen(
    viewModel: RoomViewModel,
    navigation: NavController,
){
    val scrollState = rememberLazyListState()
    var rooms = viewModel.roomsData
    Column {
        TopBar(R.drawable.hamburger_menu, navigation)
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .background(color = DarkGray)
        ){
            item{
                RoomList(rooms, navigation)
            }
        }
    }
}



@Composable
fun TopBar(
    @DrawableRes rightButton: Int,
    navigation: NavController,
    onClick: () -> Unit = {},
) {
    var elevation: ButtonElevation? = ButtonDefaults.buttonElevation(defaultElevation = 12.dp)
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Gray)
    ){
        Text(
            text = "IoTech",
            style = TextStyle(color = Green, fontSize = 24.sp),
            fontWeight = FontWeight.Bold
        )
        Button(
            contentPadding = PaddingValues(),
            elevation = elevation,
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(containerColor = White,
                contentColor = Gray),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .width(38.dp)
                .height(38.dp)
        ) {
            Icon(
                painter = painterResource(id = rightButton),
                contentDescription = null,
            )
        }
    }
}


@Composable
fun RoomList(
    rooms: List<Room>,
    navigation: NavController,
){
    Column{
        Row(
            modifier = Modifier
                .padding(5.dp)
                .padding(bottom = 0.dp)
        ){
            IconButton(onClick = { navigation.navigate(Routes.SCREEN_ADD_ROOM) }
            ) {
                Icon(
                    painterResource(id = R.drawable.add),
                    contentDescription = "Add room"
                )
            }
            Text(text = "Rooms", fontWeight = FontWeight.Bold, fontSize = 30.sp)
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            content = {
                items(rooms.size){
                    RoomCard(image = rooms[it].image, name = rooms[it].title){
                        navigation.navigate(Routes.getRoomDetailsPath(it))
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
fun RoomCard(
    image: String,
    name: String,
    onClick: () -> Unit,
){
    Box(
        modifier = Modifier
            .background(DarkGray)
            .height(220.dp)
    ){

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Gray),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable {
                    onClick()
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
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
                        .padding(bottom = 10.dp)
                        .weight(1f)
                )
            }

        }
    }
}