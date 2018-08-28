package com.example.razvan.licenta.Menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.razvan.licenta.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class GraphAdapter extends BaseAdapter {
    Context context;
    private List<GraphList> lists;


    public GraphAdapter(Context context, List<GraphList> lists){
        this.context = context;
        this.lists = lists;

    }


    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) 0;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(final int position, View itemView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.piegraph_list, null);

        TextView txtName = (TextView)view.findViewById(R.id.piegraph_name);
        TextView txtAmount = (TextView) view.findViewById(R.id.piegraph_amount);
        TextView txtPercent = (TextView) view.findViewById(R.id.piegraph_percent);
        DecimalFormat precision = new DecimalFormat("0.00");

        double sum;

        sum = lists.stream()
                .collect(Collectors.summarizingDouble(GraphList::getAmount))
                .getSum();

        txtName.setText(lists.get(position).getName());
        txtAmount.setText(String.valueOf(precision.format(lists.get(position).getAmount())));
        if(!txtAmount.toString().equals("")){
            double amount = Double.parseDouble(txtAmount.getText().toString());
            double res = (amount*100 / sum);
            txtPercent.setText(String.valueOf(precision.format(res)+ " %"));
        }

        return view;
    }


}
