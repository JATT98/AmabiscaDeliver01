package com.example.amabiscadeliver.Connect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amabiscadeliver.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder>{
    GlobalVarLog var =   GlobalVarLog.getInstance();

    private Context context;
    private List itemsList;

    public ItemAdapter(Context context , List items){
        this.context = context;
        itemsList = items;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_product , parent , false);
        return new ItemAdapter.ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item item = (Item) itemsList.get(position);

        holder.product.setText(item.get_nombre());
        holder.quantity.setText(String.valueOf(item.get_cantidad()));

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        TextView product, quantity;
        ConstraintLayout constraintLayout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            product = itemView.findViewById(R.id.productTxt);
            quantity = itemView.findViewById(R.id.quantityTxt);

            constraintLayout = itemView.findViewById(R.id.item_product);
        }
    }
}
