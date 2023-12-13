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

import java.io.*;

/**
 * data-storage; com.izanagicraft.storage.repository:FileDataRepository
 *
 * @author <a href="https://github.com/sanguine6660">@sanguine6660</a>
 * @since 13.12.2023
 */

public class FileDataRepository<T> implements DataRepository<T> {

    private final File baseDirectory;

    /**
     * Constructs a {@code FileDataRepository} with the specified base path.
     * If the directory specified by the base path does not exist, it will be created.
     *
     * @param baseDirectoryPath the base path where data files will be stored
     */
    public FileDataRepository(String baseDirectoryPath) {
        this.baseDirectory = new File(baseDirectoryPath);
        if (!this.baseDirectory.exists()) {
            this.baseDirectory.mkdirs();
        }
    }

    /**
     * Constructs a {@code FileDataRepository} with the specified base directory.
     *
     * @param baseDirectory the base directory where data files will be stored
     */
    public FileDataRepository(File baseDirectory) {
        this.baseDirectory = baseDirectory;
        if (!baseDirectory.exists()) {
            baseDirectory.mkdirs();
        }
    }

    /**
     * Generates the file path for a given key based on the base directory.
     * The generated file path includes the base directory, and it uses the key to create a directory structure.
     * For example, if the key is "foo.bar", the file path will be "baseDirectory/foo/bar.dat".
     *
     * @param key the key for which the file path is generated
     * @return the file path associated with the specified key
     */
    public String getKeyFilePath(String key) {
        String sanitizedKey = key.replace('.', File.separatorChar);
        return baseDirectory.getPath() + File.separator + sanitizedKey + ".dat";
    }

    @Override
    public T getData(String key) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getKeyFilePath(key)))) {
            return SerializationUtils.deserialize(ois.readAllBytes());
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public T storeData(String key, T value) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getKeyFilePath(key)))) {
            oos.write(SerializationUtils.serialize(value));
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception based on your use case
        }
        return value;
    }

    @Override
    public boolean isStored(String key) {
        File file = new File(getKeyFilePath(key));
        return file.exists();
    }

    @Override
    public void clearStorage() {
        File[] files = baseDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

}
