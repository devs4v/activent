package com.agnostix.activent.activent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivam on 11/16/2014.
 */
public class EmailListAdapter extends BaseAdapter {
    ArrayList<ArrayList<String>> messages;
    Context context;
    public EmailListAdapter(Context context, ArrayList<ArrayList<String>> objects) {
        this.context = context;
        messages = objects;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public ArrayList<String> getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ArrayList<String> emailItem = getItem(position);
        View view;
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_email_item, parent, false);

            TextView titleText = (TextView)view.findViewById(R.id.email_item_title);
            titleText.setText(emailItem.get(0));

            TextView threadText = (TextView)view.findViewById(R.id.email_item_thread_id);
            threadText.setText(emailItem.get(1));

            TextView snippetText = (TextView)view.findViewById(R.id.email_item_snippet);
            snippetText.setText(emailItem.get(2));

        }else{
            view = convertView;
        }

        return view;
    }
}
