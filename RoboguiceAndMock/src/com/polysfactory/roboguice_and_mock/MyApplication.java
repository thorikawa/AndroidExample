package com.polysfactory.roboguice_and_mock;

import java.util.List;

import roboguice.application.GuiceApplication;

import com.google.inject.Module;

public class MyApplication extends GuiceApplication {
	@Override
	protected void addApplicationModules(List<Module> modules) {
		modules.add(new MyModule());
	}
}
