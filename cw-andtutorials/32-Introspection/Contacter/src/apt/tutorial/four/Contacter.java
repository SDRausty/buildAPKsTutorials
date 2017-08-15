package apt.tutorial.four;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Contacter extends ListActivity {
	private static final int PICK_REQUEST=1337;
	private static Uri CONTENT_URI=null;
	
	static {
		int sdk=new Integer(Build.VERSION.SDK).intValue();
		
		if (sdk>=5) {
			try {
				Class clazz=Class.forName("android.provider.ContactsContract$Contacts");
			
				CONTENT_URI=(Uri)clazz.getField("CONTENT_URI").get(clazz);
			}
			catch (Throwable t) {
				Log.e("PickDemo", "Exception when determining CONTENT_URI", t);
			}
		}
		else {
			CONTENT_URI=Contacts.People.CONTENT_URI;
		}
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		if (CONTENT_URI==null) {
			Toast
				.makeText(this, "We are experiencing technical difficulties...",
									Toast.LENGTH_LONG)
				.show();
			finish();
			
			return;
		}
		
		setContentView(R.layout.main);
		
		Button btn=(Button)findViewById(R.id.pick);
		
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent i=new Intent(Intent.ACTION_PICK, CONTENT_URI);

				startActivityForResult(i, PICK_REQUEST);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
																		Intent data) {
		if (requestCode==PICK_REQUEST) {
			if (resultCode==RESULT_OK) {
				Uri contact=data.getData();
				Intent stub=new Intent();
				
				stub.setData(contact);
				
				PackageManager pm=getPackageManager();
				List<ResolveInfo> actions=pm.queryIntentActivities(stub, 0);
				
				Collections.sort(actions,
										 new ResolveInfo.DisplayNameComparator(pm));
				
				setListAdapter(new ActionAdapter(pm, actions));
			}
		}
	}
	
	class ActionAdapter extends ArrayAdapter<ResolveInfo> {
		private PackageManager pm=null;
		
		ActionAdapter(PackageManager pm, List<ResolveInfo> apps) {
			super(Contacter.this, R.layout.row, apps);
			this.pm=pm;
		}
		
		@Override
		public View getView(int position, View convertView,
													ViewGroup parent) {
			if (convertView==null) {
				convertView=newView(parent);
			}
			
			bindView(position, convertView);
			
			return(convertView);
		}
		
		private View newView(ViewGroup parent) {
			return(getLayoutInflater().inflate(R.layout.row, parent, false));
		}
		
		private void bindView(int position, View row) {
			TextView label=(TextView)row.findViewById(R.id.label);
			
			label.setText(getItem(position).loadLabel(pm));
			
			ImageView icon=(ImageView)row.findViewById(R.id.icon);
			
			icon.setImageDrawable(getItem(position).loadIcon(pm));
		}
	}
}