package com.kaba.academy.controller;

import com.kaba.academy.dto.PasswordDTO;
import com.kaba.academy.model.Password;
import com.kaba.academy.model.PasswordTestUtil;
import com.kaba.academy.service.EncryptionServiceTest;
import com.kaba.academy.service.PasswordNotFoundException;
import com.kaba.academy.service.PasswordService;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Petri Kainulainen
 */
public class PasswordControllerTest extends AbstractTestController {

    private static final String FIELD_NAME_TITLE = "title";
    private static final String FIELD_NAME_DESCRIPTION = "description";
    private static final String FIELD_NAME_USER_NAME = "username";


    private static final Long PASSWORD_ID = Long.valueOf(5);
    private static final String TITLE = "Foo";
    private static final String TITLE_UPDATED = "FooUpdated";
    private static final String DESCRIPTION = "Bar";
    private static final String DESCRIPTION_UPDATED = "BarUpdated";
    private static final String USERNAME = "Sven";
    private static final String USERNAME_UPDATED = "Sören";


    private PasswordController controller;

    private PasswordService passwordServiceMock;

    @Override
    public void setUpTest() {
        controller = new PasswordController();

        controller.setMessageSource(getMessageSourceMock());

        passwordServiceMock = mock(PasswordService.class);
        controller.setPasswordService(passwordServiceMock);
    }

    @Test
    public void delete() throws PasswordNotFoundException {
        Password deleted = PasswordTestUtil.createModelObject(PASSWORD_ID, TITLE, DESCRIPTION, USERNAME);
        when(passwordServiceMock.delete(PASSWORD_ID)).thenReturn(deleted);

        initMessageSourceForFeedbackMessage(PasswordController.FEEDBACK_MESSAGE_KEY_PASSWORD_DELETED);

        RedirectAttributes attributes = new RedirectAttributesModelMap();
        String view = controller.delete(PASSWORD_ID, attributes);

        verify(passwordServiceMock, times(1)).delete(PASSWORD_ID);
        verifyNoMoreInteractions(passwordServiceMock);
        assertFeedbackMessage(attributes, PasswordController.FEEDBACK_MESSAGE_KEY_PASSWORD_DELETED);

        String expectedView = createExpectedRedirectViewPath(PasswordController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }

    @Test
    public void deleteWhenPasswordIsNotFound() throws PasswordNotFoundException {
        when(passwordServiceMock.delete(PASSWORD_ID)).thenThrow(new PasswordNotFoundException());

        initMessageSourceForErrorMessage(PasswordController.ERROR_MESSAGE_KEY_DELETED_PASSWORD_WAS_NOT_FOUND);

        RedirectAttributes attributes = new RedirectAttributesModelMap();
        String view = controller.delete(PASSWORD_ID, attributes);

        verify(passwordServiceMock, times(1)).delete(PASSWORD_ID);
        verifyNoMoreInteractions(passwordServiceMock);
        assertErrorMessage(attributes, PasswordController.ERROR_MESSAGE_KEY_DELETED_PASSWORD_WAS_NOT_FOUND);

        String expectedView = createExpectedRedirectViewPath(PasswordController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }

    @Test
    public void showCreatePasswordForm() {
        Model model = new BindingAwareModelMap();

        String view = controller.showCreatePasswordForm(model);

        verifyZeroInteractions(passwordServiceMock);

        assertEquals(PasswordController.PASSWORD_ADD_FORM_VIEW, view);

        PasswordDTO added = (PasswordDTO) model.asMap().get(PasswordController.MODEL_ATTIRUTE_PASSWORD);
        assertNotNull(added);

        assertNull(added.getId());
        assertNull(added.getTitle());
        assertNull(added.getDescription());
    }

    @Test
    public void submitCreatePasswordForm() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("/password/create", "POST");

        PasswordDTO created = PasswordTestUtil.createDTO(PASSWORD_ID, TITLE, DESCRIPTION, USERNAME);
        Password model = PasswordTestUtil.createModelObject(PASSWORD_ID, TITLE, DESCRIPTION, USERNAME);
        when(passwordServiceMock.create(created)).thenReturn(model);

        initMessageSourceForFeedbackMessage(PasswordController.FEEDBACK_MESSAGE_KEY_PASSWORD_CREATED);

        RedirectAttributes attributes = new RedirectAttributesModelMap();
        BindingResult result = bindAndValidate(mockRequest, created);

        String view = controller.submitCreatePasswordForm(created, result, attributes);

        verify(passwordServiceMock, times(1)).create(created);
        verifyNoMoreInteractions(passwordServiceMock);

        String expectedViewPath = createExpectedRedirectViewPath(PasswordController.REQUEST_MAPPING_LIST);
        assertEquals(expectedViewPath, view);

        assertFeedbackMessage(attributes, PasswordController.FEEDBACK_MESSAGE_KEY_PASSWORD_CREATED);

        verify(passwordServiceMock, times(1)).create(created);
        verifyNoMoreInteractions(passwordServiceMock);
    }

    @Test
    public void submitEmptyCreatePasswordForm() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("/password/create", "POST");

        PasswordDTO created = new PasswordDTO();

        RedirectAttributes attributes = new RedirectAttributesModelMap();
        BindingResult result = bindAndValidate(mockRequest, created);

        String view = controller.submitCreatePasswordForm(created, result, attributes);

        verifyZeroInteractions(passwordServiceMock);

        assertEquals(PasswordController.PASSWORD_ADD_FORM_VIEW, view);
        assertFieldErrors(result, FIELD_NAME_TITLE);
    }

