package com.ofk.bd.extractorlibrary.model;

import java.util.List;

public class PlayerResponse {
    private PlayabilityStatus playabilityStatus;
    private StreamingData streamingData;
    private YoutubeMeta videoDetails;
    private Captions captions;
    private String playerJs;

    public String getPlayerJs() {
        if (playerJs.startsWith("http") && playerJs.contains("youtube.com")) {
            return playerJs.replace("\\", "");
        } else {
            return "https://www.youtube.com" + playerJs.replace("\\", "");
        }
    }

    public void setPlayerJs(String playerJs) {
        this.playerJs = playerJs;
    }

    public Captions getCaptions() {
        return captions;
    }

    public void setCaptions(Captions captions) {
        this.captions = captions;
    }

    public PlayabilityStatus getPlayabilityStatus() {
        return playabilityStatus;
    }

    public void setPlayabilityStatus(PlayabilityStatus playabilityStatus) {
        this.playabilityStatus = playabilityStatus;
    }

    public StreamingData getStreamingData() {
        return streamingData;
    }

    public void setStreamingData(StreamingData streamingData) {
        this.streamingData = streamingData;
    }

    public YoutubeMeta getVideoDetails() {
        return videoDetails;
    }

    public void setVideoDetails(YoutubeMeta videoDetails) {
        this.videoDetails = videoDetails;
    }

    public class Captions {
        private PlayerCaptionsTracklistRenderer playerCaptionsTracklistRenderer;

        public PlayerCaptionsTracklistRenderer getPlayerCaptionsTracklistRenderer() {
            return playerCaptionsTracklistRenderer;
        }

        public void setPlayerCaptionsTracklistRenderer(PlayerCaptionsTracklistRenderer playerCaptionsTracklistRenderer) {
            this.playerCaptionsTracklistRenderer = playerCaptionsTracklistRenderer;
        }

        public class PlayerCaptionsTracklistRenderer {
            private List<YTSubtitles> captionTracks;

            public List<YTSubtitles> getCaptionTracks() {
                return captionTracks;
            }

            public void setCaptionTracks(List<YTSubtitles> captionTracks) {
                this.captionTracks = captionTracks;
            }
        }
    }


    public class PlayabilityStatus {
        private String status;
        private boolean playableInEmbed;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isPlayableInEmbed() {
            return playableInEmbed;
        }

        public void setPlayableInEmbed(boolean playableInEmbed) {
            this.playableInEmbed = playableInEmbed;
        }
    }
}


