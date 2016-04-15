package ru.motleycrew.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.motleycrew.entity.Data;
import ru.motleycrew.repository.DataRepository;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Created by vas on 03.04.16.
 */
@Service("dataService")
public class DataServiceImpl implements  DataService {

    private static final Logger LOG = LoggerFactory.getLogger(DataServiceImpl.class);

    @Autowired
    @Qualifier("dataRepository")
    private DataRepository dataRepository;

    @Override
    public boolean persist(String messageId) {
        try {
            Data data = new Data(messageId);
            data.setTitle("hyb title test");
            data.setText("Many test text here may be");
            data.setApproved(true);
            data.setDate(new Date());
            dataRepository.persist(data);
            return true;
        } catch (Exception e) {
            LOG.error("ERROR SAVING DATA: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Set<String> getRandomData() {
        return dataRepository.getRandomData();
    }
}
