package com.example.razvan.licenta.Menu;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.razvan.licenta.Main.MainActivity;
import com.example.razvan.licenta.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {
    ListView list;
      TabHistoryWeekAdapter adapter;

    private List<OverviewListView> lists = MainActivity.getOverviewListViews();

    public History()

    {
        // Required empty public constructor
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tabhistory_week, container, false);
        list = (ListView) view.findViewById(R.id.listview_week);

//        DBHelper db = new DBHelper(getContext());
//        lists = db.getHistoryWeek();



        adapter = new TabHistoryWeekAdapter(getContext(), (ArrayList<OverviewListView>) lists);
        list.setAdapter(adapter);

        return view;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.editText) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}