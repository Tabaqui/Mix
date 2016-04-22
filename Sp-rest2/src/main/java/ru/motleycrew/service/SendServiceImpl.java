package ru.motleycrew.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.motleycrew.entity.Data;
import ru.motleycrew.entity.User;

import java.util.List;

/**
 * Created by User on 18.04.2016.
 */
@Service
public class SendServiceImpl  implements SendService {

    @Autowired
    private GcmMessageSender gcmMessageSender;

    @Override
    public void send(Data data) {
        List<User> targetList = data.getUsers();
        for (User user : targetList) {
            gcmMessageSender.send(data.getId(), user.getToken(), false);
        }
    }
}
