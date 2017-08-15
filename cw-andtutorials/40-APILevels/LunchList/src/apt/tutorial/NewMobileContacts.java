package apt.tutorial;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;

class NewMobileContacts implements MobileContactsBridge {
	public Cursor getMobileNumbers(Activity host) {
		String[] PROJECTION=new String[] { Contacts._ID,
																				Contacts.DISPLAY_NAME,
																				Phone.NUMBER
																			};
		String[] ARGS={String.valueOf(Phone.TYPE_MOBILE)};
		
		return(host.managedQuery(Phone.CONTENT_URI,
																PROJECTION, Phone.TYPE+"=?",
																ARGS, Contacts.DISPLAY_NAME));
	}
	
	public String getDisplayNameField() {
		return(Contacts.DISPLAY_NAME);
	}
}