package fabien.mynotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import fabien.bdd.NoteBDD;
import fabien.modele.Note;
import fabien.validators.NoteValidator;

/**
 * Created by Fabien on 10/06/2015.
 */
public class AddNoteActivity extends Activity implements View.OnClickListener {
    private NoteBDD noteBdd;
    private FormEditText matiere, note, coeff;
    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemReset;
    private ResideMenuItem itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);
        noteBdd = new NoteBDD(this);

        matiere = (FormEditText) findViewById(R.id.add_note_matiere);
        note = (FormEditText) findViewById(R.id.add_note_note);
        coeff = (FormEditText) findViewById(R.id.add_note_coeff);

        note.addValidator(new NoteValidator(getString(R.string.add_note_error)));

        setUpMenu();
    }

    public void addNewNote(View view) {
        boolean allValid = true;
        FormEditText[] formEditTexts = {matiere, note, coeff};
        for(FormEditText f : formEditTexts){
            allValid = f.testValidity() && allValid;
        }

        if (allValid) {
            noteBdd.open();
            noteBdd.insertNote(new Note(0, matiere.getText().toString(), Double.valueOf(note.getText().toString()), Integer.valueOf(coeff.getText().toString())));
            noteBdd.close();
            Toast.makeText(this, getResources().getString(R.string.add_ok), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
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
        }
        resideMenu.closeMenu();
    }

    public ResideMenu getResideMenu(){
        return resideMenu;
    }


    // FIN DU MENU

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);
        finish();
    }
}
