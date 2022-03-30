package com.djostanisic.smartshoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.djostanisic.smartshoppinglist.db.Category;
import com.djostanisic.smartshoppinglist.viewmodel.MainActivityViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryListAdapter.HandleCategoryClick {

    private MainActivityViewModel viewModel;
    private TextView noResultTextView;
    private RecyclerView recyclerView;
    private CategoryListAdapter categoryListAdapter;
    private Category categoryForEdit;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Changes action bar text.
        getSupportActionBar().setTitle("Shopping list");

        noResultTextView = findViewById(R.id.noResult);
        recyclerView = findViewById(R.id.recyclerView);
        mAuth = FirebaseAuth.getInstance();

        ImageView addNew = findViewById(R.id.addNewCategoryImageView);

        //Shows alert dialog when clicked on the image view.
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCategoryDialog(false);
            }
        });
        initViewModel();
        initRecyclerView();
        viewModel.getAllCategoryList();
        }

        //Checking to see if the user is logged in, if not then go to login activity.
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
        }
    }

        private void initRecyclerView(){

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            categoryListAdapter = new CategoryListAdapter(this, this);
            recyclerView.setAdapter(categoryListAdapter);

        }

        private void initViewModel(){
            viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
            viewModel.getCategoryListObserver().observe(this, new Observer<List<Category>>() {
                @Override
                public void onChanged(List<Category> categories) {
                    if(categories == null){
                        noResultTextView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        categoryListAdapter.setCategoryList(categories);
                        recyclerView.setVisibility(View.VISIBLE);
                        noResultTextView.setVisibility(View.GONE);
                    }
                }
            });
        }

    private void showAddCategoryDialog(boolean isForEdit){
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.add_category_layout, null);

        EditText enterCategoryInput = dialogView.findViewById(R.id.enterCategoryInput);
        TextView createButton = dialogView.findViewById(R.id.createButton);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);

        if(isForEdit){
            createButton.setText("Update");
            enterCategoryInput.setText(categoryForEdit.categoryName);
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = enterCategoryInput.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(MainActivity.this, "Enter category name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isForEdit){
                    categoryForEdit.categoryName = name;
                    viewModel.updateCategory(categoryForEdit);
                } else{
                    viewModel.insertCategory(name);
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    //Opens item in a new window.
    @Override
    public void itemClick(Category category) {
        Intent intent = new Intent(MainActivity.this, ShowItemsListActivity.class);

        //Pass data between activity, so that we can connect it with the DB. This gets category id and name.
        //Name is just for the title bar.
        intent.putExtra("category_id", category.uid);
        intent.putExtra("category_name", category.categoryName);
        startActivity(intent);
    }

    //Deletes category
    @Override
    public void removeItem(Category category) {
        viewModel.deleteCategory(category);
    }

    //Opens up dialog box for editing category name.
    @Override
    public void editItem(Category category) {
        this.categoryForEdit = category;
        showAddCategoryDialog(true);
    }
}
