package com.purpleasphalt.jdbc2Gsheets.google.impl;

import com.google.api.services.sheets.v4.model.GridData;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.common.collect.Lists;

import java.util.List;

public class Sheets {
    public static class Builder {
        private String title;
        private List<RowData> rowData;

        private Builder() {
        }

        public static Builder init() {
            return new Builder();
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withRowData(List<RowData> rowData) {
            this.rowData = rowData;
            return this;
        }

        public Sheet build() {
            Sheet sheet = new Sheet();
            SheetProperties props = new SheetProperties();
            props.setTitle(title);
            GridData gridData = new GridData();
            gridData.setRowData(rowData);
            sheet.setProperties(props);
            sheet.setData(Lists.newArrayList(gridData));
            return sheet;
        }
    }
}
