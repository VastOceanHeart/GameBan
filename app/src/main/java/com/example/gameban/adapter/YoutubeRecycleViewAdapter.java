package com.example.gameban.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameban.Video;
import com.example.gameban.databinding.YoutubeRvLayoutBinding;
import com.example.gameban.model.YoutubeVideo;

import java.io.InputStream;
import java.util.List;

public class YoutubeRecycleViewAdapter extends RecyclerView.Adapter<YoutubeRecycleViewAdapter.ViewHolder> {

    //Due to the card view in this RecycleView will start the Video activity, this is used to record started activity
    private final androidx.fragment.app.FragmentActivity activity;
    private List<YoutubeVideo> youtubeVideos;

    public YoutubeRecycleViewAdapter(androidx.fragment.app.FragmentActivity activity, List<YoutubeVideo> youtubeVideos) {
        this.activity = activity;
        this.youtubeVideos = youtubeVideos;
    }

    //Creates a new viewholder that is constructed with a new View, inflated from a layout
    @Override
    public YoutubeRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //binding the cardView with this RecyclerViewAdapter
        YoutubeRvLayoutBinding binding =
                YoutubeRvLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    //This method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull YoutubeRecycleViewAdapter.ViewHolder viewHolder, int position) {
        final YoutubeVideo youtubeVideo = youtubeVideos.get(position);
        viewHolder.binding.youtbubeRvTitle.setText(youtubeVideo.getTitle());

        new YoutubeRecycleViewAdapter.DownloadImageTask((ImageView) viewHolder.binding.youtbubeRvThumbnail)
                .execute(youtubeVideo.getThumbnailUrl());

        //After user click one of video, using inner nested video player to play that video
        viewHolder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Video.class);

                //Using bundle to pass video ID and game name
                Bundle bundle = new Bundle();
                if (youtubeVideo.getVideoId() != null) {
                    bundle.putString("videoId", youtubeVideo.getVideoId());
                    bundle.putString("videoTitle", youtubeVideo.getTitle());
                    bundle.putString("videoPublishDate", youtubeVideo.getPublishDate());
                    bundle.putString("videoDescription", youtubeVideo.getDescription());
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
                //Sometimes the return list will including a "Topic" about that game which does not support to play as a video
                else
                    Toast.makeText(view.getContext().getApplicationContext(), "This is a Topic rather than Video", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return youtubeVideos.size();
    }

    public void addYoutubeVideos(List<YoutubeVideo> videos) {
        youtubeVideos = videos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private YoutubeRvLayoutBinding binding;

        public ViewHolder(YoutubeRvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    /**
     * Load image from url
     * <p>
     * Clegg, K. (2012). Load image from url. Retrieved 3 May 2022, from https://stackoverflow.com/questions/5776851/load-image-from-url
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

