package com.polysfactory.roboguice_and_mock.test;

import java.util.List;

import roboguice.application.GuiceApplication;
import roboguice.config.AbstractAndroidModule;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.TextView;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;
import com.google.inject.Module;
import com.polysfactory.roboguice_and_mock.Person;
import com.polysfactory.roboguice_and_mock.TopActivity;
import com.polysfactory.roboguice_and_mock.impl.Droid;

public class TestCase extends ActivityUnitTestCase<TopActivity> {

	final class MyMockModule extends AbstractAndroidModule {
		private Person mockPerson;

		public void setMock(Person person) {
			this.mockPerson = person;
		}

		@Override
		protected void configure() {
			bind(Person.class).toInstance(mockPerson);
		}
	}

	final class MyMockApplication extends GuiceApplication {
		private Module myModule;

		public void setMyModule(Module myModule) {
			this.myModule = myModule;
		}

		@Override
		protected void addApplicationModules(List<Module> modules) {
			if (myModule == null) {
				throw new IllegalArgumentException("Please call setMyModule before running the tests!");
			}
			modules.add(myModule);
		}

		MyMockApplication(Context context) {
			super();
			attachBaseContext(context);
		}
	}

	public TestCase() {
		super(TopActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

	}

	@MediumTest
	@UsesMocks(Droid.class)
	public void testMockDroid1() {

		// create a mock and learn it's behavior
		Droid mockDroid = AndroidMock.createMock(Droid.class);
		AndroidMock.expect(mockDroid.getName()).andStubReturn("mock");
		mockDroid.setColor("white");
		AndroidMock.replay(mockDroid);

		// set up mock application object
		Context context = getInstrumentation().getTargetContext();
		MyMockApplication application = new MyMockApplication(context);
		MyMockModule myMockModule = new MyMockModule();
		myMockModule.setMock(mockDroid);
		application.setMyModule(myMockModule);
		setApplication(application);

		// start activity
		Intent intent = new Intent(context, TopActivity.class);
		TopActivity activity = startActivity(intent, null, null);
		TextView textView = (TextView) activity
		        .findViewById(com.polysfactory.roboguice_and_mock.R.id.text);

		// verify
		assertEquals("mock", textView.getText());
		AndroidMock.verify(mockDroid);
	}
	
	@MediumTest
	@UsesMocks(Droid.class)
	public void testMockDroid2() {

		// create a mock and learn it's behavior
		Droid mockDroid = AndroidMock.createMock(Droid.class);
		AndroidMock.expect(mockDroid.getName()).andStubReturn("droid");
		mockDroid.setColor("green");
		AndroidMock.replay(mockDroid);

		// set up mock application object
		Context context = getInstrumentation().getTargetContext();
		MyMockApplication application = new MyMockApplication(context);
		MyMockModule myMockModule = new MyMockModule();
		myMockModule.setMock(mockDroid);
		application.setMyModule(myMockModule);
		setApplication(application);

		// start activity
		Intent intent = new Intent(context, TopActivity.class);
		TopActivity activity = startActivity(intent, null, null);
		TextView textView = (TextView) activity
		        .findViewById(com.polysfactory.roboguice_and_mock.R.id.text);

		// verify
		assertEquals("droid", textView.getText());
		AndroidMock.verify(mockDroid);
	}

}
