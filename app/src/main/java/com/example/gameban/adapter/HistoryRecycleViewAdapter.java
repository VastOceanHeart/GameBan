package com.example.gameban.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameban.databinding.HistoryRvLayoutBinding;
import com.example.gameban.model.SearchHistory;

import java.util.List;

public class HistoryRecycleViewAdapter extends RecyclerView.Adapter<HistoryRecycleViewAdapter.ViewHolder> {
    private List<SearchHistory> searchHistories;

    public HistoryRecycleViewAdapter(List<SearchHistory> searchHistories) {
        this.searchHistories = searchHistories;
    }

    //Creates a new viewholder that is constructed with a new View, inflated from a layout
    @Override
    public HistoryRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Binding the cardView with this RecyclerViewAdapter
        HistoryRvLayoutBinding binding =
                HistoryRvLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    //This method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull HistoryRecycleViewAdapter.ViewHolder viewHolder, int position) {
        final SearchHistory searchHistory = searchHistories.get(position);
        viewHolder.binding.historyRvGameId.setText("📄 Game ID: " + searchHistory.getGameId());
        viewHolder.binding.historyRvGameName.setText("🎮 Game Name: " + searchHistory.getGameName());
        viewHolder.binding.historyRvGamePrice.setText("🧾 Game Price: " + searchHistory.getGamePrice());
        viewHolder.binding.historyRvGamePraiseRate.setText("🤍 Game Praise Rate: " + Double.toString(searchHistory.getGamePraiseRate()) + "%");
    }

    @Override
    public int getItemCount() {
        return searchHistories.size();
    }

    public void addSearchHistories(List<SearchHistory> histories) {
        searchHistories = histories;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private HistoryRvLayoutBinding binding;

        public ViewHolder(HistoryRvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
