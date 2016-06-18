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

package com.patloew.navigationviewfragmentadapters;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

/**
 * An adapter for a NavigationView, which saves the state and removes the fragment
 * after another menu item is selected. This means, that only the fragment for
 * the current menu item is kept in memory, but there is overhead for recreating
 * when it is selected again.
 */
public abstract class NavigationViewStateFragmentAdapter extends BaseNavigationViewFragmentAdapter {

    private static final String KEY_STATE = "state";

    private StateMap stateMap = new StateMap();

    public NavigationViewStateFragmentAdapter(FragmentManager fragmentManager, @IdRes int containerId, @IdRes int defaultMenuItemId, Bundle savedInstanceState) {
        super(fragmentManager, containerId, defaultMenuItemId);

        if(savedInstanceState != null) {
            if(!savedInstanceState.containsKey(KEY_STATE)) {
                throw new IllegalStateException("You must call NavigationViewStateFragmentAdapter.onSaveInstanceState() in your Activity onSaveInstanceState()");
            } else {
                stateMap = savedInstanceState.getParcelable(KEY_STATE);
                currentlyAttachedId = savedInstanceState.getInt(KEY_CURRENTID);
            }
        }
    }

    public final void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(KEY_STATE, stateMap);
        outState.putInt(KEY_CURRENTID, currentlyAttachedId);
    }

    @Override
    final NavigationView.OnNavigationItemSelectedListener getFragmentAdapterItemSelectedListener() {
        return new FragmentAdapterItemSelectedListener();
    }

    private final class FragmentAdapterItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            int itemId = item.getItemId();
            boolean handleItem = shouldHandleMenuItem(itemId);
            boolean selectItem = false;

            if(handleItem) {
                if (shouldAddToBackStack(itemId)) {
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    backstackAnimations.apply(fragmentTransaction);
                    fragmentTransaction
                            .replace(containerId, getFragment(itemId), getTag(itemId))
                            .addToBackStack(null)
                            .commit();
                    fm.executePendingTransactions();

                } else {
                    String addTag = getTag(itemId);

                    if (fm.findFragmentByTag(addTag) == null) {
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        animations.apply(fragmentTransaction);

                        String removeTag = getTag(currentlyAttachedId);

                        Fragment addFragment = getFragment(itemId);
                        Fragment removeFragment = fm.findFragmentByTag(removeTag);

                        if (removeFragment != null) {
                            stateMap.put(removeTag, fm.saveFragmentInstanceState(removeFragment));
                            fragmentTransaction.remove(removeFragment);
                        }

                        Fragment.SavedState fss = stateMap.get(addTag);
                        if (fss != null) {
                            addFragment.setInitialSavedState(fss);
                            stateMap.remove(addTag);
                        }
                        fragmentTransaction.add(containerId, addFragment, addTag);

                        fragmentTransaction.commitNow();
                    }

                    currentlyAttachedId = itemId;

                    selectItem = true;
                }
            }

            if(listener != null) {
                listener.onNavigationItemSelected(item);
            } else if(!handleItem) {
                throw new IllegalStateException("You have to set a listener with setNavigationItemSelectedListener() when menu items should not be handled by the adapter");
            }

            return selectItem;
        }
    }

}
