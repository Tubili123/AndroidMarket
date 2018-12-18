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
import com.tubili.androidmarket.MessageActivity;
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

    ArrayList<String> userIDs = new ArrayList<>();
    public static List<User> allUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_from_user_server);

        mMessageListView = findViewById(R.id.listviewUsers);
        allUser = new ArrayList<>();
        userMessageAdapter = new UserMessageAdapter(this, R.layout.user_item_message, allUser);
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
                String userID;
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    userID = childSnapshot.getKey();
                    userIDs.add(userID);
                    Log.i("Anahtar", userID);
                }

                if(!userIDs.isEmpty())
                {
                    for (String id: userIDs) {
                        setUserName(id);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUserName(final String userID) {

        users.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                user.setPhone(userID);

                if (user != null){
                    allUser.add(user);
                    userMessageAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.i("Tıklandım Bebek", allUser.get(position).getPhone());


        Intent intent =new Intent(this, MessageActivityServer.class).putExtra("position", position);
        startActivity(intent);


    }
}
