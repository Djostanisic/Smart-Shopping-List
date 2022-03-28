package com.example.smartshoppinglist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshoppinglist.db.Items;

import java.util.List;

//Uses data from the db about the categories(list of categories) and makes(inflates) layout.
public class ItemsListAdapter extends RecyclerView.Adapter<ItemsListAdapter.MyViewHolder> {

    private Context context;
    private List<Items> itemsList;
    private HandleItemsClick clickListener;

    public ItemsListAdapter(Context context, HandleItemsClick clickListener){
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setCategoryList(List<Items> itemsList){
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }

    //Inflates recycler view layout.
    @NonNull
    @Override
    public ItemsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override //Called by RecyclerView to display the data at the specified position.
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvItemName.setText(this.itemsList.get(position).itemName);

        //Item click listener. Opens activity for item content.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.itemClick(itemsList.get(position));
            }
        });

        holder.removeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.removeItem(itemsList.get(position));
            }
        });

        holder.editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.editItem(itemsList.get(position));
            }
        });

        //If you finish an item(click on it) then it will get a line across.
        if(this.itemsList.get(position).completed){
            holder.tvItemName.setPaintFlags(holder.tvItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvItemName.setTextColor(Color.LTGRAY);
        } else {
            holder.tvItemName.setPaintFlags(0);
            holder.tvItemName.setTextColor(Color.BLACK);
        }
    }

    //Gets number of items in the recycler view.
    @Override
    public int getItemCount() {

        if(itemsList == null || itemsList.size() == 0)
            return 0;
         else
            return itemsList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        ImageView removeCategory;
        ImageView editCategory;

        public MyViewHolder(View view){
            super(view);
            tvItemName = view.findViewById(R.id.tvCategoryName);
            removeCategory = view.findViewById(R.id.removeCategory);
            editCategory = view.findViewById(R.id.editCategory);
    }
    }

    public interface HandleItemsClick {
        void itemClick(Items item);
        void removeItem(Items item);
        void editItem(Items item);
    }
}
