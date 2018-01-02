package io.mob.resu.reandroidsdk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import io.mob.resu.reandroidsdk.error.ExceptionTracker;
import io.mob.resu.reandroidsdk.error.Log;

class DataBase {
    private static final String DATABASE_NAME = "resulticks";
    private static final int DATABASE_VERSION = 1;
    private static final String CAMPAIGN_TABLE = "resulticks_campaign_table";
    private static final String NOTIFICATION_TABLE = "resulticks_notification_table";
    private static final String SCREENS_TABLE = "resulticks_screens_table";
    private static final String REGISTER_EVENT_TABLE = "resulticks_register_event_table";
    private static final String CREATE_CAMPAIGN_TABLE = "CREATE TABLE IF NOT EXISTS " + CAMPAIGN_TABLE + " (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + "value LONG TEXT NOT NULL);";
    private static final String CREATE_NOTIFICATION_TABLE = "CREATE TABLE IF NOT EXISTS " + NOTIFICATION_TABLE + " (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "value LONG TEXT NOT NULL);";
    private static final String CREATE_SCREENS_TABLE = "CREATE TABLE IF NOT EXISTS " + SCREENS_TABLE + " (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "value LONG TEXT NOT NULL);";
    private static final String CREATE_REGISTER_EVENT_TABLE = "CREATE TABLE IF NOT EXISTS " + REGISTER_EVENT_TABLE + " (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "viewid TEXT NOT NULL, " +
            "screenname TEXT NOT NULL, " +
            "value LONG TEXT NOT NULL);";


    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    DataBase(Context ctx) {
        DBHelper = new DatabaseHelper(ctx);
    }

    private DataBase open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    /**
     * Getting All Table wise
     *
     * @param i
     * @return
     */
    ArrayList<MData> getData(Table i) {
        try {
            db = DBHelper.getReadableDatabase();
            ArrayList<MData> array_list = new ArrayList<>();
            String tableName = getTableName(i);
            Cursor cursor;
            cursor = db.rawQuery("select * from " + tableName, null);
            if (cursor != null && cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {
                    do {
                        MData mData = new MData();

                        mData.setId(cursor.getInt(0));
                        mData.setValues(cursor.getString(1));
                        array_list.add(mData);
                    } while (cursor.moveToNext());
                }

            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return array_list;
        } catch (Exception e) {
            ExceptionTracker.track(e);
            return new ArrayList<>();
        }
    }


    /**
     * Provide Already register Event
     *
     * @param tablename
     * @param viewId
     * @param screenName
     * @return
     */
    public MRegisterEvent getData(Table tablename, String viewId, String screenName) {

        try {
            if (db != null) {
                if (db.isOpen())
                    db.close();
            }
            db = DBHelper.getReadableDatabase();
            String tableName = getTableName(tablename);
            Cursor cursor;
            MRegisterEvent mRegisterEvent = new MRegisterEvent();
            cursor = getCursorCount(viewId, screenName, tableName);
            if (cursor != null & cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        try {
                            mRegisterEvent.setViewId(cursor.getString(1));
                            mRegisterEvent.setScreenName(cursor.getString(2));
                            //  JSONObject jsonObject = new JSONObject(cursor.getString(3));
                            //mRegisterEvent=LoganSquare.parse(jsonObject.toString(),MRegisterEvent.class);
                            mRegisterEvent.setEventID(cursor.getString(0));
                        } catch (Exception e) {
                            ExceptionTracker.track(e);
                        }


                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            close();
            return mRegisterEvent;
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return null;
    }

    /**
     * Delete list of rows table wise
     *
     * @param values
     * @param i
     */
    void deleteData(ArrayList<MData> values, Table i) {

        dbOpen();
        try {
            for (MData mData : values) {
                deleteData(mData, i);
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        close();
    }

    private void dbOpen() {

        try {
            if (db != null) {
                if (db.isOpen())
                    db.close();
            }
            open();
        } catch (SQLException e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * Delete data row id wise
     *
     * @param id
     * @param i
     */
    private void deleteEventData(String id, Table i) {

        try {
            dbOpen();
            String tableName = getTableName(i);
            Log.e("datas deleted", tableName + " " + id);
            db.execSQL("delete from " + tableName + " where _id='" + id + "'");
            close();
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }


    /**
     * Delete data row id wise
     *
     * @param mData
     * @param i
     */
    public void deleteData(MData mData, Table i) {
        dbOpen();
        try {
            int id = mData.getId();
            deleteEventData("" + id, i);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * enum type to get table name
     *
     * @param i
     * @return
     */
    @NonNull
    private String getTableName(Table i) {
        String tableName = "";
        switch (i) {
            case CAMPAIGN_TABLE:
                tableName = CAMPAIGN_TABLE;
                break;
            case NOTIFICATION_TABLE:
                tableName = NOTIFICATION_TABLE;
                break;
            case SCREENS_TABLE:
                tableName = SCREENS_TABLE;
                break;
            case REGISTER_EVENT_TABLE:
                tableName = REGISTER_EVENT_TABLE;
                break;
        }
        return tableName;
    }


    /**
     * insert Data table wise
     *
     * @param value
     * @param i
     */
    void insertData(String value, Table i) {
        try {
            dbOpen();
            String tableName = getTableName(i);
            ContentValues initialValues = new ContentValues();
            initialValues.put("value", value);
            Log.e("data  insert to ", "" + value);
            Log.e("data  insert to ", "" + tableName);
            db.insert(tableName, null, initialValues);
            close();
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }


    }

    /**
     * Update Data table wise
     *
     * @param value
     * @param viewId
     * @param screenName
     * @param i
     */
    void insertOrUpdateData(String value, String viewId, String screenName, Table i) {
        try {
            dbOpen();
            String tableName = getTableName(i);
            ContentValues initialValues = new ContentValues();
            initialValues.put("value", value);
            initialValues.put("viewid", viewId);
            initialValues.put("screenname", screenName);

            Log.e("data  insert to ", "" + tableName);
            Cursor cursor = getCursorCount(viewId, screenName, tableName);
            if (cursor != null && cursor.getCount() > 0)
                db.update(tableName, initialValues, "viewid" + "=?" + " AND screenname" + "=?", new String[]{viewId, screenName});
            else
                db.insert(tableName, null, initialValues);
            close();
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }


    }


    /**
     * getting view id wise register events
     *
     * @param viewId
     * @param screenName
     * @param tableName
     * @return
     */
    private Cursor getCursorCount(String viewId, String screenName, String tableName) {
        return db.query(tableName, new String[]{"_id", "viewid", "screenname", "value"}, "viewid" + "=?" + " AND screenname" + "=?", new String[]{viewId, screenName}, null, null, null, null);
        // return db.rawQuery("SELECT * FROM " + tableName , null);
    }

    /**
     * database close
     */
    private synchronized void close() {
        if (db != null) {
            db.close();
        }
    }

    /**
     * Enum table names
     */
    enum Table {
        NOTIFICATION_TABLE,
        CAMPAIGN_TABLE,
        SCREENS_TABLE,
        REGISTER_EVENT_TABLE
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //DB TABLE CREATION
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_CAMPAIGN_TABLE);
                db.execSQL(CREATE_NOTIFICATION_TABLE);
                db.execSQL(CREATE_SCREENS_TABLE);
                db.execSQL(CREATE_REGISTER_EVENT_TABLE);
            } catch (SQLException e) {
                ExceptionTracker.track(e);
            }
            Log.e("DATA BASE CREATED", "DATA BASE CREATED");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("UPGRADE", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }


}
