package fabien.mynotes;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.ArrayList;
import java.util.List;

import fabien.adapters.NoteAdapter;
import fabien.bdd.NoteBDD;
import fabien.modele.Note;

/**
 * Created by Fabien on 10/06/2015.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private List<Note> listeNotes = new ArrayList<>();
    private NoteBDD noteBdd;
    private TextView maMoyenne;
    private NiftyDialogBuilder dialogBuilder;
    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemReset;
    private ResideMenuItem itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        maMoyenne = (TextView) findViewById(R.id.moyenne);

        noteBdd = new NoteBDD(this);
        loadData();


        dialogBuilder = NiftyDialogBuilder.getInstance(MainActivity.this);
        setList();

        setUpMenu();
    }

    private void loadData() {
        noteBdd.open();
        listeNotes = noteBdd.getAllRaws();
        noteBdd.close();
    }

    private void setList() {
        NoteAdapter noteAdapter = new NoteAdapter(this, listeNotes);
        final ListView listeNotesView = (ListView) findViewById(R.id.liste_notes);
        listeNotesView.setAdapter(noteAdapter);

        if (listeNotes.size() != 0) {
            String moy = String.valueOf(moyenne(listeNotes)).substring(0,6);
            maMoyenne.setText(getResources().getString(R.string.accueil_ma_moyenne, moy));
        } else {
            maMoyenne.setText(getResources().getString(R.string.accueil_ma_moyenne, "0"));
        }

        listeNotesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Note note = listeNotes.get(position);

                dialogBuilder
                        .withTitle(getString(R.string.choix_mod_rem))
                        .withTitleColor(R.color.white_font)
                        .withDividerColor("#11000000")
                        .withMessage("Matiere : " + note.getMatiere() +
                                "\nNote : " + note.getNote() +
                                "\nCoeff : " + note.getCoeff())
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
                                Intent intent = new Intent(getApplicationContext(), ModifyNoteActivity.class);
                                intent.putExtra("id", note.getId());
                                intent.putExtra("matiere", note.getMatiere());
                                intent.putExtra("note", note.getNote());
                                intent.putExtra("coeff", note.getCoeff());
                                startActivity(intent);
                                overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
                                finish();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                supprNote(note.getMatiere());
                                loadData();
                                setList();
                                dismiss();
                            }
                        })
                        .show();
            }
        });
    }


    // DEBUT DU MENU
    private void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome = new ResideMenuItem(this, R.drawable.icon_home, getString(R.string.menu_accueil));
        itemList = new ResideMenuItem(this, R.drawable.icon_list, getString(R.string.menu_liste));
        itemReset = new ResideMenuItem(this, R.drawable.icon_reset, getString(R.string.menu_reset));

        itemHome.setOnClickListener(this);
        itemList.setOnClickListener(this);
        itemReset.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemList, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemReset, ResideMenu.DIRECTION_LEFT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view == itemReset){
            noteBdd.dropTable();
            loadData();
            setList();
        }
        resideMenu.closeMenu();
    }

    public ResideMenu getResideMenu(){
        return resideMenu;
    }


    // FIN DU MENU

    private void dismiss() {
        dialogBuilder.dismiss();
    }

    private double moyenne(List<Note> listeNotes) {
        double totalNote = 0;
        int totalCoeff = 0;
        for (Note n : listeNotes){
            totalNote += n.getNote() * n.getCoeff();
            totalCoeff += n.getCoeff();
        }
        return totalNote/totalCoeff;
    }

    public void addNote(View view) {
        startActivity(new Intent(getApplicationContext(), AddNoteActivity.class));
        overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
        finish();
    }

    public void supprNote(String nom){
        noteBdd.open();
        noteBdd.removeNoteWithName(nom);
        noteBdd.close();
    }
}