package com.polysfactory.mocktest;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.widget.ArrayAdapter;

public class MyActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Cursor cursor = getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI,
		        new String[] { ImageColumns.TITLE }, null, null, null);
		if (cursor != null) {
			String[] names = new String[cursor.getCount()];
			int i = 0;
			while (cursor.moveToNext()) {
				names[i++] = cursor.getString(0);
			}
			cursor.close();
			setListAdapter(new ArrayAdapter<String>(this,
			        android.R.layout.simple_expandable_list_item_1, names));
		}
	}
}