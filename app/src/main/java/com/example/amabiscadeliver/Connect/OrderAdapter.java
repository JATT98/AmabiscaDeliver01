package com.example.amabiscadeliver.Connect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amabiscadeliver.Activity.OrderActivity;
import com.example.amabiscadeliver.Activity.OrderDetailActivity;
import com.example.amabiscadeliver.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    GlobalVarLog var =   GlobalVarLog.getInstance();

    private ManagementPhone managementPhone;
    private ClickButtonListener clickButtonListener;
    private Context context;
    private List ordersList;

    public OrderAdapter(Context context , List orders, ClickButtonListener clickButtonListener){
        this.context = context;
        ordersList = orders;
        managementPhone = new ManagementPhone(context);
        this.clickButtonListener = clickButtonListener;

    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order , parent , false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        Order order = (Order) ordersList.get(position);

        holder.title.setText("Orden #" + order.get_codigo());
        holder.date.setText(order.get_fecha());
        holder.customer.setText(order.get_cliente());
        holder.state.setText(order.get_estado().toUpperCase());

        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managementPhone.clickNumber(new ClickButtonListener() {
                    @Override
                    public void Click() {
                        if (order.get_telefono().equals("")){
                            var.set_phone("46463819");
                        }else{
                            var.set_phone(order.get_telefono());
                        }
                        clickButtonListener.Click();
                    }
                });
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, OrderDetailActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("title", order.get_codigo());
                bundle.putString("date", order.get_fecha());
                bundle.putString("customer", order.get_cliente());
                bundle.putString("state", order.get_estado());

                intent.putExtras(bundle);
                context.startActivity(intent);
                ((OrderActivity) context).finish();
            }
        });
    }


    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        TextView title, date, customer, state, phone;
        ConstraintLayout constraintLayout;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.orderTxt);
            date = itemView.findViewById(R.id.dateTxt);
            customer = itemView.findViewById(R.id.clientTxt);
            phone = itemView.findViewById(R.id.callDeliverTxt);
            state = itemView.findViewById(R.id.stateTxt);

            constraintLayout = itemView.findViewById(R.id.order_layout);
        }
    }
}
