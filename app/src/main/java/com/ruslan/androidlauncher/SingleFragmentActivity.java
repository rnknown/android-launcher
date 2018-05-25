package com.ruslan.androidlauncher;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by rusla on 08.03.2016.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        // Set system wallpaper
        FrameLayout fragment_container = (FrameLayout) findViewById(R.id.fragment_container);
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        fragment_container.setBackground(wallpaperDrawable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // User should not be able to go back to system launcher when pressing back button.
        Toast.makeText(this, "This is your launcher now!\nLoading...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SingleFragmentActivity.this, AndroidLauncherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
