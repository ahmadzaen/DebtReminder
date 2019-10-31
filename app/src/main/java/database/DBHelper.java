package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "debt";
    private static final int DATABASE_VERSION = 10;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.w("Version","Currect version is "+ sqLiteDatabase.getVersion());
        String peminjaman = "CREATE TABLE peminjaman (idPeminjaman INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "name VARCHAR,"+
                "telphon VARCHAR,"+
                "amount INTEGER,"+
                "description TEXT,"+
                "dateOfLoan DATETIME,"+
                "dateDue DATETIME)";

        String pembayaran = "CREATE TABLE pembayaran (idPembayaran INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "idPeminjaman INTEGER,"+
                "datePay DATETIME,"+
                "pay INTEGER,"+
                "description TEXT)";

        sqLiteDatabase.execSQL(peminjaman);
        sqLiteDatabase.execSQL(pembayaran);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w("Update DB", "DB IS UPDATE TO "+sqLiteDatabase.getVersion());
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS peminjaman");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS pembayaran");
        onCreate(sqLiteDatabase);
    }
}
