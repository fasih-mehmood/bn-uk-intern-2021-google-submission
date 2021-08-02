package com.google;

import java.util.*;

public class VideoPlayer {

    private final VideoLibrary videoLibrary;
    private Video currentPlayingVid;
    private boolean isPlaying;
    private Map<String, VideoPlaylist> playlists;

    public VideoPlayer() {
        this.videoLibrary = new VideoLibrary();
        this.playlists = new HashMap<>();
        currentPlayingVid = null;
        isPlaying = false;
    }

    public void numberOfVideos() {
        System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
    }

    public void showAllVideos() {
        System.out.println("Here's a list of all available videos:");
        var vids = videoLibrary.getVideos();
        Collections.sort(vids, Comparator.comparing(Video::getTitle));
        vids.forEach(vid -> {
            System.out.print("  " + vid);
            if (this.videoLibrary.isFlagged(vid.getVideoId())) {
                System.out.printf(" - FLAGGED (reason: %s)", this.videoLibrary.getFlaggedReason(vid.getVideoId()));
            }
            System.out.println();
        });
    }

    public void playVideo(String videoId) {
        if (this.videoLibrary.isFlagged(videoId)) {
            System.out.printf("Cannot play video: Video is currently flagged (reason: %s)%n", this.videoLibrary.getFlaggedReason(videoId));
        } else {
            var vid = this.videoLibrary.getVideo(videoId);
            if (vid == null) {
                System.out.println("Cannot play video: Video does not exist");
            } else {
                Video currentVid = this.currentPlayingVid;
                if (currentVid != null) {
                    System.out.println("Stopping video: " + currentVid.getTitle());
                }
                currentPlayingVid = vid;
                isPlaying = true;
                System.out.println("Playing video: " + vid.getTitle());
            }
        }
    }

    public void stopVideo() {
        var vid = this.currentPlayingVid;
        if (vid == null) {
            System.out.println("Cannot stop video: No video is currently playing");
        } else {
            currentPlayingVid = null;
            isPlaying = false;
            System.out.println("Stopping video: " + vid.getTitle());
        }
    }

    public void playRandomVideo() {
        var vid = this.currentPlayingVid;
        if (vid != null) {
            System.out.println("Stopping video: " + vid.getTitle());
        }
        List<Video> vids = this.videoLibrary.getPlayableVideos();
        if (vids.size() < 1) {
            System.out.println("No videos available");
        } else {
            Random random = new Random();
            Video newVid = vids.get(random.nextInt(vids.size()));
            currentPlayingVid = newVid;
            isPlaying = true;
            System.out.println("Playing video: " + newVid.getTitle());
        }
    }

    public void pauseVideo() {
        var vid = this.currentPlayingVid;
        if (vid == null) {
            System.out.println("Cannot pause video: No video is currently playing");
        } else {
            if (this.isPlaying) {
                System.out.println("Pausing video: " + vid.getTitle());
            } else {
                System.out.println("Video already paused: " + vid.getTitle());
            }
            this.isPlaying = false;
        }
    }

    public void continueVideo() {
        var vid = this.currentPlayingVid;
        if (vid == null) {
            System.out.println("Cannot continue video: No video is currently playing");
        } else {
            if (this.isPlaying) {
                System.out.println("Cannot continue video: Video is not paused");
            } else {
                System.out.println("Continuing video: " + vid.getTitle());
            }
            this.isPlaying = true;
        }
    }

    public void showPlaying() {
        var vid = this.currentPlayingVid;
        if (vid == null) {
            System.out.println("No video is currently playing");
        } else {
            String paused = "";
            if (!this.isPlaying) {
                paused = "- PAUSED";
            }
            System.out.printf("Currently playing: %s %s%n", vid, paused);
        }
    }

    public void createPlaylist(String playlistName) {
        if (this.playlists.containsKey(playlistName.toLowerCase(Locale.ROOT))) {
            System.out.println("Cannot create playlist: A playlist with the same name already exists");
        } else {
            this.playlists.put(playlistName.toLowerCase(Locale.ROOT), new VideoPlaylist(playlistName));
            System.out.println("Successfully created new playlist: " + playlistName);
        }
    }

    public void addVideoToPlaylist(String playlistName, String videoId) {
        if (this.videoLibrary.isFlagged(videoId)) {
            System.out.printf("Cannot add video to %s: Video is currently flagged (reason: %s)%n", playlistName, this.videoLibrary.getFlaggedReason(videoId));
        } else {
            if (!this.playlists.containsKey(playlistName.toLowerCase(Locale.ROOT))) {
                System.out.printf("Cannot add video to %s: Playlist does not exist%n", playlistName);
            } else if (this.videoLibrary.getVideo(videoId) == null) {
                System.out.printf("Cannot add video to %s: Video does not exist%n", playlistName);
            } else {
                var playlist = this.playlists.get(playlistName.toLowerCase(Locale.ROOT));
                if (playlist.containsVideo(videoId)) {
                    System.out.printf("Cannot add video to %s: Video already added%n", playlistName);
                } else {
                    playlist.addVideo(videoId);
                    System.out.printf("Added video to %s: %s%n", playlistName, this.videoLibrary.getVideo(videoId).getTitle());
                }
            }
        }
    }

    public void showAllPlaylists() {
        if (this.playlists.isEmpty()) {
            System.out.println("No playlists exist yet");
        } else {
            System.out.println("Showing all playlists:");
            playlists.values().forEach(playlist -> {
                System.out.println("  " + playlist.getName());
            });
        }
    }

