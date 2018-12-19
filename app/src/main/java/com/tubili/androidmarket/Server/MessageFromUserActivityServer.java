package com.tubili.androidmarket.Server;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tubili.androidmarket.Adapter.UserMessageAdapter;
import com.tubili.androidmarket.Model.User;
import com.tubili.androidmarket.R;

import java.util.ArrayList;
import java.util.List;

public class MessageFromUserActivityServer extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView mMessageListView;
    UserMessageAdapter userMessageAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference messages;
    DatabaseReference users;
    String userID;

    ArrayList<String> userIDContainer = new ArrayList<>();
    public static List<User> userContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_from_user_server);

        mMessageListView = findViewById(R.id.listviewUsers);
        userContainer = new ArrayList<>();
        userMessageAdapter = new UserMessageAdapter(this, R.layout.user_item_message, userContainer);
        mMessageListView.setAdapter(userMessageAdapter);
        mMessageListView.setOnItemClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        messages = firebaseDatabase.getReference("messages");
        users = firebaseDatabase.getReference("User");

        getUserIDs();

    }

    private void getUserIDs() {
        messages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    userID = childSnapshot.getKey();
                    userIDContainer.add(userID);
                }

                if(!userIDContainer.isEmpty())
                    setUserName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUserName() {
            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   userMessageAdapter.clear();
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        userID = childSnapshot.getKey();
                        if(userIDContainer.contains(userID)){
                            User user = childSnapshot.getValue(User.class);
                            user.setPhone(userID);
                            userContainer.add(user);
                        }
                    }
                    userMessageAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent =new Intent(this, MessageActivityServer.class).putExtra("position", position);
        startActivity(intent);
    }
}
