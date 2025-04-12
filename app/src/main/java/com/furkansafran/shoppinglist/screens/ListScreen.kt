package com.furkansafran.shoppinglist.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.furkansafran.shoppinglist.model.Item

@Composable
fun ItemList(itemList: List<Item>,navController: NavController) {
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FAB {
                navController.navigate("add_item_screen")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primaryContainer)
        ) {
            items(itemList.size) {
                ItemRow(itemList[it],navController = navController)

            }
        }
    }
}
@Composable
fun ItemRow(item: Item,navController: NavController){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.primaryContainer)
        .clickable {
            navController.navigate("details_screen/${item.id}")
        })  {
        Text(text = item.itemName,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(10.dp)
            , fontWeight = FontWeight.Bold)
    }

}
@Composable
fun FAB(onClick: () -> Unit){
    FloatingActionButton(onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primaryContainer ) {
        Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.Black)
    }
}