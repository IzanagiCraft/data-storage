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

/**
 * data-storage; com.izanagicraft.storage.repository:CachedRedisDataRepository
 * <p>
 * A data repository that combines the functionality of {@link RedisDataRepository} and {@link InMemoryDataRepository}
 * to provide a caching mechanism. It delegates calls based on caching logic, attempting to retrieve data from an
 * in-memory cache first and falling back to a Redis database when necessary.
 *
 * @param <T> the type of data to be stored in the repository
 * @author <a href="https://github.com/sanguine6660">@sanguine6660</a>
 * @since 13.12.2023
 */
public class CachedRedisDataRepository<T> implements RedisRepository<T> {

    private final RedisDataRepository<T> redisDataRepository;
    private final InMemoryDataRepository<T> inMemoryDataRepository;

    /**
     * Constructs a {@code CachedRedisDataRepository} with the specified Redis server connection string.
     *
     * @param redisConnection the Redis server connection string
     */
    public CachedRedisDataRepository(String redisConnection) {
        this.redisDataRepository = new RedisDataRepository<>(redisConnection);
        this.inMemoryDataRepository = new InMemoryDataRepository<>();
    }

    @Override
    public T getData(String key) {
        if (!redisDataRepository.isStored(key)) return null;

        // Try to get data from in-memory cache
        T data = inMemoryDataRepository.getData(key);

        // If not found in memory, try to get from Redis database
        if (data == null) {
            data = redisDataRepository.getData(key);

            // If found in Redis database, store in-memory cache
            if (data != null) {
                inMemoryDataRepository.storeData(key, data);
            }
        }

        return data;
    }

    @Override
    public T storeData(String key, T value) {
        // Store in both in-memory cache and Redis database
        inMemoryDataRepository.storeData(key, value);
        redisDataRepository.storeData(key, value);
        return value;
    }

    @Override
    public boolean isStored(String key) {
        // Check if the data is in either in-memory cache or Redis database
        return inMemoryDataRepository.isStored(key) || redisDataRepository.isStored(key);
    }

    @Override
    public void clearStorage() {
        // Clear both in-memory cache and Redis database
        inMemoryDataRepository.clearStorage();
        redisDataRepository.clearStorage();
    }

    @Override
    public T storeDataWithExpiration(String key, T value, long expirationSeconds) {
        return redisDataRepository.storeDataWithExpiration(key, value, expirationSeconds);
    }

}
