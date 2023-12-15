/*
 * ▪  ·▄▄▄▄• ▄▄▄·  ▐ ▄  ▄▄▄·  ▄▄ • ▪   ▄▄· ▄▄▄   ▄▄▄· ·▄▄▄▄▄▄▄▄
 * ██ ▪▀·.█▌▐█ ▀█ •█▌▐█▐█ ▀█ ▐█ ▀ ▪██ ▐█ ▌▪▀▄ █·▐█ ▀█ ▐▄▄·•██
 * ▐█·▄█▀▀▀•▄█▀▀█ ▐█▐▐▌▄█▀▀█ ▄█ ▀█▄▐█·██ ▄▄▐▀▀▄ ▄█▀▀█ ██▪  ▐█.▪
 * ▐█▌█▌▪▄█▀▐█ ▪▐▌██▐█▌▐█ ▪▐▌▐█▄▪▐█▐█▌▐███▌▐█•█▌▐█ ▪▐▌██▌. ▐█▌·
 * ▀▀▀·▀▀▀ • ▀  ▀ ▀▀ █▪ ▀  ▀ ·▀▀▀▀ ▀▀▀·▀▀▀ .▀  ▀ ▀  ▀ ▀▀▀  ▀▀▀
 *
 *
 *    @@@@@
 *    @@* *@@
 *      @@@  @@@
 *         @@@  @@ @@@       @@@@@@@@@@@
 *           @@@@@@@@   @@@@@@@@@@@@@@@@@@@@@
 *            @@@    @@@@@@@@@@@@@@@@@@@@@@@@@@@
 *                 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
 *                @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
 *                @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
 *               #@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
 *               #@@@   @@                 @@  @@@@  @@@@
 *                @@@@      @@@      @@@@      @@@@   @@@
 *                @@@@@@                     @@@@@@    @@
 *                 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
 *                  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@
 *                   @@@@@@@@@@@@@@@@@@@@@@@@@@@
 *                     @@@@@@@@@@@@@@@@@@@@@@@
 *                       @@@@@@@@@@@@@@@@@@@
 *                           @@@@@@@@@@@
 *
 * Copyright (c) 2023 - present | sanguine6660 <sanguine6660@gmail.com>
 * Copyright (c) 2023 - present | izanagicraft.com <contact@izanagicraft.com>
 * Copyright (c) 2023 - present | izanagicraft.com team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.izanagicraft.storage.repository;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * data-storage; com.izanagicraft.storage.repository:RedisRepository
 *
 * @author <a href="https://github.com/sanguine6660">@sanguine6660</a>
 * @since 13.12.2023
 */
public interface RedisRepository<T> extends DataRepository<T> {

    /**
     * Stores data with the specified key and an expiration time.
     *
     * @param key               the key to store data
     * @param value             the data to be stored
     * @param expirationSeconds the seconds after which the data will expire
     * @return the stored data
     */
    T storeDataWithExpiration(String key, T value, long expirationSeconds);

    /**
     * Stores the provided data with the specified key and an expiration time.
     * The expiration time is represented as a string in a format that includes
     * years (y), months (M), weeks (w), days (d), hours (h), minutes (m),
     * seconds (s), and milliseconds (ms).
     *
     * @param key        the key to store the data
     * @param value      the data to be stored
     * @param expiration a string representing the expiration time in the format
     *                   "yMdHmsms" (e.g., "1d12h30m" for 1 day, 12 hours, and 30 minutes)
     * @return the stored data
     * @throws IllegalArgumentException if the provided expiration string has an invalid format
     */
    default T storeDataWithExpiration(String key, T value, String expiration) {
        // Delegates to the main storeDataWithExpiration method after parsing the expiration string
        return storeDataWithExpiration(key, value, parseTimeString(expiration).toSeconds());
    }

    /**
     * Parses the given time string and returns the equivalent {@link Duration}.
     *
     * @param timeString the time string to be parsed
     * @return the {@link Duration} equivalent of the parsed time string
     * @throws IllegalArgumentException if the time string has an invalid format
     */
    default Duration parseTimeString(String timeString) {
        // Define a regex pattern to match the components of the time string
        Pattern pattern = Pattern.compile("(?:(\\d+)y)?(?:(\\d+)M)?(?:(\\d+)w)?(?:(\\d+)d)?(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?(?:(\\d+)ms)?");

        // Parse the time string using the regex pattern
        Matcher matcher = pattern.matcher(timeString);

        // Extract the matched components
        if (matcher.matches()) {
            long years = parseOrDefault(matcher.group(1), 0);
            long months = parseOrDefault(matcher.group(2), 0);
            long weeks = parseOrDefault(matcher.group(3), 0);
            long days = parseOrDefault(matcher.group(4), 0);
            long hours = parseOrDefault(matcher.group(5), 0);
            long minutes = parseOrDefault(matcher.group(6), 0);
            long seconds = parseOrDefault(matcher.group(7), 0);
            long milliseconds = parseOrDefault(matcher.group(8), 0);

            // Calculate the total duration
            return Duration.ofDays(years * 365 + months * 30 + weeks * 7 + days)
                    .plusHours(hours)
                    .plusMinutes(minutes)
                    .plusSeconds(seconds)
                    .plusMillis(milliseconds);
        } else {
            throw new IllegalArgumentException("Invalid time string format: " + timeString);
        }
    }


    /**
     * Parses the given string value into a long, or returns a default value if the string is null.
     *
     * @param value        the string value to be parsed
     * @param defaultValue the default value to be returned if the string is null
     * @return the parsed long value or the default value if the string is null
     */
    private long parseOrDefault(String value, long defaultValue) {
        return value != null ? Long.parseLong(value) : defaultValue;
    }

}
