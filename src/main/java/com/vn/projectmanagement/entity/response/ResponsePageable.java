package com.vn.projectmanagement.entity.response;

import org.springframework.data.domain.Page;

public record ResponsePageable<T>(Object data, Page<T> pageable) {
}
