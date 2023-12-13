/*
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

import java.util.concurrent.CompletableFuture;

/**
 * redis-handler; com.izanagicraft.redis.repository:DataRepository
 * <p>
 * Represents a data repository for storing and caching data.
 * Implementations may include various storage and caching mechanisms.
 *
 * @author <a href="https://github.com/sanguine6660">@sanguine6660</a>
 * @since 13.12.2023
 */
public interface DataRepository<T> {

    /**
     * Retrieves data associated with the specified key.
     *
     * @param key the key to retrieve data
     * @return the data associated with the key, or null if not found
     */
    T getData(String key);

    /**
     * Stores data with the specified key.
     *
     * @param key   the key to store data
     * @param value the data to be stored
     * @return the stored data
     */
    T storeData(String key, T value);

    /**
     * Checks if data associated with the specified key is present in the storage.
     *
     * @param key the key to check for in the storage
     * @return true if the data is present in the cache, false otherwise
     */
    boolean isStored(String key);

    /**
     * Clears the storage, removing all stored data.
     */
    void clearStorage();

    /**
     * Asynchronously retrieves data associated with the specified key.
     *
     * @param key the key to retrieve data
     * @return a CompletableFuture that completes with the data associated with the key, or null if not found
     */
    default CompletableFuture<T> getDataAsync(String key) {
        return CompletableFuture.supplyAsync(() -> getData(key));
    }

    /**
     * Asynchronously stores data with the specified key.
     *
     * @param key   the key to store data
     * @param value the data to be stored
     * @return a CompletableFuture that completes with the stored data
     */
    default CompletableFuture<T> storeDataAsync(String key, T value) {
        return CompletableFuture.supplyAsync(() -> storeData(key, value));
    }

    /**
     * Asynchronously checks if data associated with the specified key is present in the cache.
     *
     * @param key the key to check for in the cache
     * @return a CompletableFuture that completes with true if the data is present in the cache, false otherwise
     */
    default CompletableFuture<Boolean> isInCacheAsync(String key) {
        return CompletableFuture.supplyAsync(() -> isStored(key));
    }

    /**
     * Asynchronously clears the cache, removing all stored data.
     *
     * @return a CompletableFuture that completes when the cache is cleared
     */
    default CompletableFuture<Void> clearCacheAsync() {
        return CompletableFuture.runAsync(this::clearStorage);
    }


}
