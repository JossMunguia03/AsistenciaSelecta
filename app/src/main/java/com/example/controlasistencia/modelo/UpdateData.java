package com.example.controlasistencia.modelo;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class UpdateData {
    private String action;
    private int id;
    @SerializedName("spreadsheet_Id")
    private String spreadsheetId;
    private String sheet;
    private Map<String, Object> fields; // Mapa para los campos a actualizar

    public UpdateData(String action, int id, String spreadsheetId, String sheet, Map<String, Object> fields) {
        this.action = action;
        this.id = id;
        this.spreadsheetId = spreadsheetId;
        this.sheet = sheet;
        this.fields = fields;
    }

    // Getters y Setters
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

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }
}
