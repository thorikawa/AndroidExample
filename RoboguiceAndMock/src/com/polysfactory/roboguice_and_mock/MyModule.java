package com.polysfactory.roboguice_and_mock;

import roboguice.config.AbstractAndroidModule;

import com.polysfactory.roboguice_and_mock.impl.Droid;

public class MyModule extends AbstractAndroidModule {
	@Override
	protected void configure() {
		bind(Person.class).to(Droid.class);
	}
}
