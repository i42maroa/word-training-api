package com.word_training.api.model.output;

import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class PageInfo {
    private final Boolean hasNext;
    private final Boolean hasPrevious;
    private final Long totalCount;

    public PageInfo(Pageable pageable, Long count) {
        this.hasNext = count > pageable.getOffset() + pageable.getPageSize();
        this.hasPrevious = pageable.getPageNumber() > 0;
        this.totalCount = count;
    }
}
