package com.word_training.api.model.output;

import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
public class Pagination<T> {
    private final List<T> items;
    private final PageInfo pageInfo;

    public Pagination(List<T> items, Pageable pageable, Long count) {
        this.items = items;
        this.pageInfo = new PageInfo(pageable, count);
    }
}
