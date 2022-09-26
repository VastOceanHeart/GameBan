package com.example.gameban.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.gameban.databinding.FragmentReportBinding;
import com.example.gameban.entity.AppUser;
import com.example.gameban.viewmodel.AppUserViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {
    private FragmentReportBinding binding;
    private AppUserViewModel appUserViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appUserViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(AppUserViewModel.class);
        binding = FragmentReportBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.progressBar.setVisibility(View.INVISIBLE);

        binding.typeButton.setOnClickListener(v -> {
            getAgePieChart();
        });

        binding.LineChartButton.setOnClickListener(v -> {
            getHistoryBarChart();
        });
        return view;
    }

    public void showPieChart() {
        binding.anyChartViewBeta.setVisibility(View.INVISIBLE);
        APIlib.getInstance().setActiveAnyChartView(binding.anyChartView);
        binding.anyChartView.setVisibility(View.VISIBLE);
    }

    public void showBarChart() {
        binding.anyChartView.setVisibility(View.INVISIBLE);
        APIlib.getInstance().setActiveAnyChartView(binding.anyChartViewBeta);
        binding.anyChartViewBeta.setVisibility(View.VISIBLE);
    }

    public AnyChartView getAgePieChart() {
        showPieChart();
        AnyChartView anyChartView = binding.anyChartView;
        binding.progressBar.setVisibility(View.VISIBLE);
        anyChartView.setProgressBar(binding.progressBar);

        List<DataEntry> data = new ArrayList<>();
        appUserViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(AppUserViewModel.class);
        appUserViewModel.getAllAppUsers().observe(getViewLifecycleOwner(), new Observer<List<AppUser>>() {
            @Override
            public void onChanged(List<AppUser> appUsers) {
                int[] ageCount = new int[201];
                int userCount = 0;
                for (AppUser oneUser : appUsers) {
                    ageCount[oneUser.age]++;
                    userCount++;
                }
                for (int index = 0; index < 201; index++) {
                    if (ageCount[index] != 0) {
                        DataEntry a = new ValueDataEntry(Integer.toString(index), ageCount[index]);
                        data.add(a);
                    }
                }
                if (data.isEmpty()) {
                    data.add(new ValueDataEntry("No data", 0));
                }
                Pie pie = AnyChart.pie();
                pie.data(data);
                pie.title("User age distribution (" + userCount + " users in total)");
                pie.labels().position("outside");
                pie.legend().title().enabled(true);
                pie.legend().title()
                        .text("ages")
                        .padding(0d, 0d, 10d, 0d);

                pie.legend()
                        .position("center-bottom")
                        .itemsLayout(LegendLayout.HORIZONTAL)
                        .align(Align.CENTER);

                anyChartView.setChart(pie);
            }
        });
        return anyChartView;
    }

    public AnyChartView getHistoryBarChart() {
        showBarChart();
        AnyChartView anyChartView = binding.anyChartViewBeta;
        binding.progressBar.setVisibility(View.VISIBLE);
        anyChartView.setProgressBar(binding.progressBar);


        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();

        appUserViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(AppUserViewModel.class);
        appUserViewModel.getAllAppUsers().observe(getViewLifecycleOwner(), new Observer<List<AppUser>>() {
            @Override
            public void onChanged(List<AppUser> appUsers) {
                ArrayList<double[]> historyList = new ArrayList<>();

                String games = "";
                for (AppUser oneUser : appUsers) {
                    if (oneUser.histories.isEmpty()) {
                        continue;
                    }
                    games = games + "_" + oneUser.histories;
                }
                if (games.length() >= 2) {
                    games = games.substring(1, games.length());

                    String[] userHistory = games.split("_");
                    for (String oneRecord : userHistory) {
                        boolean find = false;
                        for (double[] oneHistory : historyList) {
                            if (Double.toString(oneHistory[0]).equals(oneRecord + ".0")) {
                                oneHistory[1] = oneHistory[1] + 1.0;
                                find = true;
                                break;
                            }
                        }
                        if (!find) {
                            double[] temp = new double[2];
                            temp[0] = Double.parseDouble(oneRecord);
                            temp[1] = 1;
                            historyList.add(temp);
                        }
                    }
                }

                ArrayList<double[]> top = new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    double[] max = new double[2];
                    max[0] = 0;
                    max[1] = 0;
                    for (double[] oneHistory : historyList) {
                        if (oneHistory[1] > max[1]) {
                            max = oneHistory;
                        }
                    }
                    if (max[1] == 0) {
                        break;
                    }
                    top.add(max);
                    historyList.remove(max);
                }
                for (double[] oneHistory : top) {
                    data.add(new ValueDataEntry("" + (int) oneHistory[0], (int) oneHistory[1]));
                }
                if (data.isEmpty()) {
                    data.add(new ValueDataEntry("No data", 0));
                }
                Column column = cartesian.column(data);

                column.tooltip()
                        .titleFormat("{%X}")
                        .position(Position.CENTER_BOTTOM)
                        .anchor(Anchor.CENTER_BOTTOM)
                        .offsetX(0d)
                        .offsetY(5d)
                        .format("{%Value}{Search times: }");

                cartesian.animation(true);
                cartesian.title("6 games with the most searches");

                cartesian.yScale().minimum(0d);

                cartesian.yAxis(0).labels().format("{%Value}{Search times: }");

                cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                cartesian.interactivity().hoverMode(HoverMode.BY_X);

                cartesian.xAxis(0).title("Game ID");
                cartesian.yAxis(0).title("Search Times");

                cartesian.labels(true);
                cartesian.labels().format("{%value}");

                cartesian.yScale().ticks().allowFractional(false);


                anyChartView.setChart(cartesian);
            }
        });
        return anyChartView;
    }
}