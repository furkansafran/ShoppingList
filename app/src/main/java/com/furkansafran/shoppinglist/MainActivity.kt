package com.furkansafran.shoppinglist

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.room.util.TableInfo
import com.furkansafran.shoppinglist.model.Item
import com.furkansafran.shoppinglist.screens.AddItemScreen
import com.furkansafran.shoppinglist.ui.theme.ShoppingListTheme
import com.furkansafran.shoppinglist.viewmodel.ItemViewModel
import kotlin.getValue
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.util.query
import com.furkansafran.shoppinglist.screens.DetailsScreen
import com.furkansafran.shoppinglist.screens.ItemList
import java.lang.reflect.Type

class MainActivity : ComponentActivity() {
    private val viewModel : ItemViewModel by viewModels<ItemViewModel>()
    @SuppressLint("ComposableDestinationInComposeScope")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ShoppingListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)){
                        NavHost(navController,startDestination = "list_screen"){
                            composable("list_screen") {

                                viewModel.getItemsList()
                                val itemList = remember {
                                    viewModel.itemList
                                }
                                ItemList(itemList.value,navController)
                            }
                            composable("add_item_screen"){
                                AddItemScreen {
                                    viewModel.saveItem(it)
                                    navController.popBackStack()
                                    navController.navigate("list_screen")
                                }
                                composable("details_screen/{itemId}",
                                    arguments = listOf(
                                        navArgument("itemId") {
                                            type = NavType.StringType
                                        }
                                    )
                                ){
                                    val itemIdString = remember {
                                        it.arguments?.getString("itemId")
                                    }
                                    viewModel.getItem(itemIdString?.toIntOrNull() ?:1)
                                    val selectedItem = remember {
                                        viewModel.selectedItem
                                    }
                                    DetailsScreen(item = selectedItem.value!!) {
                                        viewModel.deleteItem(selectedItem.value!!)
                                        navController.popBackStack()
                                        navController.navigate("list_screen")

                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}




