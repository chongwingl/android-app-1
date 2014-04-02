package com.project.aidememoire.adapter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseAdapter {
	
	
	public static final String KEY_ID = "_id";
	// table personne
	public static final String KEY_NAME = "name";
	public static final String KEY_SURNAME = "surname";
	// table dette
    public static final String KEY_DETTE = "dette";
    public static final String KEY_DATE = "date";
    public static final String KEY_SOMME = "somme";
    // table crédit
    public static final String KEY_CREDIT = "credit";
    // clé permettant de récupérer la personne concerné par l'opération
    public static final String KEY_P = "p_id";

    private static final String TAG = "DBAdapter";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE_T1 =
        "create table personne (_id integer primary key autoincrement, name text, surname text not null);";
    private static final String DATABASE_CREATE_T2 =
        "create table dette (_id integer primary key autoincrement, p_id integer foreign_key, date integer not null, somme integer not null);";
    private static final String DATABASE_CREATE_T3 =
    	"create table credit (_id integer primary key autoincrement, p_id integer foreign_key, date integer not null, somme integer not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_P = "personne";
    private static final String DATABASE_TABLE_C = "credit";
    private static final String DATABASE_TABLE_D = "dette";
    private static final int DATABASE_VERSION = 2;

    private final Context ctx;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_T1);
            db.execSQL(DATABASE_CREATE_T2);
            db.execSQL(DATABASE_CREATE_T3);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS personne;");
            db.execSQL("DROP TABLE IF EXISTS dette;");
            db.execSQL("DROP TABLE IF EXISTS credit;");
            onCreate(db);
        }
    }
    
    public DataBaseAdapter(Context ctx) {
        this.ctx = ctx;
    }
    
    public DataBaseAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(ctx);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }
    
    public boolean addCreditLine(String name, String surname, int date, int sum) {
    	long p_id = this.addPeople(name, surname);
    	if(p_id > 0){
    		if(this.addCredit(p_id, date, sum) > 0) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean addDetteLine(String name, String surname, int date, int sum) {
    	long p_id = this.addPeople(name, surname);
    	if(p_id > 0){
    		if(this.addDette(p_id, date, sum) > 0) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Create a new person using his name and surname provided. If the person is
     * successfully created return the new id for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    public long addPeople(String name, String surname){
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_SURNAME, surname);
    	return db.insert(DATABASE_TABLE_P, null, initialValues);
    }
    
    public long addCredit(long p_id, int date, int somme){
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_P, p_id);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_SOMME, somme);
    	return db.insert(DATABASE_TABLE_C, null, initialValues);
    }
    
    public long addDette(long p_id, int date, int somme){
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_P, p_id);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_SOMME, somme);
    	return db.insert(DATABASE_TABLE_D, null, initialValues);
    }
    
    /**
     * Return a Cursor over the list of all people in the database
     * 
     * @return Cursor over all people
     */
    public Cursor fetchAllPeople() {

        return db.query(DATABASE_TABLE_P, new String[] {KEY_ID, KEY_NAME, KEY_SURNAME}, 
        		null, null, null, null, null);
    }
    
    public Cursor fetchAllCredit() {

        return db.query(DATABASE_TABLE_C, new String[] {KEY_ID, KEY_P, KEY_DATE, KEY_SOMME}, 
        		null, null, null, null, null);
    }
    
    public Cursor fetchAllDette() {

        return db.query(DATABASE_TABLE_D, new String[] {KEY_ID, KEY_P, KEY_DATE, KEY_SOMME}, 
        		null, null, null, null, null);
    }
    
    /**
     * Delete the person with the given id
     * 
     * @param id id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deletePeople(long id) {

        return db.delete(DATABASE_TABLE_P, KEY_ID + "=" + id, null) > 0;
    }
    public boolean deleteCredit(long id) {

        return db.delete(DATABASE_TABLE_C, KEY_ID + "=" + id, null) > 0;
    }
    public boolean deleteDette(long id) {

        return db.delete(DATABASE_TABLE_D, KEY_ID + "=" + id, null) > 0;
    }

}


