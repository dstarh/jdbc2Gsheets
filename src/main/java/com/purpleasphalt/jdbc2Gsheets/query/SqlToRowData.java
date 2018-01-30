package com.purpleasphalt.jdbc2Gsheets.query;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;

import com.google.api.services.sheets.v4.model.RowData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.purpleasphalt.jdbc2Gsheets.configuration.Connections;

import javax.sql.DataSource;


public interface SqlToRowData {
    List<RowData> getRowData(String sql, DataSource dataSource);
}
