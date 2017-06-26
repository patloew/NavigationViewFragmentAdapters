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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

/**
 * An adapter for a NavigationView, which detaches Fragments after another menu item
 * is selected. This means, that each Fragment for a menu item is kept in memory,
 * but there is no overhead for recreating when it is attached again.
 */
public abstract class NavigationViewFragmentAdapter extends BaseNavigationViewFragmentAdapter {

    public NavigationViewFragmentAdapter(@NonNull FragmentManager fragmentManager, @IdRes int containerId, @IdRes int defaultMenuItemId, @Nullable Bundle savedInstanceState) {
       super(fragmentManager, containerId, defaultMenuItemId);

        if(savedInstanceState != null) {
            if(!savedInstanceState.containsKey(KEY_CURRENTID)) {
                throw new IllegalStateException("You must call NavigationViewFragmentAdapter.onSaveInstanceState() in your Activity onSaveInstanceState()");
            } else {
                currentlyAttachedId = savedInstanceState.getInt(KEY_CURRENTID);
            }
        }
    }

    public final void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_CURRENTID, currentlyAttachedId);
    }

    @Override
    @NonNull
    final OnNavigationItemSelectedListener getFragmentAdapterItemSelectedListener() {
        return new FragmentAdapterItemSelectedListener();
    }

    private final class FragmentAdapterItemSelectedListener implements OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            int itemId = item.getItemId();
            boolean handleItem = shouldHandleMenuItem(itemId);
            boolean selectItem = false;

            if(handleItem) {
                if (shouldAddToBackStack(itemId)) {
                    backstackAnimations.apply(fm.beginTransaction())
                            .replace(containerId, getFragment(itemId), getTag(itemId))
                            .addToBackStack(null)
                            .commit();
                    fm.executePendingTransactions();

                } else {
                    String attachTag = getTag(itemId);

                    Fragment attachFragment = fm.findFragmentByTag(attachTag);

                    if (attachFragment == null || !attachFragment.isAdded()) {
                        Fragment detachFragment = fm.findFragmentByTag(getTag(currentlyAttachedId));

                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        animations.apply(fragmentTransaction);

                        if (detachFragment != null && detachFragment != attachFragment) {
                            fragmentTransaction.detach(detachFragment);
                        }

                        if (attachFragment == null) {
                            attachFragment = getFragment(itemId);
                            fragmentTransaction.add(containerId, attachFragment, attachTag);
                        } else {
                            fragmentTransaction.attach(attachFragment);
                        }

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
