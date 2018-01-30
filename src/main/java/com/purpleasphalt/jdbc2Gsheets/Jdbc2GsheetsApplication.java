package com.purpleasphalt.jdbc2Gsheets;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.FileSystemResource;

import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.purpleasphalt.jdbc2Gsheets.configuration.Connections;
import com.purpleasphalt.jdbc2Gsheets.google.SheetsFactory;
import com.purpleasphalt.jdbc2Gsheets.google.impl.Sheets;
import com.purpleasphalt.jdbc2Gsheets.google.impl.Spreadsheets;
import com.purpleasphalt.jdbc2Gsheets.query.SqlToRowData;

@ComponentScan("com.purpleasphalt.jdbc2Gsheets")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Jdbc2GsheetsApplication implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SheetsFactory sheetsFactory;
    @Autowired
    private Connections connections;
    @Autowired
    private SqlToRowData sqlToRowData;

    public static void main(String[] args) {
        SpringApplication.run(Jdbc2GsheetsApplication.class, args)
                         .stop();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!args.containsOption("sql")) {
            logger.error("Sql file not found");
            System.exit(1);
        }
        if (!args.containsOption("title")) {
            logger.error("Title not found");
            System.exit(1);
        }
        String title = args.getOptionValues("title")
                           .get(0);
        String sqlFileName = args.getOptionValues("sql")
                                 .get(0);
        InputStream is = new FileSystemResource(sqlFileName).getInputStream();
        String sql = IOUtils.toString(is);
        Spreadsheets.Builder sb = Spreadsheets.Builder.init()
                                                      .withTitle(title);
        connections.getDataSources()
                   .forEach((name, dataSource) -> {
                       sb.withSheet(Sheets.Builder.init()
                                                  .withRowData(sqlToRowData.getRowData(sql, dataSource))
                                                  .withTitle(name)
                                                  .build());
                   });

        Spreadsheet created = sheetsFactory.getSheetsService()
                                           .spreadsheets()
                                           .create(sb.build())
                                           .execute();
        System.out.println("*******************************************");
        System.out.println(created.getSpreadsheetUrl());
        System.out.println("*******************************************");
    }
}
