package calllog.project;

import com.newrelic.agent.android.NewRelic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.provider.CallLog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Call_List extends Activity {
	
	String number, name, duration, date, type;
	
	DBAdapter db = new DBAdapter(this);
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call__list);
		
		NewRelic.withApplicationToken(
				"AAbc16618c9ca317705e8d692d6b0537c844aa00ef"
				).start(this.getApplication());
		
		listView = (ListView)findViewById(R.id.callLog);
		
		String[] callInfo = {
		        android.provider.CallLog.Calls.CACHED_NAME, 
		        android.provider.CallLog.Calls.NUMBER,
		        android.provider.CallLog.Calls.TYPE,
		        android.provider.CallLog.Calls.DATE,
		        android.provider.CallLog.Calls.DURATION
		        };
		String callOrder = android.provider.CallLog.Calls.DATE + " DESC"; 
		 
		Cursor c = getContentResolver().query(
		        android.provider.CallLog.Calls.CONTENT_URI,
		        callInfo,
		        null,
		        null,
		        callOrder
		        );
		
		// get start of cursor
		
		final ArrayList<String> list = new ArrayList<String>();
		
		if(c.moveToFirst()){
		  // loop through cursor 
		  do{
			  //tostring it and send to database lite
			  String name = c.getString(0);
              String number = c.getString(1);
              String type = c.getString(2);
              String date = c.getString(3);
              Date callDate = new Date(Long.valueOf(date));
              String duration = c.getString(4) + "s";
              
              int typeContent = Integer.parseInt(type);
              switch (typeContent) {
              case CallLog.Calls.OUTGOING_TYPE:
            	  type = "OUTGOING";
                  break;

              case CallLog.Calls.INCOMING_TYPE:
            	  type = "INCOMING";
                  break;

              case CallLog.Calls.MISSED_TYPE:
            	  type = "MISSED";
                  break;
              }
              
              date = callDate.toString();
              
              if (name == null) {
            	  name = "Unknown";
              }
              
          /*  Log.i("CALLS", "name: "+name);
              Log.i("CALLS", "number: "+number);
              Log.i("CALLS", "type: "+type);
              Log.i("CALLS", "date: "+date);
              Log.i("CALLS", "duration: "+duration);
          */   
              //send to database
			  db.open();
			  long id = db.insertCall(name, number, type, date, duration, null);
			  Log.i("name", "IME: "+name);
			  //Log.i("DATABASE", "data column: "+db);
			  db.close();
              String populate = name +" " + number+" id = "+id;
              list.add(populate);
		  } while (c.moveToNext());
		  
		}
		final StableArrayAdapter adapter = new StableArrayAdapter(this,
		        android.R.layout.simple_list_item_1, list);
		    listView.setAdapter(adapter);
		    
		    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		        @Override
		        public void onItemClick(AdapterView<?> parent, final View view,
		            int position, long id) {
		        	//treba je dobit pozicijo kerga je kliknu
		        	Intent i = new Intent(Call_List.this, Edit_Call.class);
		        	int idD = (int) id+1;
		        	
		        	db.open();
		    		Cursor c = db.getCallLog(idD);
		    		String nameDB = c.getString(c.getColumnIndex("NAME"));
		            String numberDB = c.getString(c.getColumnIndex("NUMBER"));
		            String typeDB = c.getString(c.getColumnIndex("TYPE"));
		            String dateDB = c.getString(c.getColumnIndex("DATE"));
		            String durationDB = c.getString(c.getColumnIndex("DURATION"));
		            db.close();
		            
		        	i.putExtra("ID", idD);
		        	i.putExtra("NAME", nameDB);
		        	i.putExtra("NUMBER", numberDB);
		        	i.putExtra("TYPE", typeDB);
		        	i.putExtra("DATE", dateDB);
		        	i.putExtra("DURATION", durationDB);
		        	
		        	
		        	Log.i("ID", "id = "+id+" iDd ="+idD);
		        	startActivity(i);
		        }
		      });
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.call__list, menu);
		return true;
	}

}


