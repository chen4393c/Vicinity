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

    public void setEventCommentNumber(int eventCommentNumber) {
        this.mEventCommentNumber = eventCommentNumber;
    }

    public long getEventTimestamp() {
        return mEventTimestamp;
    }

    public void setEventTimestamp(long eventTimestamp) {
        this.mEventTimestamp = eventTimestamp;
    }

    public double getEventLongitude() {
        return mEventLongitude;
    }

    public void setEventLongitude(double eventLongitude) {
        this.mEventLongitude = eventLongitude;
    }

    public double getEventLatitude() {
        return mEventLatitude;
    }

    public void setEventLatitude(double eventLatitude) {
        this.mEventLatitude = eventLatitude;
    }

    public String getEventReporterId() {
        return mEventReporterId;
    }

    public void setEventReporterId(String eventReporterId) {
        this.mEventReporterId = eventReporterId;
    }

    public String getEventLevel() {
        return mEventLevel;
    }

    public void setEventLevel(String eventLevel) {
        this.mEventLevel = eventLevel;
    }

    public int getEventLikeNumber() {
        return mEventLikeNumber;
    }

    public void setEventLikeNumber(int eventLikeNumber) {
        this.mEventLikeNumber = eventLikeNumber;
    }

    public String getEventDescription() {
        return mEventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.mEventDescription = eventDescription;
    }
}

