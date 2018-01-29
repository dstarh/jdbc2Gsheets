package com.purpleasphalt.jdbc2Gsheets.query;

import java.sql.ResultSetMetaData;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.purpleasphalt.jdbc2Gsheets.configuration.Connections;

@Component
public class QueryTest {
    private static final Logger logger = LoggerFactory.getLogger(QueryTest.class);
    @Autowired
    private Connections connections;

    public void test() {
        connections.getDataSources()
                   .forEach((name, dataSource) -> {
                       logger.error(name);
                       JdbcTemplate jdbcTemplate = new JdbcTemplate();
                       jdbcTemplate.setDataSource(dataSource);
                       List<Object> objs = jdbcTemplate.query("select id from firm_user limit 20;", (resultSet, i) -> {
                           ResultSetMetaData meta = resultSet.getMetaData();
                           int count = meta.getColumnCount();
                           logger.error(String.valueOf(count));

                           for (int j = 1; j <= count; j++) {
                               logger.error(meta.getColumnName(j));
                           }
                           return resultSet.getInt("id");
                       });
                       System.out.println(objs);

                   });
    }


}
