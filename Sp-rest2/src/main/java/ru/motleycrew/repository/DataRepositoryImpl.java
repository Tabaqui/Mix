package ru.motleycrew.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ru.motleycrew.entity.Data;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by vas on 03.04.16.
 */
@Repository("dataRepository")
public class DataRepositoryImpl implements DataRepository<Data> {

    @Autowired
    protected JdbcOperations jdbcOperation;

    @Override
    public void persist(Data object) {

        Object[] params = new Object[]{object.getId(), object.getMessageId(), object.getText(), object.getDate()};
        int[] types = new int[]{Types.VARCHAR, Types.VARCHAR, Types.CLOB, Types.TIMESTAMP_WITH_TIMEZONE};

        jdbcOperation.update("insert into message(\n" +
                " id, message_id, text, created_at) \n" +
                " values (?, ?, ?, ?);", params, types);
    }

    @Override
    public void delete(Data object) {
        jdbcOperation.update("delete from t_table\n" +
                " where message_id = '" + object.getId() + "';");
    }

    @Override
    public Set<String> getRandomData() {
        Set<String> result = new HashSet<>();
//        SqlRowSet rowSet = jdbcOperation.queryForRowSet("select data_description from t_table");
//        while (rowSet.next()) {
//            result.add(rowSet.getString("data_description"));
//        }
        result.add("bow");
        result.add("baw");
        return result;
    }
}
