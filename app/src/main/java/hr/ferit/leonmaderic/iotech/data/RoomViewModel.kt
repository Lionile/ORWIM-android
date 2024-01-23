package hr.ferit.leonmaderic.iotech.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import hr.ferit.leonmaderic.iotech.ui.theme.getFileNameFromUri
import java.io.ByteArrayOutputStream

class RoomViewModel: ViewModel(){
    val db = Firebase.firestore
    val roomsData = mutableStateListOf<Room>()
    val storage = Firebase.storage

    init {
        fetchDatabaseData()
    }
    private fun fetchDatabaseData() {
        var newRoomsData = mutableStateListOf<Room>()

        db.collection("rooms")
            .get()
            .addOnSuccessListener { result ->
                for (data in result.documents) {
                    val room = data.toObject(Room::class.java)
                    if (room != null) {
                        newRoomsData.add(room)
                        Log.d("FIRESTORE", "room added to local list, room count: ${newRoomsData.size}")
                    }
                }
                roomsData.clear()
                roomsData.addAll(newRoomsData)
            }
            .addOnFailureListener {
                Log.d("FIRESTORE", "Error getting room documents.")
            }
    }



    fun updateData(){
        fetchDatabaseData()
    }



    fun updateRoom(room: Room){
        db.collection("rooms")
            .document(room.id)
            .set(room)
    }



    fun addDeviceToRoom(device: Device, room: Room){
        var devices = room.devices
        devices.add(device)
        val documentRef = db.collection("rooms").document(room.id);
        documentRef.update("devices", devices)
            .addOnSuccessListener {
                Log.d("FIRESTORE", "Added a device to room: ${room.title}")
                this.updateData()
            }
    }



    fun addRoom(context: Context, imageUri: Uri?, title: String, devices: List<Device> = listOf()){
        var bitmap: Bitmap? = null
        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap = ImageDecoder.decodeBitmap(source)
                Log.d(
                    "Add room",
                    "Image filepath: ${getFileNameFromUri(context, uri = it)}"
                )
            }
        }

        val storageRef = storage.reference
        val imageFileRef = storageRef.child("images/${imageUri?.let { getFileNameFromUri(context,uri = it) }}")
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()
        var downloadUrl: String = ""

        var uploadTask = imageFileRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { taskSnapshot ->
            imageFileRef.downloadUrl.addOnSuccessListener { uri ->
                downloadUrl = uri.toString()

                val room = Room("",
                    downloadUrl,
                    title,
                    mutableListOf()
                )

                val docReference = db.collection("rooms").document()

                room.id = docReference.id

                docReference.set(room)
                    .addOnSuccessListener {
                        Log.d("FIRESTORE", "DocumentSnapshot written with ID: ${docReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("FIRESTORE", "Error adding document", e)
                    }
                fetchDatabaseData()
            }.addOnFailureListener { exception ->
                Log.e("Download URL", "Error getting download URL", exception)
            }
        }
    }



    fun deleteRoom(context: Context, room: Room){

        db.collection("rooms").document(room.id.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Room deleted", Toast.LENGTH_LONG).show()
                Log.d("FIREBASE", "Room document ${room.id} successfully deleted!")


                // If room is deleted, also delete the image that was used for it
                val storageRef = storage.reference

                val imageRef: StorageReference = storage.getReferenceFromUrl(room.image)

                imageRef.delete().addOnSuccessListener {
                    Log.d("FIREBASE", "Image from room document ${room.id} successfully deleted!")
                }.addOnSuccessListener {
                    Log.d("FIREBASE", "Error deleting image from room document ${room.id}")
                }

            }
            .addOnFailureListener{
                    e -> Log.w("FIREBASE", "Error deleting room document ${room.id}", e)
            }

        this.updateData()
    }
}