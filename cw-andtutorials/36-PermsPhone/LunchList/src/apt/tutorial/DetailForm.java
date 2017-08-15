package apt.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class DetailForm extends Activity {
	EditText name=null;
	EditText address=null;
	EditText phone=null;
	EditText notes=null;
	RadioGroup types=null;
	RestaurantHelper helper=null;
	String restaurantId=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
		helper=new RestaurantHelper(this);
		
		name=(EditText)findViewById(R.id.name);
		address=(EditText)findViewById(R.id.addr);
		phone=(EditText)findViewById(R.id.phone);
		notes=(EditText)findViewById(R.id.notes);
		types=(RadioGroup)findViewById(R.id.types);
		
		Button save=(Button)findViewById(R.id.save);
		
		save.setOnClickListener(onSave);
		
		restaurantId=getIntent().getStringExtra(LunchList.ID_EXTRA);
		
		if (restaurantId!=null) {
			load();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	
		helper.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(getApplication())
																 .inflate(R.menu.option_detail, menu);
																 
		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.call) {
			String toDial="tel:"+phone.getText().toString();
			
			if (toDial.length()>4) {
				startActivity(new Intent(Intent.ACTION_CALL,
																 Uri.parse(toDial)));
			
				return(true);
			}
		}
		
		return(super.onOptionsItemSelected(item));
	}
	
	private void load() {
		Cursor c=helper.getById(restaurantId);

		c.moveToFirst();		
		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		phone.setText(helper.getPhone(c));
		notes.setText(helper.getNotes(c));
		
		if (helper.getType(c).equals("sit_down")) {
			types.check(R.id.sit_down);
		}
		else if (helper.getType(c).equals("take_out")) {
			types.check(R.id.take_out);
		}
		else {
			types.check(R.id.delivery);
		}
		
		c.close();
	}
	
	private View.OnClickListener onSave=new View.OnClickListener() {
		public void onClick(View v) {
			String type=null;
			
			switch (types.getCheckedRadioButtonId()) {
				case R.id.sit_down:
					type="sit_down";
					break;
				case R.id.take_out:
					type="take_out";
					break;
				case R.id.delivery:
					type="delivery";
					break;
			}

			if (restaurantId==null) {
				helper.insert(name.getText().toString(),
											address.getText().toString(), type,
											notes.getText().toString(),
											phone.getText().toString());
			}
			else {
				helper.update(restaurantId, name.getText().toString(),
											address.getText().toString(), type,
											notes.getText().toString(),
											phone.getText().toString());
			}
			
			finish();
		}
	};
}