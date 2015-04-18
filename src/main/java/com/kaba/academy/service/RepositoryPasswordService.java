package com.kaba.academy.service;

import com.kaba.academy.dto.PasswordDTO;
import com.kaba.academy.model.Password;
import com.kaba.academy.repository.PasswordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * This implementation of the PasswordService interface communicates with
 * the database by using a Spring Data JPA repository.
 *
 * @author Petri Kainulainen
 */
@Service
public class RepositoryPasswordService implements PasswordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryPasswordService.class);

    @Resource
    private PasswordRepository passwordRepository;

    @Transactional
    @Override
    public Password create(PasswordDTO created) {
        LOGGER.debug("Creating a new password with information: " + created);

        Password password = Password.getBuilder(created.getTitle(), created.getDescription(), created.getUsername(), created.getPasswordValue()).build();

        return passwordRepository.save(password);
    }

    @Transactional(rollbackFor = PasswordNotFoundException.class)
    @Override
    public Password delete(Long passwordId) throws PasswordNotFoundException {
        LOGGER.debug("Deleting password with id: " + passwordId);

        Password deleted = passwordRepository.findOne(passwordId);

        if (deleted == null) {
            LOGGER.debug("No password found with id: " + passwordId);
            throw new PasswordNotFoundException();
        }

        passwordRepository.delete(deleted);
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Password> findAll() {
        LOGGER.debug("Finding all password");
        return passwordRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Password findById(Long id) {
        LOGGER.debug("Finding password by id: " + id);
        return passwordRepository.findOne(id);
    }

    @Transactional(rollbackFor = PasswordNotFoundException.class)
    @Override
    public Password update(PasswordDTO updated) throws PasswordNotFoundException {
        LOGGER.debug("Updating password with information: " + updated);

        Password password = passwordRepository.findOne(updated.getId());

        if (password == null) {
            LOGGER.debug("No password found with id: " + updated.getId());
            throw new PasswordNotFoundException();
        }

        password.update(updated.getTitle(), updated.getDescription(), updated.getUsername());

        return password;
    }

    @Transactional(rollbackFor = PasswordNotFoundException.class)
    @Override
    public Password updatePasswordValue(PasswordDTO updated) throws PasswordNotFoundException {
        LOGGER.debug("Updating password with information: " + updated);

        Password password = passwordRepository.findOne(updated.getId());

        if (password == null) {
            LOGGER.debug("No password found with id: " + updated.getId());
            throw new PasswordNotFoundException();
        }

        password.updatePassword(updated.getPasswordValue());

        return password;
    }

    /**
     * This setter method should be used only by unit tests.
     *
     * @param passwordRepository
     */
    protected void setPasswordRepository(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }
}
