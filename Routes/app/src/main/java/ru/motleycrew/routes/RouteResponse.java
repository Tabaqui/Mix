package ru.motleycrew.routes;

/**
 * Created by User on 15.03.2016.
 */
public class RouteResponse {

    private ResponseState mState;
    private Route mRoute;

    public RouteResponse() {
    }

    public ResponseState getState() {
        return mState;
    }

    public void setState(ResponseState state) {
        this.mState = state;
    }

    public Route getRoute() {
        return mRoute;
    }

    public void setRoute(Route route) {
        mRoute = route;
    }
}
