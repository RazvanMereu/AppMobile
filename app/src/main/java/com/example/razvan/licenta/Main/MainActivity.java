package com.example.razvan.licenta.Main;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.razvan.licenta.Expenses.ExpenseAdd;
import com.example.razvan.licenta.Login.LoginActivity;
import com.example.razvan.licenta.Menu.GraphAll;
import com.example.razvan.licenta.Menu.GraphList;
import com.example.razvan.licenta.Menu.History;
import com.example.razvan.licenta.Menu.Overview;
import com.example.razvan.licenta.Menu.OverviewListView;
import com.example.razvan.licenta.R;
import com.example.razvan.licenta.Settings.Setting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private static List<OverviewListView> overviewListViews = new ArrayList<>();
    private static List<OverviewListView> finalList = new ArrayList<>();

    private static List<GraphList> graphList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDataStartActivity(Overview.class, "Overview");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private List<OverviewListView> getCentralizedResults(List<OverviewListView> overviewListViews) {
        List<OverviewListView> listViews = new ArrayList<>();


        return listViews;
    }

    public static List<GraphList> getGraphList() {
        return graphList;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.add:
                startActivity(new Intent(this, ExpenseAdd.class));
                return true;

            case R.id.archive:
                startActivity(new Intent(this, Setting.class));
                return true;

            case R.id.action_1:
                return true;

            case R.id.exit:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static List<OverviewListView> getOverviewListViews() {
        return overviewListViews;
    }

    public static List<OverviewListView> getFinalList() {
        return finalList;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Overview) {
            setTitle("Overview");
            getDataStartActivity(Overview.class, "Overview");
        } else if (id == R.id.History) {
            setTitle("History");
            getDataStartActivity(History.class, "History");
        } else if (id == R.id.Graph_all) {
            setTitle("Graph");
            getDataStartActivity(GraphAll.class, "GraphAll");
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void getDataStartActivity(final Class clazz, final String className) {
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }


        FirebaseUser user = firebaseAuth.getCurrentUser();

        DatabaseReference expenseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Expenses");
        expenseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String category = "";
                String note = "";
                String date = "";
                String amount = "";
                OverviewListView overviewListView;
                overviewListViews = new ArrayList<>();
                finalList = new ArrayList<>();
                graphList = new ArrayList<>();
                for (DataSnapshot expense : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : expense.getChildren()) {
                        if (snapshot.getKey().equals("Category")) {
                            category = snapshot.getValue().toString();
                        }
                        if (snapshot.getKey().equals("Amount")) {
                            amount = snapshot.getValue().toString();
                        }
                        if (snapshot.getKey().equals("Date")) {
                            date = snapshot.getValue().toString();
                        }
                        if (snapshot.getKey().equals("Note")) {
                            note = snapshot.getValue().toString();
                        }
                    }
                    overviewListView = new OverviewListView(category, (double) Float.parseFloat(amount), date, note);
                    overviewListViews.add(overviewListView);
                }

                // overviewListViews = getCentralizedResults(overviewListViews);

                List<OverviewListView> centralizedList = overviewListViews;

                centralizedList.stream()
                        .collect(Collectors.groupingBy(OverviewListView::getName, Collectors.summarizingDouble(OverviewListView::getAmount)))
                        .forEach((categorie, total) -> finalList.add(new OverviewListView(categorie, total.getSum(), "", "")));

                centralizedList.stream()
                        .collect(Collectors.groupingBy(OverviewListView::getName, Collectors.summarizingDouble(OverviewListView::getAmount)))
                        .forEach((categorie, total) -> graphList.add(new GraphList(categorie, total.getSum())));


                Object obj = null;

                try {
                    obj = clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                final Object finalObj = obj;

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, (Fragment) finalObj, className);
                fragmentTransaction.commitAllowingStateLoss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("EROARE: ", databaseError.getMessage());
            }
        });
    }
}
