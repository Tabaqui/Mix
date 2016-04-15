package ru.motleycrew.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.motleycrew.utils.RestException;

/**
 * Created by vas on 03.04.16.
 */
@Controller
public class ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerController.class.getName());

    @ExceptionHandler(RestException.class)
    @ResponseBody
    public String handleException(RestException e) {
        LOG.error("Ошибка: " + e.getMessage(), e);
        return  "Ошибка: " + e.getMessage();
    }
}
