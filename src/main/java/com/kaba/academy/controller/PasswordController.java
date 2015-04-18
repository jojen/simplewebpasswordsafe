package com.kaba.academy.controller;

import com.kaba.academy.dto.PasswordDTO;
import com.kaba.academy.model.Password;
import com.kaba.academy.service.EncryptionService;
import com.kaba.academy.service.PasswordNotFoundException;
import com.kaba.academy.service.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Petri Kainulainen
 */
@Controller
@SessionAttributes("password")
public class PasswordController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordController.class);

    protected static final String ERROR_MESSAGE_KEY_DELETED_PASSWORD_WAS_NOT_FOUND = "error.message.deleted.not.found";
    protected static final String ERROR_MESSAGE_KEY_EDITED_PASSWORD_WAS_NOT_FOUND = "error.message.edited.not.found";

    protected static final String FEEDBACK_MESSAGE_KEY_PASSWORD_CREATED = "feedback.message.password.created";
    protected static final String FEEDBACK_MESSAGE_KEY_PASSWORD_DELETED = "feedback.message.password.deleted";
    protected static final String FEEDBACK_MESSAGE_KEY_PASSWORD_EDITED = "feedback.message.password.edited";

    protected static final String MODEL_ATTIRUTE_PASSWORD = "password";
    protected static final String MODEL_ATTRIBUTE_PASSWORDS = "passwords";

    protected static final String PASSWORD_ADD_FORM_VIEW = "password/create";
    protected static final String PASSWORD_EDIT_FORM_VIEW = "password/edit";
    protected static final String STRING_VIEW = "json/string";
    protected static final String PASSWORD_LIST_VIEW = "password/list";

    protected static final String REQUEST_MAPPING_LIST = "/";

    @Resource
    private PasswordService passwordService;

    @Resource
    private EncryptionService encryptionService;


    /**
     * Processes delete password requests.
     *
     * @param id         The id of the deleted password.
     * @param attributes
     * @return
     */
    @RequestMapping(value = "/password/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id, RedirectAttributes attributes) {
        LOGGER.debug("Deleting password with id: " + id);

        try {
            Password deleted = passwordService.delete(id);
            addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_PASSWORD_DELETED, deleted.getName());
        } catch (PasswordNotFoundException e) {
            LOGGER.debug("No password found with id: " + id);
            addErrorMessage(attributes, ERROR_MESSAGE_KEY_DELETED_PASSWORD_WAS_NOT_FOUND);
        }

        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }

    /**
     * Set password value.
     *
     * @param id The id of the deleted password.
     * @return
     */
    @RequestMapping(value = "/password/update/{id}/value", method = RequestMethod.POST)
    public String updatePasswordValue(@PathVariable("id") Long id,
                                      @RequestParam("pw") String password,
                                      Model model) {
        LOGGER.debug("update password value with id: " + id);
        Password p = passwordService.findById(id);
        if (p != null) {
            PasswordDTO passwordDTO = constructFormObject(p);
            passwordDTO.setPasswordValue(encryptionService.encrypt("TODO", password));
            passwordService.updatePasswordValue(passwordDTO);
            model.addAttribute("self", true);
        }

        return STRING_VIEW;
    }

    /**
     * Updazte password value.
     *
     * @param id The id of the deleted password.
     * @return
     */
    @RequestMapping(value = "/password/get/{id}/value", method = RequestMethod.GET)
    public String getPasswordValue(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("Deleting password with id: " + id);
        Password p = passwordService.findById(id);
        if (p != null) {
            model.addAttribute("self", encryptionService.decrypt("TODO", p.getPasswordValue()));
        }
        return STRING_VIEW;
    }

    /**
     * Processes create password requests.
     *
     * @param model
     * @return The name of the create password form view.
     */
    @RequestMapping(value = "/password/create", method = RequestMethod.GET)
    public String showCreatePasswordForm(Model model) {
        LOGGER.debug("Rendering create password form");

        model.addAttribute(MODEL_ATTIRUTE_PASSWORD, new PasswordDTO());

        return PASSWORD_ADD_FORM_VIEW;
    }

    /**
     * Processes the submissions of create passwords form.
     *
     * @param created       The information of the created passwords.
     * @param bindingResult
     * @param attributes
     * @return
     */
    @RequestMapping(value = "/password/create", method = RequestMethod.POST)
    public String submitCreatePasswordForm(@Valid @ModelAttribute(MODEL_ATTIRUTE_PASSWORD) PasswordDTO created, BindingResult bindingResult, RedirectAttributes attributes) {
        LOGGER.debug("Create password form was submitted with information: " + created);

        if (bindingResult.hasErrors()) {
            return PASSWORD_ADD_FORM_VIEW;
        }

        Password password = passwordService.create(created);

        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_PASSWORD_CREATED, password.getName());

        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }

    /**
     * Processes edit password requests.
     *
     * @param id         The id of the edited password.
     * @param model
     * @param attributes
     * @return The name of the edit password form view.
     */
    @RequestMapping(value = "/password/edit/{id}", method = RequestMethod.GET)
    public String showEditPasswordForm(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
        LOGGER.debug("Rendering edit password form for password with id: " + id);

        Password password = passwordService.findById(id);
        if (password == null) {
            LOGGER.debug("No password found with id: " + id);
            addErrorMessage(attributes, ERROR_MESSAGE_KEY_EDITED_PASSWORD_WAS_NOT_FOUND);
            return createRedirectViewPath(REQUEST_MAPPING_LIST);
        }

        model.addAttribute(MODEL_ATTIRUTE_PASSWORD, constructFormObject(password));

        return PASSWORD_EDIT_FORM_VIEW;
    }

    /**
     * Processes the submissions of edit password form.
     *
     * @param updated       The information of the edited password.
     * @param bindingResult
     * @param attributes
     * @return
     */
    @RequestMapping(value = "/password/edit", method = RequestMethod.POST)
    public String submitEditPasswordForm(@Valid @ModelAttribute(MODEL_ATTIRUTE_PASSWORD) PasswordDTO updated, BindingResult bindingResult, RedirectAttributes attributes) {
        LOGGER.debug("Edit password form was submitted with information: " + updated);

        if (bindingResult.hasErrors()) {
            LOGGER.debug("Edit password form contains validation errors. Rendering form view.");
            return PASSWORD_EDIT_FORM_VIEW;
        }

        try {
            Password password = passwordService.update(updated);
            addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_PASSWORD_EDITED, password.getName());
        } catch (PasswordNotFoundException e) {
            LOGGER.debug("No password was found with id: " + updated.getId());
            addErrorMessage(attributes, ERROR_MESSAGE_KEY_EDITED_PASSWORD_WAS_NOT_FOUND);
        }

        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }

    private PasswordDTO constructFormObject(Password password) {
        PasswordDTO formObject = new PasswordDTO();

        formObject.setId(password.getId());
        formObject.setTitle(password.getTitle());
        formObject.setDescription(password.getDescription());
        formObject.setUsername(password.getUsername());

        return formObject;
    }

    /**
     * Processes requests to home page which lists all available passwords.
     *
     * @param model
     * @return The name of the password list view.
     */
    @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
    public String showList(Model model) {
        LOGGER.debug("Rendering password list page");

        List<Password> passwords = passwordService.findAll();
        model.addAttribute(MODEL_ATTRIBUTE_PASSWORDS, passwords);

        return PASSWORD_LIST_VIEW;
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param passwordService
     */
    protected void setPasswordService(PasswordService passwordService) {
        this.passwordService = passwordService;
    }
}
