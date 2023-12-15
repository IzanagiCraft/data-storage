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

package tests;

import com.izanagicraft.storage.repository.DataRepository;
import com.izanagicraft.storage.repository.InMemoryDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * data-storage; tests:InMemoryDataRepositoryTest
 *
 * @author <a href="https://github.com/sanguine6660">@sanguine6660</a>
 * @since 13.12.2023
 */
class InMemoryDataRepositoryTest {

    private DataRepository<String> stringRepository;
    private DataRepository<Integer> integerRepository;

    @BeforeEach
    void setUp() {
        // Initialize a new InMemoryDataRepository for String and Integer types before each test
        stringRepository = new InMemoryDataRepository<>();
        integerRepository = new InMemoryDataRepository<>();
    }

    @Test
    void getData_ShouldReturnStoredStringValue() {
        String key = "testKey";
        String value = "testValue";

        // Store a String value in the String repository
        stringRepository.storeData(key, value);

        // Retrieve the stored String value using the getData method
        assertEquals(value, stringRepository.getData(key), "getData should return the stored String value");
    }

    @Test
    void getData_ShouldReturnStoredIntegerValue() {
        String key = "testKey";
        Integer value = 42;

        // Store an Integer value in the Integer repository
        integerRepository.storeData(key, value);

        // Retrieve the stored Integer value using the getData method
        assertEquals(value, integerRepository.getData(key), "getData should return the stored Integer value");
    }

    @Test
    void storeData_ShouldStoreDataSuccessfullyForString() {
        String key = "testKey";
        String value = "testValue";

        // Store a String value in the String repository and check if it returns the correct value
        assertEquals(value, stringRepository.storeData(key, value), "storeData should return the stored String value");

        // Check if the String data is actually stored in the repository
        assertTrue(stringRepository.isStored(key), "isStored should return true for a stored String key");
    }

    @Test
    void storeData_ShouldStoreDataSuccessfullyForInteger() {
        String key = "testKey";
        Integer value = 42;

        // Store an Integer value in the Integer repository and check if it returns the correct value
        assertEquals(value, integerRepository.storeData(key, value), "storeData should return the stored Integer value");

        // Check if the Integer data is actually stored in the repository
        assertTrue(integerRepository.isStored(key), "isStored should return true for a stored Integer key");
    }

}
