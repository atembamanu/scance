package com.blueman.scanwithit.qrcode.ui.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.blueman.scanwithit.R;
import com.blueman.scanwithit.qrcode.models.attendanceStatus.Attendance;
import com.blueman.scanwithit.qrcode.models.network.ApiClient;
import com.blueman.scanwithit.qrcode.models.network.ApiInterface;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.vo.DateData;


public class DashboardFragment extends Fragment implements OnChartValueSelectedListener {

    private static final String TAG = "DashboardFragment";
    private DashboardViewModel dashboardViewModel;

    private MCalendarView calendarView;
    private PieChart pieChart;
    private ApiInterface apiInterface;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);

        SharedPreferences pref = Objects.requireNonNull(this.getActivity()).getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private
        final int studentId = pref.getInt("student_id", 0);


        calendarView = root.findViewById(R.id.calendarView);


        pieChart = root.findViewById(R.id.piechart);
//        dashboardViewModel.getText().observe(getActivity(), s -> textView.setText(s));

        pieChart.setUsePercentValues(true);


        //get and mark present dates
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //getPresentdates
        Call<Attendance> call = apiInterface.getPresentDates(studentId);
        call.enqueue(new Callback<Attendance>() {
            @Override
            public void onResponse(Call<Attendance> call, Response<Attendance> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        List<String> presentDates = response.body().getData();

                        ArrayList<DateData> dates = new ArrayList<>();

                        for (String presentDate : presentDates) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate localDate = LocalDate.parse(presentDate, formatter);

                            int year = localDate.getYear();
                            int month = localDate.getMonthValue();
                            int day = localDate.getDayOfMonth();

                            Log.d(TAG, "onResponse: "+ year +"...."+month+"...."+day);

                            dates.add(new DateData(year, month, day));


                        }

                            for (int i = 0; i < dates.size(); i++) {
                                calendarView.setMarkedStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.colorPresent));
                                calendarView.markDate(dates.get(i).getYear(), dates.get(i).getMonth(), dates.get(i).getDay());
                        }
                    }else{
                        //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Attendance> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });



        //get and save latedates
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //getPresentdates
        Call<Attendance> call2 = apiInterface.getLateDates(studentId);
        call2.enqueue(new Callback<Attendance>() {
            @Override
            public void onResponse(Call<Attendance> call, Response<Attendance> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        List<String> lateDates = response.body().getData();

                        ArrayList<DateData> dates = new ArrayList<>();

                        for (String lateDate : lateDates) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate localDate = LocalDate.parse(lateDate, formatter);

                            int year = localDate.getYear();
                            int month = localDate.getMonthValue();
                            int day = localDate.getDayOfMonth();

                            dates.add(new DateData(year, month, day));


                        }

                        for (int i = 0; i < dates.size(); i++) {
                            calendarView.setMarkedStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.colorAbsent));
                            calendarView.markDate(dates.get(i).getYear(), dates.get(i).getMonth(), dates.get(i).getDay());
                        }
                    }else{
                        //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Attendance> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });



        setUpPieChart();

        return root;
    }

    private void setUpPieChart() {
        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(3f, 0));
        yvalues.add(new Entry(15f, 1));


        PieDataSet dataSet = new PieDataSet(yvalues, "Attendance Results");
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Late");
        xVals.add("On Time");



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

        dataSet.setColors(new int[]{R.color.colorAbsent, R.color.colorPresent}, getActivity());
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