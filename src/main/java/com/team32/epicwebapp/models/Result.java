package com.team32.epicwebapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A data class for all results. Multiple results are
 * associated a student and the modules they study.
 *
 * @author Alonzo Fong
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public class Result {

    @NotNull
    private final long moduleId;

    @NotNull
    private final String quizName;

    @NotNull
    private final Date date;

    @NotNull
    private final int stage;

    @NotNull
    private final int result;

    public static class SortByDate implements Comparator<Result>{

        @Override
        public int compare(Result r1, Result r2) {
            return r1.getDate().compareTo(r2.getDate());
        }
    }

    public static class SortByModuleId implements Comparator<Result>{

        @Override
        public int compare(Result r1, Result r2) {
            return Long.compare(r1.getModuleId(), r2.getModuleId());
        }
    }

    public static List<Result> order(List<Result> resultsList) {
        Comparator<Result> compareModuleDate = Comparator.comparing(Result::getModuleId)
                                                        .thenComparing(Result::getDate);

        return resultsList.stream()
                .sorted(compareModuleDate)
                .collect(Collectors.toList());
    }
}
