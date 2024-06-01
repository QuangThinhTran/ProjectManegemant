package com.vn.projectmanagement.entity.response;

import org.springframework.data.domain.Pageable;

public record ResponsePageable(Object data, Pageable pageable) {}
