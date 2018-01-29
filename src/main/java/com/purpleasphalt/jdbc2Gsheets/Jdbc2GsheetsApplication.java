package com.purpleasphalt.jdbc2Gsheets;

import java.util.ArrayList;
import java.util.List;

import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.google.api.services.sheets.v4.Sheets.Spreadsheets;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridData;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.purpleasphalt.jdbc2Gsheets.google.SheetsFactory;

@ComponentScan("com.purpleasphalt.jdbc2Gsheets")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Jdbc2GsheetsApplication implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SheetsFactory sheetsFactory;



    public static void main(String[] args) {
        SpringApplication.run(Jdbc2GsheetsApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        Spreadsheet spreadsheet = new Spreadsheet();
        SpreadsheetProperties sheetProps = new SpreadsheetProperties();
        sheetProps.setTitle("This is another test");
        spreadsheet.setProperties(sheetProps);
        Sheet sheet = new Sheet();
        SheetProperties sheetProperties = new SheetProperties();
        sheetProperties.setTitle("this is my title");
        sheet.setProperties(sheetProperties);
        GridData gridData = new GridData();
        RowData rowData = new RowData();
        List<CellData> cellData = new ArrayList<>();
        for(int i=0; i<10; i++){
            CellData cd = new CellData();
            ExtendedValue ev = new ExtendedValue();
            ev.setStringValue("hello");
            cd.setUserEnteredValue(ev);
            cellData.add(cd);
        }
        rowData.setValues(cellData);
        List<RowData> rows = new ArrayList<>();
        rows.add(rowData);
        gridData.setRowData(rows);
        List<GridData> gridDataList = new ArrayList<>();
        gridDataList.add(gridData);
        sheet.setData(gridDataList);
        List<Sheet> sheets = new ArrayList<>();
        sheets.add(sheet);
        spreadsheet.setSheets(sheets);
        Spreadsheet created = sheetsFactory.getSheetsService()
                                                .spreadsheets()
                                                .create(spreadsheet)
                                                .execute();
        logger.error(created.getSpreadsheetUrl());
    }
}
