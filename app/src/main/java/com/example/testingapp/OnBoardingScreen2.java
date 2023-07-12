package com.example.testingapp;

import static android.Manifest.permission.PACKAGE_USAGE_STATS;
import static android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


public class OnBoardingScreen2 extends AppCompatActivity {

    //Variables
    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter3 sliderAdapter;
    TextView[] dots;
    Button letsGetStarted;
    Animation animation;
    int currentPos;
    public final static int REQUEST_NOTIFICATION_ACCESS = 1234;
    private static final int REQUEST_CODE_DEVICE_ADMIN = 1003;
    private static final int REQUEST_LOCATION_PERMISSION = 1001;
    private static final int IGNORE_BATTERY_OPTIMIZATION_REQUEST = 1002;
    private static final int REQUEST_APP_SUPERVISION = 1004;
    private static final int REQUEST_OVERLAY_APP = 1005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen2);

        //Hooks
        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        letsGetStarted = findViewById(R.id.get_started_btn);
        //Call adapter
        sliderAdapter = new SliderAdapter3(this);
        viewPager.setAdapter(sliderAdapter);

        //Dots
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);

        letsGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPos == 0) {
                    requestOverlayPermission();
                }
                else if(currentPos == 1){
                    requestPackageUsagePermission();
                }
                else if(currentPos == 2){
                    boolean isNotificationServiceRunning = isNotificationServiceRunning();
                    if(!isNotificationServiceRunning){
                        startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                }
                else if(currentPos == 3){
                    requestDeviceAdminPermission();
                }
                else if(currentPos==4){
                    requestIgnoreBatteryOptimization();
                }
                else if(currentPos==5){
                    requestLocationPermission();
                }

            }
        });


    }

    public void skip(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void next(View view) {
        viewPager.setCurrentItem(currentPos + 1);
    }

    private void addDots(int position) {

        dots = new TextView[6];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(40);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.black));
        }

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPos = position;


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.getPackageName()));
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);

                startActivityForResult(intent, REQUEST_OVERLAY_APP);
            } else {
                Toast.makeText(this, "Permission Already Given", Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean isNotificationServiceRunning() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners =
                Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }
    private void requestPackageUsagePermission() {
        if (ContextCompat.checkSelfPermission(OnBoardingScreen2.this, PACKAGE_USAGE_STATS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivityForResult(intent, REQUEST_APP_SUPERVISION);
            }

        }
        else Toast.makeText(this, "Permission Already Given", Toast.LENGTH_LONG).show();
    }
    private void requestDeviceAdminPermission() {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdminComponent = new ComponentName(OnBoardingScreen2.this, DeviceAdmin.class);
        if (!devicePolicyManager.isAdminActive(deviceAdminComponent)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminComponent);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Device admin permission is required for...");
            startActivityForResult(intent, REQUEST_CODE_DEVICE_ADMIN);
        }
        else Toast.makeText(this, "Already Given the Permission", Toast.LENGTH_SHORT).show();
    }
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            Toast.makeText(this, "Permission Already Given", Toast.LENGTH_LONG).show();
        }
    }
    private void requestIgnoreBatteryOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivityForResult(intent, IGNORE_BATTERY_OPTIMIZATION_REQUEST);
        } else {
            // No need for permission on older Android versions
            // Proceed with your background operations
            Toast.makeText(this, "Permission not required on this device", Toast.LENGTH_SHORT).show();
        }
    }

}

