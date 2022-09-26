package com.example.gameban;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.gameban.databinding.ActivityVideoBinding;
import com.example.gameban.retrofit.YoutubeRetrofitInterface;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;


public class Video extends YouTubeBaseActivity {
    private static final String youtubeApiKey = "AIzaSyDuaeGrPBo8xX4WJLu9ByNMHJh5-ZcKW7s";
    private ActivityVideoBinding binding;
    private YoutubeRetrofitInterface youtubeRetrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Register this activity in activityContainer
        activityContainer.getInstance().addActivity(this);

        //Receive the video related information from home fragment via the bundle
        Bundle videoBundle = getIntent().getExtras();
        String videoId = videoBundle.get("videoId").toString();
        String videoTitle = videoBundle.get("videoTitle").toString();
        String videoPublishDate = videoBundle.get("videoPublishDate").toString();
        String videoDescription = videoBundle.get("videoDescription").toString();

        //Format videoPublishDate (Remove "T" and "Z")
        videoPublishDate = videoPublishDate.substring(0, videoPublishDate.length() - 1);
        videoPublishDate = (videoPublishDate.split("T")[0] + " " + videoPublishDate.split("T")[1]);

        //Initialize the video player and play the video via video ID
        binding.youtubePlayerView.initialize(youtubeApiKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(videoId);

                //Following are various interactive button
                //Pause & continue button
                binding.pauseCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (youTubePlayer.isPlaying())
                            youTubePlayer.pause();
                        else
                            youTubePlayer.play();
                    }
                });

                //Jump to button
                binding.jumpToButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int hour = binding.jumpToTimeHour.getText().toString().equals("") ? 0 : (Integer.parseInt(binding.jumpToTimeHour.getText().toString()) * 3600000);
                        int minute = binding.jumpToTimeMinute.getText().toString().equals("") ? 0 : (Integer.parseInt(binding.jumpToTimeMinute.getText().toString()) * 60000);
                        int second = binding.jumpToTimeSecond.getText().toString().equals("") ? 0 : (Integer.parseInt(binding.jumpToTimeSecond.getText().toString()) * 1000);
                        int jumpTime = hour + minute + second;

                        if (jumpTime > youTubePlayer.getDurationMillis())
                            Toast.makeText(view.getContext(), "The jump time point is greater than the total length of the video!", Toast.LENGTH_LONG).show();
                        else if (jumpTime < 0)
                            Toast.makeText(view.getContext(), "Error, try jumping back to negative time", Toast.LENGTH_LONG).show();
                        else
                            youTubePlayer.seekToMillis(jumpTime);
                    }
                });

                //style change switch
                binding.styleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isSwitch) {
                        if (isSwitch)
                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                        else
                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    }
                });

                //Forward ten second button
                binding.forwardTenSecondButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        youTubePlayer.seekRelativeMillis(10000);
                    }
                });

                //Back ten second button
                binding.backTenSecondButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        youTubePlayer.seekRelativeMillis(-10000);
                    }
                });

                //Get duration button
                binding.getDurationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Get the duration of current video
                        int durationInMillisecond = youTubePlayer.getDurationMillis();

                        //Calculate the hour part of the duration
                        int durationInHour = (int) (durationInMillisecond / 3600000.0);
                        //Calculate the minute part of the duration
                        int durationInMinute = (int) ((durationInMillisecond - (durationInHour * 3600000)) / 60000.0);
                        //Calculate the second part of the duration
                        int durationInSecond = (int) ((durationInMillisecond - (durationInMinute * 60000)) / 1000.0);

                        //Set the textViews
                        binding.getDurationHour.setText(Integer.toString(durationInHour));
                        binding.getDurationMinute.setText(Integer.toString(durationInMinute));
                        binding.getDurationSecond.setText(Integer.toString(durationInSecond));
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(view.getContext(), "Does not find related videos!", Toast.LENGTH_LONG).show();
            }
        });

        binding.videoTitle.setText("💬 Title: " + videoTitle);
        binding.videoPublishDate.setText("🕓 Publish Date: " + videoPublishDate);
        binding.videoDescription.setText("📄 Description: " + videoDescription);

    }
}