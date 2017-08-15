/***
	Copyright (c) 2008-2011 CommonsWare, LLC
	Licensed under the Apache License, Version 2.0 (the "License"); you may not
	use this file except in compliance with the License. You may obtain	a copy
	of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
	by applicable law or agreed to in writing, software distributed under the
	License is distributed on an "AS IS" BASIS,	WITHOUT	WARRANTIES OR CONDITIONS
	OF ANY KIND, either express or implied. See the License for the specific
	language governing permissions and limitations under the License.
	
	From _Tuning Android Applications_
		http://commonsware.com/AndTuning
*/

package com.commonsware.android.tuning.lua;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LuaActivity extends Activity {
	public static native String executeLua(String script);
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
	}
	
	public void runScript(View v) {
		EditText script=(EditText)findViewById(R.id.script);
		String src=script.getText().toString();
		
		try {
			String eval_result=executeLua(src);
			Toast.makeText(this, eval_result, Toast.LENGTH_LONG).show();
		}
		catch (Exception e) {
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			
			builder
				.setTitle("Exception!")
				.setMessage(e.toString())
				.setPositiveButton("OK", null)
				.show();
		}
	}
	
	static { System.loadLibrary("lua"); }
}
