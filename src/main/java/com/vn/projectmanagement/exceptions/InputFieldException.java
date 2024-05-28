package com.vn.projectmanagement.exceptions;

import java.util.Map;

public record InputFieldException(Map<String, String> messages, int status) {
}
