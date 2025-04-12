package com.furkansafran.shoppinglist.screens


import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.room.util.TableInfo
import coil.compose.rememberAsyncImagePainter
import com.furkansafran.shoppinglist.R

import com.furkansafran.shoppinglist.model.Item
import java.io.ByteArrayOutputStream
import java.nio.file.WatchEvent


@Composable
fun AddItemScreen(saveFunction : (item: Item) -> Unit){

    val itemName  = remember {
        mutableStateOf("")
    }
    val storeName = remember {
        mutableStateOf("")
    }
    val price = remember {
        mutableStateOf("")
    }
    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()
        .background(color = MaterialTheme.colorScheme.primaryContainer)
        ,contentAlignment = Alignment.Center){

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                ImagePicker(onImageSelected = {
                    selectedImageUri.value = it

                })
                TextField(itemName.value, onValueChange = {
                    itemName.value = it
                }, placeholder = {
                    Text(text = "Enter Item Name")
                }, colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ))
            TextField(storeName.value, onValueChange = {
                storeName.value = it
            }, placeholder = {
                Text(text = "Enter Store Name")
            }, colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ))
            TextField(price.value, onValueChange = {
                price.value = it
            }, placeholder = {
                Text(text = "Enter Price")
            }, colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ))
            Button(onClick = {
                val imageByteArray = selectedImageUri.value?.let {
                    resizeImage(context,it,600,400)
                }?: ByteArray(0)
                //Save Item
                val itemToInsert = Item(itemName.value,storeName.value,price.value,imageByteArray)
                saveFunction(itemToInsert)
            }, ) {
                Text(text = "Kaydet")


            }
        }
    }
}
@Composable
fun ImagePicker(onImageSelected: (Uri?) -> Unit){
    var selectedImageUri by remember{
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        android.Manifest.permission.READ_MEDIA_IMAGES
    }else{
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){Uri ->
        selectedImageUri = Uri


    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){granted ->
        if (granted){
            galleryLauncher.launch("image/*")
        }else{
            Toast.makeText(context,"Permission Denied",Toast.LENGTH_LONG).show()
        }

    }
    Column(modifier = Modifier.fillMaxWidth()
        , verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        selectedImageUri?.let{
            Image(painter= rememberAsyncImagePainter(model = it)
            , contentDescription = "Selected Image"
            , modifier = Modifier.size(300.dp,200.dp)
                    .padding(16.dp))
            onImageSelected(it)
        }?: Image(
            painter = painterResource(R.drawable.selectimage),
            contentDescription = "Select Image"
            , modifier = Modifier.size(300.dp,200.dp)
                .padding(16.dp)
                .fillMaxWidth()
                .clickable{
                    if (ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED){
                        galleryLauncher.launch("image/*")
                    }else{
                        permissionLauncher.launch(permission)
                    }

                }
        )
    }

}
fun resizeImage(context: Context, Uri: Uri, maxWidth: Int, maxHeight: Int): ByteArray?{
    return try {
        val inputStream = context.contentResolver.openInputStream(Uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        val ratio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
        var width = maxWidth
        var height = (width / ratio).toInt()
        if (height > maxHeight){
            height = maxHeight
            width = (height * ratio).toInt()
        }

        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap,width,height,false)

        val byteArrayOutputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        byteArrayOutputStream.toByteArray()
    }catch (e: Exception){
        e.printStackTrace()
        null
    }


}