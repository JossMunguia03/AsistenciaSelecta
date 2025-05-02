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

import com.example.controlasistencia.EmpleadoFormActivity;
import com.example.controlasistencia.R;
import com.example.controlasistencia.dao.EmpleadoDAO;
import com.example.controlasistencia.modelo.Empleado;

public class EmpleadosAdapter extends ArrayAdapter<Empleado> {
    private Context context;
    private List<Empleado> empleados;
    private EmpleadoDAO empleadoDAO;

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
                empleadoDAO.open();
                empleadoDAO.deleteEmpleado(empleado.getId());
                empleadoDAO.close();
                empleados.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Empleado eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }
} 