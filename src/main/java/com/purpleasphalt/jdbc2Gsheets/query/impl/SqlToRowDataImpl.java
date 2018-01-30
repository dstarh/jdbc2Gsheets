package com.purpleasphalt.jdbc2Gsheets.query.impl;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.api.services.sheets.v4.model.RowData;
import com.purpleasphalt.jdbc2Gsheets.query.RowDataResultSetExtractor;
import com.purpleasphalt.jdbc2Gsheets.query.SqlToRowData;

@Component
public class SqlToRowDataImpl implements SqlToRowData {

    @Override
    public List<RowData> getRowData(String sql, DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate.query(sql, new RowDataResultSetExtractor());
    }
}
