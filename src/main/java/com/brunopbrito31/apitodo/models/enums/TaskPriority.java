package com.brunopbrito31.apitodo.models.enums;

public enum TaskPriority {
    HIGHEST(0),
    MEDIUM(1),
    LOWEST(2);

    private int selectedPriority;

    private TaskPriority(int priority) {
        this.selectedPriority = priority;
    }
}
