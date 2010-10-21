package com.polysfactory.mocktest.test;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;

public class MockImagesProvider extends ContentProvider {

	private static final String[] IMAGE_COLUMNS = { ImageColumns._ID, ImageColumns._COUNT,
	        ImageColumns.DATA, ImageColumns.DATE_ADDED, ImageColumns.DATE_MODIFIED,
	        ImageColumns.DISPLAY_NAME, ImageColumns.SIZE, ImageColumns.TITLE };

	private static final String[] VALUE1 = { "1", "1", "/sdcard/hoge.jpg", "1000", "1000",
	        "display_name_test", "100", "title_test" };

	public static final int IMAGE = 1;

	public static final int IMAGE_ID = 2;

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
	        String sortOrder) {
		MatrixCursor result = new MatrixCursor(projection);
		String[] values = new String[projection.length];
		for (int i = 0; i < projection.length; i++) {
			for (int j = 0; j < IMAGE_COLUMNS.length; j++) {
				if (IMAGE_COLUMNS[j].equals(projection[i])) {
					values[i] = VALUE1[j];
				}
			}
		}
		result.addRow(values);
		return result;

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}
}