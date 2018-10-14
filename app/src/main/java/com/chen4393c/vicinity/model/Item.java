package com.chen4393c.vicinity.model;

public class Item {

    private int mDrawableId;
    private String mDrawableLabel;

    public Item() {
        // no-argument constructor for Firebase database fetching
    }

    public Item(String drawableLabel, int drawableId) {
        this.mDrawableId = drawableId;
        this.mDrawableLabel = drawableLabel;
    }

    public int getDrawableId() {
        return mDrawableId;
    }

    public void setDrawableId(int drawableId) {
        this.mDrawableId = drawableId;
    }

    public String getDrawableLabel() {
        return mDrawableLabel;
    }

    public void setDrawableLabel(String drawableLabel) {
        this.mDrawableLabel = drawableLabel;
    }
}
