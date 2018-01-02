package io.mob.resu.reandroidsdk;

import java.util.ArrayList;



public class MScreenTracker
{
    private ArrayList<MRecord> MRecord;

    private String screen;

    private String subScreen;

    public MScreenTracker() {
    }

    public MScreenTracker(ArrayList<MRecord> MRecord, String screen, String subScreen) {
        this.MRecord = MRecord;
        this.screen = screen;
        this.subScreen = subScreen;

    }

    public ArrayList<MRecord> getMRecord()
    {
        return MRecord;
    }

    public void setMRecord(ArrayList<MRecord> MRecord)
    {
        this.MRecord = MRecord;
    }

    public String getScreen ()
    {
        return screen;
    }

    public void setScreen (String screen)
    {
        this.screen = screen;
    }

    public String getSubScreen()
    {
        return subScreen;
    }

    public void setSubScreen(String subScreen)
    {
        this.subScreen = subScreen;
    }

    @Override
    public String toString()
    {
        return "MRecord [MRecord = "+ MRecord +", screen = "+screen+", subScreen = "+ subScreen +"]";
    }
}

