package com.tubili.androidmarket.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tubili.androidmarket.Model.Message;
import com.tubili.androidmarket.Model.User;
import com.tubili.androidmarket.R;

import org.w3c.dom.Text;

import java.util.List;

public class UserMessageAdapter extends ArrayAdapter<User> {
    public UserMessageAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.user_item_message, parent, false);
        }

        TextView userMessageTextView = (TextView) convertView.findViewById(R.id.userMessageTextView);

        User user = getItem(position);
        userMessageTextView.setText(user.getName());

        return convertView;
    }
}
