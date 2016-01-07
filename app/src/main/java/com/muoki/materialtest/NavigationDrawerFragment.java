package com.muoki.materialtest;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements InfoAdapter.MyClickListener {

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private InfoAdapter infoAdapter;

    public static final String PREF_FILE_NAME = "stateFile";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWER, "false"));

        if (savedInstanceState != null)
        {
            mFromSavedInstanceState = true;
        }
    }

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        recyclerView = (RecyclerView)layout.findViewById(R.id.DrawerList);
        infoAdapter = new InfoAdapter(getActivity(), getData());
        infoAdapter.setMyClickListener(this);

        recyclerView.setAdapter(infoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    public static List<Information> getData()
    {
        List<Information> data = new ArrayList<>();

        int icons [] = {R.drawable.car, R.drawable.chat, R.drawable.download, R.drawable.location, R.drawable.settings};
        String titles [] = {"Car", "Chat", "Download", "Location", "Settings"};

        for (int i=0; i<icons.length && i<titles.length; i++)
        {
            Information current = new Information();
            current.iconId = icons[i];
            current.tittle = titles[i];

            data.add(current);
        }
        return data;
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar)
    {
        mDrawerLayout = drawerLayout;
        containerView = getActivity().findViewById(fragmentId);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);

                if (!mUserLearnedDrawer)
                {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {

                if (slideOffset<0.6)
                {
                    toolbar.setAlpha(1-slideOffset);
                }


            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState)
        {
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences (Context context, String preferenceName, String preferenceValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences (Context context, String preferenceName, String defaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void myItemClicked(View view, int position)
    {
        startActivity(new Intent(getActivity(), SubActivity.class));

    }
}
