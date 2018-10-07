package com.chen4393c.vicinity.model;

public class TrafficEvent {
    private String id;
    private String eventType;
    private int mEventCommentNumber;
    private long mEventTimestamp;
    private double mEventLongitude;
    private double mEventLatitude;
    private String mEventReporterId;
    private String mEventLevel;
    private int mEventLikeNumber;
    private String mEventDescription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String event_type) {
        this.eventType = event_type;
    }

    public int getEventCommentNumber() {
        return mEventCommentNumber;
    }

    public void setEventCommentNumber(int event_comment_number) {
        this.mEventCommentNumber = event_comment_number;
    }

    public long getEventTimestamp() {
        return mEventTimestamp;
    }

    public void setEventTimestamp(long event_timestamp) {
        this.mEventTimestamp = event_timestamp;
    }

    public double getEventLongitude() {
        return mEventLongitude;
    }

    public void setEventLongitude(double event_longitude) {
        this.mEventLongitude = event_longitude;
    }

    public double getEventLatitude() {
        return mEventLatitude;
    }

    public void setEventLatitude(double event_latitude) {
        this.mEventLatitude = event_latitude;
    }

    public String getEventReporterId() {
        return mEventReporterId;
    }

    public void setEventReporterId(String event_reporter_id) {
        this.mEventReporterId = event_reporter_id;
    }

    public String getEventLevel() {
        return mEventLevel;
    }

    public void setEventLevel(String event_level) {
        this.mEventLevel = event_level;
    }

    public int getEventLikeNumber() {
        return mEventLikeNumber;
    }

    public void setEventLikeNumber(int event_like_number) {
        this.mEventLikeNumber = event_like_number;
    }

    public String getEventDescription() {
        return mEventDescription;
    }

    public void setEventDescription(String event_description) {
        this.mEventDescription = event_description;
    }
}

