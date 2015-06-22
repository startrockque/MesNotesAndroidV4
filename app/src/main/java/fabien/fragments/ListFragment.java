package fabien.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.special.ResideMenu.ResideMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fabien.activity.MainActivity;
import fabien.adapters.NoteAdapter;
import fabien.comparator.NameComparator;
import fabien.comparator.NoteComparator;
import fabien.modele.Note;
import fabien.mynotes.R;

/**
 * Created by Fabien on 17/06/2015.
 */
public class ListFragment extends Fragment implements View.OnClickListener {
    private TextView maMoyenne, annee, matiere, note, coeff;
    private NiftyDialogBuilder dialogBuilder;
    private List<Note> listeNotes = new ArrayList<>();

    private View parentView;
    private MainActivity parentActivity;
    private ResideMenu resideMenu;
    private NameComparator nameComparator;

    private boolean sortedByYear, sortedByName, sortedByNote, sortedByCoeff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.list_notes, container, false);
        dialogBuilder = NiftyDialogBuilder.getInstance(parentView.getContext());
        setUpViews();
        listeNotes = parentActivity.getListeNotes();
        setList();
        return parentView;
    }

    private void setUpViews(){
        parentActivity = (MainActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        parentActivity.loadData();
        maMoyenne = (TextView) parentView.findViewById(R.id.moyenne);

        annee = (TextView) parentView.findViewById(R.id.entete_periode);
        matiere = (TextView) parentView.findViewById(R.id.entete_matiere);
        note = (TextView) parentView.findViewById(R.id.entete_note);
        coeff = (TextView) parentView.findViewById(R.id.entete_coeff);

        annee.setOnClickListener(this);
        matiere.setOnClickListener(this);
        note.setOnClickListener(this);
        coeff.setOnClickListener(this);
    }

    public void setList() {
        NoteAdapter noteAdapter = new NoteAdapter(parentView.getContext(), listeNotes);
        final ListView listeNotesView = (ListView) parentView.findViewById(R.id.liste_notes);
        listeNotesView.setAdapter(noteAdapter);

        if (listeNotes.size() != 0) {
            String moy = String.valueOf(parentActivity.moyenne(listeNotes));
            maMoyenne.setText(getResources().getString(R.string.accueil_ma_moyenne, moy.substring(0,Math.min(6, moy.length()))));
        } else {
            maMoyenne.setText(getResources().getString(R.string.accueil_ma_moyenne, "0"));
        }

        listeNotesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                final Note note = listeNotes.get(position);

                dialogBuilder
                        .withTitle(getString(R.string.choix_mod_rem))
                        .withTitleColor(R.color.white_font)
                        .withDividerColor("#11000000")
                        .withMessage("Matiere : " + note.getMatiere() +
                                "\nNote : " + note.getNote() +
                                "\nCoeff : " + note.getCoeff() +
                                "\nAnnée : " + note.getPeriode().getAnnee() +
                                "\nSemestre : " + note.getPeriode().getSemestre())
                        .withMessageColor("#FFFFFFFF")
                        .withDialogColor("#FFE74C3C")
                        .isCancelableOnTouchOutside(true)
                        .withDuration(700)
                        .withEffect(Effectstype.Slit)
                        .withButton1Text(getString(R.string.modify))
                        .withButton2Text(getString(R.string.remove))
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle b = new Bundle();
                                b.putInt("id", note.getId());
                                b.putString("matiere", note.getMatiere());
                                b.putDouble("note", note.getNote());
                                b.putInt("coeff", note.getCoeff());
                                b.putString("annee", note.getPeriode().getAnnee());
                                b.putInt("semestre", note.getPeriode().getSemestre());
                                ModifyNoteFragment modifyNoteFragment = new ModifyNoteFragment();
                                modifyNoteFragment.setArguments(b);
                                dialogBuilder.dismiss();
                                parentActivity.changeFragment(modifyNoteFragment);
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                parentActivity.supprNote(note.getMatiere(), note.getPeriode());
                                listeNotes.remove(note);
                                parentActivity.loadData();
                                setList();
                                dialogBuilder.dismiss();
                            }
                        })
                        .show();
            }
        });

        nameComparator = new NameComparator();

        sortedByYear = false;
        sortedByName = false;
        sortedByNote = false;
        sortedByCoeff = false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.entete_periode:
                if (!sortedByYear)
                    sortByYear();
                break;
            case R.id.entete_matiere:
                if (!sortedByName)
                    sortByName();
                break;
            case R.id.entete_note:
                if (!sortedByNote)
                    sortByNote();
                break;
            case R.id.entete_coeff:
                if (!sortedByCoeff)
                    sortByCoeff();
                break;
        }
    }

    private void sortByCoeff() {
        sortedByYear = false;
        sortedByName = false;
        sortedByNote = false;
        sortedByCoeff = true;
    }

    private void sortByNote() {
        sortedByYear = false;
        sortedByName = false;
        sortedByNote = true;
        sortedByCoeff = false;
    }

    private void sortByName() {
        System.out.println("SORT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Collections.sort(listeNotes, nameComparator);
        setList();
        sortedByYear = false;
        sortedByName = true;
        sortedByNote = false;
        sortedByCoeff = false;
    }

    private void sortByYear() {
        sortedByYear = true;
        sortedByName = false;
        sortedByNote = false;
        sortedByCoeff = false;
    }
}
