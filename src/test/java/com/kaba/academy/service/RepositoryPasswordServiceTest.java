package com.kaba.academy.service;

import com.kaba.academy.dto.PasswordDTO;
import com.kaba.academy.model.Password;
import com.kaba.academy.model.PasswordTestUtil;
import com.kaba.academy.repository.PasswordRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Petri Kainulainen
 */
public class RepositoryPasswordServiceTest {

    private static final Long PASSWORD_ID = Long.valueOf(5);
    private static final String TITLE = "Foo";
    private static final String TITLE_UPDATED = "FooUpdated";
    private static final String DESCRIPTION = "Bar";
    private static final String DESCRIPTION_UPDATED = "BarUpdated";
    private static final String USERNAME = "Sven";
    private static final String USERNAME_UPDATED = "Sören";

    private RepositoryPasswordService passwordService;

    private PasswordRepository passwordRepositoryMock;


    @Before
    public void setUp() {
        passwordService = new RepositoryPasswordService();

        passwordRepositoryMock = mock(PasswordRepository.class);
        passwordService.setPasswordRepository(passwordRepositoryMock);

    }

    @Test
    public void create() {
        PasswordDTO created = PasswordTestUtil.createDTO(null, TITLE, DESCRIPTION, USERNAME);
        Password persisted = PasswordTestUtil.createModelObject(PASSWORD_ID, TITLE, DESCRIPTION, USERNAME);

        when(passwordRepositoryMock.save(any(Password.class))).thenReturn(persisted);

        Password returned = passwordService.create(created);

        ArgumentCaptor<Password> passwordArgument = ArgumentCaptor.forClass(Password.class);
        verify(passwordRepositoryMock, times(1)).save(passwordArgument.capture());
        verifyNoMoreInteractions(passwordRepositoryMock);

        assertPassword(created, passwordArgument.getValue());
        assertEquals(persisted, returned);
    }

    @Test
    public void delete() throws PasswordNotFoundException {
        Password deleted = PasswordTestUtil.createModelObject(PASSWORD_ID, TITLE, DESCRIPTION, USERNAME);
        when(passwordRepositoryMock.findOne(PASSWORD_ID)).thenReturn(deleted);

        Password returned = passwordService.delete(PASSWORD_ID);

        verify(passwordRepositoryMock, times(1)).findOne(PASSWORD_ID);
        verify(passwordRepositoryMock, times(1)).delete(deleted);
        verifyNoMoreInteractions(passwordRepositoryMock);

        assertEquals(deleted, returned);
    }

    @Test(expected = PasswordNotFoundException.class)
    public void deleteWhenPasswordIsNotFound() throws PasswordNotFoundException {
        when(passwordRepositoryMock.findOne(PASSWORD_ID)).thenReturn(null);

        passwordService.delete(PASSWORD_ID);

        verify(passwordRepositoryMock, times(1)).findOne(PASSWORD_ID);
        verifyNoMoreInteractions(passwordRepositoryMock);
    }

    @Test
    public void findAll() {
        List<Password> passwords = new ArrayList<Password>();
        when(passwordRepositoryMock.findAll()).thenReturn(passwords);

        List<Password> returned = passwordService.findAll();

        verify(passwordRepositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(passwordRepositoryMock);

        assertEquals(passwords, returned);
    }

    @Test
    public void findById() {
        Password password = PasswordTestUtil.createModelObject(PASSWORD_ID, TITLE, DESCRIPTION, USERNAME);
        when(passwordRepositoryMock.findOne(PASSWORD_ID)).thenReturn(password);

        Password returned = passwordService.findById(PASSWORD_ID);

        verify(passwordRepositoryMock, times(1)).findOne(PASSWORD_ID);
        verifyNoMoreInteractions(passwordRepositoryMock);

        assertEquals(password, returned);
    }

    @Test
    public void update() throws PasswordNotFoundException {
        PasswordDTO updated = PasswordTestUtil.createDTO(PASSWORD_ID, TITLE_UPDATED, DESCRIPTION_UPDATED, USERNAME);
        Password password = PasswordTestUtil.createModelObject(PASSWORD_ID, TITLE, DESCRIPTION, USERNAME);

        when(passwordRepositoryMock.findOne(updated.getId())).thenReturn(password);

        Password returned = passwordService.update(updated);

        verify(passwordRepositoryMock, times(1)).findOne(updated.getId());
        verifyNoMoreInteractions(passwordRepositoryMock);

        assertPassword(updated, returned);
    }

    @Test(expected = PasswordNotFoundException.class)
    public void updateWhenPasswordIsNotFound() throws PasswordNotFoundException {
        PasswordDTO updated = PasswordTestUtil.createDTO(PASSWORD_ID, TITLE_UPDATED, DESCRIPTION_UPDATED, USERNAME_UPDATED);

        when(passwordRepositoryMock.findOne(updated.getId())).thenReturn(null);

        passwordService.update(updated);

        verify(passwordRepositoryMock, times(1)).findOne(updated.getId());
        verifyNoMoreInteractions(passwordRepositoryMock);
    }

    private void assertPassword(PasswordDTO expected, Password actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), expected.getDescription());
    }

}
