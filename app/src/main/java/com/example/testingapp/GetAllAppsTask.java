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
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        @SuppressLint("QueryPermissionsNeeded")
//        List<ApplicationInfo> apps = packageManager.getInstalledApplications(0);
//        apps= checkForLaunchIntent(apps);
        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES |
                PackageManager.GET_UNINSTALLED_PACKAGES;

//        PackageManager pm = getPackageManager();
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(flags);
        List<ApplicationInfo>app=new ArrayList<>();
        for (ApplicationInfo appInfo : applications) {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                // System application
                app.add(appInfo);

            } else {
                // Installed by user
            }
        }
        return app;
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