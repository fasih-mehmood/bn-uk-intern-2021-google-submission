package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = new ignoreCaseStringArrayList();
    this.tags.addAll(tags);
  }
  
  /** Returns video info */
  public String toString() {
    return title + " (" + videoId + ") [" + String.join(" ", tags) + "]";
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }
  
  class ignoreCaseStringArrayList extends ArrayList<String> {
    @Override
    public boolean contains(Object o) {
      String otherStr = (String)o;
      return this.stream().anyMatch(str -> (str.equalsIgnoreCase(otherStr)));
    }
  }
}
