package com.codegym.kfcbackend.dto.response;

import com.codegym.kfcbackend.entity.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageCacheResponse<T> {
    private List<T> content;
    private long totalElements;
    private long totalPages;
}
