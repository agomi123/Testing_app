package com.example.testingapp;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class GetAllAppsTask extends AsyncTask<Void, Void, List<ApplicationInfo>> {

    private MainActivity activity;
    private List<ApplicationInfo> apps;
    private PackageManager packageManager;

    public GetAllAppsTask(MainActivity activity, List<ApplicationInfo> apps, PackageManager pm) {
        this.activity = activity;
        this.apps = apps;
        this.packageManager = pm;
    }

    @Override
    protected List<ApplicationInfo> doInBackground(Void... params) {
        //getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
//        final PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        @SuppressLint("QueryPermissionsNeeded")
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(0);
//        for (ApplicationInfo app : apps) {
//            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
//                // Exclude system apps
//                String installerPackageName = packageManager.getInstallerPackageName(app.packageName);
//                if ("com.android.vending".equals(installerPackageName)) {
//                    // It is installed from the Google Play Store
////                    apk.add(app);
//                }
//            }
//        }
        return apps;
    }
    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<>();
        for (ApplicationInfo applicationInfo : list) {
            try {
                if (packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null) {
                    applist.add(applicationInfo);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return applist;
    }

    @Override
    protected void onPostExecute(List<ApplicationInfo> list) {
        super.onPostExecute(list);
        activity.callBackDataFromAsynctask(list);
    }
}