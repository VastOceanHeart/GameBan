package com.example.gameban.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameban.adapter.YoutubeRecycleViewAdapter;
import com.example.gameban.databinding.FragmentHomeBinding;
import com.example.gameban.entity.AppUser;
import com.example.gameban.model.YoutubeVideo;
import com.example.gameban.retrofit.SearchSteamNewsResponse;
import com.example.gameban.retrofit.SearchSteamSpyResponse;
import com.example.gameban.retrofit.SearchYoutubeResponse;
import com.example.gameban.retrofit.SteamNewsItems;
import com.example.gameban.retrofit.SteamRetrofitClient;
import com.example.gameban.retrofit.SteamRetrofitInterface;
import com.example.gameban.retrofit.SteamSpyRetrofitClient;
import com.example.gameban.retrofit.SteamSpyRetrofitInterface;
import com.example.gameban.retrofit.YoutubeItems;
import com.example.gameban.retrofit.YoutubeRetrofitClient;
import com.example.gameban.retrofit.YoutubeRetrofitInterface;
import com.example.gameban.viewmodel.AppUserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The home fragment, which is used to support the searching of game id and display the game newest
 * news and related videos.
 */
public class HomeFragment extends Fragment {

    private static final String youtubeApiKey = "AIzaSyDuaeGrPBo8xX4WJLu9ByNMHJh5-ZcKW7s";
    private String keyword;
    private FragmentHomeBinding binding;

    //Retrofit related
    private YoutubeRetrofitInterface youtubeRetrofitInterface;
    private SteamRetrofitInterface steamRetrofitInterface;
    private SteamSpyRetrofitInterface steamSpyRetrofitInterface;

    //RecycleView related
    private RecyclerView.LayoutManager layoutManager;
    private List<YoutubeVideo> youtubeVideos;
    private YoutubeRecycleViewAdapter youtubeRecycleViewAdapter;

    //Room related
    private AppUserViewModel appUserViewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        //Initialise the Retrofit interface for youtube
        youtubeRetrofitInterface = YoutubeRetrofitClient.getRetrofitService();

        //Initialise the Retrofit interface for steam news
        steamRetrofitInterface = SteamRetrofitClient.getRetrofitService();

        //Initialise the Retrofit interface for steam spy
        steamSpyRetrofitInterface = SteamSpyRetrofitClient.getRetrofitService();

        //Get current login user's email
        Bundle loginBundle = getActivity().getIntent().getExtras();
        String currentUserEmail = loginBundle.get("email").toString();

        //Interactive with the room
        appUserViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(AppUserViewModel.class);
        CompletableFuture<AppUser> appUserCompletableFuture = appUserViewModel.findByEmailFuture(currentUserEmail);

        /**
         * Search game news, game name and related youtube videos via the appid
         */
        binding.searchSteamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the game id from user
                keyword = binding.searchText.getText().toString();

                //Initialise API query via Call<SearchYoutubeResponse> and various parameters
                Call<SearchSteamNewsResponse> steamNewsCallAsync = steamRetrofitInterface.steamNewsSearch(keyword, 5, "json");

