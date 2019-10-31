package org.d3ifcool.debtreminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.PembayaranListAdapter;
import adapter.PeminjamanListAdapter;
import database.DBHelper;
import entities.Pembayaran;
import entities.Peminjaman;
import function.FunctionPembayaran;
import function.FunctionPeminjaman;

public class DetailActivity extends AppCompatActivity {

    private TextView mAmount;
    private TextView mName;
    private TextView mPhone;
    private TextView mDescription;
    private TextView mDateOfLand;
    private TextView mDateDue;
    private RecyclerView recyclerView;
    private DBHelper mDbHelper = new DBHelper(this);
    private Cursor mCursor;
    private int mPayment;
    private TextView mTvPayment;
    private TextView mKurang;
    private TextView mSisa;
    private Peminjaman peminjaman;

    @Override
    protected void onStart() {
        super.onStart();
        peminjaman = (Peminjaman) getIntent().getSerializableExtra("peminjaman");

        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        sqLiteDatabase.isOpen();

        mCursor = sqLiteDatabase.rawQuery("SELECT SUM(pay) FROM pembayaran WHERE idPeminjaman = ?",new String[]{String.valueOf(peminjaman.getIdPeminjaman())});
        if (mCursor.moveToFirst()){
            mPayment = mCursor.getInt(0);
        }else {
            mPayment = -1;
        }
        mCursor.close();

        mTvPayment = findViewById(R.id.tv_payment);
        mTvPayment.setText(String.valueOf(mPayment));

        mKurang = findViewById(R.id.tv_amount);
        if (mPayment >= peminjaman.getAmount()){
            mKurang.setText("0");
        }else {
            mKurang.setText(String.valueOf(peminjaman.getAmount() - mPayment));
        }

        mSisa = findViewById(R.id.tv_sisa);
        if (mPayment == peminjaman.getAmount()){
            mSisa.setText("pas");
        }else if (mPayment < peminjaman.getAmount()){
            mSisa.setText("0");
        }else if (mPayment > peminjaman.getAmount()){
            mSisa.setText(String.valueOf(mPayment-peminjaman.getAmount()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // TODO: 08/04/2019 back button 
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        final Intent intent = getIntent();
        peminjaman = (Peminjaman) intent.getSerializableExtra("peminjaman");

        mName = findViewById(R.id.tv_detail_name);
        mName.setText(peminjaman.getName());
        mAmount = findViewById(R.id.tv_detail_amount);
        mAmount.setText(String.valueOf(peminjaman.getAmount()));
        mDescription = findViewById(R.id.tv_detail_description);
        mDescription.setText(peminjaman.getDescription());
        mDateOfLand = findViewById(R.id.tv_detail_dl);
        mDateOfLand.setText(peminjaman.getDateOfLoan());
        mDateDue = findViewById(R.id.tv_detail_dd);
        mDateDue.setText(peminjaman.getDateDue());

        recyclerView = findViewById(R.id.rv_payment);
        FunctionPembayaran functionPembayaran =  new FunctionPembayaran(this);
        ArrayList<Pembayaran> listPembayaran = new ArrayList<>();
        listPembayaran.addAll(functionPembayaran.findIdPeminjaman(peminjaman.getIdPeminjaman()));
        PembayaranListAdapter adapter = new PembayaranListAdapter(this,listPembayaran);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(this,layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_debt_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_call:
                call();
                break;
            case R.id.action_edit:
                editPeminjaman();
                break;
            case R.id.action_delete:
                deletePeminjaman();
                break;
            case R.id.action_add_payment:
                payAmount();
                break;
                default:
                    new IllegalArgumentException("Upslala");
        }

        return super.onOptionsItemSelected(item);
    }

    public void editPeminjaman(){
        peminjaman = (Peminjaman) getIntent().getSerializableExtra("peminjaman");
        peminjaman.setIdPeminjaman(peminjaman.getIdPeminjaman());
        peminjaman.setAmount(peminjaman.getAmount());
        peminjaman.setName(peminjaman.getName());
        peminjaman.setTelphon(peminjaman.getTelphon());
        peminjaman.setDescription(peminjaman.getDescription());
        peminjaman.setDateOfLoan(peminjaman.getDateOfLoan());
        peminjaman.setDateDue(peminjaman.getDateDue());

        Intent intentEdit = new Intent(DetailActivity.this, EditPeminjamanActivity.class);
        intentEdit.putExtra("peminjaman",peminjaman);
        startActivity(intentEdit);
    }

    public void deletePeminjaman(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
                public void onClick(DialogInterface dialogInterface, int i) {
           FunctionPeminjaman functionPeminjaman = new FunctionPeminjaman(getBaseContext());
           FunctionPembayaran functionPembayaran = new FunctionPembayaran(getBaseContext());
                if (functionPeminjaman.delete(peminjaman.getIdPeminjaman())&& functionPembayaran.deleteAllId(peminjaman.getIdPeminjaman())){
                            Intent intentDelete = new Intent(DetailActivity.this,MainActivity.class);
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

    public void payAmount(){
        peminjaman = (Peminjaman) getIntent().getSerializableExtra("peminjaman");
        peminjaman.setIdPeminjaman(peminjaman.getIdPeminjaman());
        peminjaman.setAmount(peminjaman.getAmount());
        peminjaman.setName(peminjaman.getName());
        peminjaman.setTelphon(peminjaman.getTelphon());
        peminjaman.setDescription(peminjaman.getDescription());
        peminjaman.setDateOfLoan(peminjaman.getDateOfLoan());
        peminjaman.setDateDue(peminjaman.getDateDue());

        Intent intentPay = new Intent(DetailActivity.this, AddPembayaranActivity.class);
        intentPay.putExtra("peminjaman",peminjaman);
        startActivity(intentPay);
    }

    public void call(){
        peminjaman = (Peminjaman) getIntent().getSerializableExtra("peminjaman");
        String phoneNumber = peminjaman.getTelphon();
        Intent dialPhoneNumber = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phoneNumber));
        startActivity(dialPhoneNumber);
    }
}
