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
import android.support.v4.app.FragmentTransaction;

final class FragmentTransactionAnimations {

    private int enter = 0;
    private int exit = 0;
    private int popEnter = 0;
    private int popExit = 0;

    public void setAnimations(@AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        this.enter = enter;
        this.exit = exit;
        this.popEnter = popEnter;
        this.popExit = popExit;
    }

    public FragmentTransaction apply(FragmentTransaction transaction) {
        return transaction.setCustomAnimations(enter, exit, popEnter, popExit);
    }
}
