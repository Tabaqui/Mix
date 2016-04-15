package ru.motleycrew.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.motleycrew.config.Credentials;
import ru.motleycrew.entity.Data;
import ru.motleycrew.repository.MessageDao;
import ru.motleycrew.service.DataService;
import ru.motleycrew.utils.Ajax;
import ru.motleycrew.utils.RestException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by vas on 03.04.16.
 */
@Controller
public class DataController extends ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(DataController.class);

    @Autowired
    @Qualifier("dataService")
    private DataService dataService;

    @Autowired
    @Qualifier("messageDao")
    private MessageDao messageDao;

    @RequestMapping(value = "/persist", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> persist(@RequestParam("data") String data) throws RestException {
        try {
            if (data == null || data.equals("")) {
                return Ajax.emptyResponce();
            }
            dataService.persist(data);
            return Ajax.emptyResponce();
        } catch (Exception e) {
            throw new RestException(e);
        }
    }

    @RequestMapping(value = "/getRandomData", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getRadomData() throws RestException {
        try {
            Set<String> result = dataService.getRandomData();
            return Ajax.successResponse(result);
        } catch (Exception e) {
            throw new RestException(e);
        }
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public @ResponseBody RestMessage getLastMessage() throws RestException {
        try {
            return new RestMessage("Test Title",
                    new Date(),
                    true,
                    "Meeting Failed. Return to your home mates.",
                    "test_message_id_1");
        } catch (Exception e) {
            throw new RestException(e);
        }
    }

    @RequestMapping(value = "/messages/add", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> createMessage(@RequestBody RestMessage message) throws RestException {
        try {
            LOG.debug("Post message" + message.getText());
            Data data = Data.newData(message);
            messageDao.create(data);
            Map<String, Object> b = new HashMap<>();
            return b;
        } catch (Exception e) {
            throw new RestException(e);
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> updatePhoneId(@RequestBody Credentials credetials) throws RestException {
        try {
            System.out.println("Get token and others");
            System.out.println(credetials.getToken());

            return new HashMap<>();
        } catch (Exception e) {
            throw new RestException(e);
        }
    }
}
