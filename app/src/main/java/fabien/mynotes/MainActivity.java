package fabien.mynotes;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.ArrayList;
import java.util.List;

import fabien.bdd.NoteBDD;
import fabien.modele.Note;

/**
 * Created by Fabien on 10/06/2015.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private List<Note> listeNotes = new ArrayList<>();
    private NoteBDD noteBdd;


    private MainActivity mContext;
    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemReset;
    private ResideMenuItem itemList;
    private ResideMenuItem itemAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        noteBdd = new NoteBDD(this);
        loadData();

        mContext = this;
        setUpMenu();
        if( savedInstanceState == null )
            changeFragment(new HomeFragment());
    }

    public void loadData() {
        noteBdd.open();
        listeNotes = noteBdd.getAllRaws();
        noteBdd.close();
    }

    public double moyenne(List<Note> listeNotes) {
        double totalNote = 0;
        int totalCoeff = 0;
        for (Note n : listeNotes){
            totalNote += n.getNote() * n.getCoeff();
            totalCoeff += n.getCoeff();
        }
        return totalNote/totalCoeff;
    }

    public void goToAddNote(View view) {
        changeFragment(new AddNoteFragment());
        overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
    }

    public void addNewNote(Note note) {
        noteBdd.open();
        noteBdd.insertNote(note);
        noteBdd.close();
        changeFragment(new ListFragment());
    }

    public void modNote(Note note) {
            noteBdd.open();
            noteBdd.updateNote(note.getId(), note);
            noteBdd.close();
    }

    public void supprNote(String nom){
        noteBdd.open();
        noteBdd.removeNoteWithName(nom);
        noteBdd.close();
    }

    public List<Note> getListeNotes() {
        return listeNotes;
    }

    public void setListeNotes(List<Note> listeNotes) {
        this.listeNotes = listeNotes;
    }



    /*-------------- DEBUT DU MENU------------------ */
    public void setUpMenu() {
        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome = new ResideMenuItem(this, R.drawable.icon_home, getString(R.string.menu_accueil));
        itemList = new ResideMenuItem(this, R.drawable.icon_list, getString(R.string.menu_liste));
        itemAdd = new ResideMenuItem(this, R.drawable.icon_add, getString(R.string.menu_add));
        itemReset = new ResideMenuItem(this, R.drawable.icon_reset, getString(R.string.menu_reset));

        itemHome.setOnClickListener(this);
        itemList.setOnClickListener(this);
        itemAdd.setOnClickListener(this);
        itemReset.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemList, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAdd, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemReset, ResideMenu.DIRECTION_LEFT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
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
        if (view == itemHome){
            changeFragment(new HomeFragment());
        }else if (view == itemReset){
            noteBdd.dropTable();
            loadData();
            changeFragment(new HomeFragment());
        }else if (view == itemList){
            changeFragment(new ListFragment());
        }else if (view == itemAdd){
            changeFragment(new AddNoteFragment());
        }
        resideMenu.closeMenu();
    }

    public void changeFragment(android.support.v4.app.Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public ResideMenu getResideMenu(){
        return resideMenu;
    }
}