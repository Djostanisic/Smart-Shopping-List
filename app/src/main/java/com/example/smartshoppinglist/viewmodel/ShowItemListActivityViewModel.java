package com.example.smartshoppinglist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.smartshoppinglist.db.AppDatabase;
import com.example.smartshoppinglist.db.Category;
import com.example.smartshoppinglist.db.Items;

import java.util.List;

/*The AndroidViewModel class is a subclass of ViewModel and similar to them,
they are designed to store and manage UI-related data are responsible to prepare & provide data
for UI and automatically allow data to survive configuration change.
The difference between the ViewModel and the AndroidViewModel class is that the later one provides you
with an application context, which you need to provide when you create a view model of type AndroidViewModel.*/
public class ShowItemListActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Items>> listOfItems;
    private AppDatabase appDatabase;

    public ShowItemListActivityViewModel(@NonNull Application application) {
        super(application);

        //Initializing live data and access to database inside constructor.
        listOfItems = new MutableLiveData<>();
        appDatabase = AppDatabase.getDBInstance(getApplication().getApplicationContext());
    }

    //A simple callback that can receive from LiveData(returns live data).
    public MutableLiveData<List<Items>> getItemsListObserver(){
        return listOfItems;
    }

    //Retrieves items from database by using category id.
    public void getAllItemsList(int categoryID){
        List<Items> itemsList = appDatabase.shoppingListDao().getAllItemsList(categoryID);
        if(itemsList.size() > 0){

            // Posts a task to a main thread to set the given value.
            listOfItems.postValue(itemsList);
        } else {
            listOfItems.postValue(null);
        }
    }

    public void insertItems(Items item){

        appDatabase.shoppingListDao().insertItem(item);
        getAllItemsList(item.categoryId);
    }

    public void updateItems(Items item){
        appDatabase.shoppingListDao().updateItem(item);
        getAllItemsList(item.categoryId);
    }

    public void deleteItems(Items item){
        appDatabase.shoppingListDao().deleteItem(item);
        getAllItemsList(item.categoryId);
    }

}
