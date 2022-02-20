package com.brunopbrito31.apitodo.models.enums;

public enum TaskStatus {
    OPEN(0),
    INPROGRESS(1),
    FINISH(2),
    CANCELED(3);

    private int selectedStatus;

    private TaskStatus(int status) {
        this.selectedStatus = status;
    }
}
