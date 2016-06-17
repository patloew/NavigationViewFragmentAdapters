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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

final class StateMap implements Parcelable {

    private final HashMap<String, Fragment.SavedState> map;

    public StateMap() {
        map = new HashMap<>();
    }

    // Parcelable

    public StateMap(Parcel in) {
        ClassLoader classLoader = getClass().getClassLoader();

        int size = in.readInt();
        map = new HashMap<>(size);

        for(int i = 0; i < size; i++){
            map.put(in.readString(), (Fragment.SavedState) in.readParcelable(classLoader));
        }
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(map.size());

        for(Map.Entry<String, Fragment.SavedState> e : map.entrySet()){
            out.writeString(e.getKey());
            out.writeParcelable(e.getValue(), flags);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StateMap> CREATOR = new Creator<StateMap>() {
        @Override
        public StateMap createFromParcel(Parcel in) {
            return new StateMap(in);
        }

        @Override
        public StateMap[] newArray(int size) {
            return new StateMap[size];
        }
    };

    // Map<K, V>

    public Fragment.SavedState get(String key) {
        return map.get(key);
    }

    public Fragment.SavedState put(String key, Fragment.SavedState value) {
        return map.put(key, value);
    }

    public Fragment.SavedState remove(String key) {
        return map.remove(key);
    }
}
