package com.team32.epicwebapp.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A cache registry for storing {@link JsonUserData} instances
 * in memory, providing read and write methods. Not assumed thread-safe.
 *
 * @author Jay Carr
 * @version 1.0
 */
@Component
public class JsonStorage {

    /**
     * The global GSON instance for all instances.
     */
    private static final Gson GSON = new GsonBuilder()
            .setDateFormat(DateFormat.FULL)
            .setPrettyPrinting()
            .create();

    /**
     * The cache mapping of user IDs to their associated JSON data.
     */
    private final Map<Long, JsonUserData> userCache = new ConcurrentHashMap<>();

    public JsonStorage() {
        // Add a shutdown hook to prevent data loss if the main thread crashes
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (long id : this.userCache.keySet()) {
                this.writeUserData(id);
            }
        }));
    }

    public JsonUserData getUserData(long userId) {
        return this.userCache.compute(userId, (id, data) -> {
            // If data already exists and is loaded into the cache
            if (data != null) {
                return data;
            }

            // Fetch path and create file if it doesn't exist
            Path userPath = JsonUserData.getUserPath(userId);
            if (!Files.exists(userPath)) {
                return new JsonUserData();
            }

            // File does exist, so read all data from it
            try (Reader reader = Files.newBufferedReader(userPath)) {
                // If GSON was able to create an object from data, return it, or return new data object
                JsonUserData userJsonData = GSON.fromJson(reader, JsonUserData.class);
                return userJsonData != null ? userJsonData : new JsonUserData();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public void writeUserData(long userId) {
        JsonUserData data = this.userCache.get(userId);

        // If data is not cached and available to be written
        if (data == null) {
            return;
        }

        // Fetch path and create file ready for writing if it doesn't exist
        Path userPath = JsonUserData.getUserPath(userId);
        if (!Files.exists(userPath)) {
            try {
                Files.createFile(userPath);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // Use GSON to convert cached object to JSON and write it
        try (Writer writer = Files.newBufferedWriter(userPath)) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
