package fabien.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import fabien.modele.Note;
import fabien.modele.Periode;

/**
 * Created by Fabien on 11/06/2015.
 */
public class NoteBDD {
    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "eleves.db";

    private static final String TABLE_NOTES = "table_des_notes";
    private static final String COL_ID = "ID";
    private static final String COL_MATIERE = "Matiere";
    private static final String COL_NOTE = "Note";
    private static final String COL_COEFF = "Coeff";
    private static final String COL_ANNEE = "Annee";
    private static final String COL_SEM = "Semestre";
    public static final String[] COLUMNS = new String[]{COL_ID, COL_MATIERE, COL_NOTE, COL_COEFF, COL_ANNEE, COL_SEM};

    private static final int NUM_COL_ID = 0;
    private static final int NUM_COL_MATIERE = 1;
    private static final int NUM_COL_NOTE = 2;
    private static final int NUM_COL_COEFF = 3;
    private static final int NUM_COL_ANNEE = 4;
    private static final int NUM_COL_SEM = 5;


    private SQLiteDatabase bdd;

    private MaBDD maBDD;

    // Crée la BDD et la table
    public NoteBDD(Context context){
        maBDD = new MaBDD(context, NOM_BDD, null, VERSION_BDD);
    }

    // Ouvre la BDD en écriture
    public void open(){
        bdd = maBDD.getWritableDatabase();
    }

    public void close(){
        maBDD.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertNote(Note note){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_MATIERE, note.getMatiere());
        values.put(COL_NOTE, note.getNote());
        values.put(COL_COEFF, note.getCoeff());
        values.put(COL_ANNEE, note.getPeriode().getAnnee());
        values.put(COL_SEM, note.getPeriode().getSemestre());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_NOTES, null, values);
    }

    public int updateNote(int id, Note note){
        //La mise à jour d'une note dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simple préciser quelle note on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_MATIERE, note.getMatiere());
        values.put(COL_NOTE, note.getNote());
        values.put(COL_COEFF, note.getCoeff());
        values.put(COL_ANNEE, note.getPeriode().getAnnee());
        values.put(COL_SEM, note.getPeriode().getSemestre());
        return bdd.update(TABLE_NOTES, values, COL_ID + " = " + id, null);
    }

    public void removeNoteWithName(String nom, Periode periode){
        //Suppression d'une note de la BDD grâce à l'ID
        bdd.execSQL("DELETE FROM "+TABLE_NOTES+" WHERE "+COL_MATIERE+ " LIKE \"" + nom + "\" AND " + COL_ANNEE + " = \"" +periode.getAnnee() + "\" AND " + COL_SEM + " = " +periode.getSemestre());
//        return bdd.delete(TABLE_NOTES, COL_MATIERE + " LIKE \"" + nom + "\"", null);
    }

    public Note getNoteWithNom(String nom){
        //Récupère dans un Cursor les valeur correspondant à une note contenue dans la BDD
        //(ici on sélectionne la note grâce à la matière)
        Cursor c = bdd.query(TABLE_NOTES, COLUMNS, COL_MATIERE + " LIKE \"" + nom + "\"", null, null, null, null);
        return cursorToNote(c);
    }

    public Note getNoteWithId(int i) {
        Cursor c = bdd.rawQuery("SELECT * FROM table_des_notes WHERE ID="+ i, null);
        return cursorToNote(c);
    }

    //Cette méthode permet de convertir un cursor en une note
    private Note cursorToNote(Cursor c){
       //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé une note
        Note note = new Note();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        note.setId(c.getInt(NUM_COL_ID));
        note.setMatiere(c.getString(NUM_COL_MATIERE));
        note.setNote(c.getDouble(NUM_COL_NOTE));
        note.setCoeff(c.getInt(NUM_COL_COEFF));
        note.setPeriode(new Periode(c.getString(NUM_COL_ANNEE), c.getInt(NUM_COL_SEM)));
        //On ferme le cursor
        c.close();

        //On retourne la note
        return note;
    }

    public long getTaille(){
        SQLiteStatement s = bdd.compileStatement("select count(*) from " + TABLE_NOTES);
        return s.simpleQueryForLong();
    }

    public void dropTable() {
        open();
        maBDD.dropTable(bdd);
        close();
    }

    public List<Note> getAllRaws() {
        Cursor c = bdd.rawQuery("select * from "+ TABLE_NOTES, null);
        List<Note> notes = new ArrayList<>();
        if (c .moveToFirst()) {
            while (!c.isAfterLast()) {
                Note note = new Note();
                note.setId(c.getInt(NUM_COL_ID));
                note.setMatiere(c.getString(NUM_COL_MATIERE));
                note.setNote(c.getDouble(NUM_COL_NOTE));
                note.setCoeff(c.getInt(NUM_COL_COEFF));
                note.setPeriode(new Periode(c.getString(NUM_COL_ANNEE), c.getInt(NUM_COL_SEM)));
                notes.add(note);
                c.moveToNext();
            }
        }
        return notes;
    }
}
