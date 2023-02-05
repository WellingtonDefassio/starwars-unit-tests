package io.wdefassio.starwars.application.service.util;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class QueryBuilder {
    public static <T> Example<T> makeQuery(T object) {
        return Example.of(object,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.EXACT));

    }

}
