package com.vn.projectmanagement.entity.response;

import com.fasterxml.jackson.annotation.JsonView;
import com.vn.projectmanagement.entity.view.View;
import org.springframework.data.domain.Page;

import java.util.List;

public class ResponsePageable<T> {

    @JsonView(View.ProjectView.class)
    private List<T> data;

    @JsonView(View.ProjectView.class)
    private int currentPage;

    @JsonView(View.ProjectView.class)
    private int totalPages;

    @JsonView(View.ProjectView.class)
    private long totalItems;

    @JsonView(View.ProjectView.class)
    private int pageSize;

    public ResponsePageable(List<T> data, Page<T> page) {
        this.data = data;
        this.currentPage = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.pageSize = page.getSize();
    }
}

