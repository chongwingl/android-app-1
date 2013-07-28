package com.project.aidememoire.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.database.DataBaseAdapter;

public class AideMemoireActivity extends Activity {
	
	private final static String TAG = "AideMemoireActivity";
	
	// le numero de la ligne saisie
	//private int mLineNumber = 1;
	// la base de données
	private DataBaseAdapter mDbHelper;
	//private ListView peopleListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }
    
}



/************** Manipulation de la BDD *****************/

/*mDbHelper = new DataBaseAdapter(this);
mDbHelper.open();
mDbHelper.addCreditLine("Martin", "Pierre", 1234567890, 1234);
mDbHelper.addCreditLine("Durand", "Pauline", 987654321, 765);
mDbHelper.addDetteLine("Dupont", "Paul", 1235679, 987);
Cursor c = mDbHelper.fetchAllPeople();

while (c.moveToNext()){
	Log.i(TAG, "People : " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2));
}

c = mDbHelper.fetchAllCredit();

while (c.moveToNext()){
	Log.i(TAG, "Crédit : " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3));
}

c = mDbHelper.fetchAllDette();

while (c.moveToNext()){
	Log.i(TAG, "Dette : " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3));
}

//peopleListView = (ListView) findViewById(R.id.peopleListView);*/
