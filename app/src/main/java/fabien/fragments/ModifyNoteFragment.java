package fabien.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.andreabaccega.widget.FormEditText;
import com.special.ResideMenu.ResideMenu;

import java.util.ArrayList;
import java.util.List;

import fabien.activity.MainActivity;
import fabien.modele.Note;
import fabien.modele.Periode;
import fabien.mynotes.R;
import fabien.validators.NoteValidator;

public class ModifyNoteFragment extends Fragment implements View.OnClickListener{
    private int id, coeff;
    private double note;
    private String matiere;

    private FormEditText matiereF, noteF, coeffF;
    private Spinner annee;
    private RadioButton radio1, radio2;
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
        annee = (Spinner) parentView.findViewById(R.id.mod_note_spinner);
        radio1 = (RadioButton) parentView.findViewById(R.id.radio11);
        radio2 = (RadioButton) parentView.findViewById(R.id.radio12);

        button = (Button) parentView.findViewById(R.id.button_mod);
        button.setOnClickListener(this);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(parentActivity, android.R.layout.simple_dropdown_item_1line, parentActivity.getAllYears());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        annee.setAdapter(spinnerAdapter);
        String value = getArguments().getString("annee");
        if (!value.equals(null)) {
            int spinnerPostion = spinnerAdapter.getPosition(value);
            annee.setSelection(spinnerPostion);
        }
        recupNote();
    }

    private void recupNote() {
        id = getArguments().getInt("id");
        matiere = getArguments().getString("matiere");
        note = getArguments().getDouble("note");
        coeff = getArguments().getInt("coeff");
        int sem = getArguments().getInt("semestre");
        if (sem == 1){
            radio1.setChecked(true);
        } else {
            radio2.setChecked(true);
        }

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

        if (allValid) {
            System.out.println("TOTO VA A LA PLAGE");
            int sem = 0;
            if (radio1.isChecked()) {
                sem = 1;
            } else if (radio2.isChecked()) {
                sem = 2;
            }
            if (sem != 0) {
                Periode periode = new Periode(annee.getSelectedItem().toString(), sem);
                parentActivity.modNote(new Note(id, matiereF.getText().toString(), Double.valueOf(noteF.getText().toString()), Integer.valueOf(coeffF.getText().toString()), periode));
            }
        }
    }
}