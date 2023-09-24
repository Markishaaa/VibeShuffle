package rs.markisha.vibeshuffle.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rs.markisha.vibeshuffle.model.Playlist;
import rs.markisha.vibeshuffle.model.Track;
import rs.markisha.vibeshuffle.utils.network.SpotifyController;

public class DBHelper extends SQLiteOpenHelper {

    private final Gson gson;

    private static final String DATABASE_NAME = "vibeshuffle.db";
    private static final int DATABASE_VERSION = 6;

    private static final String TABLE_PLAYLIST = "playlist";
    private static final String TABLE_TRACK = "track";

    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_JSON_DATA = "json_data";

    private final String CREATE_TABLE_PLAYLIST = "CREATE TABLE " + TABLE_PLAYLIST + "("
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_JSON_DATA + " TEXT"
            + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        gson = new Gson();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLAYLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old tables (if they exist)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);

        // Recreate the database with the new schema
        onCreate(db);
    }

    private String playlistToJson(Playlist playlist) {
        return gson.toJson(playlist);
    }

    private Playlist jsonToPlaylist(String json) {
        return gson.fromJson(json, Playlist.class);
    }

    public void insertOrUpdatePlaylist(Playlist playlist, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, type);

        // Convert Playlist object to JSON
        String playlistJson = playlistToJson(playlist);
        values.put(COLUMN_JSON_DATA, playlistJson);

        try {
            db.beginTransaction();

            // Check the number of rows in the table
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PLAYLIST, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int rowCount = cursor.getInt(0);
                    if (rowCount < 2) {
                        // If there are less than 2 items, simply insert
                        db.insert(TABLE_PLAYLIST, null, values);
                    } else {
                        // If there are already 2 items, update one with the same type
                        String whereClause = COLUMN_TYPE + " = ?";
                        String[] whereArgs = {type};

                        db.update(TABLE_PLAYLIST, values, whereClause, whereArgs);
                    }
                }
                cursor.close();
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public Playlist getPlaylistOfType(String type) {
        Playlist playlist = null;
        String selectQuery = "SELECT * FROM " + TABLE_PLAYLIST + " WHERE " + COLUMN_TYPE + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{type});

        if (cursor.moveToFirst()) {
            int i = cursor.getColumnIndex(COLUMN_JSON_DATA);
            String json = i != -1 ? cursor.getString(i) : null;

            if (json != null) {
                playlist = jsonToPlaylist(json);
            }
        }

        cursor.close();

        return playlist;
    }

    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PLAYLIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int i = cursor.getColumnIndex(COLUMN_JSON_DATA);
                String json = i != -1 ? cursor.getString(i) : null;

                if (json != null) {
                    playlists.add(jsonToPlaylist(json));
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        return playlists;
    }

}
