package fabien.mynotes;

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

/**
 * Created by Fabien on 10/06/2015.
 */
public class AddNoteFragment extends Fragment implements View.OnClickListener{
    private FormEditText matiere, note, coeff;
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
        btn = (Button) parentView.findViewById(R.id.button_add);

        btn.setOnClickListener(this);

        note.addValidator(new NoteValidator(getString(R.string.add_note_error)));
    }

    private void addNewNote() {
        boolean allValid = true;
        FormEditText[] formEditTexts = {matiere, note, coeff};
        for(FormEditText f : formEditTexts){
            allValid = f.testValidity() && allValid;
        }

        if (allValid) {
            parentActivity.addNewNote(new Note(0, matiere.getText().toString(), Double.valueOf(note.getText().toString()), Integer.valueOf(coeff.getText().toString())));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn){
            addNewNote();
        }
    }
}
