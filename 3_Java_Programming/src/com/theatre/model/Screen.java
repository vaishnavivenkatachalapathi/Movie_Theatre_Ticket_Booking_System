package com.theatre.model;

/**
 * Theatre Screen domain class (FR-02: Manage theatre screens).
 */
public class Screen {
    private int screenId;
    private String screenName;
    private int seatingCapacity;

    public Screen(int screenId, String screenName, int seatingCapacity) {
        if (seatingCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero!");
        }
        this.screenId = screenId;
        this.screenName = screenName;
        this.seatingCapacity = seatingCapacity;
    }

    public int getScreenId() { return screenId; }
    public String getScreenName() { return screenName; }
    public int getSeatingCapacity() { return seatingCapacity; }

    @Override
    public String toString() {
        return String.format("[%d] %s (Capacity: %d seats)", screenId, screenName, seatingCapacity);
    }
}
