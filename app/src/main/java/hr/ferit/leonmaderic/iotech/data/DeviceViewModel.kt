package hr.ferit.leonmaderic.iotech.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import hr.ferit.leonmaderic.iotech.ui.theme.getFileNameFromUri
import java.io.ByteArrayOutputStream

class DeviceViewModel: ViewModel() {
    val db = Firebase.firestore
    val devicesData = mutableStateListOf<Device>()
    val storage = Firebase.storage

    init {
        fetchDatabaseData()
    }
    private fun fetchDatabaseData() {
        var newDevicesData = mutableStateListOf<Device>()

        db.collection("devices")
            .get()
            .addOnSuccessListener { result ->
                for (data in result.documents) {
                    val device = data.toObject(Device::class.java)
                    if (device != null) {
                        newDevicesData.add(device)
                        Log.d("FIRESTORE", "device added to local list, device count: ${newDevicesData.size}")
                    }
                }
                devicesData.clear()
                devicesData.addAll(newDevicesData)
            }
            .addOnFailureListener {
                Log.d("FIRESTORE", "Error getting device documents.")
            }
    }



    fun updateData(){
        fetchDatabaseData()
    }
}