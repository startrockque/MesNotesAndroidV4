package fabien.mynotes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.andreabaccega.widget.FormEditText;
import com.special.ResideMenu.ResideMenu;

import fabien.modele.Note;
import fabien.validators.NoteValidator;

public class ModifyNoteFragment extends Fragment implements View.OnClickListener{
    private int id, coeff;
    private double note;
    private String matiere;

    private FormEditText matiereF, noteF, coeffF;
    private Button button;

    private View parentView;
    private ResideMenu resideMenu;
    private MainActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.modify_note, container, false);
        parentActivity = (MainActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        setUpViews();
        return parentView;
    }

    private void setUpViews(){
        matiereF = (FormEditText) parentView.findViewById(R.id.mod_note_matiere);
        noteF = (FormEditText) parentView.findViewById(R.id.mod_note_note);
        coeffF = (FormEditText) parentView.findViewById(R.id.mod_note_coeff);

        button = (Button) parentView.findViewById(R.id.button_mod);
        button.setOnClickListener(this);
        recupNote();
    }

    private void recupNote() {
        id = getArguments().getInt("id");
        matiere = getArguments().getString("matiere");
        note = getArguments().getDouble("note");
        coeff = getArguments().getInt("coeff");

        matiereF.setText(matiere);
        noteF.setText(String.valueOf(note));
        coeffF.setText(String.valueOf(coeff));

        noteF.addValidator(new NoteValidator(getString(R.string.add_note_error)));
    }

    @Override
    public void onClick(View v) {
        boolean allValid = true;
        FormEditText[] fields = {matiereF, noteF, coeffF};
        for (FormEditText f : fields){
            allValid = f.testValidity() && allValid;
        }

        if (allValid)
            parentActivity.modNote(new Note(id, matiereF.getText().toString(), Double.valueOf(noteF.getText().toString()), Integer.valueOf(coeffF.getText().toString())));
    }
}