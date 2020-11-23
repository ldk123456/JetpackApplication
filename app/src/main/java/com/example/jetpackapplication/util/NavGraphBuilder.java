package com.example.jetpackapplication.util;

import android.content.ComponentName;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.example.jetpackapplication.MainApplication;
import com.example.jetpackapplication.model.Destination;
import com.example.jetpackapplication.navigator.FixFragmentNavigator;

import java.util.Map;

public class NavGraphBuilder {
    private NavGraphBuilder() {
    }

    private static class Holder {
        private static final NavGraphBuilder INSTANCE = new NavGraphBuilder();
    }

    public static NavGraphBuilder getInstance() {
        return Holder.INSTANCE;
    }

    public void build(NavController controller, FragmentActivity activity, int containerId) {
        NavigatorProvider provider = controller.getNavigatorProvider();

//        FragmentNavigator fragmentNavigator = provider.getNavigator(FragmentNavigator.class);
        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(activity, activity.getSupportFragmentManager(), containerId);
        provider.addNavigator(fragmentNavigator);
        ActivityNavigator activityNavigator = provider.getNavigator(ActivityNavigator.class);

        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));

        Map<String, Destination> destConfig = AppConfig.getInstance().getDestConfig();
        for (Destination dest : destConfig.values()) {
            if (dest == null) {
                continue;
            }

            if (dest.isFragment) {
                FixFragmentNavigator.Destination destination = fragmentNavigator.createDestination();
                destination.setClassName(dest.className);
                destination.setId(dest.id);
                destination.addDeepLink(dest.pageUrl);

                navGraph.addDestination(destination);
            } else {
                ActivityNavigator.Destination destination = activityNavigator.createDestination();
                destination.setComponentName(new ComponentName(MainApplication.applicationContext().getPackageName(), dest.className));
                destination.setId(dest.id);
                destination.addDeepLink(dest.pageUrl);

                navGraph.addDestination(destination);
            }

            if (dest.asStarter) {
                navGraph.setStartDestination(dest.id);
            }
        }

        controller.setGraph(navGraph);
    }
}
