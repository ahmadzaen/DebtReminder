package org.d3ifcool.debtreminder;

import android.app.PendingIntent;
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
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.util.Calendar;
import entities.Peminjaman;
import function.FunctionPeminjaman;

public class AddActivity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener, com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {
    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextAmount;
    private EditText editTextDescription;
    private TextView textViewD3;
    private Button buttonAddD3;

    Calendar now = Calendar.getInstance();
    com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd;
    com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTextName = findViewById(R.id.edit_text_name);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextAmount = findViewById(R.id.edit_text_amount);
        editTextDescription = findViewById(R.id.edit_text_description);
        textViewD3 = findViewById(R.id.textViewD3);
        textViewD3.setVisibility(View.GONE);

        // TODO: 08/04/2019 back buttonq 
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                AddActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                AddActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false
        );

        buttonAddD3 = findViewById(R.id.button_d3);
        buttonAddD3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                insertPeminjaman();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertPeminjaman(){


        FunctionPeminjaman functionPeminjaman = new FunctionPeminjaman(getBaseContext());
        Peminjaman peminjaman = new Peminjaman();
        peminjaman.setName(editTextName.getText().toString().trim());
        peminjaman.setTelphon(editTextPhone.getText().toString().trim());
        peminjaman.setAmount(Integer.parseInt(editTextAmount.getText().toString()));
        peminjaman.setDescription(editTextDescription.getText().toString().trim());
        peminjaman.setDateDue(textViewD3.getText().toString().trim());

        if (TextUtils.isEmpty(editTextName.getText().toString().trim())||
                TextUtils.isEmpty(editTextAmount.getText().toString().trim())||
                TextUtils.isEmpty(editTextPhone.getText().toString().trim())||
                TextUtils.isEmpty(textViewD3.getText().toString().trim())||
                TextUtils.isEmpty(editTextDescription.getText().toString().trim())){
            Toast.makeText(this, "please compleated!", Toast.LENGTH_SHORT).show();
        }else if (functionPeminjaman.create(peminjaman)){
            Intent intentAdd = new Intent(AddActivity.this, MainActivity.class);
            Toast.makeText(AddActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
            startActivity(intentAdd);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR,year);
        now.set(Calendar.MONTH,monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(now.getTime());
        buttonAddD3.setText(currentDateString);
        textViewD3.setText(currentDateString);
        tpd.show(getFragmentManager(),"Timepickerdialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
        now.set(Calendar.MINUTE,minute);
        now.set(Calendar.SECOND,second);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("test","I am a String");

        NotifyMe.Builder notifyMe = new NotifyMe.Builder(getApplicationContext());

        notifyMe.title("Ups sudah jatuh tempo pembayaran");
        notifyMe.content("Coba cek hutang ke "+editTextName.getText());
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
}
