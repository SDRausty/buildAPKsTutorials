package apt.tutorial;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.database.sqlite.SQLiteDatabase;

public class AppWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context ctxt,
												AppWidgetManager mgr,
												int[] appWidgetIds) {
		ComponentName me=new ComponentName(ctxt, AppWidget.class);
		RemoteViews updateViews=new RemoteViews("apt.tutorial",
																						R.layout.widget);
		RestaurantHelper helper=new RestaurantHelper(ctxt);
		
		try {
			Cursor c=helper
								.getReadableDatabase()
								.rawQuery("SELECT COUNT(*) FROM restaurants", null);
			
			c.moveToFirst();
			
			int count=c.getInt(0);
			
			c.close();
			
			if (count>0) {
				int offset=(int)(count*Math.random());
				String args[]={String.valueOf(offset)};
				
				c=helper
						.getReadableDatabase()
						.rawQuery("SELECT name FROM restaurants LIMIT 1 OFFSET ?", args);
				c.moveToFirst();
				updateViews.setTextViewText(R.id.name, c.getString(0));
			}
			else {
				updateViews.setTextViewText(R.id.name,
																		ctxt.getString(R.string.empty));
			}
		}
		finally {
			helper.close();
		}
		
		mgr.updateAppWidget(me, updateViews);
	}
}