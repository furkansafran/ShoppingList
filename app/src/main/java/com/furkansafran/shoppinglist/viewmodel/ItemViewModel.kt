package com.furkansafran.shoppinglist.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.room.RoomDatabase
import com.furkansafran.shoppinglist.model.Item
import com.furkansafran.shoppinglist.roomdb.ItemDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch


class ItemViewModel(application: Application) : AndroidViewModel(application) {

    val  itemList =  mutableStateOf<List<Item>>(listOf())
    val selectedItem = mutableStateOf<Item?>(null)
    private val db = Room.databaseBuilder(getApplication(),
        ItemDatabase::class.java, "Items").build()

    private val itemDao = db.itemDao()

    fun getItemsList(){
        viewModelScope.launch {
            val items= withContext(Dispatchers.IO){
               itemDao.getItemWithNameAndId()
            }
            itemList.value = items

        }
    }
    fun getItem(id: Int){
        viewModelScope.launch{

            val item = withContext(Dispatchers.IO) {
                itemDao.getItemById(id)
            }

             item?.let {
                 selectedItem.value = it
        }
        }
    }
    fun saveItem(item: Item){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                itemDao.insert(item)
            }

        }
    }
    fun deleteItem(item: Item){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                itemDao.delete(item)
            }

        }
    }
}