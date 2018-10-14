package com.chen4393c.vicinity.model;

public class Event {
    private String mId;
    private String mType;
    private int mCommentNumber;
    private long mTimestamp;
    private double mLatitude;
    private double mLongitude;
    private String mReporterId;
    private int mLikeNumber;
    private String mDescription;
    private String mImageUri;
    private Item mItem;

    private Event(EventBuilder builder) {
        mId = builder.mId;
        mType = builder.mType;
        mCommentNumber = builder.mCommentNumber;
        mTimestamp = builder.mTimestamp;
        mLatitude = builder.mLatitude;
        mLongitude = builder.mLongitude;
        mReporterId = builder.mReporterId;
        mLikeNumber = builder.mLikeNumber;
        mDescription = builder.mDescription;
        mImageUri = builder.mImageUri;
        mItem = builder.mItem;
    }

    public String getId() {
        return mId;
    }

    public String getType() {
        return mType;
    }

    public int getCommentNumber() {
        return mCommentNumber;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getReporterId() {
        return mReporterId;
    }

    public int getLikeNumber() {
        return mLikeNumber;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getImageUri() {
        return mImageUri;
    }

    public Item getItem() {
        return mItem;
    }

    public static class EventBuilder {
        private String mId;
        private String mType;
        private int mCommentNumber;
        private long mTimestamp;
        private double mLatitude;
        private double mLongitude;
        private String mReporterId;
        private int mLikeNumber;
        private String mDescription;
        private String mImageUri;
        private Item mItem;

        public EventBuilder setId(String id) {
            mId = id;
            return this;
        }

        public EventBuilder setType(String type) {
            mType = type;
            return this;
        }

        public EventBuilder setCommentNumber(int commentNumber) {
            mCommentNumber = commentNumber;
            return this;
        }

        public EventBuilder setTimestamp(long timestamp) {
            mTimestamp = timestamp;
            return this;
        }

        public EventBuilder setLatitude(double latitude) {
            mLatitude = latitude;
            return this;
        }

        public EventBuilder setLongitude(double longitude) {
            mLongitude = longitude;
            return this;
        }

        public EventBuilder setReporterId(String reporterId) {
            mReporterId = reporterId;
            return this;
        }

        public EventBuilder setLikeNumber(int likeNumber) {
            mLikeNumber = likeNumber;
            return this;
        }

        public EventBuilder setDescription(String description) {
            mDescription = description;
            return this;
        }

        public EventBuilder setImageUri(String imageUri) {
            mImageUri = imageUri;
            return this;
        }

        public EventBuilder setItem(Item item) {
            mItem = item;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}
