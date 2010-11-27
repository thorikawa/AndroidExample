package com.polysfactory.roboguice_and_mock;

import roboguice.activity.GuiceActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.TextView;

import com.google.inject.Inject;

public class TopActivity extends GuiceActivity {
	@InjectView(R.id.text)
	private TextView textView;

	@Inject
	private Person person;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (person.getName().equals("droid")) {
			person.setColor("green");
		} else {
			person.setColor("white");
		}
		textView.setText(person.getName());
	}
}
