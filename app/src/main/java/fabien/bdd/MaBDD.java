package fabien.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Fabien on 11/06/2015.
 */
public class MaBDD extends SQLiteOpenHelper {
    private static final String TABLE_NOTES = "table_des_notes";
    private static final String COL_ID = "ID";
    private static final String COL_MATIERE = "matiere";
    private static final String COL_NOTE = "note";
    private static final String COL_COEFF = "coeff";

    private static final String CREATE_BDD = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_MATIERE + " TEXT NOT NULL, "
            + COL_NOTE + " INTEGER NOT NULL, "
            + COL_COEFF + " INTEGER NOT NULL);";

    private static final String DROP_BDD = "DELETE FROM "+ TABLE_NOTES +";";

    public MaBDD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Se lance seulement à la création
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // NOTHING
    }

    public void dropTable(SQLiteDatabase db) {
        db.execSQL(DROP_BDD);
    }
}
