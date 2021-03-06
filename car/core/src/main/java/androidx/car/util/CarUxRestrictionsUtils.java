/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.car.util;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.content.Context;
import android.text.InputFilter;
import android.widget.TextView;

import androidx.annotation.RestrictTo;
import androidx.car.uxrestrictions.CarUxRestrictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class that helps applying {@link CarUxRestrictions} to {@code View}s.
 *
 * @hide
 */
@RestrictTo(LIBRARY_GROUP)
public class CarUxRestrictionsUtils {

    private CarUxRestrictionsUtils() {};

    private static InputFilter sStringLengthFilter;

    /**
     * Applies active UX restrictions to a {@code TextView}.
     *
     * <p>Adds/removes a {@link android.text.InputFilter.LengthFilter} that truncates the text
     * in {@code TextView}.
     *
     * @param carUxRestrictions current Car UX restrictions.
     * @param tv TextView that UX restrictions should be applied to.
     */
    public static void apply(Context context, CarUxRestrictions carUxRestrictions, TextView tv) {
        if (sStringLengthFilter == null) {
            int lengthLimit = carUxRestrictions.getMaxRestrictedStringLength();
            sStringLengthFilter = new InputFilter.LengthFilter(lengthLimit);
        }

        int activeUxr = carUxRestrictions.getActiveRestrictions();

        // Note the list returned by Arrays.asList does not support structural modification.
        List<InputFilter> filters = Arrays.asList(tv.getFilters());
        if ((activeUxr & CarUxRestrictions.UX_RESTRICTIONS_LIMIT_STRING_LENGTH) != 0) {
            // Check whether length filter exists to avoid unnecessary array operations.
            if (!filters.contains(sStringLengthFilter)) {
                ArrayList<InputFilter> updatedFilters = new ArrayList<>(filters);
                updatedFilters.add(sStringLengthFilter);
                tv.setFilters(updatedFilters.toArray(new InputFilter[updatedFilters.size()]));
            }
        } else if (filters.contains(sStringLengthFilter)) {
            ArrayList<InputFilter> updatedFilters = new ArrayList<>(filters);
            updatedFilters.remove(sStringLengthFilter);
            tv.setFilters(updatedFilters.toArray(new InputFilter[updatedFilters.size()]));
        }
    }
}

