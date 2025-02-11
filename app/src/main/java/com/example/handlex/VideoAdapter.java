package com.example.handlex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private final String[] videoIds;
    private final String[] lessonTitles;
    private final OnLessonCompleteListener listener;

    public interface OnLessonCompleteListener {
        void onLessonCompleted(int lessonNumber);
    }

    public VideoAdapter(String[] videoIds, String[] lessonTitles, OnLessonCompleteListener listener) {
        this.videoIds = videoIds;
        this.lessonTitles = lessonTitles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.lessonTitle.setText(lessonTitles[position]);
        holder.youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
            youTubePlayer.loadVideo(videoIds[position], 0);
        });

        holder.itemView.setOnClickListener(v -> {
            listener.onLessonCompleted(position + 1); // Notify lesson completion
        });
    }

    @Override
    public int getItemCount() {
        return videoIds.length;
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        YouTubePlayerView youTubePlayerView;
        TextView lessonTitle;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubePlayerView = itemView.findViewById(R.id.youtubePlayerView);
            lessonTitle = itemView.findViewById(R.id.lessonTitle);
        }
    }
}