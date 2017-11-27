package io.mob.resu.reandroidsdk;

import java.util.ArrayList;

/**
 * Created by Interakt on 11/23/17.
 */

public class MScreenTracker
{
    private ArrayList<MRecord> MRecord;

    private String screen;

    private String subscreen;

    public MScreenTracker() {
    }

    public MScreenTracker(ArrayList<MRecord> MRecord, String screen, String subscreen) {
        this.MRecord = MRecord;
        this.screen = screen;
        this.subscreen = subscreen;

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

    public String getSubscreen ()
    {
        return subscreen;
    }

    public void setSubscreen (String subscreen)
    {
        this.subscreen = subscreen;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [MRecord = "+ MRecord +", screen = "+screen+", subscreen = "+subscreen+"]";
    }
}

