package com.example.gameban;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class activityContainer {


    private static activityContainer instance = new activityContainer();
    private static List<Activity> activityStack = new ArrayList<Activity>();

    public activityContainer() {
    }

    public static activityContainer getInstance() {
        return instance;
    }

    /**
     * Add an Activity to the stack
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new ArrayList<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * Removes the specified Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * End All Activities
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


}
