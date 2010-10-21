package com.polysfactory.mocktest.test;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;

public class MyMockContext extends ContextWrapper {

	private ContentResolver contentResolver;

	public MyMockContext(Context context) {
		super(context);
		contentResolver = new MyMockContentResolver(context);
	}


	@Override
	public ContentResolver getContentResolver() {
		return contentResolver;
	}

}
