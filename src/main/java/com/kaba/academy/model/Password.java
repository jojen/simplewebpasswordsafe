package com.kaba.academy.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Date;

/**
 * An entity class which contains the information of a single password.
 *
 * @author Petri Kainulainen
 */
@Entity
@Table(name = "passwords")
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    private Date creationTime;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "username", nullable = true)
    private String username;

    @Column(name = "passwordValue", nullable = true)
    private String passwordValue;

    @Column(name = "modification_time", nullable = false)
    private Date modificationTime;

    @Version
    private long version = 0;


    public Long getId() {
        return id;
    }

    /**
     * Gets a builder which is used to create Password objects.
     *
     * @return A new Builder instance.
     */
    public static Builder getBuilder(String title, String description, String username, String passwordValue) {
        return new Builder(title, description, username, passwordValue);
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordValue() {
        return passwordValue;
    }

    /**
     * Gets the full name of the password.
     *
     * @return The full name of the password.
     */
    @Transient
    public String getName() {
        StringBuilder name = new StringBuilder();

        name.append(title);
        name.append(" ");
        name.append(description);

        return name.toString();
    }

    public Date getModificationTime() {
        return modificationTime;
    }

    public long getVersion() {
        return version;
    }

    public void update(String title, String description, String username) {
        this.title = title;
        this.description = description;
        this.username = username;
    }

    public void updatePassword(String passwordValue) {
        this.passwordValue = passwordValue;
    }

    @PreUpdate
    public void preUpdate() {
        modificationTime = new Date();
    }

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        creationTime = now;
        modificationTime = now;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * A Builder class used to create new Password objects.
     */
    public static class Builder {
        Password built;

        /**
         * Creates a new Builder instance.
         *
         * @param title       The first name of the created Password object.
         * @param description The last name of the created Password object.
         */
        Builder(String title, String description, String username, String passwordValue) {
            built = new Password();
            built.title = title;
            built.description = description;
            built.username = username;
            built.passwordValue = passwordValue;
        }

        /**
         * Builds the new Password object.
         *
         * @return The created Password object.
         */
        public Password build() {
            return built;
        }
    }

    /**
     * This setter method should only be used by unit tests.
     *
     * @param id
     */
    protected void setId(Long id) {
        this.id = id;
    }
}
