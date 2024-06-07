package com.vn.projectmanagement.entity.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record ResponsePageable<T>(List<T> data, Page<T> pageable) {
}
