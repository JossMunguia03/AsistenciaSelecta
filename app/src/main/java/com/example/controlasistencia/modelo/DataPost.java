package com.example.controlasistencia.modelo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataPost {

    @SerializedName("spreadsheet_Id")
    private String  spreadsheetId;
    private String sheet;
    private List<List<String>> rows;

    public DataPost(String googleSheetId, String sheet, List<List<String>> rows) {
        this.spreadsheetId = googleSheetId;
        this.sheet = sheet;
        this.rows = rows;
    }

    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public void setSpreadsheetId(String spreadsheetId) {
        this.spreadsheetId = spreadsheetId;
    }

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }


}
