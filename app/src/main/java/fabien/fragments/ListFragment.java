package fabien.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.special.ResideMenu.ResideMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fabien.activity.MainActivity;
import fabien.adapters.NoteAdapter;
import fabien.comparator.CoeffComparator;
import fabien.comparator.NameComparator;
import fabien.comparator.NoteComparator;
import fabien.comparator.YearComparator;
import fabien.modele.Note;
import fabien.mynotes.R;

/**
 * Created by Fabien on 17/06/2015.
 */
public class ListFragment extends Fragment implements View.OnClickListener {
    private TextView maMoyenne, annee, matiere, note, coeff, emptyView;
    private NiftyDialogBuilder dialogBuilder;
    private Spinner spinnerAnnee, spinnerSemestre, spinnerMatiere;
    private ListView listeNotesView;
    private ProgressBar loadingView;

    private ArrayAdapter<String> yearAdapter, matiereAdapter;
    private ArrayAdapter<String> semesterAdapter;

    private List<Note> listeNotes = new ArrayList<>();
    private ArrayList<Note> listeToDisplay = new ArrayList<>();
    private List<String> listYears = new ArrayList<>();
    private List<String> listSemesters = new ArrayList<>();
    private List<String> listMatieres = new ArrayList<>();

    private View parentView;
    private MainActivity parentActivity;
    private ResideMenu resideMenu;

    private YearComparator yearComparator;
    private NameComparator nameComparator;
    private NoteComparator noteComparator;
    private CoeffComparator coeffComparator;
    private boolean sortedByYear, sortedByName, sortedByNote, sortedByCoeff;

    private Populate populate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.list_notes, container, false);
        dialogBuilder = NiftyDialogBuilder.getInstance(parentView.getContext());
        parentActivity = (MainActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        parentActivity.loadData();
        populate = new Populate();

        setList();

        setUpViews();

        populate.execute(listeToDisplay);

        return parentView;
    }


    public void setList() {
        listeNotes = parentActivity.getListeNotes();
        listeToDisplay.addAll(listeNotes);

        listYears = parentActivity.getAllYears();
        listSemesters = parentActivity.getAllSemesters();
        listMatieres = parentActivity.getAllMatieres();

        nameComparator = new NameComparator();
        yearComparator = new YearComparator();
        noteComparator = new NoteComparator();
        coeffComparator = new CoeffComparator();

        sortedByYear = false;
        sortedByName = false;
        sortedByNote = false;
        sortedByCoeff = false;
    }

    private void setUpViews() {
        loadingView = (ProgressBar) parentView.findViewById(R.id.loading_view);
        emptyView = (TextView) parentView.findViewById(R.id.empty_view);

        maMoyenne = (TextView) parentView.findViewById(R.id.moyenne);

        if (listeToDisplay.size() != 0) {
            String moy = String.valueOf(parentActivity.moyenne(listeToDisplay));
            maMoyenne.setText(getResources().getString(R.string.accueil_ma_moyenne, moy.substring(0, Math.min(6, moy.length()))));
        } else {
            maMoyenne.setText(getResources().getString(R.string.accueil_ma_moyenne, "0"));
        }

        listeNotesView = (ListView) parentView.findViewById(R.id.liste_notes);

        annee = (TextView) parentView.findViewById(R.id.entete_periode);
        matiere = (TextView) parentView.findViewById(R.id.entete_matiere);
        note = (TextView) parentView.findViewById(R.id.entete_note);
        coeff = (TextView) parentView.findViewById(R.id.entete_coeff);

        spinnerAnnee = (Spinner) parentView.findViewById(R.id.spinner_année);
        spinnerSemestre = (Spinner) parentView.findViewById(R.id.spinner_semestre);
        spinnerMatiere = (Spinner) parentView.findViewById(R.id.spinner_matiere);

        refreshList();
    }

    private void refreshList() {
        listeNotesView.setAdapter(new NoteAdapter(parentView.getContext(), listeToDisplay));
    }

    private void setUpAdapters() {
        yearAdapter = new ArrayAdapter<>(parentActivity, android.R.layout.simple_dropdown_item_1line, listYears);
        semesterAdapter = new ArrayAdapter<>(parentActivity, android.R.layout.simple_dropdown_item_1line, listSemesters);
        matiereAdapter = new ArrayAdapter<>(parentActivity, android.R.layout.simple_dropdown_item_1line, listMatieres);

        yearAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        semesterAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        matiereAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinnerAnnee.setAdapter(yearAdapter);
        spinnerSemestre.setAdapter(semesterAdapter);
        spinnerMatiere.setAdapter(matiereAdapter);

    }

    private void setUpListeners() {
        listeNotesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                final Note note = listeToDisplay.get(position);

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
                                listeToDisplay.remove(note);
                                parentActivity.loadData();
                                setUpAdapters();
                                dialogBuilder.dismiss();
                            }
                        })
                        .show();
            }
        });

        annee.setOnClickListener(this);
        matiere.setOnClickListener(this);
        note.setOnClickListener(this);
        coeff.setOnClickListener(this);

        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortBySpinners(spinnerAnnee.getSelectedItemPosition(), spinnerSemestre.getSelectedItemPosition(), spinnerMatiere.getSelectedItemPosition());
