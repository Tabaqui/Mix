package ru.motleycrew.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.motleycrew.controller.IncomingMessage;
import ru.motleycrew.controller.IncomingUser;
import ru.motleycrew.entity.Data;
import ru.motleycrew.entity.User;
import ru.motleycrew.repository.UserDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by User on 18.04.2016.
 */
@Service
public class MessageService {

    @Autowired
    private UserDao<User> userDao;

    public Data getData(IncomingMessage message) {
        Data data = new Data();
        data.setText(message.getTitle());
        try {
            data.setDate(new SimpleDateFormat("yyyy.MM.dd'T'HH:mm:ssZ").parse(message.getDate()));
        } catch (ParseException ex) {
            throw new RuntimeException("Parse Message date format exception");
        }
        data.setTitle(message.getTitle());
        data.setApproved(message.isApproved());
//        data.setMessageId(message.getId());
        List<User> dataUsers = getUsers(message.getParticipants());
        data.setUsers(dataUsers);
        return data;
    }

    private List<User> getUsers(List<IncomingUser> users) {
        List<String> userMails = users.stream()
                .map(IncomingUser::getMail)
                .collect(Collectors.toList());
        return userDao.find(userMails);
    }

    public IncomingMessage getMessage(Data data) {
        IncomingMessage message = new IncomingMessage();
        message.setTitle(data.getTitle());
        message.setText(data.getText());
        message.setDate(new SimpleDateFormat("yyyy.MM.dd'T'HH:mm:ssZ").format(data.getDate()));
        message.setApproved(data.isApproved());
        message.setParticipants(getIncomingUsers(data.getUsers()));
        return message;
    }

    private List<IncomingUser> getIncomingUsers(List<User> users) {
        return users.stream()
                .map(user -> new IncomingUser(user.getLogin()))
                .collect(Collectors.toList());

    }
}
