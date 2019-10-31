package org.d3ifcool.debtreminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.allyants.notifyme.NotifyMe;
import java.text.DateFormat;
import java.util.Calendar;
import entities.Peminjaman;
import function.FunctionPeminjaman;

public class EditPeminjamanActivity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener, com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener{

    private EditText mName;
    private EditText mPhone;
    private EditText mAmount;
    private EditText mDescription;
    private TextView mDd;
    private Button mBtnDateDue;
    private Peminjaman peminjaman;

    Calendar now = Calendar.getInstance();
    com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd;
    com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_peminjaman);

        peminjaman = (Peminjaman) getIntent().getSerializableExtra("peminjaman");

        mName = findViewById(R.id.et_name);
        mName.setText(peminjaman.getName());

        mPhone = findViewById(R.id.et_phone);
        mPhone.setText(peminjaman.getTelphon());

        mAmount = findViewById(R.id.et_amount);
        mAmount.setText(String.valueOf(peminjaman.getAmount()));

        mDescription = findViewById(R.id.et_description);
        mDescription.setText(peminjaman.getDescription());

        mDd = findViewById(R.id.hiden);
        mDd.setText(peminjaman.getDateDue());

        dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false
        );

        mBtnDateDue = findViewById(R.id.btn_d3);
        mBtnDateDue.setText(peminjaman.getDateDue());
        mBtnDateDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                updatePeminjaman();
                break;
            default:
                new IllegalArgumentException("Upslala");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR,year);
        now.set(Calendar.MONTH,monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(now.getTime());
        mBtnDateDue.setText(currentDateString);
        mDd.setText(currentDateString);
        tpd.show(getFragmentManager(),"Timepickerdialog");
    }

    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
        now.set(Calendar.MINUTE,minute);
        now.set(Calendar.SECOND,second);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("test","I am a String");

        NotifyMe.Builder notifyMe = new NotifyMe.Builder(getApplicationContext());
        notifyMe.title("Ups sudah jatuh tempo pembayaran");
        notifyMe.content("Coba cek hutang ke "+mName.getText());
        notifyMe.color(255,0,0,255);
        notifyMe.led_color(255,255,255,255);
        notifyMe.time(now);
        notifyMe.delay(0);
        notifyMe.large_icon(R.mipmap.ic_launcher_round);
        notifyMe.addAction(intent,"Snooze",false);
        notifyMe.addAction(new Intent(),"Dismiss",true,false);
        notifyMe.addAction(intent,"done");
        notifyMe.build();
    }

    public void updatePeminjaman(){
        FunctionPeminjaman functionPeminjaman = new FunctionPeminjaman(getBaseContext());
        peminjaman.setName(mName.getText().toString().trim());
        peminjaman.setTelphon(mPhone.getText().toString().trim());
        peminjaman.setAmount(Integer.parseInt(mAmount.getText().toString().trim()));
        peminjaman.setDescription(mDescription.getText().toString().trim());
        peminjaman.setDateDue(mDd.getText().toString().trim());

        if (TextUtils.isEmpty(mName.getText().toString().trim())||
                TextUtils.isEmpty(mAmount.getText().toString().trim())||
                TextUtils.isEmpty(mPhone.getText().toString().trim())||
                TextUtils.isEmpty(mDd.getText().toString().trim())||
                TextUtils.isEmpty(mDescription.getText().toString().trim())) {
            Toast.makeText(this, "please compleated!", Toast.LENGTH_SHORT).show();
        }else if(functionPeminjaman.Update(peminjaman)){
            Intent intentUpdate = new Intent(EditPeminjamanActivity.this, DetailActivity.class);
            intentUpdate.putExtra("peminjaman",peminjaman);
            Toast.makeText(EditPeminjamanActivity.this, "Data berhasil diedit", Toast.LENGTH_SHORT).show();
            startActivity(intentUpdate);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

}
