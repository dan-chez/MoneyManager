package co.danchez.moneymanager.Utilidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.danchez.moneymanager.R;

public class SpinnerAdapter extends ArrayAdapter {

    private Context context;
    private int selectedItem = 0;
    private LayoutInflater inflater;
    private List<String> items = new ArrayList<>();
    private boolean conError = false;
    private TextView tvError;
    private String msgError;
    private ViewGroup parent;

    public SpinnerAdapter(Context context, int textViewResourceId, List<String> itemList, TextView tvError_, String msgError_) {
        super(context, textViewResourceId);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items.addAll(itemList);
        this.tvError = tvError_;
        this.msgError = msgError_;

    }

    public TextView getView(int position, View convertView, ViewGroup parent_) {
        TextView v = (TextView) inflater.inflate(R.layout.layout_spinner, parent_, false);
        v.setTextColor(context.getResources().getColor(R.color.grisPolvo));
        v.setBackgroundColor(context.getResources().getColor(R.color.white));
        v.setText(getItem(position).toString());
        this.parent = parent_;
        return v;
    }

    public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) inflater.inflate(R.layout.layout_spinner, parent, false);
        if (getCount() > position) {
            if (position == selectedItem && position != 0) {
                v.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                v.setBackgroundColor(context.getResources().getColor(R.color.grisPolvo));
            } else {
                v.setTextColor(context.getResources().getColor(R.color.grisPolvo));
                v.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            v.setText(getItem(position).toString());
        }
        parent.setBackground(context.getResources().getDrawable(R.drawable.spinner_bg));
        return v;
    }

    private void actualizarEstado(String msgError, boolean mostrarMensaje) {
        if (conError && mostrarMensaje) {
            if (parent != null)
                parent.setBackground(context.getResources().getDrawable(R.drawable.spinner_error_bg));
            if (tvError != null && !msgError.equals("")) {
                tvError.setText(msgError);
                tvError.setVisibility(View.VISIBLE);
            }
        } else if (!conError) {
            if (parent != null)
                parent.setBackground(context.getResources().getDrawable(R.drawable.spinner_bg));
            if (tvError != null) {
                tvError.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void validar(boolean mostrarMensaje) {
        conError = selectedItem == 0;
        actualizarEstado(msgError, mostrarMensaje);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(String itemAdd) {
        items.add(itemAdd);
    }

    public void setItemList(List<String> itemList) {
        items.clear();
        items.addAll(itemList);
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public boolean isConError() {
        return conError;
    }

    public void setConError(boolean conError) {
        this.conError = conError;
        notifyDataSetChanged();
    }
}
