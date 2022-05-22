package com.example.sqllite.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sqliteklsb.R;
import com.example.sqllite.MainActivity;
import com.example.sqllite.database.DBController;
import com.example.sqllite.database.EditTeman;
import com.example.sqllite.database.Teman;
import com.tanjung.mysqlpro.app.AppContoller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Teman_Adapter extends RecyclerView.Adapter<Teman_Adapter.TemanViewHolder> {
    private static Object Control;
    private ArrayList<Teman> ListData;
    private Context control;
    public Teman_Adapter(ArrayList<Teman> listData) {
        this.ListData = listData;
    }
    @Override
    public TemanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutinf = LayoutInflater.from(parent.getContext());
        View view = layoutinf.inflate(R.layout.row_data_teman, parent, false);
        Control = parent.getContext();
        return new TemanViewHolder(view);
    }
    @Override
    public void onBindViewHolder(Teman_Adapter.TemanViewHolder holder, int position) {
        String nma, tlp, id;
        id = ListData.get(position).getId();
        nma = ListData.get(position).getNama();
        tlp = ListData.get(position).getTelpon();
        DBController db = new DBController(control);
        holder.namaTxt.setTextColor(Color.BLUE);
        holder.namaTxt.setTextSize(20);
        holder.namaTxt.setText(nma);
        holder.telponTxt.setText(tlp);
        holder.cardku.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu pm = new PopupMenu(view.getContext(), view);
                pm.inflate(R.menu.popup1);
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                Bundle bendel = new Bundle();
                                bendel.putString("kunci1", id);
                                bendel.putString("kunci2", nma);
                                bendel.putString("kunci3", tlp);
                                Intent intent = new Intent(view.getContext(), EditTeman.class);
                                intent.putExtras(bendel);
                                view.getContext().startActivity(intent);
                                break;
                            case R.id.hapus:
                                AlertDialog.Builder alertdb = new AlertDialog.Builder(view.getContext());
                                alertdb.setTitle("Yakin " + nma + " akan dihapus?");
                                alertdb.setMessage("Tekan ya untuk menghapus");
                                alertdb.setCancelable(false);
                                alertdb.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {

                                        HapusData(id);
                                        Toast.makeText(view.getContext(), "Data " + id + " telah dihapus", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                                        view.getContext().startActivity(intent);
                                    }
                                });
                                alertdb.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog adlg = alertdb.create();
                                adlg.show();
                                break;
                        }
                        return true;
                    }
                });
                pm.show();
                return true;
            }
        });
    }
    private void HapusData(final String idx) {
        String url_update = "http://10.0.2.2/umyTI/deletetm.php";
        final String TAG = MainActivity.class.getSimpleName();
        final String TAG_SUCCES = "succes";
        final int[] sukses = new int[1];
        StringRequest stringReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respon: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    sukses[0] = jObj.getInt(TAG_SUCCES);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error : " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", idx);
                return params;
            }
        };
        AppContoller.getInstance().addToRequestQueue(stringReq);
    }
    public int getItemCount() {
        return (ListData != null) ? ListData.size() : 0;
    }
    public class TemanViewHolder extends RecyclerView.ViewHolder {
        private CardView cardku;
        private TextView namaTxt, telponTxt;
        public TemanViewHolder(View view) {
            super(view);
            cardku = (CardView) view.findViewById(R.id.card);
            namaTxt = (TextView) view.findViewById(R.id.textNama);
            telponTxt = (TextView) view.findViewById(R.id.textTelpon);
        }
    }
}