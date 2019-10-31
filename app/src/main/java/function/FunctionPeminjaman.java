package function;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.DBHelper;
import entities.Peminjaman;

public class FunctionPeminjaman {

    private DBHelper dbHelper;

    public FunctionPeminjaman(Context context){
        dbHelper = new DBHelper(context);
    }

    public List<Peminjaman> findAll(){
        try {
            List<Peminjaman> peminjamans = new ArrayList<>();
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM peminjaman",null);
            if (cursor.moveToFirst()){
                do {
                    Peminjaman peminjaman = new Peminjaman();
                    peminjaman.setIdPeminjaman(cursor.getInt(0));
                    peminjaman.setName(cursor.getString(1));
                    peminjaman.setTelphon(cursor.getString(2));
                    peminjaman.setAmount(cursor.getInt(3));
                    peminjaman.setDescription(cursor.getString(4));
                    peminjaman.setDateOfLoan(cursor.getString(5));
                    peminjaman.setDateDue(cursor.getString(6));
                    peminjamans.add(peminjaman);
                }while (cursor.moveToNext());
            }
            sqLiteDatabase.close();
            return  peminjamans;
        }catch (Exception ex){
            return null;
        }
    }

    public boolean create(Peminjaman peminjaman){
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            ContentValues contentValues= new ContentValues();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
            String strDate = sdf.format(new Date());

            contentValues.put("name", peminjaman.getName());
            contentValues.put("telphon", peminjaman.getTelphon());
            contentValues.put("amount", peminjaman.getAmount());
            contentValues.put("description", peminjaman.getDescription());
            contentValues.put("dateOfLoan", strDate);
            contentValues.put("dateDue", peminjaman.getDateDue());
            long rows = sqLiteDatabase.insert("peminjaman",null, contentValues);
            sqLiteDatabase.close();
            return rows > 0;
        }catch (Exception ex){
            return false;
        }
    }

    public boolean delete(int idPeminjaman){
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            int rows = sqLiteDatabase.delete("peminjaman","idPeminjaman = ?",new String[]{String.valueOf(idPeminjaman)});
            sqLiteDatabase.close();
            return rows>0;
        }catch (Exception ex){
            return false;
        }
    }

    public boolean Update(Peminjaman peminjaman){
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", peminjaman.getName());
            contentValues.put("telphon", peminjaman.getTelphon());
            contentValues.put("amount", peminjaman.getAmount());
            contentValues.put("description", peminjaman.getDescription());
            contentValues.put("dateDue", peminjaman.getDateDue());
            int rows = sqLiteDatabase.update("peminjaman",contentValues,"idPeminjaman = ?", new String[]{String.valueOf(peminjaman.getIdPeminjaman())});
            sqLiteDatabase.close();
            return rows > 0;
        }catch (Exception ex){
            return false;
        }
    }
}
