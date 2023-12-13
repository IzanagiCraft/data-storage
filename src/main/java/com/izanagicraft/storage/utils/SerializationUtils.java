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

package com.izanagicraft.storage.utils;

import java.io.*;

/**
 * data-storage; com.izanagicraft.storage.utils:SerializationUtils
 * <p>
 * Utility class for object serialization and deserialization.
 *
 * @author <a href="https://github.com/sanguine6660">@sanguine6660</a>
 * @since 13.12.2023
 */
public class SerializationUtils {

    /**
     * Serializes an object to a byte array.
     *
     * @param obj the object to serialize
     * @return a byte array representing the serialized object
     * @throws IOException if an I/O error occurs during serialization
     */
    public static <T> byte[] serialize(T obj) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        }
    }

    /**
     * Deserializes an object from a byte array.
     *
     * @param data the byte array representing the serialized object
     * @return the deserialized object
     * @throws IOException            if an I/O error occurs during deserialization
     * @throws ClassNotFoundException if the class of the deserialized object cannot be found
     */
    public static <T> T deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (T) ois.readObject();
        }
    }

}
