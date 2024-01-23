package hr.ferit.leonmaderic.iotech.data

import androidx.annotation.DrawableRes
import com.google.firebase.firestore.PropertyName
import java.text.DecimalFormat

data class Room(
    var id: String = "",
    val image: String = "",
    val title: String = "",
    val devices: MutableList<Device> = mutableListOf(),
)