package net.shangtai.snmplights;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class SNMPLightsActivity extends Activity {
	public final static String TAG="SNMPLights";

	public void onCreate(Bundle state) {
		super.onCreate(state);

		setContentView(R.layout.main);

		Button prefs=(Button)findViewById(R.id.preferences);
		prefs.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivityForResult(new Intent(getBaseContext(), Preferences.class), 0);
			}
		});



	}
}