    public void showPlaylist(String playlistName) {
        if (!this.playlists.containsKey(playlistName.toLowerCase(Locale.ROOT))) {
            System.out.printf("Cannot show playlist %s: Playlist does not exist%n", playlistName);
        } else {
            System.out.println("Showing playlist: " + playlistName);
            var vids = this.playlists.get(playlistName.toLowerCase(Locale.ROOT)).getVideoList();
            if (vids.isEmpty()) {
                System.out.println("  No videos here yet");
            } else {
                vids.forEach(videoId -> {
                    System.out.print("  " + this.videoLibrary.getVideo(videoId));
                    if (this.videoLibrary.isFlagged(videoId)) {
                        System.out.printf(" - FLAGGED (reason: %s)", this.videoLibrary.getFlaggedReason(videoId));
                    }
                    System.out.println();
                });
            }
        }
    }

    public void removeFromPlaylist(String playlistName, String videoId) {
        if (!this.playlists.containsKey(playlistName.toLowerCase(Locale.ROOT))) {
            System.out.printf("Cannot remove video from %s: Playlist does not exist%n", playlistName);
        } else if (this.videoLibrary.getVideo(videoId) == null) {
            System.out.printf("Cannot remove video from %s: Video does not exist%n", playlistName);
        } else {
            var playlist = this.playlists.get(playlistName.toLowerCase(Locale.ROOT));
            if (playlist.containsVideo(videoId)) {
                playlist.removeVideo(videoId);
                System.out.printf("Removed video from %s: %s%n", playlistName, this.videoLibrary.getVideo(videoId).getTitle());
            } else {
                System.out.printf("Cannot remove video from %s: Video is not in playlist%n", playlistName);
            }
        }
    }

    public void clearPlaylist(String playlistName) {
        if (!this.playlists.containsKey(playlistName.toLowerCase(Locale.ROOT))) {
            System.out.printf("Cannot clear playlist %s: Playlist does not exist%n", playlistName);
        } else {
            this.playlists.get(playlistName.toLowerCase(Locale.ROOT)).clearVideoList();
            System.out.println("Successfully removed all videos from " + playlistName);
        }
    }

    public void deletePlaylist(String playlistName) {
        if (!this.playlists.containsKey(playlistName.toLowerCase(Locale.ROOT))) {
            System.out.printf("Cannot delete playlist %s: Playlist does not exist%n", playlistName);
        } else {
            this.playlists.remove(playlistName.toLowerCase(Locale.ROOT));
            System.out.println("Deleted playlist: " + playlistName);
        }
    }

    public void searchVideos(String searchTerm) {
        List<Video> vids = new ArrayList<>();
        this.videoLibrary.getPlayableVideos().forEach(vid -> {
            if (vid.getTitle().toLowerCase(Locale.ROOT).contains(searchTerm.toLowerCase(Locale.ROOT))) {
                vids.add(vid);
            }
        });
        displaySearchResult(searchTerm, vids);
    }

    public void searchVideosWithTag(String videoTag) {
        List<Video> vids = new ArrayList<>();
        this.videoLibrary.getPlayableVideos().forEach(vid -> {
            if (vid.getTags().contains(videoTag)) {
                vids.add(vid);
            }
        });
        displaySearchResult(videoTag, vids);
    }

    private void displaySearchResult(String searchTerm, List<Video> searchResult) {
        if (searchResult.isEmpty()) {
            System.out.println("No search results for " + searchTerm);
        } else {
            System.out.printf("Here are the results for %s:%n", searchTerm);
            Collections.sort(searchResult, Comparator.comparing(Video::getTitle));
            int counter = 0;
            for (var vid : searchResult) {
                counter++;
                System.out.println("  " + counter + ") " + vid);
            }
            System.out.println("Would you like to play any of the above? If yes, specify the number of the video.\n"
                    + "If your answer is not a valid number, we will assume it's a no.");
            Scanner in = new Scanner(System.in);
            try {
                var input = in.nextInt();
                if (input > 0 && input <= counter) {
                    playVideo(searchResult.get(input - 1).getVideoId());
                }
            } catch (InputMismatchException e) {
            }
        }
    }

    public void flagVideo(String videoId) {
        flagVideo(videoId, "Not supplied");
    }

    public void flagVideo(String videoId, String reason) {
        var vid = this.videoLibrary.getVideo(videoId);
        if (vid == null) {
            System.out.println("Cannot flag video: Video does not exist");
        } else if (this.videoLibrary.isFlagged(videoId)) {
            System.out.println("Cannot flag video: Video is already flagged");
        } else {
            if (this.currentPlayingVid != null && this.currentPlayingVid.equals(vid)) {
                stopVideo();
            }
            System.out.printf("Successfully flagged video: %s (reason: %s)%n", vid.getTitle(), reason);
            this.videoLibrary.flagVideo(videoId, reason);
        }
    }

    public void allowVideo(String videoId) {
        var vid = this.videoLibrary.getVideo(videoId);
        if (vid == null) {
            System.out.println("Cannot remove flag from video: Video does not exist");
        } else if (!this.videoLibrary.isFlagged(videoId)) {
            System.out.println("Cannot remove flag from video: Video is not flagged");
        } else {
            this.videoLibrary.allowVideo(videoId);
            System.out.println("Successfully removed flag from video: " + vid.getTitle());
        }
    }
}
