package com.tubili.androidmarket.Server;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tubili.androidmarket.Common.Common;
import com.tubili.androidmarket.Model.Request;
import com.tubili.androidmarket.R;
import com.tubili.androidmarket.Server.Services.OrderListenService;
import com.tubili.androidmarket.Server.ViewHolders.OrderViewHolderServer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class OrderStatusActivityServer extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textViewName;
    TextView textViewStatus;
    TextView textViewPhone;
    TextView textViewAddress;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference request;
    FirebaseRecyclerAdapter adapter;
    private MaterialSpinner orderStatus;
    RelativeLayout orderItemLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status_server);

        //init Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        request = firebaseDatabase.getReference("Request");

        textViewName = findViewById(R.id.order_id);
        textViewStatus = findViewById(R.id.order_status);
        textViewPhone = findViewById(R.id.order_phone);
        textViewAddress = findViewById(R.id.order_address);
        orderItemLayout = findViewById(R.id.orderItemLayout);

        recyclerView = findViewById(R.id.recycler_order_status);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        if(getIntent().getStringExtra("phone") != null)
        {
            showOrders(getIntent().getStringExtra("phone"));
        }
        else
        {
            showOrders(Common.currentUser.getPhone());
        }


        Intent intentServer = new Intent(OrderStatusActivityServer.this, OrderListenService.class);
        startService(intentServer);
    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), (Request) adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.DELETE))
        {
            deleteOrder(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }


    //Helper Method
    private void showOrders(String phone)
    {
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(
                request.orderByChild("phone"), Request.class).build();
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolderServer>(options) {
            @NonNull
            @Override
            public OrderViewHolderServer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new OrderViewHolderServer(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolderServer holder, int position, @NonNull final Request model) {
                TextView textViewId = holder.itemView.findViewById(R.id.order_id);
                textViewId.setText(adapter.getRef(position).getKey());

                TextView textViewPhone = holder.itemView.findViewById(R.id.order_phone);
                textViewPhone.setText(model.getPhone());

                TextView textViewAddress = holder.itemView.findViewById(R.id.order_address);
                textViewAddress.setText(model.getAddress());

                TextView textViewStatus = holder.itemView.findViewById(R.id.order_status);
                textViewStatus.setText(Common.getStatus(model.getStatus()));

            }
        };
        recyclerView.setAdapter(adapter);
    }



    private void showUpdateDialog(final String key, final Request item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Siparişi Güncelle");
        alertDialog.setMessage("Lütfen Durum Seçin");
        alertDialog.setIcon(R.drawable.ic_access_time_black_24dp);

        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.update_order_dialog, null);
        alertDialog.setView(view);

        orderStatus = view.findViewById(R.id.order_status);

        orderStatus.setItems("Alındı", "Yolda", "Teslim Edildi");
        orderStatus.setSelectedIndex(Integer.parseInt(item.getStatus()));

        alertDialog.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item.setStatus(String.valueOf(orderStatus.getSelectedIndex()));
                request.child(key).setValue(item);
            }
        });

        alertDialog.setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }


    private void deleteOrder(String key) {
        request.child(key).removeValue();
    }


}
