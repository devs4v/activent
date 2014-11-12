package com.agnostix.activent.activent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A fragment representing a single Event detail screen.
 * This fragment is either contained in a {@link EventListActivity}
 * in two-pane mode (on tablets) or a {@link EventDetailActivity}
 * on handsets.
 */
public class EventDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            LinearLayout eventDetail = (LinearLayout) rootView.findViewById(R.id.event_detail);

            //set the event title
            TextView tw_title = (TextView) eventDetail.findViewById(R.id.event_title);
            tw_title.setText("Seminar by Dr PK");

            //set the day stamp
            TextView tw_timestamp = (TextView) eventDetail.findViewById(R.id.event_timestamp);
            tw_timestamp.setText("C01");

            //set the start time
            //TextView tw_start_time = (TextView) eventDetail.findViewById(R.id.event_start_time);
            //tw_start_time.setText(mItem.content);

            //set the end time
            //TextView tw_end_time = (TextView) eventDetail.findViewById(R.id.event_end_time);
            //tw_end_time.setText(mItem.content);


        }

        return rootView;
    }
}
