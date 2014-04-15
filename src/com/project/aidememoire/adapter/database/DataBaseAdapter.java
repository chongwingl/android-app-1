package com.project.aidememoire.adapter.database;

import java.util.Locale;

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
	// table somme
    public static final String KEY_TYPE = "type";
    public static final String KEY_DATE = "date";
    public static final String KEY_MONTANT = "montant";
    // clé permettant de récupérer la personne concerné par l'opération
    public static final String KEY_P = "p_id";

    private static final String TAG = "DBAdapter";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private boolean isOpen = false;
    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE_T1 =
        "create table personne (_id integer primary key autoincrement, name text, surname text not null);";
    private static final String DATABASE_CREATE_T2 =
        "create table somme (_id integer primary key autoincrement, p_id integer foreign_key, date text not null, montant integer not null, type text not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_P = "personne";
    private static final String DATABASE_TABLE_S = "somme";
    private static final int DATABASE_VERSION = 2;
    
    // Obtenir toutes les personnes
    private static final String GET_PERSON_QUERY = 
    		"select _id, name, surname from personne where name=\"{{name}}\" and surname=\"{{surname}}\"";
    // Obtenir tous les crédits et dettes d'une personne
    private static final String GET_MONEY_OF_PERSON_QUERY = 
    		"select name, surname, montant, date, type from somme inner join personne on personne._id=somme.p_id "+
    		"where personne.name=\"{{name}}\" and personne.surname=\"{{surname}}\"";
    // Obtenir une dette ou un crédit d'un personne
    private static final String GET_SPECIFIED_MONEY_QUERY = "select name, surname, montant, date, type from somme inner join personne on personne._id=somme.p_id "+
    		"where personne.name=\"{{name}}\" and personne.surname=\"{{surname}}\" and somme.montant={{somme}} and somme.date=\"{{date}}\" " +
    		"and somme.type=\"{{type}}\"";
    // Obtenir tout le contenu des tables
    private static final String GET_ALL = "select * from somme inner join personne on personne._id=somme.p_id";
    
    public static final String ORDER_BY_NAME = "order by name";
    public static final String ORDER_BY_SURNAME = "order by surname";
    public static final String ORDER_BY_SUM = " order by montant";
    public static final String ORDER_BY_DATE = "order by date";
    
    public static final String FILTER_BY_NAME = "where personne.name=\"{{arg}}\"";
    public static final String FILTER_BY_SURNAME = "where personne.surname=\"{{arg}}\"";
    public static final String FILTER_BY_SUM = "where somme.montant=\"{{arg}}\"";
    public static final String FILTER_BY_DATE = "where somme.date=\"{{arg}}\"";
    
    private final Context ctx;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_T1);
            db.execSQL(DATABASE_CREATE_T2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS personne;");
            db.execSQL("DROP TABLE IF EXISTS somme;");
            onCreate(db);
        }
    }
    
    public DataBaseAdapter(Context ctx) {
        this.ctx = ctx;
    }
    
    public DataBaseAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(ctx);
        db = dbHelper.getWritableDatabase();
        isOpen = true;
        return this;
    }

    public void close() {
        dbHelper.close();
        isOpen = false;
    }
    
    public boolean isOpen(){
    	return isOpen;
    }
    
    public boolean addSommeLine(String name, String surname, String date, int sum, String type) {
    	long p_id;
    	
    	Cursor c = fetchPerson(name, surname);
    	if(c.moveToNext()){
    		Log.i(TAG, c.getString(0));
    		p_id = c.getLong(0);
    	}
    	else {
    		p_id = this.addPerson(name, surname);
    	}
    	
    	if(p_id > 0){
    		if(this.addSomme(p_id, date, sum, type) > 0) {
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
    public long addPerson(String name, String surname){
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name.toLowerCase(Locale.FRANCE));
        initialValues.put(KEY_SURNAME, surname.toLowerCase(Locale.FRANCE));
    	return db.insert(DATABASE_TABLE_P, null, initialValues);
    }
    
    public long addSomme(long p_id, String date, int somme, String type){
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_P, p_id);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_MONTANT, somme);
        initialValues.put(KEY_TYPE, type);
    	return db.insert(DATABASE_TABLE_S, null, initialValues);
    }
    
    public Cursor fetchPerson(String name, String surname){
    	return db.rawQuery(
    		GET_PERSON_QUERY
    			.replace("{{name}}", name.toLowerCase(Locale.FRANCE))
    			.replace("{{surname}}", surname.toLowerCase(Locale.FRANCE)), 
    		null);
    }
    
    public Cursor fetchMoneyOfPerson(String name, String surname){
    	return db.rawQuery(GET_MONEY_OF_PERSON_QUERY
    			.replace("{{name}}", name.toLowerCase(Locale.FRANCE))
    			.replace("{{surname}}", surname.toLowerCase(Locale.FRANCE)), 
    		null);
    }
   
    public Cursor fetchSpecifiedMoney(String name, String surname, int somme, String date, String type){
    	
    	return db.rawQuery(GET_SPECIFIED_MONEY_QUERY
    			.replace("{{name}}", name.toLowerCase(Locale.FRANCE))
    			.replace("{{surname}}", surname.toLowerCase(Locale.FRANCE))
    			.replace("{{somme}}", String.valueOf(somme))
    			.replace("{{date}}", date)
    			.replace("{{type}}", String.valueOf(type)), 
    		null);
    }
    
    /**
     * Return a Cursor over the list of all people in the database
     * 
     * @return Cursor over all people
     */
    public Cursor fetchAllPersons() {

        return db.query(DATABASE_TABLE_P, new String[] {KEY_ID, KEY_NAME, KEY_SURNAME}, 
        		null, null, null, null, null);
    }
    
    public Cursor fetchAllMoney() {

        return db.query(DATABASE_TABLE_S, new String[] {KEY_ID, KEY_P, KEY_DATE, KEY_MONTANT, KEY_TYPE},
        		null, null, null, null, null);
    }
    
    public Cursor fetchAll(){
    	return db.rawQuery(GET_ALL, null);
    }
    
    public Cursor fetchAll(String orderByFilterBy, String whereArg){
    	if(whereArg != null){
    		orderByFilterBy = orderByFilterBy.replace("{{arg}}", whereArg);
    	}
    	return db.rawQuery(GET_ALL + " " + orderByFilterBy, null);
    }
    /**
     * Delete the person with the given id
     * 
     * @param id id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deletePerson(long id) {

        return db.delete(DATABASE_TABLE_P, KEY_ID + "=" + id, null) > 0;
    }
    public boolean deleteSomme(long id) {
  
        return db.delete(DATABASE_TABLE_S, KEY_ID + "=?", new String [] {String.valueOf(id)}) > 0;
    }
    
    public boolean updatePerson(long id, String name, String surname){
	
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		values.put(KEY_SURNAME, surname);
		int val = db.update(DATABASE_TABLE_P, values, KEY_ID + "=" + id, null);
    	if(val > 0) {
    		return true;
    	}
		return false;
    }
    
    public boolean updateSomme(long id, int montant, String type, String date){
    	
    	ContentValues values = new ContentValues();
		values.put(KEY_DATE, date);
		values.put(KEY_TYPE, type);
		values.put(KEY_MONTANT, montant);
		int val = db.update(DATABASE_TABLE_S, values, KEY_ID + "=?", new String [] {String.valueOf(id)});
		if(val > 0) {
    		return true;
    	}
    	
    	return false;
    }


}