    @Test
    public void submitCreatePasswordFormWithEmptyTitle() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("/password/create", "POST");

        PasswordDTO created = PasswordTestUtil.createDTO(null, null, DESCRIPTION, USERNAME);

        RedirectAttributes attributes = new RedirectAttributesModelMap();
        BindingResult result = bindAndValidate(mockRequest, created);

        String view = controller.submitCreatePasswordForm(created, result, attributes);

        verifyZeroInteractions(passwordServiceMock);

        assertEquals(PasswordController.PASSWORD_ADD_FORM_VIEW, view);
        assertFieldErrors(result, FIELD_NAME_TITLE);
    }


    @Test
    public void showEditPasswordForm() {
        Password password = PasswordTestUtil.createModelObject(PASSWORD_ID, TITLE, DESCRIPTION, USERNAME);
        when(passwordServiceMock.findById(PASSWORD_ID)).thenReturn(password);

        Model model = new BindingAwareModelMap();
        RedirectAttributes attributes = new RedirectAttributesModelMap();

        String view = controller.showEditPasswordForm(PASSWORD_ID, model, attributes);

        verify(passwordServiceMock, times(1)).findById(PASSWORD_ID);
        verifyNoMoreInteractions(passwordServiceMock);

        assertEquals(PasswordController.PASSWORD_EDIT_FORM_VIEW, view);

        PasswordDTO formObject = (PasswordDTO) model.asMap().get(PasswordController.MODEL_ATTIRUTE_PASSWORD);

        assertNotNull(formObject);
        assertEquals(password.getId(), formObject.getId());
        assertEquals(password.getTitle(), formObject.getTitle());
        assertEquals(password.getDescription(), formObject.getDescription());
    }

    @Test
    public void showEditPasswordFormWhenPasswordIsNotFound() {
        when(passwordServiceMock.findById(PASSWORD_ID)).thenReturn(null);

        initMessageSourceForErrorMessage(PasswordController.ERROR_MESSAGE_KEY_EDITED_PASSWORD_WAS_NOT_FOUND);

        Model model = new BindingAwareModelMap();
        RedirectAttributes attributes = new RedirectAttributesModelMap();

        String view = controller.showEditPasswordForm(PASSWORD_ID, model, attributes);

        verify(passwordServiceMock, times(1)).findById(PASSWORD_ID);
        verifyNoMoreInteractions(passwordServiceMock);

        String expectedView = createExpectedRedirectViewPath(PasswordController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);

        assertErrorMessage(attributes, PasswordController.ERROR_MESSAGE_KEY_EDITED_PASSWORD_WAS_NOT_FOUND);
    }

    @Test
    public void submitEditPasswordForm() throws PasswordNotFoundException {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("/password/edit", "POST");
        PasswordDTO updated = PasswordTestUtil.createDTO(PASSWORD_ID, TITLE_UPDATED, DESCRIPTION_UPDATED, USERNAME_UPDATED);
        Password password = PasswordTestUtil.createModelObject(PASSWORD_ID, TITLE_UPDATED, DESCRIPTION_UPDATED, USERNAME_UPDATED);

        when(passwordServiceMock.update(updated)).thenReturn(password);

        initMessageSourceForFeedbackMessage(PasswordController.FEEDBACK_MESSAGE_KEY_PASSWORD_EDITED);

        BindingResult bindingResult = bindAndValidate(mockRequest, updated);
        RedirectAttributes attributes = new RedirectAttributesModelMap();

        String view = controller.submitEditPasswordForm(updated, bindingResult, attributes);

        verify(passwordServiceMock, times(1)).update(updated);
        verifyNoMoreInteractions(passwordServiceMock);

        String expectedView = createExpectedRedirectViewPath(PasswordController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);

        assertFeedbackMessage(attributes, PasswordController.FEEDBACK_MESSAGE_KEY_PASSWORD_EDITED);

        assertEquals(updated.getTitle(), password.getTitle());
        assertEquals(updated.getDescription(), password.getDescription());
    }

    @Test
    public void submitEditPasswordFormWhenPasswordIsNotFound() throws PasswordNotFoundException {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("/password/edit", "POST");
        PasswordDTO updated = PasswordTestUtil.createDTO(PASSWORD_ID, TITLE_UPDATED, DESCRIPTION_UPDATED, USERNAME_UPDATED);

        when(passwordServiceMock.update(updated)).thenThrow(new PasswordNotFoundException());
        initMessageSourceForErrorMessage(PasswordController.ERROR_MESSAGE_KEY_EDITED_PASSWORD_WAS_NOT_FOUND);

        BindingResult bindingResult = bindAndValidate(mockRequest, updated);
        RedirectAttributes attributes = new RedirectAttributesModelMap();

        String view = controller.submitEditPasswordForm(updated, bindingResult, attributes);

        verify(passwordServiceMock, times(1)).update(updated);
        verifyNoMoreInteractions(passwordServiceMock);

        String expectedView = createExpectedRedirectViewPath(PasswordController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);

        assertErrorMessage(attributes, PasswordController.ERROR_MESSAGE_KEY_EDITED_PASSWORD_WAS_NOT_FOUND);
    }

    @Test
    public void submitEmptyEditPasswordForm() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("/password/edit", "POST");
        PasswordDTO updated = PasswordTestUtil.createDTO(PASSWORD_ID, null, null, null);

        BindingResult bindingResult = bindAndValidate(mockRequest, updated);
        RedirectAttributes attributes = new RedirectAttributesModelMap();

        String view = controller.submitEditPasswordForm(updated, bindingResult, attributes);

        verifyZeroInteractions(passwordServiceMock);

        assertEquals(PasswordController.PASSWORD_EDIT_FORM_VIEW, view);
        assertFieldErrors(bindingResult, FIELD_NAME_TITLE);
    }

    @Test
    public void submitEditPasswordFormWhenTitleIsEmpty() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("/password/edit", "POST");
        PasswordDTO updated = PasswordTestUtil.createDTO(PASSWORD_ID, null, DESCRIPTION_UPDATED, USERNAME_UPDATED);

        BindingResult bindingResult = bindAndValidate(mockRequest, updated);
        RedirectAttributes attributes = new RedirectAttributesModelMap();

        String view = controller.submitEditPasswordForm(updated, bindingResult, attributes);

        verifyZeroInteractions(passwordServiceMock);

        assertEquals(PasswordController.PASSWORD_EDIT_FORM_VIEW, view);
        assertFieldErrors(bindingResult, FIELD_NAME_TITLE);
    }

    @Test
    public void showList() {
        List<Password> passwords = new ArrayList<Password>();
        when(passwordServiceMock.findAll()).thenReturn(passwords);

        Model model = new BindingAwareModelMap();
        String view = controller.showList(model);

        verify(passwordServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(passwordServiceMock);

        assertEquals(PasswordController.PASSWORD_LIST_VIEW, view);
        assertEquals(passwords, model.asMap().get(PasswordController.MODEL_ATTRIBUTE_PASSWORDS));
    }
}
