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

import com.example.controlasistencia.DepartamentoFormActivity;
import com.example.controlasistencia.DepartamentosActivity;
import com.example.controlasistencia.R;
import com.example.controlasistencia.dao.DepartamentoDAO;
import com.example.controlasistencia.dao.GoogleDAO;
import com.example.controlasistencia.modelo.Departamento;

public class DepartamentosAdapter extends ArrayAdapter<Departamento> {
    private Context context;
    private List<Departamento> departamentos;
    private DepartamentoDAO departamentoDAO;
    private GoogleDAO googleDAO;

    public DepartamentosAdapter(Context context, List<Departamento> departamentos) {
        super(context, R.layout.item_departamento, departamentos);
        this.context = context;
        this.departamentos = departamentos;
        this.departamentoDAO = new DepartamentoDAO(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_departamento, parent, false);

        TextView tvNombre = rowView.findViewById(R.id.tvNombre);
        TextView tvDescripcion = rowView.findViewById(R.id.tvDescripcion);
        ImageButton btnEditar = rowView.findViewById(R.id.btnEditar);
        ImageButton btnEliminar = rowView.findViewById(R.id.btnEliminar);

        final Departamento departamento = departamentos.get(position);
        tvNombre.setText(departamento.getNombre());
        tvDescripcion.setText(departamento.getDescripcion());

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DepartamentoFormActivity.class);
                intent.putExtra("departamento_id", departamento.getId());
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
                builder.setTitle("Eliminar Departamento");
                builder.setMessage("¿Estás seguro de que deseas eliminar este departamento?");
                builder.setPositiveButton("Sí", (dialog, which) -> {
                    googleDAO.eliminarDataGoogle(departamento.getId(), "departamento", new GoogleDAO.GoogleDelateCallback() {


                        @Override
                        public void onDataDeleted(String mensajeExito) {
                            v.getContext().startActivity(new Intent(v.getContext(), DepartamentosActivity.class));
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