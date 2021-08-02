package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {
    String playlistName;
    List<String> videoList;

    public VideoPlaylist(String playlistName) {
        videoList = new ArrayList<>();
        this.playlistName = playlistName;
    }

    public void addVideo(String videoID) {
        videoList.add(videoID);
    }

    public void removeVideo(String videoID) {
        videoList.remove(videoID);
    }

    public boolean containsVideo(String videoID) {
        return videoList.contains(videoID);
    }

    public void clearVideoList() {
        videoList.clear();
    }

    public String getName() {
        return this.playlistName;
    }

    public List<String> getVideoList() {
        return new ArrayList<>(videoList);
    }
}
