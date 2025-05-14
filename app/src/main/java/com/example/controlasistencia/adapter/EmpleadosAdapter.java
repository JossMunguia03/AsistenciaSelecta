package com.example.controlasistencia.adapter;

import android.app.AlertDialog;
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

import com.example.controlasistencia.DepartamentosActivity;
import com.example.controlasistencia.EmpleadoFormActivity;
import com.example.controlasistencia.EmpleadosActivity;
import com.example.controlasistencia.R;
import com.example.controlasistencia.dao.EmpleadoDAO;
import com.example.controlasistencia.dao.GoogleDAO;
import com.example.controlasistencia.modelo.Empleado;

public class EmpleadosAdapter extends ArrayAdapter<Empleado> {
    private Context context;
    private List<Empleado> empleados;
    private EmpleadoDAO empleadoDAO;

    private GoogleDAO googleDAO;

    public EmpleadosAdapter(Context context, List<Empleado> empleados) {
        super(context, R.layout.item_empleado, empleados);
        this.context = context;
        this.empleados = empleados;
        this.empleadoDAO = new EmpleadoDAO(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_empleado, parent, false);

        TextView tvNombre = rowView.findViewById(R.id.tvNombre);
        TextView tvPuesto = rowView.findViewById(R.id.tvPuesto);
        ImageButton btnEditar = rowView.findViewById(R.id.btnEditar);
        ImageButton btnEliminar = rowView.findViewById(R.id.btnEliminar);

        final Empleado empleado = empleados.get(position);
        tvNombre.setText(empleado.getNombre() + " " + empleado.getApellidos());
        tvPuesto.setText(empleado.getPuesto());

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EmpleadoFormActivity.class);
                intent.putExtra("empleado_id", empleado.getId());
                context.startActivity(intent);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //inicio de la alerta
                googleDAO = new GoogleDAO();
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Eliminar Empleado");
                builder.setMessage("¿Estás seguro de que deseas eliminar este Empleado?");
                builder.setPositiveButton("Sí", (dialog, which) -> {
                    googleDAO.eliminarDataGoogle(empleado.getId(), "empleado", new GoogleDAO.GoogleDelateCallback() {


                        @Override
                        public void onDataDeleted(String mensajeExito) {
                            v.getContext().startActivity(new Intent(v.getContext(), EmpleadosActivity.class));
                        }

                        @Override
                        public void onDeleteFailed(String mensajeError) {

                        }
                    });
                });
                builder.setNegativeButton("No", null);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog show = builder
                        .show();


                //fin de la alerta
            }
        });

        return rowView;
    }
} 