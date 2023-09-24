package rs.markisha.vibeshuffle;

import android.app.Application;

import rs.markisha.vibeshuffle.utils.database.DBHelper;

public class VibeShuffle extends Application {

    private static DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DBHelper(this);
    }

    public static DBHelper getDBHelper() {
        return dbHelper;
    }

}
