package com.example.controlasistencia.dao;

import android.util.Log;

import com.example.controlasistencia.modelo.IGoogleSheets;
import com.example.controlasistencia.modelo.UpdateData;
import com.example.controlasistencia.utils.Common;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleDAO {
    private IGoogleSheets iGoogleSheets;

    public interface GoogleCallback{
        void onDataUpdated(String mensajeExito);
        void onUpdateFailed(String mensajeError);
    }
    public GoogleDAO() {
        iGoogleSheets = Common.iGSGetMethodClient(Common.BASE_URL);
    }
    public void actualizarDataGoogle(int id, String sheetName, Map<String, Object> fieldsToUpdate, final GoogleCallback callback) {
        UpdateData updateData = new UpdateData(
                "actualizar",
                id,
                Common.GOOGLE_SHEET_ID,
                sheetName,
                fieldsToUpdate
        );

        iGoogleSheets.updateData(updateData).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ActualizarData", "Respuesta: " + response.body());
                    callback.onDataUpdated(response.body());
                } else {
                    String errorMessage = "Error al actualizar datos. Código: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += ", Mensaje: " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    callback.onUpdateFailed(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ActualizarData", "Fallo al conectar: " + t.getMessage());
                callback.onUpdateFailed("Error de conexión al actualizar datos.");
            }
        });
    }
}