                //Request the news about the corresponding game via the provided appId
                steamNewsCallAsync.enqueue(new Callback<SearchSteamNewsResponse>() {
                    @Override
                    public void onResponse(Call<SearchSteamNewsResponse> call, Response<SearchSteamNewsResponse> response) {
                        if (response.isSuccessful() && response.body().getSteamAppNews().getSteamNewsItemsList().size() != 0) {
                            //Display a progressBar to let the user to wait searching result
                            binding.searchProgressBar.setVisibility(View.VISIBLE);

                            //Store the search history for current user
                            appUserCompletableFuture.thenAccept(appUser -> {
                                if (appUser.histories.equals(""))
                                    appUser.histories = keyword;
                                else
                                    appUser.histories = appUser.histories + "_" + keyword;
                                //Update the information of current user into database
                                appUserViewModel.updateAppUser(appUser);
                            });

                            //get the title and url for that games
                            List<SteamNewsItems> steamNewsItems = response.body().getSteamAppNews().getSteamNewsItemsList();
                            String newsTitle = steamNewsItems.get(0).getTitle();
                            String newsUrl = steamNewsItems.get(0).getUrl();

                            //binding.searchSteamNewsResult.setText(result);
                            binding.newsTitle.setText("💬 News Title: " + newsTitle);
                            binding.newsUrl.setText("🌐 News Link: " + newsUrl);


                            /*Get the name's name via provided appId. Due to the reason that several games' data do not exist in Steamdb but
                             exist in Steam spy DB (I do not know why),
                             the order of the nesting cannot change*/
                            Call<SearchSteamSpyResponse> steamSpyCallAsync = steamSpyRetrofitInterface.steamSpySearch("appdetails", keyword);
                            steamSpyCallAsync.enqueue(new Callback<SearchSteamSpyResponse>() {
                                @Override
                                public void onResponse(Call<SearchSteamSpyResponse> spyCall, Response<SearchSteamSpyResponse> spyResponse) {
                                    if (spyResponse.isSuccessful()) {
                                        String gameName = spyResponse.body().getName();
                                        binding.gameName.setText("🎮 Game Name: " + gameName);

                                        //Search related video via the game name
                                        //Initialise API query via Call<SearchYoutubeResponse> and various parameters
                                        Call<SearchYoutubeResponse> youtubeCallAsync = youtubeRetrofitInterface.youtubeSearch("snippet", 50, youtubeApiKey, gameName);

                                        youtubeCallAsync.enqueue(new Callback<SearchYoutubeResponse>() {

                                            @Override
                                            public void onResponse(Call<SearchYoutubeResponse> call,
                                                                   Response<SearchYoutubeResponse> response) {
                                                if (response.isSuccessful()) {
                                                    //After get the result, ending the progressBar
                                                    binding.searchProgressBar.setVisibility(View.GONE);
                                                    List<YoutubeItems> youtubeItemsList = response.body().getYoutubeItemsList();

                                                    //fill the recycle view
                                                    youtubeVideos = new ArrayList<YoutubeVideo>();
                                                    youtubeRecycleViewAdapter = new YoutubeRecycleViewAdapter(getActivity(), youtubeVideos);

                                                    //Use recycle view to display the videos
                                                    binding.youtubeRecyclerView.setAdapter(youtubeRecycleViewAdapter);
                                                    layoutManager = new LinearLayoutManager(container.getContext());
                                                    binding.youtubeRecyclerView.setLayoutManager(layoutManager);
                                                    for (YoutubeItems youtubeItem : youtubeItemsList) {
                                                        YoutubeVideo youtubeVideo = new YoutubeVideo(youtubeItem.getYoutubeId().getVideoId()
                                                                , youtubeItem.getYoutubeSnippet().getTitle()
                                                                , youtubeItem.getYoutubeSnippet().getYoutubeThumbnail().getYoutubeThumbnailDefault().getUrl()
                                                                , youtubeItem.getYoutubeSnippet().getPublishDate()
                                                                , youtubeItem.getYoutubeSnippet().getDesc());
                                                        youtubeVideos.add(youtubeVideo);
                                                        youtubeRecycleViewAdapter.addYoutubeVideos(youtubeVideos);
                                                    }
                                                } else
                                                    Toast.makeText(view.getContext().getApplicationContext(), "Does not find related videos!", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onFailure(Call<SearchYoutubeResponse> call, Throwable t) {
                                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else
                                        Toast.makeText(view.getContext().getApplicationContext(), "This game does not exist!", Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onFailure(Call<SearchSteamSpyResponse> call, Throwable t) {
                                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(view.getContext().getApplicationContext(), "This game does not exist!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchSteamNewsResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        //Click to turn to the specific news website
        binding.newsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the game's news url link
                String newsUrl = binding.newsUrl.getText().toString();
                //If the content in url link is not default model, turn to that news website page
                if (!newsUrl.equals("🌐 News Link")) {
                    //Click to jump to url corresponding website
                    Uri uri = Uri.parse(newsUrl.split("🌐 News Link: ")[1]);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                //If the content in url link is default model, prompt error
                else
                    Toast.makeText(view.getContext().getApplicationContext(), "There is not news", Toast.LENGTH_LONG).show();

            }
        });

        //Clear the input inside the search line
        binding.clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.searchText.setText("");
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}