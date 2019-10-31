package org.d3ifcool.debtreminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import entities.Pembayaran;
import function.FunctionPembayaran;

public class EditDeletePeminjamanActivity extends AppCompatActivity {

    private EditText mEtPayment;
    private  EditText mEtDescription;
    private Pembayaran pembayaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_peminjaman);

        // TODO: 08/04/2019 back button
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        pembayaran = (Pembayaran) getIntent().getSerializableExtra("pembayaran");
        mEtPayment = findViewById(R.id.et_payment_pembayaran);
        mEtPayment.setText(String.valueOf(pembayaran.getPay()));
        mEtDescription = findViewById(R.id.et_description_pembayaran);
        mEtDescription.setText(pembayaran.getDescription());
    }

    private void updatePayment(){
        pembayaran = (Pembayaran) getIntent().getSerializableExtra("pembayaran");
        FunctionPembayaran bayar = new FunctionPembayaran(getBaseContext());
        pembayaran.setPay(Integer.parseInt(mEtPayment.getText().toString().trim()));
        pembayaran.setDescription(mEtDescription.getText().toString().trim());
        if (TextUtils.isEmpty(mEtPayment.getText())||
                TextUtils.isEmpty(mEtDescription.getText())){
            Toast.makeText(this, "please completed", Toast.LENGTH_SHORT).show();
        }else if (bayar.Update(pembayaran)){
            Intent intent = new Intent(this,DetailActivity.class);
            intent.putExtra("pembayaran", pembayaran);
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditDeletePeminjamanActivity.this);
            builder.setMessage("fail");
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

    private void deletePayment(){
        pembayaran = (Pembayaran) getIntent().getSerializableExtra("pembayaran");
        AlertDialog.Builder builder = new AlertDialog.Builder(EditDeletePeminjamanActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FunctionPembayaran bayar = new FunctionPembayaran(getBaseContext());
                if (bayar.delete(pembayaran.getIdPembayaran())){
                    Intent intentDelete = new Intent(EditDeletePeminjamanActivity.this,DetailActivity.class);
                    Toast.makeText(EditDeletePeminjamanActivity.this, "success", Toast.LENGTH_SHORT).show();
                    startActivity(intentDelete);
                }else {
                    AlertDialog.Builder builderDeleteInformation = new AlertDialog.Builder(getBaseContext());
                    builderDeleteInformation.setCancelable(false);
                    builderDeleteInformation.setMessage("Fail");
                    builderDeleteInformation.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builderDeleteInformation.create().show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    private void back(){
        Intent intent = new Intent(this,DetailActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_delete_pembayaran,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                updatePayment();
                break;
            case R.id.action_delete:
                deletePayment();
                break;
            default:
                new IllegalArgumentException("Ups!!");
        }
        return super.onOptionsItemSelected(item);
    }
}
