package com.tubili.androidmarket.Adapter;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.tubili.androidmarket.CartActivity;
import com.tubili.androidmarket.Database.Database;
import com.tubili.androidmarket.Interface.ItemClickListener;
import com.tubili.androidmarket.Model.Order;
import com.tubili.androidmarket.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    private List<Order> orders;
    private CartActivity context;
    private ItemClickListener itemClickListener;



    public CartAdapter(CartActivity context, List<Order> orders, ItemClickListener itemClickListener) {
        this.orders = orders;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewItemName, textViewItemPrice;
        private ElegantNumberButton btnQuantity;
        private Button cartItemRemove;


//        private ImageView imageView;
        ViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.cart_item_name);
            textViewItemPrice = itemView.findViewById(R.id.cart_item_price);
            btnQuantity = itemView.findViewById(R.id.cart_change_quantity);
            cartItemRemove = itemView.findViewById(R.id.cart_item_remove);




//            textViewTotalPrice =  itemView.findViewById(R.id.order_price);

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onclick(view, viewHolder.getAdapterPosition(), false);
            }
        });

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.textViewItemName.setText(orders.get(position).getProductName());

        Locale locale = new Locale("tr", "TR");
        final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        final double price = Double.parseDouble(orders.get(position).getPrice()) * Double.parseDouble(orders.get(position).getQuantity())
            - Double.parseDouble(orders.get(position).getDiscount()) * Double.parseDouble(orders.get(position).getQuantity());
        holder.textViewItemPrice.setText(numberFormat.format(price));
        holder.textViewItemPrice.append("  İndirim: "+Double.parseDouble(orders.get(position).getDiscount())+"TL");
        holder.btnQuantity.setNumber(orders.get(position).getQuantity());

        holder.btnQuantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                //Updating the price after quantity is changed
                double result = Double.parseDouble(orders.get(position).getPrice()) * newValue;
                holder.textViewItemPrice.setText(numberFormat.format(result));
                holder.textViewItemPrice.append("  İndirim: "+Double.parseDouble(orders.get(position).getDiscount())*newValue+"TL");
                //Update database
                Order order = orders.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(context).updateCart(order);

                //Update total amount
                double total = 0;
                for(Order cartOrder: orders)
                {
                    total += (Double.parseDouble(cartOrder.getPrice()) * Double.parseDouble(cartOrder.getQuantity())
                            - Double.parseDouble(cartOrder.getDiscount()) * Double.parseDouble(cartOrder.getQuantity()));
                }
                context.textViewPrice.setText(String.format(" %.5s TL", total));

            }
        });

        holder.cartItemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = orders.get(position);
                orders.remove(order);
                new Database(context).updateCart(order);



                double total = 0;
                for(Order cartOrder: orders)
                {
                    total += (Double.parseDouble(cartOrder.getPrice()) * Double.parseDouble(cartOrder.getQuantity())
                            - Double.parseDouble(cartOrder.getDiscount()) * Double.parseDouble(cartOrder.getQuantity()));
                }
                context.textViewPrice.setText(String.format(" %.5s TL", total));
                CartAdapter.this.notifyItemRemoved(position);
            }
        });


    }




    @Override
    public int getItemCount() {
        return orders.size();
    }

}
