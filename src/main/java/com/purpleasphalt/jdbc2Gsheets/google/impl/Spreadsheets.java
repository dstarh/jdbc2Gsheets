package com.purpleasphalt.jdbc2Gsheets.google.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;

public class Spreadsheets {

    public static class Builder {
        private String title;
        private List<Sheet> sheets;

        private Builder() {
            sheets = new ArrayList<>();
        }

        public static Builder init() {
            return new Builder();
        }

        public Builder withSheet(Sheet sheet) {
            sheets.add(sheet);
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Spreadsheet build() {
            Spreadsheet spreadsheet = new Spreadsheet();
            SpreadsheetProperties props = new SpreadsheetProperties();
            props.setTitle(title);
            spreadsheet.setProperties(props);
            spreadsheet.setSheets(sheets);
            return spreadsheet;
        }
    }
}
