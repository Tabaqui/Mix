package ru.motleycrew.controller;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.motleycrew.config.Credentials;
import ru.motleycrew.entity.Data;
import ru.motleycrew.entity.User;
import ru.motleycrew.repository.MessageDao;
import ru.motleycrew.repository.UserDao;
import ru.motleycrew.service.MessageService;
import ru.motleycrew.service.SendService;
import ru.motleycrew.utils.RestException;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vas on 03.04.16.
 */
@Controller
public class DataController extends ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(DataController.class);

    @Autowired
    @Qualifier("messageDao")
    private MessageDao messageDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SendService sendService;

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public
    @ResponseBody
    IncomingMessage getLastMessage(@RequestParam(value = "id") String id) throws RestException {
        try {
            LOG.debug("Get message by id " + id);
            Data data = messageDao.find(id);
            IncomingMessage message = messageService.getMessage(data);
            return message;
        } catch (Exception e) {
            throw new RestException(e);
        }
    }

    @RequestMapping(value = "/messages/add", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> createMessage(@RequestBody IncomingMessage message) throws RestException {
        try {
            LOG.debug("Post message" + message.getText());
            Data data = messageService.getData(message);
            messageDao.create(data);
            sendService.send(data);
            Map<String, Object> b = new HashMap<>();
            return b;
        } catch (Exception e) {
            throw new RestException(e);
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> token(@RequestBody TokenSwap token) throws RestException {
        try {
            System.out.println("Get token and others");
            userDao.update(token);
            return new HashMap<>();
        } catch (Exception e) {
            throw new RestException(e);
        }
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> register(@RequestBody Credentials credentials) throws RestException {
        try {
            byte[] login = credentials.getLogin().getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(login);
            final String result = new String(Hex.encodeHex(md.digest()));
            User user = new User();
            user.setToken(credentials.getToken());
            user.setHash(result);
            user.setLogin(credentials.getLogin());
            userDao.create(user);
            Map<String, Object> b = new HashMap<>();
            b.put("id", result);
            return b;
        } catch (Exception ex) {
            throw new RestException(ex);
        }
    }
}
