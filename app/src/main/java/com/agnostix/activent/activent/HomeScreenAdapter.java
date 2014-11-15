package com.agnostix.activent.activent;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shivam on 11/10/2014.
 */
public class HomeScreenAdapter extends BaseAdapter {
    private Context context;
    private Activity activity;
    private ArrayList<String[]> events = new ArrayList<String[]>();
    private HomeScreenItemClickListener callbackListener;

    /*  Expecting an array list of an array to be the data items,
        in the following format of indices */
    private static final int E_TITLE = 0;
    private static final int E_DAY = 1;
    private static final int E_START_TIME = 2;
    private static final int E_END_TIME = 3;
    private static final int E_PLACE = 4;

    private int footerItem;

    public interface HomeScreenItemClickListener{
        public void onHomeScreenItemClick(View v, int resid);
        public void setUIObjects(View v, int objectToBeAdded);
    }

    public HomeScreenAdapter(Context context, Activity activity, ArrayList<String[]> eventItems){
        this.context = context;
        this.events = eventItems;
        this.activity = activity;
        if(events == null){ //dummy data to show something
            events = new ArrayList<String[]>();
            //TODO add an AsyncTask that fetches the current happening events info from the database
            events.add(new String[]{"Seminar", "thu nov 9", "3:30pm", "5:30pm", "C01"});
            //events.add(new String[]{"Spicmacay", "thu nov 9", "3:30pm", "5:30pm", "C21"});
            //events.add(new String[]{"Project Meeting", "thu nov 9", "3:30pm", "5:30pm", "library"});
        }
        footerItem = events.size();

        try{
            callbackListener = (HomeScreenItemClickListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement HomeScreenItemClickListener");
        }
    }
    @Override
    public int getCount() {
        return footerItem+2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if(convertView == null){
            if(position == 0){
                //The top row containing dumm button
                //removed from deployment
                view = inflater.inflate(R.layout.hello_button, null);
                Button button = (Button) view.findViewById(R.id.hello_button);
                callbackListener.setUIObjects(view, R.id.hello_button);

            }else if(position == footerItem+1){
                //The last row needs to be the tiles
                view = inflater.inflate(R.layout.fragment_tile_list, null);
                callbackListener.setUIObjects(view, R.id.home_screen_tile_sign_in);
                callbackListener.setUIObjects(view, R.id.home_screen_tile_agenda);
                callbackListener.setUIObjects(view, R.id.home_screen_tile_places);
                callbackListener.setUIObjects(view, R.id.home_screen_tile_info);

            }else{
                //The normal big tiles containing individual events
                view = inflater.inflate(R.layout.home_current_event_item, null);

                int actualPosition = position - 1;
                String[] thisEvent = events.get(actualPosition);

                TextView eventTitle = (TextView)view.findViewById(R.id.home_current_event_title);
                eventTitle.setText(thisEvent[E_TITLE]);

                TextView eventPlace = (TextView)view.findViewById(R.id.home_current_event_place);
                eventPlace.setText(thisEvent[E_PLACE]);

                TextView eventDay = (TextView)view.findViewById(R.id.home_current_event_day);
                eventDay.setText(thisEvent[E_DAY]);

                TextView eventStartTime = (TextView)view.findViewById(R.id.home_current_event_start_time);
                eventStartTime.setText(thisEvent[E_START_TIME]);

                TextView textView = (TextView)view.findViewById(R.id.home_current_event_end_time);
                textView.setText(thisEvent[E_END_TIME]);
            }
        }else{
            view = convertView;
        }
        return view;
    }
}
