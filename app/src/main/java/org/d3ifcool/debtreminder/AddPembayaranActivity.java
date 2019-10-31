package org.d3ifcool.debtreminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import entities.Pembayaran;
import entities.Peminjaman;
import function.FunctionPembayaran;

public class AddPembayaranActivity extends AppCompatActivity {
    private EditText mEtPayment;
    private  EditText mEtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pembayaran);

        // TODO: 08/04/2019 back button 
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        mEtPayment = findViewById(R.id.et_payment_add_pembayaran);
        mEtDescription = findViewById(R.id.et_description_add_pembayaran);

    }

    private void insertPayment(){
        FunctionPembayaran functionPembayaran = new FunctionPembayaran(getBaseContext());
        Pembayaran pembayaran = new Pembayaran();
        final Peminjaman peminjaman = (Peminjaman) getIntent().getSerializableExtra("peminjaman");
        int id = peminjaman.getIdPeminjaman();
        mEtPayment = findViewById(R.id.et_payment_add_pembayaran);

        pembayaran.setIdPeminjaman(id);
        pembayaran.setPay(Integer.parseInt(mEtPayment.getText().toString().trim()));
        pembayaran.setDescription(mEtDescription.getText().toString().trim());
        peminjaman.setIdPeminjaman(peminjaman.getIdPeminjaman());
        peminjaman.setAmount(peminjaman.getAmount());
        peminjaman.setName(peminjaman.getName());
        peminjaman.setTelphon(peminjaman.getTelphon());
        peminjaman.setDescription(peminjaman.getDescription());
        peminjaman.setDateOfLoan(peminjaman.getDateOfLoan());
        peminjaman.setDateDue(peminjaman.getDateDue());

        if (TextUtils.isEmpty(mEtPayment.getText().toString().trim()) || TextUtils.isEmpty(mEtDescription.getText().toString().trim())){
            Toast.makeText(this, "please complete the form", Toast.LENGTH_SHORT).show();
        }else if (functionPembayaran.create(pembayaran)){
            Intent intentAdd = new Intent(AddPembayaranActivity.this,DetailActivity.class);
            intentAdd.putExtra("pembayaran",pembayaran);
            intentAdd.putExtra("peminjaman",peminjaman);
            Toast.makeText(this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
            startActivity(intentAdd);
        }else {

            AlertDialog.Builder builder = new AlertDialog.Builder(AddPembayaranActivity.this);
            builder.setMessage("Fail DB");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        }
    }

    private void back(){
        Intent intent = new Intent(this,DetailActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pembayaran_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                insertPayment();
                finish();
                return true;
            case R.id.home:
                back();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
