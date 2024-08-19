package com.example.footballreservation.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.footballreservation.model.Field;
import com.example.footballreservation.model.Reservation;
import com.example.footballreservation.model.User;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "FootballReservation.db";

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_FIELDS = "fields";
    private static final String TABLE_RESERVATIONS = "reservations";

    // Common column names
    private static final String KEY_ID = "id";

    // Users Table column names
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IS_ADMIN = "is_admin";

    // Fields Table column names
    private static final String KEY_FIELD_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PRICE_PER_HOUR = "price_per_hour";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_IS_AVAILABLE = "is_available";

    // Reservations Table column names
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_FIELD_ID = "field_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_TOTAL_PRICE = "total_price";
    private static final String KEY_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_IS_ADMIN + " INTEGER" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_FIELDS_TABLE = "CREATE TABLE " + TABLE_FIELDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIELD_NAME + " TEXT UNIQUE,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_PRICE_PER_HOUR + " REAL,"
                + KEY_IS_AVAILABLE + " INTEGER,"
                + KEY_IMAGE_URL + " TEXT" + ")";
        db.execSQL(CREATE_FIELDS_TABLE);

        String CREATE_RESERVATIONS_TABLE = "CREATE TABLE " + TABLE_RESERVATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_FIELD_ID + " INTEGER,"
                + KEY_DATE + " TEXT,"
                + KEY_START_TIME + " TEXT,"
                + KEY_END_TIME + " TEXT,"
                + KEY_TOTAL_PRICE + " REAL,"
                + KEY_STATUS + " TEXT,"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_FIELD_ID + ") REFERENCES " + TABLE_FIELDS + "(" + KEY_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_RESERVATIONS_TABLE);

        // Add sample data
        addSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        onCreate(db);
    }

    private void addSampleData(SQLiteDatabase db) {
        // Add admin
        ContentValues adminValues = new ContentValues();
        adminValues.put(KEY_USERNAME, "admin");
        adminValues.put(KEY_PASSWORD, "admin123");
        adminValues.put(KEY_EMAIL, "admin@fsa.ma");
        adminValues.put(KEY_IS_ADMIN, 1);
        db.insert(TABLE_USERS, null, adminValues);
    }

    // User methods
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_IS_ADMIN, user.isAdmin() ? 1 : 0);
        return db.insert(TABLE_USERS, null, values);
    }
    public String getUserNameById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_USERNAME + " FROM " + TABLE_USERS + " WHERE " + KEY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        String userName = "Unknown";
        if (cursor.moveToFirst()) {
            userName = cursor.getString(0);
        }
        cursor.close();
        return userName;
    }

    public User getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID, KEY_USERNAME, KEY_PASSWORD, KEY_EMAIL, KEY_IS_ADMIN},
                KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4) == 1
            );
            cursor.close();
            return user;
        }
        return null;
    }

    // Field methods
    public List<Field> getAllFields() {
        List<Field> fieldList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FIELDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Check if cursor is null or empty
        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (cursor.getColumnCount() >= 6) { // Ensure there are at least 6 columns
                    Field field = new Field(
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIELD_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE_PER_HOUR)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_AVAILABLE)) != 0,
                            cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_URL))
                    );
                    fieldList.add(field);
                }
            } while (cursor.moveToNext());
            cursor.close(); // Close cursor after use
        }

        return fieldList;
    }

    public long addField(Field field) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIELD_NAME, field.getName());
        values.put(KEY_DESCRIPTION, field.getType()); // Adjust according to your database schema
        values.put(KEY_PRICE_PER_HOUR, field.getPricePerHour());
        values.put(KEY_IMAGE_URL, field.getImageUrl()); // Adjust according to your database schema
        values.put(KEY_IS_AVAILABLE, field.isAvailable() ? 1 : 0);
        return db.insert(TABLE_FIELDS, null, values);
    }

    // Reservation methods
    public long addReservation(Reservation reservation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, reservation.getUserId());
        values.put(KEY_FIELD_ID, reservation.getFieldId());
        values.put(KEY_DATE, reservation.getDate());
        values.put(KEY_START_TIME, reservation.getStartTime());
        values.put(KEY_END_TIME, reservation.getEndTime());
        values.put(KEY_TOTAL_PRICE, reservation.getTotalPrice());
        values.put(KEY_STATUS, reservation.getStatus());
        return db.insert(TABLE_RESERVATIONS, null, values);
    }

    public List<Reservation> getReservationsByUser(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESERVATIONS, null, KEY_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getDouble(6),
                        cursor.getString(7)
                );
                reservations.add(reservation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reservations;
    }
    public int getNonAdminUserCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_USERS + " WHERE " + KEY_IS_ADMIN + " = ?";
        Cursor cursor = db.rawQuery(countQuery, new String[]{"0"});

        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0); // The count is in the first column
            }
            cursor.close();
        }

        return count;
    }
    public Field getFieldById(int fieldId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FIELDS, null, KEY_ID + "=?",
                new String[]{String.valueOf(fieldId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Field field = new Field(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIELD_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE_PER_HOUR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_AVAILABLE)) != 0,
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE_URL))
            );
            cursor.close();
            return field;
        }
        return null;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RESERVATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getDouble(6),
                        cursor.getString(7)
                );
                reservations.add(reservation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reservations;
    }

    public boolean updateReservationStatus(int reservationId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, newStatus);
        int rowsAffected = db.update(TABLE_RESERVATIONS, values, KEY_ID + "=?",
                new String[]{String.valueOf(reservationId)});
        return rowsAffected > 0;
    }

    public boolean checkConflictingReservation(int fieldId, String date, String startTime, String endTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RESERVATIONS +
                " WHERE " + KEY_FIELD_ID + " = ? AND " + KEY_DATE + " = ? AND " +
                "((? BETWEEN " + KEY_START_TIME + " AND " + KEY_END_TIME + ") OR " +
                "(? BETWEEN " + KEY_START_TIME + " AND " + KEY_END_TIME + ") OR " +
                "(" + KEY_START_TIME + " BETWEEN ? AND ?))";
        Cursor cursor = db.rawQuery(query, new String[]{
                String.valueOf(fieldId), date, startTime, endTime, startTime, endTime
        });
        boolean hasConflict = cursor.getCount() > 0;
        cursor.close();
        return hasConflict;
    }
    public boolean deleteField(int fieldId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;
        db.beginTransaction();
        try {
            db.delete(TABLE_RESERVATIONS, KEY_FIELD_ID + "=?", new String[]{String.valueOf(fieldId)});

            int rowsAffected = db.delete(TABLE_FIELDS, KEY_ID + "=?", new String[]{String.valueOf(fieldId)});

            if (rowsAffected > 0) {
                db.setTransactionSuccessful();
                success = true;
            }
        } finally {
            db.endTransaction();
        }
        return success;
    }
}