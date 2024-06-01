package com.vn.projectmanagement.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PaginateService<T> {
    Page<T> paginate(List<T> list, Pageable pageable);
}
