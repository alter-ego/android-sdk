package com.sensorberg.sdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.sensorberg.sdk.action.Action;
import com.sensorberg.sdk.demo.HistoryService.HistoryService;

@SuppressWarnings("javadoc")
public class DemoActivity extends Activity
{
	private TextView textView;

    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        textView = new TextView(this);

		StringBuilder builder = new StringBuilder("this is a very basic example...\n");
		for (int i = 0; i < HistoryService.actions.size(); i++) {
			Action action = HistoryService.actions.get(i);
			builder.append(i).append(": ").append(action.getUuid().toString()).append("\n");
		}
		textView.setText(builder.toString());

		setContentView(textView);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		((DemoApplication) getApplication()).setActivityContext(this);
	}

	@Override
	protected void onPause() {
		((DemoApplication) getApplication()).setActivityContext(null);
		super.onPause();
	}
}
