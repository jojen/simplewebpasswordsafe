package com.kaba.academy.service;

import com.kaba.academy.dto.PasswordDTO;
import com.kaba.academy.model.Password;

import java.util.List;

/**
 * Declares methods used to obtain and modify password information.
 * @author Petri Kainulainen
 */
public interface PasswordService {

    /**
     * Creates a new password.
     * @param created   The information of the created password.
     * @return  The created password.
     */
    public Password create(PasswordDTO created);

    /**
     * Deletes a password.
     * @param passwordId  The id of the deleted password.
     * @return  The deleted password.
     * @throws PasswordNotFoundException  if no password is found with the given id.
     */
    public Password delete(Long passwordId) throws PasswordNotFoundException;

    /**
     * Finds all password.
     * @return  A list of passwords.
     */
    public List<Password> findAll();

    /**
     * Finds password by id.
     * @param id    The id of the wanted password.
     * @return  The found password. If no pasword is found, this method returns null.
     */
    public Password findById(Long id);

    /**
     * Updates the information of a password.
     * @param updated   The information of the updated password.
     * @return  The updated password.
     * @throws PasswordNotFoundException  if no password is found with given id.
     */
    public Password update(PasswordDTO updated) throws PasswordNotFoundException;


    /**
     * Updates the information of a password.
     * @param updated   The information of the updated password.
     * @return  The updated password.
     * @throws PasswordNotFoundException  if no password is found with given id.
     */
    public Password updatePasswordValue(PasswordDTO updated) throws PasswordNotFoundException;
}
