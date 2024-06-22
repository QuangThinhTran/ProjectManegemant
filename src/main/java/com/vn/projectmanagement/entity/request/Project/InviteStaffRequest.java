package com.vn.projectmanagement.entity.request.Project;

import lombok.Data;

import java.util.UUID;

@Data
public class InviteStaffRequest {
    private UUID projectID;
}
