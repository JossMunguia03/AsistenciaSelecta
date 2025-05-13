package com.example.controlasistencia.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import com.example.controlasistencia.AsistenciaFormActivity;
import com.example.controlasistencia.R;
import com.example.controlasistencia.dao.AsistenciaDAO;
import com.example.controlasistencia.dao.EmpleadoDAO;
import com.example.controlasistencia.modelo.Asistencia;
import com.example.controlasistencia.modelo.Empleado;

public class AsistenciasAdapter extends ArrayAdapter<Asistencia> {
    private Context context;
    private List<Asistencia> asistencias;
    private AsistenciaDAO asistenciaDAO;
    private EmpleadoDAO empleadoDAO;

    public AsistenciasAdapter(Context context, List<Asistencia> asistencias) {
        super(context, R.layout.item_asistencia, asistencias);
        this.context = context;
        this.asistencias = asistencias;
        this.asistenciaDAO = new AsistenciaDAO(context);
        this.empleadoDAO = new EmpleadoDAO(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_asistencia, parent, false);

        TextView tvEmpleado = rowView.findViewById(R.id.tvEmpleado);
        TextView tvFecha = rowView.findViewById(R.id.tvFecha);
        TextView tvTipo = rowView.findViewById(R.id.tvTipo);
        ImageButton btnEditar = rowView.findViewById(R.id.btnEditar);
        ImageButton btnEliminar = rowView.findViewById(R.id.btnEliminar);

        final Asistencia asistencia = asistencias.get(position);
        /*
        empleadoDAO.open();
        Empleado empleado = empleadoDAO.getEmpleado(asistencia.getIdEmpleado());
        empleadoDAO.close();

        if (empleado != null) {
            tvEmpleado.setText(empleado.getNombre() + " " + empleado.getApellidos());
        }

         */
        tvEmpleado.setText(asistencia.getEmpleado() + " " + asistencia.getApellidoEmpleado());
        //tvFecha.setText(asistencia.getFechaEntrada());
        tvFecha.setText(asistencia.getFecha());
        tvTipo.setText(asistencia.getTipoAsistencia());

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AsistenciaFormActivity.class);
                intent.putExtra("asistencia_id", asistencia.getId());
                intent.putExtra("empleado_id", asistencia.getIdEmpleado());
                context.startActivity(intent);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asistenciaDAO.open();
                asistenciaDAO.deleteAsistencia(asistencia.getId());
                asistenciaDAO.close();
                asistencias.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Asistencia eliminada", Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }
} 