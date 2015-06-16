package fabien.mynotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;

import fabien.bdd.NoteBDD;
import fabien.modele.Note;
import fabien.validators.NoteValidator;

public class ModifyNoteActivity extends Activity {
    private NoteBDD noteBdd;

    private int id, coeff;
    private double note;
    private String matiere;

    private FormEditText matiereF, noteF, coeffF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_note);

        noteBdd = new NoteBDD(this);

        matiereF = (FormEditText) findViewById(R.id.mod_note_matiere);
        noteF = (FormEditText) findViewById(R.id.mod_note_note);
        coeffF = (FormEditText) findViewById(R.id.mod_note_coeff);

        recupNote();

    }

    private void recupNote() {
        id = getIntent().getExtras().getInt("id");
        matiere = getIntent().getExtras().getString("matiere");
        note = getIntent().getExtras().getDouble("note");
        coeff = getIntent().getExtras().getInt("coeff");

        matiereF.setText(matiere);
        noteF.setText(String.valueOf(note));
        coeffF.setText(String.valueOf(coeff));

        noteF.addValidator(new NoteValidator(getString(R.string.add_note_error)));
    }

    public void modNote(View view) {
        boolean allValid = true;
        FormEditText[] fields = {matiereF, noteF, coeffF};
        for (FormEditText f : fields){
            allValid = f.testValidity() && allValid;
        }

        if (allValid){
            Note newNote = new Note(id, matiereF.getText().toString(), Double.valueOf(noteF.getText().toString()), Integer.valueOf(coeffF.getText().toString()));
            noteBdd.open();
            noteBdd.updateNote(id, newNote);
            noteBdd.close();
            Toast.makeText(this, "Modif OK", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);
        finish();
    }
}