package com.djostanisic.smartshoppinglist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.djostanisic.smartshoppinglist.db.Category;

import java.util.List;

//Uses data from the db about the categories(list of categories) and makes(inflates) layout.
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private HandleCategoryClick clickListener;

    public CategoryListAdapter (Context context, HandleCategoryClick clickListener){
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setCategoryList(List<Category> categoryList){
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    //Inflates recycler view layout.
    @NonNull
    @Override
    public CategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override //Called by RecyclerView to display the data at the specified position.
    public void onBindViewHolder(@NonNull CategoryListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvCategoryName.setText(this.categoryList.get(position).categoryName);

        //Item click listener. Opens activity for item content.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.itemClick(categoryList.get(position));
            }
        });

        holder.removeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.removeItem(categoryList.get(position));
            }
        });

        holder.editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.editItem(categoryList.get(position));
            }
        });
    }

    //Gets number of items in the recycler view.
    @Override
    public int getItemCount() {

        if(categoryList == null || categoryList.size() == 0)
            return 0;
         else
            return categoryList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView removeCategory;
        ImageView editCategory;

        public MyViewHolder(View view){
            super(view);
            tvCategoryName = view.findViewById(R.id.tvCategoryName);
            removeCategory = view.findViewById(R.id.removeCategory);
            editCategory = view.findViewById(R.id.editCategory);
    }
    }

    public interface HandleCategoryClick {
        void itemClick(Category category);
        void removeItem(Category category);
        void editItem(Category category);
    }
}
