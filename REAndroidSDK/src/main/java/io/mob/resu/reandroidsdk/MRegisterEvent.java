package io.mob.resu.reandroidsdk;


/**
 * Created by Interakt on 10/11/17.
 */
class MRegisterEvent {

    private String eventID;
    private String eventName;
    private String timeStamp;
    private String viewId;
    private String screenName;

    /**
     *
     * 1 Active
     * 2 DeActive
     * 3 Edited
     * 4 Deleted
     *
     */

    private int eventStatus;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

}
