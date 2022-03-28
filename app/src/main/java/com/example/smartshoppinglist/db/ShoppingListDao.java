package com.example.smartshoppinglist.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShoppingListDao {

    //Functions for categories.

    //Gets all the categories.
    @Query("Select * from Category")
    List<Category> getAllCategoriesList();

    //"..." means that zero or more "Category" objects (or a single array of them) may be passed as the argument(s) for that method.
    @Insert
    void insertCategory(Category...categories);

    @Update
    void updateCategory(Category categories);

    @Delete
    void deleteCategory(Category categories);


    //Functions for items.

    //Gets all the items based on the category ID
    @Query("Select * from Items where categoryId = :catId")
    List<Items> getAllItemsList(int catId);

    @Insert
    void insertItem(Items items);

    @Update
    void updateItem(Items items);

    @Delete
    void deleteItem(Items items);
}
