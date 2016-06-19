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

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

abstract class BaseNavigationViewFragmentAdapter {

    static final String KEY_CURRENTID = "currentId";

    final FragmentManager fm;
    final int containerId;

    NavigationView.OnNavigationItemSelectedListener listener = null;

    FragmentTransactionAnimations animations = new FragmentTransactionAnimations();
    FragmentTransactionAnimations backstackAnimations = new FragmentTransactionAnimations();

    int currentlyAttachedId;

    private boolean attached = false;

    public BaseNavigationViewFragmentAdapter(@NonNull FragmentManager fragmentManager, @IdRes int containerId, @IdRes int defaultMenuItemId) {
        this.fm = fragmentManager;
        this.containerId = containerId;
        this.currentlyAttachedId = defaultMenuItemId;
    }

    // For internal use
    @NonNull
    abstract NavigationView.OnNavigationItemSelectedListener getFragmentAdapterItemSelectedListener();


    /**
     * Sets custom animations for fragment transactions when replacing
     * navigation view fragments.
     */
    public final void setCustomAnimations(@AnimRes int enter, @AnimRes int exit) {
        animations.setAnimations(enter, exit, 0, 0);
    }

    /**
     * Sets custom animations for fragment transactions when replacing
     * navigation view fragments.
     */
    public final void setCustomAnimations(@AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        animations.setAnimations(enter, exit, popEnter, popExit);
    }

    /**
     * Sets custom animations for fragment transactions which are added
     * to the back stack.
     */
    public final void setBackStackCustomAnimations(@AnimRes int enter, @AnimRes int exit) {
        backstackAnimations.setAnimations(enter, exit, 0, 0);
    }

    /**
     * Sets custom animations for fragment transactions which are added
     * to the back stack.
     */
    public final void setBackStackCustomAnimations(@AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        backstackAnimations.setAnimations(enter, exit, popEnter, popExit);
    }

    /**
     * Sets an OnNavigationItemSelectedListener for adding additional behavior (e.g. closing
     * a drawer). This is called after the fragment transaction is commited. The return value of
     * OnNavigationItemSelectedListener.onNavigationItemSelected(MenuItem) is ignored.
     *
     * @param listener An OnNavigationItemSelectedListener
     */
    public final void setNavigationItemSelectedListener(@Nullable NavigationView.OnNavigationItemSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * Attaches this adapter to a NavigationView. This can only
     * be called once per adapter.
     *
     * @param navigationView The NavigationView to attach to.
     */
    public final void attachTo(NavigationView navigationView) {
        if(attached) {
            throw new IllegalStateException("The adapter can only be attached once.");

        } else {
            navigationView.setNavigationItemSelectedListener(getFragmentAdapterItemSelectedListener());

            if(!shouldAddToBackStack(currentlyAttachedId)) {
                navigationView.getMenu().performIdentifierAction(currentlyAttachedId, 0);
            }

            attached = true;
        }
    }

    /**
     * Get the fragment tag for the Fragment with menuItemId. Override
     * method for custom tags.
     *
     * @param menuItemId
     * @return Unique tag for the Fragment with menuItemId
     */
    @NonNull
    public String getTag(@IdRes int menuItemId) {
        return "itemId:" + menuItemId;
    }

    /**
     * Get the Fragment for menuItemId.
     *
     * @param menuItemId
     * @return The Fragment for menuItemId
     */
    @NonNull
    public abstract Fragment getFragment(@IdRes int menuItemId);

    /**
     * Can be overriden to declare that some menu items (e.g. settings fragment)
     * should not simply replace the current fragment, but also be
     * added to the back stack.
     *
     * @param menuItemId
     * @return Whether menuItemId fragment should be added to the back stack
     */
    public boolean shouldAddToBackStack(@IdRes int menuItemId) {
        return false;
    }

    /**
     * Can be overriden to declare that some menu items should not be handled by the adapter (e.g. for
     * starting an Activity). You have to handle these items yourself by setting a listener with
     * setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener).
     *
     * @param menuItemId
     * @return Whether menuItem should be handled by adapter
     */
    public boolean shouldHandleMenuItem(@IdRes int menuItemId) {
        return true;
    }
}
