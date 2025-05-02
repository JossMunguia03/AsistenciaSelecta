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

import com.example.controlasistencia.DepartamentoFormActivity;
import com.example.controlasistencia.R;
import com.example.controlasistencia.dao.DepartamentoDAO;
import com.example.controlasistencia.modelo.Departamento;

public class DepartamentosAdapter extends ArrayAdapter<Departamento> {
    private Context context;
    private List<Departamento> departamentos;
    private DepartamentoDAO departamentoDAO;

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
                departamentoDAO.open();
                departamentoDAO.deleteDepartamento(departamento.getId());
                departamentoDAO.close();
                departamentos.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Departamento eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }
} 