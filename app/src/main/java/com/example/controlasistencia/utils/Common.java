package com.example.controlasistencia.utils;

import com.example.controlasistencia.modelo.GoogleSheetsResponse;
import com.example.controlasistencia.modelo.IGoogleSheets;

public class Common {
    public static String BASE_URL = "https://script.google.com/macros/s/AKfycbxzJpFSibpAOogIrBsuEctM437pHJoootCuZHCwdWGUFD_lH0kBzid61muaR60V1baA/";
    public static String GOOGLE_SHEET_ID = "12JjpuSzoLkUYuD9-FQYKrQOvQaq7GKCHe4woxI7OJ9M";


    public static IGoogleSheets iGSGetMethodClient(String baseUrl){
        return GoogleSheetsResponse.getClientGetMethod(baseUrl).create(IGoogleSheets.class);
    }
}
