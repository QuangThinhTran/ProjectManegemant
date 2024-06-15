package com.vn.projectmanagement.entity.dto;

import lombok.Data;

@Data
public class PageMetadataDTO {
    private int size;
    private long totalElements;
    private int totalPages;
    private int number;
}
