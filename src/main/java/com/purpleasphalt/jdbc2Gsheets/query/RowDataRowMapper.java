package com.purpleasphalt.jdbc2Gsheets.query;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.google.api.services.sheets.v4.model.RowData;

public class RowDataRowMapper implements RowMapper<RowData> {
    @Override
    public RowData mapRow(ResultSet resultSet, int i) throws SQLException {
//        resultSet.getMetaData().getColumnClassName();
        return null;
    }
}
