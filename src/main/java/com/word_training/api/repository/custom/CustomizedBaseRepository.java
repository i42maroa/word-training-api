package com.word_training.api.repository.custom;

import org.springframework.data.mongodb.core.query.Criteria;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public interface CustomizedBaseRepository {
    default <T> Criteria getAllCriteria(T filters, Function<T, List<Criteria>> listCriteria){
        return Optional.ofNullable(filters)
                .map(listCriteria)
                .filter(Predicate.not(List::isEmpty))
                .map(criteria -> new Criteria().andOperator(criteria))
                .orElseGet(Criteria::new);
    }

    default <T> Stream<Criteria> equalsCriteria(String field, T value){
        return Stream.ofNullable(value)
                .map(filter -> new Criteria(field).is(filter));
    }

    default Stream<Criteria> regexCriteria(String field, String regex){
        return Stream.ofNullable(regex)
                .map(filter -> new Criteria(field).regex( Pattern.compile(Pattern.quote(filter), Pattern.CASE_INSENSITIVE)));
    }

    default Stream<Criteria> elemMatch(String field, Stream<Criteria> criteria){
        return criteria
                .map(filter -> new Criteria(field).elemMatch(filter));
    }

    default <T> Stream<Criteria> inCriteria(String field, List<T> value){
        return Stream.ofNullable(value)
                .filter(Predicate.not(Collection::isEmpty))
                .map(filter -> new Criteria(field).in(filter));
    }

    default Stream<Criteria> inCriteriaWithRegex(String field, List<String> value){
        var regexIn = Stream.ofNullable(value)
                .flatMap(Collection::stream)
                .map(filter -> new Criteria(field).regex(filter))
                .toList();

        return Stream.of(regexIn)
                .filter(Predicate.not(List::isEmpty))
                .map(orCriteria -> new Criteria().orOperator(orCriteria));
    }

    default Stream<Criteria> emptyArray(String field, Boolean isEmpty){
        return Stream.ofNullable(field)
                .map(show -> Optional.of(isEmpty)
                                .filter(v -> v)
                                .map(yes -> new Criteria(field).size(0))
                                .orElse(new Criteria(field).not().size(0))
                        );
    }
    default <T> Stream<Criteria> rangeCriteria(String field, T gte, T lte){
        return Optional.of(field)
                .filter(f -> Objects.nonNull(gte) && Objects.nonNull(lte))
                .map(range -> new Criteria().andOperator(new Criteria().andOperator(new Criteria(field).gte(gte), new Criteria(field).lte(lte))))
                .or(() -> Optional.ofNullable(gte).map(value -> new Criteria(field).gte(value)))
                .or(()-> Optional.ofNullable(lte).map(value -> new Criteria(field).lte(value)))
                .stream();
    }

    default Stream<Criteria> orOperator(Stream<Criteria> criteria){
        return Stream.of(new Criteria().orOperator(criteria.toList()));
    }
}
