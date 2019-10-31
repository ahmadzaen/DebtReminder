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
import entities.Pembayaran;

public class FunctionPembayaran {

    private DBHelper dbHelper;

    public FunctionPembayaran(Context context){
        dbHelper = new DBHelper(context);
    }

    public List<Pembayaran> findIdPeminjaman(int idPeminjaman){
        try {
            List<Pembayaran> pembayarans = new ArrayList<>();
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM pembayaran  WHERE idPeminjaman = ?",new String[]{String.valueOf(idPeminjaman)});
            if (cursor.moveToFirst()){
                do {
                    Pembayaran pembayaran = new Pembayaran();
                    pembayaran.setIdPembayaran(cursor.getInt(0));
                    pembayaran.setIdPeminjaman(cursor.getInt(1));
                    pembayaran.setDatePay(cursor.getString(2));
                    pembayaran.setPay(cursor.getInt(3));
                    pembayaran.setDescription(cursor.getString(4));
                    pembayarans.add(pembayaran);
                }while (cursor.moveToNext());
            }
            sqLiteDatabase.close();
            return  pembayarans;
        }catch (Exception ex){
            return null;
        }
    }

    public boolean create(Pembayaran pembayaran){
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            ContentValues contentValues= new ContentValues();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
            String strDate = sdf.format(new Date());

            contentValues.put("idPeminjaman", pembayaran.getIdPeminjaman());
            contentValues.put("datePay", strDate);
            contentValues.put("pay", pembayaran.getPay());
            contentValues.put("description", pembayaran.getDescription());
            long rows = sqLiteDatabase.insert("pembayaran",null, contentValues);
            sqLiteDatabase.close();
            return rows > 0;
        }catch (Exception ex){
            return false;
        }
    }

    public boolean delete(int idPembayaran){
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            int rows = sqLiteDatabase.delete("pembayaran","idPembayaran = ?",new String[]{String.valueOf(idPembayaran)});
            sqLiteDatabase.close();
            return rows>0;
        }catch (Exception ex){
            return false;
        }
    }

    public boolean deleteAllId(int idPeminjaman){
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            int rows = sqLiteDatabase.delete("pembayaran","idPeminjaman = ?", new String[]{String.valueOf(idPeminjaman)});
            sqLiteDatabase.close();
            return rows>0;
        }catch (Exception ex){
            return false;
        }
    }

    public boolean Update(Pembayaran pembayaran){
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("pay", pembayaran.getPay());
            contentValues.put("description", pembayaran.getDescription());
            int rows = sqLiteDatabase.update("pembayaran",contentValues,"idPembayaran = ?", new String[]{String.valueOf(pembayaran.getIdPembayaran())});
            sqLiteDatabase.close();
            return rows > 0;
        }catch (Exception ex){
            return false;
        }
    }
}
