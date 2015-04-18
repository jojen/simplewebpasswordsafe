package com.kaba.academy.model;

import com.kaba.academy.dto.PasswordDTO;

/**
 * An utility class which contains useful methods for unit testing password related
 * functions.
 *
 * @author Petri Kainulainen
 */
public class PasswordTestUtil {


    public static PasswordDTO createDTO(Long id, String title, String description, String username) {
        PasswordDTO dto = new PasswordDTO();
        dto.setId(id);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setUsername(username);

        return dto;
    }

    public static Password createModelObject(Long id, String title, String description, String username) {
        Password model = Password.getBuilder(title, description, username, null).build();

        model.setId(id);

        return model;
    }
}
