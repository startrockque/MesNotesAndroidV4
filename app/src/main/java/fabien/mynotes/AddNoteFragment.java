package fabien.mynotes;

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

import fabien.modele.Note;
import fabien.modele.Periode;
import fabien.validators.NoteValidator;

/**
 * Created by Fabien on 10/06/2015.
 */
public class AddNoteFragment extends Fragment implements View.OnClickListener{
    private FormEditText matiere, note, coeff;
    private Spinner annee;
    private RadioButton radio1, radio2;
    private Button btn;

    private View parentView;
    private MainActivity parentActivity;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.add_note, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews(){
        parentActivity = (MainActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        matiere = (FormEditText) parentView.findViewById(R.id.add_note_matiere);
        note = (FormEditText) parentView.findViewById(R.id.add_note_note);
        coeff = (FormEditText) parentView.findViewById(R.id.add_note_coeff);
        annee = (Spinner) parentView.findViewById(R.id.add_note_spinner);
        radio1 = (RadioButton) parentView.findViewById(R.id.radio1);
        radio2 = (RadioButton) parentView.findViewById(R.id.radio2);
        btn = (Button) parentView.findViewById(R.id.button_add);

        btn.setOnClickListener(this);

        note.addValidator(new NoteValidator(getString(R.string.add_note_error)));

        List<String> annees = new ArrayList<>();
        annees.add(getString(R.string.l1));
        annees.add(getString(R.string.l2));
        annees.add(getString(R.string.l3));
        annees.add(getString(R.string.m1));
        annees.add(getString(R.string.m2));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(parentActivity, android.R.layout.simple_dropdown_item_1line, annees);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        annee.setAdapter(spinnerAdapter);
    }

    private void addNewNote() {
        boolean allValid = true;
        FormEditText[] formEditTexts = {matiere, note, coeff};
        for(FormEditText f : formEditTexts){
            allValid = f.testValidity() && allValid;
        }

        if (allValid) {
            int sem = 0;
            if (radio1.isChecked()){
                sem = 1;
            } else if (radio2.isChecked()){
                sem = 2;
            }
            if (sem != 0) {
                Periode periode = new Periode(annee.getSelectedItem().toString(), sem);
                parentActivity.addNewNote(new Note(0, matiere.getText().toString(), Double.valueOf(note.getText().toString()), Integer.valueOf(coeff.getText().toString()), periode));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn){
            addNewNote();
        }
    }
}
