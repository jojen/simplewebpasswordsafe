package com.kaba.academy.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * A DTO object which is used as a form object
 * in create password and edit password forms.
 *
 * @author Petri Kainulainen
 */
public class PasswordDTO {

    private Long id;

    @NotEmpty
    private String title;

    private String username;

    private String description;

    private String passwordValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordValue() {
        return passwordValue;
    }

    public void setPasswordValue(String passwordValue) {
        this.passwordValue = passwordValue;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
