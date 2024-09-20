package com.example.napolinights.model;

public class UserRole {
    private int roleId;
    private String roleName;
    private String description;
    private String level;
    private boolean canCreateOrders;

    public UserRole(int roleId, String roleName, String description, String level) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
        this.level = level;
    }

}
