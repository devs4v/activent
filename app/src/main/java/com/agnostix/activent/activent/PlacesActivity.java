package com.agnostix.activent.activent;

import java.util.ArrayList;
import java.util.Locale;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class PlacesActivity extends ActionBarActivity implements ActionBar.TabListener {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    Context activityContext;
    int sectionNumber;
    ArrayList<Integer> fragmentTags;

    private TextView fillText;
    private TextView fillTotal;
    private ImageView fillProgress;
    private View levelsBox;
    private ProgressBar loadingSpinner;
    private int[][] showLevels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_places, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());

        int position = tab.getPosition();
        sectionNumber = position;
        Toast.makeText(getApplicationContext(), "on page: " + position, Toast.LENGTH_SHORT).show();
        setUiVarsOnPageChange();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void onStart(){
        super.onStart();
        //new getPlacesLevelTask().execute();
    }

    @Override
    public void onAttachFragment(Fragment fragment){
        super.onAttachFragment(fragment);
    }

    private void setUiVarsOnPageChange(){

        //Activity activity = this;
        fillText = (TextView)findViewById(R.id.places_seats_available);
        fillTotal = (TextView)findViewById(R.id.places_seats_total);
        fillProgress = (ImageView)findViewById(R.id.places_progress);
        levelsBox = findViewById(R.id.places_level);
        loadingSpinner = (ProgressBar)findViewById(R.id.places_wait_progress);

        if(showLevels != null){
            hideProgressShowLevels();
        }else{
            new getPlacesLevelTask().execute();
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void hideProgressShowLevels() {

        //setUiVars();

        final int fillLevel = showLevels[sectionNumber][0];
        int totalLevel = showLevels[sectionNumber][1];

        final int FADE_IN_DURATION = 500;
        final int SHOW_PROGRESS_DURATION = 1000;

        AnimatorSet animations = new AnimatorSet();

        AnimatorSet fadeInObjects = new AnimatorSet();
        ValueAnimator fadeInAnimator = ValueAnimator.ofFloat(0f, 1f);
        fadeInAnimator.setDuration(FADE_IN_DURATION);
        fadeInAnimator.setInterpolator(new DecelerateInterpolator());
        fadeInAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                levelsBox.setAlpha(value.floatValue());
            }
        });
        fadeInObjects.playSequentially(fadeInAnimator);

        AnimatorSet animateLevels = new AnimatorSet();
        ValueAnimator textAnimator = ValueAnimator.ofInt(0, fillLevel);
        textAnimator.setDuration(SHOW_PROGRESS_DURATION);
        textAnimator.setInterpolator(new BounceInterpolator());
        textAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer)animation.getAnimatedValue();
                fillText.setText(String.valueOf(value.intValue()));
            }
        });
        ValueAnimator progressAnimator = ValueAnimator.ofInt(0, ((fillLevel*10000)/totalLevel));
        progressAnimator.setDuration(SHOW_PROGRESS_DURATION);
        progressAnimator.setInterpolator(new BounceInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer)animation.getAnimatedValue();
                fillProgress.setImageLevel(value);
            }
        });
        animateLevels.playTogether(textAnimator, progressAnimator);


        animations.playSequentially(fadeInObjects, animateLevels);

        fillTotal.setText(String.valueOf(totalLevel));
        loadingSpinner.setVisibility(View.GONE);
        levelsBox.setVisibility(View.VISIBLE);
        levelsBox.setAlpha(0f);
        animations.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showProgressHideLevels(){
        /*loadingSpinner.setVisibility(View.VISIBLE);
        //View fillBox = findViewById(R.id.places_level);
        levelsBox.setAlpha(0f);
        levelsBox.setVisibility(View.VISIBLE);*/
    }

    private class getPlacesLevelTask extends AsyncTask<Void, Void, int[][]>{

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onPreExecute(){
            showProgressHideLevels();
        }

        @Override
        protected int[][] doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int[][] levels = new int[][]{{32, 180}, {89, 180}, {140, 180}};

            return levels;
        }

        @Override
        protected void onPostExecute(int[][] levels){
            showLevels = levels;
            hideProgressShowLevels();
        }

    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragments;
        private final int F_TITLE = 0;
        String[][] fragmentList = new String[][]{
                {"C01"},
                {"C11"},
                {"C21"}
        };


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<Fragment>();

            for(int i = 1; i<=fragmentList.length; i++) {
                fragments.add(PlaceholderFragment.newInstance(i));
            }
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return fragmentList[position][F_TITLE];
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber){
            PlaceholderFragment fragment = new PlaceholderFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;

        }

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_places, container, false);
            ImageView progress = (ImageView)rootView.findViewById(R.id.places_progress);
            progress.setImageLevel(0);

            /*fillText = (TextView)rootView.findViewById(R.id.places_seats_available);
            fillProgress = (ImageView)rootView.findViewById(R.id.places_progress);
            levelsBox = rootView.findViewById(R.id.places_level);
            loadingSpinner = (ProgressBar)rootView.findViewById(R.id.places_wait_progress);*/
            return rootView;
        }
    }

}
