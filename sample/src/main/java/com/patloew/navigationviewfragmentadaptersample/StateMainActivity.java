/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */

package com.patloew.navigationviewfragmentadaptersample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.patloew.navigationviewfragmentadapters.CloseDrawerNavigationItemSelectedListener;
import com.patloew.navigationviewfragmentadapters.NavigationViewStateFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StateMainActivity extends AppCompatActivity implements MainActivityView {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MyNavigationViewAdapter myNavigationViewAdapter;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(myNavigationViewAdapter != null) { myNavigationViewAdapter.onSaveInstanceState(outState); }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.stateadapter_label);

        myNavigationViewAdapter = new MyNavigationViewAdapter(getSupportFragmentManager(), R.id.container, R.id.navitem_1, savedInstanceState);
        myNavigationViewAdapter.attachTo(navigationView);
        myNavigationViewAdapter.setCustomAnimations(R.anim.abc_fade_in, 0);
        myNavigationViewAdapter.setBackStackCustomAnimations(R.anim.abc_grow_fade_in_from_bottom, 0, R.anim.abc_fade_in, R.anim.abc_shrink_fade_out_from_bottom);
        myNavigationViewAdapter.setNavigationItemSelectedListener(new CloseDrawerNavigationItemSelectedListener(drawerLayout) {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if(item.getItemId() == R.id.navitem_sample_activity) {
                    startActivity(new Intent(StateMainActivity.this, SampleActivity.class));
                }

                return super.onNavigationItemSelected(item);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            if(drawerLayout.getDrawerLockMode(navigationView) == DrawerLayout.LOCK_MODE_UNLOCKED) {
                if(drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            } else {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void activateDrawerLayout() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void activateBackLayout() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private class MyNavigationViewAdapter extends NavigationViewStateFragmentAdapter {

        public MyNavigationViewAdapter(FragmentManager fragmentManager, @IdRes int containerId, @IdRes int defaultMenuItemId, Bundle savedInstanceState) {
            super(fragmentManager, containerId, defaultMenuItemId, savedInstanceState);
        }

        @NonNull
        @Override
        public Fragment getFragment(@IdRes int menuItemId) {
            switch (menuItemId) {
                case R.id.navitem_1:
                    return SampleFragment.newInstance("Fragment 1");
                case R.id.navitem_2:
                    return SampleFragment.newInstance("Fragment 2");
                case R.id.navitem_3:
                    return SampleFragment.newInstance("Fragment 3");
                default:
                    return SettingsFragment.newInstance();
            }
        }

        @Override
        public boolean shouldAddToBackStack(@IdRes int menuItemId) {
            return menuItemId == R.id.navitem_settings;
        }

        @Override
        public boolean shouldHandleMenuItem(@IdRes int menuItemId) {
            return menuItemId != R.id.navitem_sample_activity;
        }
    }
}
