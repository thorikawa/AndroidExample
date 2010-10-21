package com.polysfactory.mocktest.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.Smoke;
import android.widget.ListView;

import com.polysfactory.mocktest.MyActivity;

public class MockTestSample extends ActivityUnitTestCase<MyActivity> {

	public MockTestSample() {
		super(MyActivity.class);
	}

	@Smoke
	public void testSample() {
		Context mockContext = new MyMockContext(getInstrumentation().getTargetContext());
		setActivityContext(mockContext);
		startActivity(new Intent(), null, null);
		final MyActivity activity = getActivity();
		ListView listView = activity.getListView();
		int count = listView.getCount();
		String item0 = (String)listView.getAdapter().getItem(0);

		assertNotNull(activity);
		assertEquals(1, count);
		assertEquals("title_test", item0);
	}
}
