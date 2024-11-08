package com.example.quacks_app;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.provider.Settings;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

public class DeviceIDTest {
    @Test
    public void userMatchesDeviceID() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String testID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        User testUser = new User(testID);
        assertEquals(testID, testUser.getDeviceId());
    }
}
