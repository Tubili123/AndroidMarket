package com.tubili.androidmarket;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tubili.androidmarket.Adapter.CartAdapter;
import com.tubili.androidmarket.Common.Common;
import com.tubili.androidmarket.Database.Database;
import com.tubili.androidmarket.Interface.ItemClickListener;
import com.tubili.androidmarket.Model.Order;
import com.tubili.androidmarket.Model.Request;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public TextView textViewPrice;
    Button buttonOrder;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference request;
    CartAdapter cartAdapter;
    List<Order> orders;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //init firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");

        orders = new ArrayList<>();
        textViewPrice = findViewById(R.id.order_price);
        buttonOrder = findViewById(R.id.btnPlaceOrder);



        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textViewPrice.getText().toString().equals("0.00TL"))
                    Toast.makeText(CartActivity.this, "Sepetiniz boş!", Toast.LENGTH_SHORT).show();
                else
                    showDialog();
            }
        });

        recyclerView = findViewById(R.id.recycler_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        loadCart();


    }


    //Helper Methods
    private void loadCart() {
        orders = new Database(this).getCarts();
        cartAdapter = new CartAdapter(this, orders, new ItemClickListener() {
            @Override
            public void onclick(View view, int position, boolean isLongClick) {
                Toast.makeText(CartActivity.this, orders.get(position).getProductName(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(cartAdapter);

        double total = 0;
        //Calculating total price
        for(Order order: orders)
        {
            total += (Double.parseDouble(order.getPrice()) * Double.parseDouble(order.getQuantity())
                    - Double.parseDouble(order.getDiscount()) * Double.parseDouble(order.getQuantity()));
        }
        textViewPrice.setText(String.format(" %.5s TL", total));
    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bir adım kaldı!");
        builder.setMessage("Lütfen adresinizi girin: ");
        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        editText.setLayoutParams(layoutParams);
        builder.setView(editText);
        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Request req = new Request(Common.currentUser.getName(),
                        Common.currentUser.getPhone(),
                        editText.getText().toString(),
                        textViewPrice.getText().toString(),
                        orders);

                //sending to firebase
                request.child(String.valueOf(System.currentTimeMillis())).setValue(req);

                new Database(CartActivity.this).cleanCart();
                Toast.makeText(CartActivity.this, "Siparişiniz alındı. Bizi tercih ettiğiniz için teşekkürler!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

}
