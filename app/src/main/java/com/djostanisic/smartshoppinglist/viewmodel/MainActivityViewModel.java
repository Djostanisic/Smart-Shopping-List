package com.djostanisic.smartshoppinglist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.djostanisic.smartshoppinglist.db.AppDatabase;
import com.djostanisic.smartshoppinglist.db.Category;

import java.util.List;

/*The AndroidViewModel class is a subclass of ViewModel and similar to them,
they are designed to store and manage UI-related data are responsible to prepare & provide data
for UI and automatically allow data to survive configuration change.
The difference between the ViewModel and the AndroidViewModel class is that the later one provides you
with an application context, which you need to provide when you create a view model of type AndroidViewModel.*/
public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Category>> listOfCategory;
    private AppDatabase appDatabase;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        //Initializing live data and access to database inside constructor.
        listOfCategory = new MutableLiveData<>();
        appDatabase = AppDatabase.getDBInstance(getApplication().getApplicationContext());
    }

    //A simple callback that can receive from LiveData(returns live data).
    public MutableLiveData<List<Category>> getCategoryListObserver(){
        return listOfCategory;
    }

    //Retrieves category list from database.
    public void getAllCategoryList(){
        List<Category> categoryList = appDatabase.shoppingListDao().getAllCategoriesList();
        if(categoryList.size() > 0){

            // Posts a task to a main thread to set the given value.
            listOfCategory.postValue(categoryList);
        } else {
            listOfCategory.postValue(null);
        }
    }

    public void insertCategory(String catName){
        Category category = new Category();
        category.categoryName = catName;
        appDatabase.shoppingListDao().insertCategory(category);
        getAllCategoryList();
    }

    public void updateCategory(Category category){
        appDatabase.shoppingListDao().updateCategory(category);
        getAllCategoryList();
    }

    public void deleteCategory(Category category){
        appDatabase.shoppingListDao().deleteCategory(category);
        getAllCategoryList();
    }

}
