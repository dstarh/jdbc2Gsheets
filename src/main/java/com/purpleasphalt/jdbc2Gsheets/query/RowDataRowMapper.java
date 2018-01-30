package com.purpleasphalt.jdbc2Gsheets.query;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.services.sheets.v4.model.CellData;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.jdbc.core.RowMapper;

import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.RowData;

public class RowDataRowMapper implements RowMapper<RowData> {
    private Map<Integer, String> typeMapCache = new HashMap<>();

    @Override
    public RowData mapRow(ResultSet resultSet, int i) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columns = metaData.getColumnCount();
        RowData rowData = new RowData();
        List<CellData> cellData = new ArrayList<>();
        for (int j = 1; j <= columns; j++) {
            if (!typeMapCache.containsKey(j)) {
                typeMapCache.put(j, metaData.getColumnClassName(i));
            }
            String type = typeMapCache.get(j);
            CellData cellDatum = new CellData();
            cellDatum.setUserEnteredValue(getExtendedValue(resultSet, j, type));
            cellData.add(cellDatum);
        }
        rowData.setValues(cellData);
        return rowData;
    }

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
}
