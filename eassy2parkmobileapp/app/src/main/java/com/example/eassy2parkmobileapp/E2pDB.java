package com.example.eassy2parkmobileapp;

/**
 * Represents an item in a e2p DB
 */
public class E2pDB {

    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("beaconID")
    private String mBeacon;

    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("isTaken")
    private boolean mTaken;

    /**
     * ToDoItem constructor
     */
    public E2pDB() {

    }

    @Override
    public String toString() {
        return getBeaconID();
    }

    /**
     * Initializes a new ToDoItem
     *
     * @param text
     *            The item text
     * @param id
     *            The item id
     */
    public E2pDB(String text, String id) {
        this.setText(text);
        this.setId(id);
    }

    /**
     * Returns the item text
     */
    public String getBeaconID() {
        return mBeacon;
    }

    /**
     * Sets the item text
     *
     * @param text
     *            text to set
     */
    public final void setText(String text) {
        mBeacon = text;
    }

    /**
     * Returns the item id
     */
    public String getId() {
        return mId;
    }

    /**
     * Sets the item id
     *
     * @param id
     *            id to set
     */
    public final void setId(String id) {
        mId = id;
    }

    /**
     * Indicates if the item is marked as completed
     */
    public boolean isComplete() {
        return mTaken;
    }

    /**
     * Marks the item as completed or incompleted
     */
    public void setComplete(boolean complete) {
        mTaken = complete;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof E2pDB && ((E2pDB) o).mId == mId;
    }
}