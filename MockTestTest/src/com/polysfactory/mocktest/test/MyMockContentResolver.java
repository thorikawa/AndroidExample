package com.polysfactory.mocktest.test;

import android.content.ContentProvider;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.test.mock.MockContentResolver;

public class MyMockContentResolver extends MockContentResolver {

	public MyMockContentResolver(Context context) {
		ContentProvider provider = new MockImagesProvider();
		ProviderInfo providerInfo = new ProviderInfo();
		providerInfo.authority = "media";
		providerInfo.enabled = true;
		providerInfo.isSyncable = false;
		providerInfo.packageName = MockImagesProvider.class.getPackage().getName();
		provider.attachInfo(context, providerInfo);
		super.addProvider("media", provider);
	}
}
