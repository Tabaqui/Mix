package ru.motleycrew.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.motleycrew.service.DataService;
import ru.motleycrew.utils.Ajax;
import ru.motleycrew.utils.RestException;

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
}
