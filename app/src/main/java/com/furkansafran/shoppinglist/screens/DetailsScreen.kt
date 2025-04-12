package com.furkansafran.shoppinglist.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.furkansafran.shoppinglist.R
import com.furkansafran.shoppinglist.model.Item

@Composable
fun DetailsScreen(item: Item?,deleteFunction:() -> Unit ) {
    Box(modifier = Modifier.fillMaxSize()
        .background(color = MaterialTheme.colorScheme.primaryContainer)
        ,contentAlignment = Alignment.Center){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = item?.itemName ?:""
                , style = MaterialTheme.typography.displayLarge
                , fontWeight = FontWeight.Bold
                , color = Color.Black
                , textAlign = TextAlign.Center)
            val imageBitmap =  item?.image?.let {byteArray ->
                BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)?.asImageBitmap()

            }
            Image(bitmap = imageBitmap?: ImageBitmap.imageResource(R.drawable.selectimage)
                , contentDescription = item?.itemName ?: ""
                , modifier = Modifier.padding(10.dp)
                    .size(300.dp,200.dp))
            Text(text = item?.storeName ?:""
                , style = MaterialTheme.typography.displaySmall
                , fontWeight = FontWeight.Bold
                , color = Color.Black
                , textAlign = TextAlign.Center)
            Text(text = item?.price ?:""
                , style = MaterialTheme.typography.displaySmall
                , fontWeight = FontWeight.Bold
                , color = Color.Black
                , textAlign = TextAlign.Center)
            Button(onClick = {
                deleteFunction()
            }) {
                Text(text = "Delete")
            }

        }

    }
}