package com.ruslan.androidlauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AndroidLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        return AndroidLauncherFragment.newInstance();
    }
}
