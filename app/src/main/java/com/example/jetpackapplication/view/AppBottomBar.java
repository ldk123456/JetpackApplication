package com.example.jetpackapplication.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jetpackapplication.R;
import com.example.jetpackapplication.model.BottomBar;
import com.example.jetpackapplication.model.Destination;
import com.example.jetpackapplication.util.AppConfig;
import com.example.jetpackapplication.util.ToolUtil;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.ArrayList;
import java.util.List;

public class AppBottomBar extends BottomNavigationView {
    private static final List<Integer> ITEM_ICONS = new ArrayList<Integer>() {
        {
            add(R.drawable.icon_tab_home);
            add(R.drawable.icon_tab_sofa);
            add(R.drawable.icon_tab_publish);
            add(R.drawable.icon_tab_find);
            add(R.drawable.icon_tab_mine);
        }
    };

    public AppBottomBar(@NonNull Context context) {
        this(context, null);
    }

    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("RestrictedApi")
    private void init() {
        BottomBar bottomBar = AppConfig.getInstance().getBottomBar();

        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};
        int[] colors = new int[]{Color.parseColor(bottomBar.activeColor), Color.parseColor(bottomBar.inActiveColor)};

        ColorStateList colorStateList = new ColorStateList(states, colors);
        setItemIconTintList(colorStateList);
        setItemTextColor(colorStateList);
        setSelectedItemId(bottomBar.selectTab);
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        List<BottomBar.TabsBean> tabs = bottomBar.tabs;
        for (int i = 0; i < tabs.size(); i++) {
            BottomBar.TabsBean tab = tabs.get(i);
            if (!tab.enable) {
                break;
            }

            int id = getId(tab.pageUrl);
            if (id == -1) {
                break;
            }

            MenuItem item = getMenu().add(0, id, tab.index, tab.title);
            item.setIcon(ITEM_ICONS.get(i));
        }

        for (int i = 0; i < tabs.size(); i++) {
            BottomBar.TabsBean tab = tabs.get(i);
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
            itemView.setIconSize(ToolUtil.dp2px(getContext(), tab.size));
            if (TextUtils.isEmpty(tab.title)) {
                itemView.setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.tintColor)));
                itemView.setShifting(false);
            }
        }
    }

    private int getId(String pageUrl) {
        Destination destination = AppConfig.getInstance().getDestConfig().get(pageUrl);
        if (destination == null) {
            return -1;
        }
        return destination.id;
    }
}
