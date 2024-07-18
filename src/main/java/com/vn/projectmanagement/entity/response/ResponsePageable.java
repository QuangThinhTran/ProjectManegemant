package com.vn.projectmanagement.entity.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class ResponsePageable<T> {

    private List<T> data;

    private int currentPage;

    private int totalPages;

    private long totalItems;

    private int pageSize;

    public ResponsePageable(List<T> data, Page<T> page) {
        this.data = data;
        this.currentPage = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.pageSize = page.getSize();
    }
}

