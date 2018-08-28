package com.example.razvan.licenta.Menu;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.razvan.licenta.Main.MainActivity;
import com.example.razvan.licenta.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraphAll extends Fragment {

    public GraphAll(){

    }

    PieChart mChart;
    ListView listView;
    List<GraphList> listOverview;
    GraphAdapter adapterOverview;
    ArrayAdapter arrayAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_piegraph, container, false);

        mChart = (PieChart) view.findViewById(R.id.pieChartGraph);
        listView = (ListView)view.findViewById(R.id.pieGraphListView);

        loadListView();
        addData();

        Legend l=mChart.getLegend();
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        return view;
    }

    private void addData() {
        final ArrayList<PieEntry> yVals = new ArrayList<PieEntry>();
        final List<String> xVals = new ArrayList<String>();

        for (int i = 0; i < listOverview.size(); i++) {
            yVals.add(new PieEntry((float) listOverview.get(i).getAmount(), i));
            xVals.add(listOverview.get(i).getName());
        }
        Log.e("si pe aici", " trece");


        //create pie data set
        PieDataSet dataSet=new PieDataSet(yVals,"");
        dataSet.setSliceSpace(3); // space between each arc slice
        dataSet.setSelectionShift(5);

        //add many colors
        ArrayList<Integer> colors=new ArrayList<Integer>();

        for(int c: ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for(int c: ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for(int c: ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for(int c: ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for(int c: ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data=new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();
        mChart.animateXY(2000, 2000);
        mChart.setHoleRadius(70f);
        mChart.setCenterText("Expense\nBudget");
        mChart.setCenterTextSize(30);
        mChart.setCenterTextColor(Color.CYAN);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void loadListView() {
        listOverview = MainActivity.getGraphList();
        adapterOverview = new GraphAdapter(getContext(), listOverview);
        listView.setAdapter(adapterOverview);
    }

}
