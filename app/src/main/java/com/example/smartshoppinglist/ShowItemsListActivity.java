package com.example.smartshoppinglist;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshoppinglist.db.Items;
import com.example.smartshoppinglist.viewmodel.ShowItemListActivityViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;

public class ShowItemsListActivity extends AppCompatActivity implements ItemsListAdapter.HandleItemsClick {

    private int category_id;
    private ItemsListAdapter itemListAdapter;
    private ShowItemListActivityViewModel viewModel;
    private RecyclerView recyclerView;
    private Items itemToUpdate = null;

@Override
protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show_items_list);

    MobileAds.initialize(this, new OnInitializationCompleteListener() {
        @Override
        public void onInitializationComplete(InitializationStatus initializationStatus) {
        }
    });

    AdView mAdView = findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);



    //Gets ID and name od category.
    category_id = getIntent().getIntExtra("category_id", 0);
    String categoryName = getIntent().getStringExtra("category_name");

    //Sets action bar title to category name.
    getSupportActionBar().setTitle(categoryName);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    final EditText addNewItemInput = findViewById(R.id.addNewItemInput);
    ImageView saveButton = findViewById(R.id.saveButton);

    saveButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String itemName = addNewItemInput.getText().toString();
            if(TextUtils.isEmpty(itemName)){
                Toast.makeText(ShowItemsListActivity.this, "Enter Item Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if(itemToUpdate == null){
            saveNewItem(itemName);
            } else {
                updateNewItem(itemName);
            }

        }
    });
    initRecyclerView();
    initViewModel();
    viewModel.getAllItemsList(category_id);
}

    //Initializes view model for items.
    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(ShowItemListActivityViewModel.class);
        viewModel.getItemsListObserver().observe(this, new Observer<List<Items>>() {
            @Override
            public void onChanged(List<Items> items) {
                if (items == null) {
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.noResult).setVisibility(View.VISIBLE);
                } else {
                    itemListAdapter.setCategoryList(items);
                    recyclerView.setVisibility(View.VISIBLE);
                    findViewById(R.id.noResult).setVisibility(View.GONE);
                }
            }
        });
    }

    //Initialization of recycler view list for items.
private void initRecyclerView(){
    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    itemListAdapter = new ItemsListAdapter(this, this );
    recyclerView.setAdapter(itemListAdapter);
}

private void saveNewItem(String itemName){
    Items item = new Items();
    item.itemName = itemName;
    item.categoryId = category_id;
    viewModel.insertItems(item);
    ((EditText) findViewById(R.id.addNewItemInput)).setText("");

}


    @Override
    public void itemClick(Items item) {
        if(item.completed){
            item.completed = false;
        } else {
        item.completed = true;
        }
        viewModel.updateItems(item);
    }

    @Override
    public void removeItem(Items item) {
        viewModel.deleteItems(item);
    }

    @Override
    public void editItem(Items item) {
        this.itemToUpdate = item;
        EditText addNewItemInput = findViewById(R.id.addNewItemInput);
        addNewItemInput.setText(item.itemName);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

    }

    private void updateNewItem(String newName){
    itemToUpdate.itemName = newName;
    viewModel.updateItems(itemToUpdate);
    ((EditText) findViewById(R.id.addNewItemInput)).setText("");
    itemToUpdate = null;

    }

    //Makes back arrow (<-) on top bar to return to the main activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}