//                ((Spinner) view).setSelection(position);
                refreshList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        spinnerAnnee.setOnItemSelectedListener(onItemSelectedListener);
        spinnerSemestre.setOnItemSelectedListener(onItemSelectedListener);
        spinnerMatiere.setOnItemSelectedListener(onItemSelectedListener);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        refreshList();
    }

    private void sortByCoeff() {
        Collections.sort(listeToDisplay, coeffComparator);
        sortedByYear = false;
        sortedByName = false;
        sortedByNote = false;
        sortedByCoeff = true;
    }

    private void sortByNote() {
        Collections.sort(listeToDisplay, noteComparator);
        sortedByYear = false;
        sortedByName = false;
        sortedByNote = true;
        sortedByCoeff = false;
    }

    private void sortByName() {
        Collections.sort(listeToDisplay, nameComparator);
        sortedByYear = false;
        sortedByName = true;
        sortedByNote = false;
        sortedByCoeff = false;
    }

    private void sortByYear() {
        Collections.sort(listeToDisplay, yearComparator);
        sortedByYear = true;
        sortedByName = false;
        sortedByNote = false;
        sortedByCoeff = false;
    }


    private void sortBySpinners(int year, int semester, int matiere) {
        populate.showLoading(listeNotesView, loadingView, emptyView);
        listeToDisplay.clear();
        ArrayList<Note> tmp = new ArrayList<>();
        if (!listYears.get(year).equals(getString(R.string.allyears))) {
            for (Note n : listeNotes) {
                if (n.getPeriode().getAnnee().equals(listYears.get(year)))
                    tmp.add(n);
            }
        } else {
            tmp.addAll(listeNotes);
        }

        listeToDisplay.addAll(tmp);
        if (!listSemesters.get(semester).equals(getString(R.string.allsemesters))) {
            for (Note n : listeToDisplay) {
                if (!(String.valueOf(n.getPeriode().getSemestre()).equals(listSemesters.get(semester))))
                    tmp.remove(n);
            }
        }

        listeToDisplay.clear();
        listeToDisplay.addAll(tmp);
        if (!listMatieres.get(matiere).equals(getString(R.string.allyears))) {
            for (Note n : listeToDisplay) {
                if (!n.getMatiere().equals(listMatieres.get(matiere)))
                    tmp.remove(n);
            }
        }

        listeToDisplay = tmp;

        if (listeToDisplay.size() <=0){
            populate.showEmptyText(listeNotesView, loadingView, emptyView);
        } else {
            populate.showContent(listeNotesView, loadingView, emptyView);
        }
    }




    private class Populate extends AsyncTask<ArrayList<Note>, Void, Void>{
        public void showLoading(View content_view,View mLoadingView,View mEmptyView){
            content_view.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }

        public void showContent(View content_view,View mLoadingView,View mEmptyView){
            content_view.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
        }

        public void showEmptyText(View content_view,View mLoadingView,View mEmptyView){
            content_view.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPreExecute(){
            //show loading indicator
            showLoading(listeNotesView, loadingView, emptyView);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(ArrayList<Note>... params){
            setUpAdapters();

            setUpListeners();

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled()){
                if(listeToDisplay.size()<=0){
                    showEmptyText(listeNotesView, loadingView, emptyView);
                } else {
                    showContent(listeNotesView,loadingView,emptyView);
                }
            }
            super.onPostExecute(result);
        }
    }
}