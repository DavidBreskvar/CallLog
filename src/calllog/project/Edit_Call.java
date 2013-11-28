package calllog.project;

import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Edit_Call extends Activity {
	
	TextView name, number, date, duration, type, opombaText;
	EditText opombaEdit;
	Button edit, delete, opombaB;
	String textOpomba;
	DBAdapter db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit__call);
		initialize();
		Intent i = new Intent();
		int id = i.getIntExtra("ID", 0);
		String nameDB = i.getStringExtra("NAME");
		String numberDB = i.getStringExtra("NUMBER");
		String typeDB = i.getStringExtra("TYPE");
		String dateDB = i.getStringExtra("DATE");
		String durationDB = i.getStringExtra("DURATION");
		
		long idD = (int) id;

        
        name.setText(nameDB);
        number.setText(numberDB);
        type.setText(typeDB);
        date.setText(dateDB);
        duration.setText(durationDB);
        
   /*     if (opombaDB != null) {
        	opombaText.setVisibility(View.VISIBLE);
        	opombaText.setText(opombaDB);
        }
*/
		
		//pridobi iz baze opombo če obstaja in jo napiši v textview
		
		edit.setText("Edit");
		edit.setTag(1);
		edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//edit => save, pokaži edittext z vsebino textview, textview = gone
				final int status =(Integer) v.getTag();
				if(status == 1) {
					edit.setText("Save");
					edit.setTag(2);
					textOpomba = opombaText.getText().toString();
					opombaEdit.setText(textOpomba);
					opombaEdit.setVisibility(View.VISIBLE);
					opombaText.setVisibility(View.GONE);
				}
				else {
					edit.setText("Edit");
					edit.setTag(1);
					textOpomba = opombaEdit.getText().toString();
					opombaText.setText(textOpomba);
					opombaEdit.setVisibility(View.GONE);
					opombaText.setVisibility(View.VISIBLE);
				}
				
				
			}
		});
		
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//zbriši iz baze in zbriši textview, edittext = invisible
				//prvo briši iz baze string
				//TODO
				opombaText.setText("");
				opombaEdit.setText("");
				opombaText.setVisibility(View.GONE);
				edit.setVisibility(View.INVISIBLE);
				delete.setVisibility(View.INVISIBLE);
				opombaEdit.setVisibility(View.GONE);
				opombaB.setVisibility(View.VISIBLE);
				
			}
		});
		
		opombaB.setText("Opombe");
		opombaB.setTag(1);
		opombaB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//prikaži edittext, gumb opombe = gone, prikaži edit in delete gumba
				
				final int status =(Integer) v.getTag();
				if(status == 1) {
					opombaEdit.setVisibility(View.VISIBLE);
					opombaB.setText("Done");
					opombaB.setTag(2);
				}
				else {
					opombaB.setTag(1);
					opombaB.setText("Opombe");
					opombaB.setVisibility(View.INVISIBLE);
					edit.setVisibility(View.VISIBLE);
					delete.setVisibility(View.VISIBLE);
					opombaText.setVisibility(View.VISIBLE);
					textOpomba = opombaEdit.getText().toString();
					opombaText.setText(textOpomba);
					opombaEdit.setVisibility(View.GONE);
					//pošlji opombo v database
					
				}
				
			}
		});
		
		
	}

	private void initialize() {
		name = (TextView)findViewById(R.id.name);
		number = (TextView)findViewById(R.id.number);
		date = (TextView)findViewById(R.id.date);
		duration = (TextView)findViewById(R.id.duration);
		type = (TextView)findViewById(R.id.callType);
		opombaEdit = (EditText)findViewById(R.id.opombaEdit);
		edit = (Button)findViewById(R.id.edit);
		delete = (Button)findViewById(R.id.delete);
		opombaB = (Button)findViewById(R.id.opombe);
		opombaText = (TextView)findViewById(R.id.opombaText);
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit__call, menu);
		return true;
	}

}
