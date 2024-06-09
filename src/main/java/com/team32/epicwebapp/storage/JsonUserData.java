package com.team32.epicwebapp.storage;

import com.team32.epicwebapp.models.Result;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An object class for user data stored in JSON format
 * within the application.
 *
 * @author Jay Carr
 * @version 1.0
 */
@Component
public class JsonUserData {

    private static final Path DATA_DIR = Paths.get("data");

    static {
        if (!Files.exists(DATA_DIR)) {
            try {
                Files.createDirectories(DATA_DIR);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final Set<Result> results = new HashSet<>();

    public void insertResult(Result result) {
        this.results.add(result);
    }

    public void removeResult(Result result) {
        this.results.remove(result);
    }

    public Set<Result> getByModule(long moduleId) {
        return this.results.stream()
                .filter(result -> result.getModuleId() == moduleId)
                .collect(Collectors.toSet());
    }

    public Set<Result> getByStage(int stage){
        return this.results.stream().filter(result -> result.getStage() == stage).collect(Collectors.toSet());
    }

    public Set<Result> getResults() {
        return Collections.unmodifiableSet(this.results);
    }

    public static Path getUserPath(long userId) {
        return DATA_DIR.resolve(userId + ".json");
    }
}
