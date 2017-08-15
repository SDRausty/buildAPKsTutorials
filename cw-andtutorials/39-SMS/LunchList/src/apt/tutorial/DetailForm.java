package apt.tutorial;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
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
		else if (item.getItemId()==R.id.photo) {
			startActivity(new Intent(this, Photographer.class));
			
			return(true);
		}
		else if (item.getItemId()==R.id.sms) {
			sendSMS();
			
			return(true);
		}
		
		return(super.onOptionsItemSelected(item));
	}
	
	private void sendSMS() {
		String[] PROJECTION=new String[] { Contacts._ID,
																				Contacts.DISPLAY_NAME,
																				Phone.NUMBER
																			};
		String[] ARGS={String.valueOf(Phone.TYPE_MOBILE)};
		final Cursor c=managedQuery(Phone.CONTENT_URI,
																PROJECTION, Phone.TYPE+"=?",
																ARGS, Contacts.DISPLAY_NAME);
		DialogInterface.OnClickListener onSMSClicked=
			new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int position) {
				c.moveToPosition(position);
				
				noReallySendSMS(c.getString(2));
			}
		};
	
		new AlertDialog.Builder(this)
			.setTitle("Pick a Person")
			.setCursor(c, onSMSClicked, Contacts.DISPLAY_NAME)
			.show();
	}
	
	private void noReallySendSMS(String phone) {
		StringBuilder buf=new StringBuilder("We are going to ");
		
		buf.append(name.getText());
		buf.append(" at ");
		buf.append(address.getText());
		buf.append(" for lunch!");
		
		SmsManager
			.getDefault()
			.sendTextMessage(phone, null,	buf.toString(), null, null);
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