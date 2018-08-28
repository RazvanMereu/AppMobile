package com.example.razvan.licenta.Menu;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.razvan.licenta.Main.MainActivity;
import com.example.razvan.licenta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Overview extends Fragment {

    private ListView listView;
    List<OverviewListView> listOverview;
    OverviewListAdapter adapterOverview;
    TextView total;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference categoryRef;
    DatabaseReference expenseRef;
    FirebaseUser myUser;
    ProgressDialog mProgressDialog;

    private List<OverviewListView> overviewListViews = MainActivity.getFinalList();

    public Overview() {
        // Empty public constructor
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myUser = FirebaseAuth.getInstance().getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        listView = (ListView) view.findViewById(R.id.list_viewAll);
        total = (TextView)view.findViewById(R.id.txtAmountOverview);
        getTotal(overviewListViews);
        adapterOverview = new OverviewListAdapter(getContext(), overviewListViews);
        listView.setAdapter(adapterOverview);

        return view;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void getTotal(List<OverviewListView> overviewListViews) {

        double sum = 0;

        for (OverviewListView overviewListView : overviewListViews) {
            sum+=overviewListView.getAmount();
        }
        DecimalFormat precision = new DecimalFormat("0.00 Lei");
        total.setText(precision.format(sum));
    }
}