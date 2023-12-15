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

import java.io.File;

/**
 * data-storage; com.izanagicraft.storage.repository:CachedFileDataRepository
 * <p>
 * A data repository that combines the functionality of {@link FileDataRepository} and {@link InMemoryDataRepository}
 * to provide a caching mechanism. It delegates calls based on caching logic, attempting to retrieve data from an
 * in-memory cache first and falling back to a file repository when necessary.
 *
 * @param <T> the type of data to be stored in the repository
 * @author <a href="https://github.com/sanguine6660">@sanguine6660</a>
 * @since 13.12.2023
 */
public class CachedFileDataRepository<T> implements DataRepository<T> {

    private final FileDataRepository<T> fileDataRepository;
    private final InMemoryDataRepository<T> inMemoryDataRepository;

    /**
     * Constructs a {@code CachedDataRepository} with the specified base path for file storage.
     *
     * @param baseDirectoryPath the base path where data files will be stored
     */
    public CachedFileDataRepository(String baseDirectoryPath) {
        this.fileDataRepository = new FileDataRepository<>(baseDirectoryPath);
        this.inMemoryDataRepository = new InMemoryDataRepository<>();
    }

    /**
     * Constructs a {@code CachedDataRepository} with the specified base directory for file storage.
     *
     * @param baseDirectory the base directory where data files will be stored
     */
    public CachedFileDataRepository(File baseDirectory) {
        this.fileDataRepository = new FileDataRepository<>(baseDirectory);
        this.inMemoryDataRepository = new InMemoryDataRepository<>();
    }

    @Override
    public T getData(String key) {
        // Try to get data from in-memory cache
        T data = inMemoryDataRepository.getData(key);

        // If not found in memory, try to get from file repository
        if (data == null) {
            data = fileDataRepository.getData(key);

            // If found in file repository, store in-memory cache
            if (data != null) {
                inMemoryDataRepository.storeData(key, data);
            }
        }

        return data;
    }

    @Override
    public T storeData(String key, T value) {
        // Store in both in-memory cache and file repository
        inMemoryDataRepository.storeData(key, value);
        fileDataRepository.storeData(key, value);
        return value;
    }

    @Override
    public boolean isStored(String key) {
        // Check if the data is in either in-memory cache or file repository
        return inMemoryDataRepository.isStored(key) || fileDataRepository.isStored(key);
    }

    @Override
    public void clearStorage() {
        // Clear both in-memory cache and file repository
        inMemoryDataRepository.clearStorage();
        fileDataRepository.clearStorage();
    }

}
