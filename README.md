# NavigationViewFragmentAdapters

 [ ![Download](https://api.bintray.com/packages/patloew/maven/NavigationViewFragmentAdapters/images/download.svg) ](https://bintray.com/patloew/maven/NavigationViewFragmentAdapters/_latestVersion) [![API](https://img.shields.io/badge/API-9%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=9)

A small library containing two adapters which allow for easy fragment management with a NavigationView.

The library handles replacing the fragments and saving/restoring the fragment state, including showing the right fragment when opening an app again after it was killed.

# Usage

First, decide which adapter suits your use case better.

The `NavigationViewFragmentAdapter` detaches fragments after another menu item is selected. This means that each Fragment for a menu item is kept in memory, but there is no overhead for recreating when it is attached again.

The `NavigationViewStateFragmentAdapter` saves the state and removes the fragment after another menu item is selected. This means that only the fragment for the current menu item is kept in memory, but there is overhead for recreating other fragments when they are selected again.

After you decided, create your adapter implementation:

	private class MyNavigationViewAdapter extends NavigationViewFragmentAdapter {

	    public MyDrawerAdapter(FragmentManager fragmentManager, @IdRes int containerId, @IdRes int defaultMenuItemId, Bundle savedInstanceState) {
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
	}

Now, create an instance and attach it to your NavigationView in your Activity `onCreate()`:

	myDrawerAdapter = new MyDrawerAdapter(getSupportFragmentManager(), R.id.container, R.id.navitem_1, savedInstanceState);
	myDrawerAdapter.attachTo(navigationView);

Also, don't forget to call `myDrawerAdapter.onSaveInstanceState()` in your Activity `onSaveInstanceState()`.

Now you have your navigation drawer up and running, including state saving of the fragments.

# Advanced Usage

## Back Stack

There are cases when some menu items (e.g. settings) should not simply replace the current fragment, but also be added to the back stack. To do this, override `shouldAddToBackStack()` in your adapter:

	@Override
    public boolean shouldAddToBackStack(@IdRes int menuItemId) {
        return menuItemId == R.id.navitem_settings;
    }

## Animations

You can provide custom animations for your fragment transactions via `setCustomAnimations()`. Separate animations can be set for when you add the transaction to the back stack via `setBackStackCustomAnimations()`.

## OnNavigationItemSelectedListener

An additional `OnNavigationItemSelectedListener` can be provided via `setNavigationItemSelectedListener()`. This can be used to add behavior after the fragment transaction was commited. If you just want to close the drawer, the lib includes a `CloseDrawerNavigationItemSelectedListener`.

# Sample

A basic sample app with example Activities for both adapters is available in the `sample` project.

# Setup

Add the following to your `build.gradle`:
	
	repositories {		
		maven { url 'https://dl.bintray.com/patloew/maven' }		
	}

	dependencies {
	    compile 'com.patloew.navigationviewfragmentadapters:adapters:0.1.1'
	}

# License

	Copyright 2016 Patrick Löwenstein

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.