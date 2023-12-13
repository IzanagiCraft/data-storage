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

import com.izanagicraft.storage.utils.SerializationUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;

/**
 * data-storage; com.izanagicraft.storage.repository:RedisDataRepository
 * <p>
 * An implementation of the {@link DataRepository} interface that stores and retrieves data using a Redis database.
 * It uses the Jedis library for communication with the Redis server.
 *
 * @param <T> the type of data to be stored/retrieved
 * @author <a href="https://github.com/sanguine6660">@sanguine6660</a>
 * @since 13.12.2023
 */
public class RedisDataRepository<T> implements RedisRepository<T> {

    private final Jedis jedis;

    /**
     * Constructs a {@code RedisDataRepository} with the specified Redis server host and port.
     *
     * @param connection the Redis server connection string
     */
    public RedisDataRepository(String connection) {
        this.jedis = new Jedis(connection);
    }

    @Override
    public T getData(String key) {
        try {
            byte[] serializedValue = jedis.get(key.getBytes());
            if (serializedValue != null) {
                return SerializationUtils.deserialize(serializedValue);
            }
        } catch (JedisException | IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle the exception based on your use case
        }
        return null;
    }

    @Override
    public T storeData(String key, T value) {
        try {
            byte[] serializedValue = SerializationUtils.serialize(value);
            jedis.set(key.getBytes(), serializedValue);
        } catch (JedisException | IOException e) {
            e.printStackTrace(); // Handle the exception based on your use case
        }
        return value;
    }

    @Override
    public boolean isStored(String key) {
        try {
            return jedis.exists(key);
        } catch (JedisException e) {
            e.printStackTrace(); // Handle the exception based on your use case
            return false;
        }
    }

    @Override
    public void clearStorage() {
        try {
            jedis.flushDB();
        } catch (JedisException e) {
            e.printStackTrace(); // Handle the exception based on your use case
        }
    }

    @Override
    public T storeDataWithExpiration(String key, T value, long expirationSeconds) {
        try {
            byte[] serializedValue = SerializationUtils.serialize(value);
            jedis.setex(key.getBytes(), expirationSeconds, serializedValue);
        } catch (JedisException | IOException e) {
            e.printStackTrace(); // Handle the exception based on your use case
        }
        return value;
    }
}
