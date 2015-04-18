package com.kaba.academy.model;

import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Petri Kainulainen
 */
public class PasswordTest {

    private static final String TITLE = "Foo";
    private static final String TITLE_UPDATED = "Foo1";
    private static final String DESCRIPTION = "Bar";
    private static final String DESCRIPTION_UPDATED = "Bar1";
    private static final String USERNAME = "Sven";
    private static final String USERNAME_UPDATED = "Sören";

    @Test
    public void build() {
        Password built = Password.getBuilder(TITLE, DESCRIPTION, USERNAME,null).build();

        assertEquals(TITLE, built.getTitle());
        assertEquals(DESCRIPTION, built.getDescription());
        assertEquals(USERNAME, built.getUsername());
        assertEquals(0, built.getVersion());

        assertNull(built.getCreationTime());
        assertNull(built.getModificationTime());
        assertNull(built.getId());
    }


    @Test
    public void prePersist() {
        Password built = Password.getBuilder(TITLE, DESCRIPTION, USERNAME,null).build();
        built.prePersist();

        Date creationTime = built.getCreationTime();
        Date modificationTime = built.getModificationTime();

        assertNotNull(creationTime);
        assertNotNull(modificationTime);
        assertEquals(creationTime, modificationTime);
    }

    @Test
    public void preUpdate() {
        Password built = Password.getBuilder(TITLE, DESCRIPTION, USERNAME,null).build();
        built.prePersist();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //Back to work
        }

        built.preUpdate();

        Date creationTime = built.getCreationTime();
        Date modificationTime = built.getModificationTime();

        assertNotNull(creationTime);
        assertNotNull(modificationTime);
        assertTrue(modificationTime.after(creationTime));
    }

    @Test
    public void update() {
        Password built = Password.getBuilder(TITLE, DESCRIPTION, USERNAME,null).build();
        built.update(TITLE_UPDATED, DESCRIPTION_UPDATED, USERNAME_UPDATED);

        assertEquals(TITLE_UPDATED, built.getTitle());
        assertEquals(DESCRIPTION_UPDATED, built.getDescription());
    }
}
