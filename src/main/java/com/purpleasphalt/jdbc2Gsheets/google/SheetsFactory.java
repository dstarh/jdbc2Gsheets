package com.purpleasphalt.jdbc2Gsheets.google;

import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;

public interface SheetsFactory {
    Sheets getSheetsService() throws IOException;
}
