package com.example.gameban.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameban.adapter.HistoryRecycleViewAdapter;
import com.example.gameban.databinding.FragmentHistoryBinding;
import com.example.gameban.entity.AppUser;
import com.example.gameban.model.SearchHistory;
import com.example.gameban.retrofit.SearchSteamSpyResponse;
import com.example.gameban.retrofit.SteamSpyRetrofitClient;
import com.example.gameban.retrofit.SteamSpyRetrofitInterface;
import com.example.gameban.viewmodel.AppUserViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    //RecycleView related
    private RecyclerView.LayoutManager layoutManager;
    private List<SearchHistory> searchHistories;
    private HistoryRecycleViewAdapter historyRecycleViewAdapter;

    //Retrofit related
    private SteamSpyRetrofitInterface steamSpyRetrofitInterface;

    //Room related
    private AppUserViewModel appUserViewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //Initialise the Retrofit interface for steam spy
        steamSpyRetrofitInterface = SteamSpyRetrofitClient.getRetrofitService();

        //Get current login user's email
        Bundle loginBundle = getActivity().getIntent().getExtras();
        String currentUserEmail = loginBundle.get("email").toString();

        //Interactive with the room
        appUserViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(AppUserViewModel.class);

        //Get all app users via the live data and observe
        appUserViewModel.getAllAppUsers().observe(getViewLifecycleOwner(), new Observer<List<AppUser>>() {
            @Override
            public void onChanged(@NonNull List<AppUser> appUsers) {
                for (AppUser appUser : appUsers) {
                    //Find the current appUser via comparing the email address
                    if (appUser.email.equals(currentUserEmail)) {

                        //After find the matched appUser (Current appUser), continue to fill recycler view
                        searchHistories = new ArrayList<SearchHistory>();
                        historyRecycleViewAdapter = new HistoryRecycleViewAdapter(searchHistories);

                        //Use recycle view to display the detail information about search history
                        //Initial recyclerView
                        binding.historyRecyclerView.setAdapter(historyRecycleViewAdapter);
                        layoutManager = new LinearLayoutManager(container.getContext());
                        binding.historyRecyclerView.setLayoutManager(layoutManager);

                        if (!appUser.histories.isEmpty()) {
                            //Display a progressBar to let the user to wait searching result
                            binding.searchProgressBar.setVisibility(View.VISIBLE);

                            //Loop to add all search histories
                            String[] UserSearchHistories = appUser.histories.split("_");

                            //Convert to Set to remove the duplication
                            Set<String> UserSearchHistoriesSet = new HashSet<String>(Arrays.asList(UserSearchHistories));

                            /*
                            For each game id in the history, interact with API to get the name, price and praise rate.
                            And then fill the recycler view
                            */
                            for (String UserSearchHistory : UserSearchHistoriesSet) {
                                String keyword = UserSearchHistory;

                                //Get the name's name via provided appId (In search history)
                                Call<SearchSteamSpyResponse> steamSpyCallAsync = steamSpyRetrofitInterface.steamSpySearch("appdetails", keyword);

                                steamSpyCallAsync.enqueue(new Callback<SearchSteamSpyResponse>() {
                                    @Override
                                    public void onResponse(Call<SearchSteamSpyResponse> spyCall, Response<SearchSteamSpyResponse> spyResponse) {
                                        if (spyResponse.isSuccessful()) {
                                            //After get the result, ending the progressBar
                                            binding.searchProgressBar.setVisibility(View.GONE);

                                            //Fill the recycle view via the data from room and API
                                            String gameName = spyResponse.body().getName();


                                            String gamePrice;
                                            if (spyResponse.body().getPrice() == null) {
                                                gamePrice = "No data";
                                            } else if (spyResponse.body().getPrice().equals("0")) {
                                                gamePrice = "Free";
                                            } else {

                                                gamePrice = Double.toString(Double.parseDouble((spyResponse.body().getPrice())) / 100.0);
                                                gamePrice = "$" + gamePrice;
                                            }

                                            double gamePositive = spyResponse.body().getPositive();
                                            double gameNegative = spyResponse.body().getNegative();
                                            double gamePraiseRate = (int) (gamePositive / (gamePositive + gameNegative) * 10000.0) / 100.0;
                                            SearchHistory searchHistory = new SearchHistory(keyword, gameName, gamePrice, gamePraiseRate);
                                            searchHistories.add(searchHistory);
                                            historyRecycleViewAdapter.addSearchHistories(searchHistories);

                                        } else
                                            Toast.makeText(view.getContext().getApplicationContext(), "This game does not exist!", Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onFailure(Call<SearchSteamSpyResponse> call, Throwable t) {
                                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else
                            binding.historyPageTitle.setText("There is no search history");
                    }
                }
            }
        });
        return view;
    }
}