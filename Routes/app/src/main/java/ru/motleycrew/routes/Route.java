package ru.motleycrew.routes;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 12.03.2016.
 */
public class Route {
    private List<LatLng> mPolyline;
    private String mStartAddress;
    private String mEndAddress;
    private String mDuration;
    private String mDistance;

    public Route() {
        mPolyline = new ArrayList<>();
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        this.mDuration = duration;
    }

    public String getEndAddress() {
        return mEndAddress;
    }

    public void setEndAddress(String endAddress) {
        this.mEndAddress = endAddress;
    }

    public String getStartAddress() {
        return mStartAddress;
    }

    public void setStartAddress(String startAddress) {
        this.mStartAddress = startAddress;
    }

    public String getDistance() {
        return mDistance;
    }

    public void setDistance(String distance) {
        mDistance = distance;
    }

    public List<LatLng> getPolyline() {
        return mPolyline;
    }

    public void addPoint(LatLng point) {
        this.mPolyline.add(point);
    }

    public String createReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Distance - ")
                .append(mDistance)
                .append("  ")
                .append("Duration - ")
                .append(mDuration)
                .append("\n")
                .append("Start: ")
                .append(formatAddress(mStartAddress))
                .append(".\n")
                .append("End: ")
                .append(formatAddress(mEndAddress));
        return sb.toString();
    }

    private String formatAddress(String address) {
        String[] split = address.split(",");
        StringBuffer result = new StringBuffer(split[0]).append(",").append(split[1]);
        if (split.length > 2) {
            result.append(",").append(split[2]);
        }
        return result.toString();
    }
}
