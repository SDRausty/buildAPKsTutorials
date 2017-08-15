package apt.tutorial;

import android.app.Activity;
import android.database.Cursor;
import android.provider.Contacts;

class OldMobileContacts implements MobileContactsBridge {
	public Cursor getMobileNumbers(Activity host) {
		String[] PROJECTION=new String[] { 	Contacts.Phones._ID,
																				Contacts.Phones.NAME,
																				Contacts.Phones.NUMBER
																			};
		String[] ARGS={String.valueOf(Contacts.Phones.TYPE_MOBILE)};
		
		return(host.managedQuery(Contacts.Phones.CONTENT_URI,
															PROJECTION,
															Contacts.Phones.TYPE+"=?", ARGS,
															Contacts.Phones.NAME));
	}
	
	public String getDisplayNameField() {
		return(Contacts.Phones.NAME);
	}
}