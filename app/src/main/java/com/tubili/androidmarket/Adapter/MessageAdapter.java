package com.tubili.androidmarket.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.library.bubbleview.BubbleTextView;
import com.tubili.androidmarket.Model.Message;
import com.tubili.androidmarket.R;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_item_message, parent, false);
        }


        Message message = getItem(position);

        if(!message.isSend()) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_item_message, parent, false);

            BubbleTextView bubbleTextViewServer = convertView.findViewById(R.id.bubbleChatServer);
            TextView tvServer = convertView.findViewById(R.id.nameTextView);
            bubbleTextViewServer.setText(message.getText());
            tvServer.setText(message.getName());
        }
        else{
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_message_server, parent, false);
            BubbleTextView bubbleTextViewUser = convertView.findViewById(R.id.bubbleChatUser);
            TextView tvUser = convertView.findViewById(R.id.nameTextViewServer);
            bubbleTextViewUser.setText(message.getText());
            tvUser.setText(message.getName());
        }





        return convertView;
    }
}
