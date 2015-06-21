package fabien.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.special.ResideMenu.ResideMenu;

import java.util.ArrayList;
import java.util.List;

import fabien.activity.MainActivity;
import fabien.adapters.NoteAdapter;
import fabien.modele.Note;
import fabien.mynotes.R;

/**
 * Created by Fabien on 17/06/2015.
 */
public class ListFragment extends Fragment {
    private TextView maMoyenne;
    private NiftyDialogBuilder dialogBuilder;
    private List<Note> listeNotes = new ArrayList<>();

    private View parentView;
    private MainActivity parentActivity;
    private ResideMenu resideMenu;

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
    }
}
