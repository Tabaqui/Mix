package ru.motleycrew.scheduler;

/**
 * Created by Vas on 29.02.16.
 */
public class LocationItem {

    private String prediction;
    private String placeId;

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
