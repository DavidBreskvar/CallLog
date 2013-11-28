package calllog.project;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;



public class myAdapter extends SimpleCursorAdapter{

	
	private final class ViewHolder {
	    public Button edit, del, opombaB;
	    public String textOpomba;
	    public TextView opombaText, type, name, number, duration, date;
	    public EditText opombaEdit;
	}

	
	private String[] values;
	private int mSelectedPosition;
	Cursor items;
	private Context ctx;
	private final LayoutInflater mLayoutInflater;
	int mLayout;
	private String[] from;
	private int[] to;
	
	public myAdapter(Context ctx, int layout, Cursor c, String[] from, int[] to) {
	    super(ctx, layout, c, from, to);
	    mLayout = R.layout.calls;
	    this.mLayoutInflater = LayoutInflater.from(ctx);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
	    if (mCursor.moveToPosition(position)) {
	        final ViewHolder viewHolder;

	        if (convertView == null) {
	            convertView = mLayoutInflater.inflate(mLayout, null);

	            viewHolder = new ViewHolder();
	            viewHolder.edit = (Button) convertView.findViewById(R.id.btnEdit);
	            viewHolder.del = (Button) convertView.findViewById(R.id.btnDel);
	            viewHolder.opombaB = (Button) convertView.findViewById(R.id.btnOpomba);
	            viewHolder.opombaText = (TextView)convertView.findViewById(R.id.callText);
	            viewHolder.opombaEdit = (EditText)convertView.findViewById(R.id.callEdit);
	            viewHolder.type = (TextView)convertView.findViewById(R.id.callType);
	            viewHolder.name = (TextView)convertView.findViewById(R.id.callName);
	            viewHolder.number = (TextView)convertView.findViewById(R.id.callNum);
	            viewHolder.date = (TextView)convertView.findViewById(R.id.callDate);
	            viewHolder.duration = (TextView)convertView.findViewById(R.id.callDur);

	            convertView.setTag(viewHolder);
	        }
	        else {
	            viewHolder = (ViewHolder) convertView.getTag();
	        }



	        viewHolder.type.setText("TYPE");
	        viewHolder.name.setText("NAME");
	        viewHolder.number.setText("NUMBER");
	        viewHolder.date.setText("DATE");
	        viewHolder.duration.setText("DURATION");

	        viewHolder.edit.setText("Edit");
	        viewHolder.edit.setTag(1);
	        viewHolder.edit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//edit => save, pokaži edittext z vsebino textview, textview = gone
					final int status =(Integer) v.getTag();
					if(status == 1) {
						viewHolder.edit.setText("Save");
						viewHolder.edit.setTag(2);
						viewHolder.textOpomba = viewHolder.opombaText.getText().toString();
						viewHolder.opombaEdit.setText(viewHolder.textOpomba);
						viewHolder.opombaEdit.setVisibility(View.VISIBLE);
						viewHolder.opombaText.setVisibility(View.GONE);
					}
					else {
						viewHolder.edit.setText("Edit");
						viewHolder.edit.setTag(1);
						viewHolder.textOpomba = viewHolder.opombaEdit.getText().toString();
						viewHolder.opombaText.setText(viewHolder.textOpomba);
						viewHolder.opombaEdit.setVisibility(View.GONE);
						viewHolder.opombaText.setVisibility(View.VISIBLE);
					}
				}
			});
	        
	        viewHolder.del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//zbriši iz baze in zbriši textview, edittext = invisible
					//prvo briši iz baze string
					//TODO
					viewHolder.opombaText.setText("");
					viewHolder.opombaEdit.setText("");
					viewHolder.opombaText.setVisibility(View.GONE);
					viewHolder.edit.setVisibility(View.INVISIBLE);
					viewHolder.del.setVisibility(View.INVISIBLE);
					viewHolder.opombaEdit.setVisibility(View.GONE);
					viewHolder.opombaB.setVisibility(View.VISIBLE);
					
				}
			});
			
	        viewHolder.opombaB.setText("Opombe");
	        viewHolder.opombaB.setTag(1);
	        viewHolder.opombaB.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//prikaži edittext, gumb opombe = gone, prikaži edit in delete gumba
					
					final int status =(Integer) v.getTag();
					if(status == 1) {
						viewHolder.opombaEdit.setVisibility(View.VISIBLE);
						viewHolder.opombaB.setText("Done");
						viewHolder.opombaB.setTag(2);
					}
					else {
						viewHolder.opombaB.setTag(1);
						viewHolder.opombaB.setText("Opombe");
						viewHolder.opombaB.setVisibility(View.INVISIBLE);
						viewHolder.edit.setVisibility(View.VISIBLE);
						viewHolder.del.setVisibility(View.VISIBLE);
						viewHolder.opombaText.setVisibility(View.VISIBLE);
						viewHolder.textOpomba = viewHolder.opombaEdit.getText().toString();
						viewHolder.opombaText.setText(viewHolder.textOpomba);
						viewHolder.opombaEdit.setVisibility(View.GONE);
						//pošlji opombo v database
						
					}
					
				}
			});
	        
	    }

	    return convertView;
	}



	public void setSelectedPosition(int position) {
	    mSelectedPosition = position;
	    notifyDataSetChanged();

	}
	} 

