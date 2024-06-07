package com.vn.projectmanagement.common.utils;

import org.springframework.stereotype.Component;

@Component
public class Helper {

    /**
     * Set slug for string
     *
     * @param string - String to set slug
     * @return - Slug string
     */
    public static String setSlug(String string) {
        return string.replaceAll("[^a-zA-Z0-9.-]", "-").toLowerCase();
    }
}
