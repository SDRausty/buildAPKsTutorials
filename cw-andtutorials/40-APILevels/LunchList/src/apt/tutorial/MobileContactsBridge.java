package apt.tutorial;

import android.app.Activity;
import android.database.Cursor;

interface MobileContactsBridge {
	Cursor getMobileNumbers(Activity host);
	String getDisplayNameField();
}