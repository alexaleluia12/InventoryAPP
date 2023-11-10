package com.example.inventory.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class ItemDaoTest {
    private lateinit var itemDao: ItemDao
    private lateinit var inventoryDatabase: InventoryDatabase

    private var item1 = Item(1, "banana", 10.0, 20)
    private var item2 = Item(2, "apple", 15.0, 25)

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // banco em memoria apenas para teste, eh deletado depois de tudo
        inventoryDatabase = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        itemDao = inventoryDatabase.itemDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        inventoryDatabase.close()
    }

    private suspend fun addOneItemToDB() {
        itemDao.insert(item1)
    }

    private suspend fun addTowItemToDB() {
        itemDao.insert(item1)
        itemDao.insert(item2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertItensIntoDB() = runBlocking {
        addOneItemToDB()
        val allItems = itemDao.getAllItems().first()
        Assert.assertEquals(allItems[0], item1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnAllItemsFromDB() = runBlocking {
        addTowItemToDB()
        val allItems = itemDao.getAllItems().first()
        Assert.assertEquals(
            allItems[0], item2
        )
        Assert.assertEquals(allItems[1], item1)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateItems_updateItemsInDB() = runBlocking {
        addTowItemToDB()
        val nitem = item1.copy(quantity = 25)
        itemDao.update(nitem)
        val allItems = itemDao.getAllItems().first()
        Assert.assertEquals(allItems[1], nitem)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItem_deleteItemFromDB() = runBlocking {
        addTowItemToDB()
        itemDao.delete(item2)
        val allItems = itemDao.getAllItems().first()
        Assert.assertEquals(allItems.size, 1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnItemFromDB() = runBlocking {
        addOneItemToDB()
        val itemFromDB = itemDao.getItem(item1.id).first()
        Assert.assertEquals(itemFromDB, item1)
    }
}