package com.example.controlasistencia.modelo;

import com.google.gson.annotations.SerializedName;

public class DeleteRequest {
    private String action;
    private int id;
    @SerializedName("spreadsheet_Id")
    private String spreadsheetId;
    private String sheet;

    public DeleteRequest(String action, int id, String spreadsheetId, String sheet) {
        this.action = action;
        this.id = id;
        this.spreadsheetId = spreadsheetId;
        this.sheet = sheet;
    }

    // Getters y Setters (opcional, pero recomendados)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
