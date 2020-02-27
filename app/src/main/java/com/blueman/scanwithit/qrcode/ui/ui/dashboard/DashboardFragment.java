package com.blueman.scanwithit.qrcode.ui.ui.dashboard;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.blueman.scanwithit.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.vo.DateData;


public class DashboardFragment extends Fragment implements OnChartValueSelectedListener {

    private DashboardViewModel dashboardViewModel;

    private  MCalendarView calendarView;
    private PieChart pieChart;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);

        calendarView = root.findViewById(R.id.calendarView);


        pieChart = root.findViewById(R.id.piechart);
//        dashboardViewModel.getText().observe(getActivity(), s -> textView.setText(s));

        pieChart.setUsePercentValues(true);


        markCalenderDates();
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();




        ArrayList<DateData> dates=new ArrayList<>();
        dates.add(new DateData(2020, 2,25));
        dates.add(new DateData(2020, 2,17));

        for(int i=0;i<dates.size();i++) {
            calendarView.setMarkedStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.colorAbsent));
            calendarView.markDate(dates.get(i).getYear(),dates.get(i).getMonth(),dates.get(i).getDay());
        }
//        calendarView.markDate(new DateData(year, month, day).setMarkStyle(new MarkStyle(MarkStyle.DOT, getResources().getColor(R.color.colorAbsent))));
        setUpPieChart();

        return root;
    }

    private void markCalenderDates() {

    }

    private void setUpPieChart() {
        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(8f, 0));
        yvalues.add(new Entry(15f, 1));
        yvalues.add(new Entry(3f, 2));


        PieDataSet dataSet = new PieDataSet(yvalues, "Attendance Results");
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Absent");
        xVals.add("Present");
        xVals.add("Late");


        PieData data = new PieData(xVals, dataSet);
        // In Percentage term
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(13f);

        pieChart.setData(data);                 
        pieChart.setDescription("");
//        pieChart.setDescriptionTextSize(13f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(25f);
        pieChart.setHoleRadius(25f);

        dataSet.setColors(new int[]{R.color.colorAbsent , R.color.colorPresent, R.color.md_light_blue_200} , getActivity());
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.setOnChartValueSelectedListener(this);

        pieChart.animateXY(1400, 1400);


    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

}