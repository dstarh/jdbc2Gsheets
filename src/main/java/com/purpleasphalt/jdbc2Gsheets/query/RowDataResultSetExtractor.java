package com.purpleasphalt.jdbc2Gsheets.query;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.RowData;

public class RowDataResultSetExtractor implements ResultSetExtractor<List<RowData>> {
    private Map<Integer, String> typeMapCache = new HashMap<>();

    ExtendedValue getExtendedValue(ResultSet rs, int index, String type) throws SQLException {
        ExtendedValue ev = new ExtendedValue();
        switch (type) {
            case "java.lang.Boolean":
                ev.setBoolValue(rs.getBoolean(index));
                break;
            case "java.lang.Integer":
                Integer intVal = rs.getInt(index);
                ev.setNumberValue(null == intVal ? null : Double.valueOf(intVal));
                break;
            case "java.lang.Long":
                Long longVal = rs.getLong(index);
                ev.setNumberValue(null == longVal ? null : Double.valueOf(longVal));
                break;
            case "java.lang.Float":
                Float floatVal = rs.getFloat(index);
                ev.setNumberValue(null == floatVal ? null : Double.valueOf(floatVal));
                break;
            case "java.lang.Double":
                Double doubleVal = rs.getDouble(index);
                ev.setNumberValue(doubleVal);
                break;
            case "java.math.BigDecimal":
                BigDecimal bd = rs.getBigDecimal(index);
                ev.setNumberValue(null == bd ? null : bd.doubleValue());
                break;
            case "java.sql.Date":
                String dateVal = rs.getString(index);
                if (null != dateVal) {
                    DateTime joda = DateTime.parse(dateVal);
                    ev.setStringValue(joda.toString(ISODateTimeFormat.dateTime()));
                }
                break;
            case "java.sql.Timestamp":
                String tsVal = rs.getString(index);
                if (null != tsVal) {
                    DateTime joda = DateTime.parse(tsVal);
                    ev.setStringValue(joda.toString(ISODateTimeFormat.dateTime()));
                }
            case "java.lang.String":
                ev.setStringValue(rs.getString(index));
                break;
            default:
                ev.setStringValue(rs.getString(index));
                break;
        }
        return ev;
    }

    @Override
    public List<RowData> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<RowData> rowData = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columns = metaData.getColumnCount();
        RowData headerDatum = new RowData();
        List<CellData> headerData = new ArrayList<>();
        for (int i = 1; i <= columns; i++) {
            CellData cellDatum = new CellData();
            ExtendedValue ev = new ExtendedValue();
            ev.setStringValue(metaData.getColumnName(i));
            cellDatum.setUserEnteredValue(ev);
            headerData.add(cellDatum);
        }
        headerDatum.setValues(headerData);
        rowData.add(headerDatum);

        while (resultSet.next()) {
            RowData rowDatum = new RowData();
            List<CellData> cellData = new ArrayList<>();
            for (int i = 1; i <= columns; i++) {
                if (!typeMapCache.containsKey(i)) {
                    typeMapCache.put(i, metaData.getColumnClassName(i));
                }
                String type = typeMapCache.get(i);
                CellData cellDatum = new CellData();
                cellDatum.setUserEnteredValue(getExtendedValue(resultSet, i, type));
                cellData.add(cellDatum);
            }
            rowDatum.setValues(cellData);
            rowData.add(rowDatum);
        }
        return rowData;
    }
}
