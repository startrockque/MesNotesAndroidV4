package fabien.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fabien.modele.Note;
import fabien.mynotes.R;
import fabien.object.CircleTransform;


/**
 * Created by Fabien on 06/06/2015.
 */
public class NoteAdapter extends BaseAdapter {
    List<Note> listeNotes;
    LayoutInflater inflater;
    Context context;

    public NoteAdapter(Context c, List<Note> listeNotes) {
        inflater = LayoutInflater.from(c);
        this.listeNotes = listeNotes;
        context = c;
    }

    @Override
    public int getCount() {
        return listeNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return listeNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    private class ViewHolder{
        TextView matiere;
        TextView note;
        TextView coeff;
        ImageView annee;
        TextView semestre;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.note, null);
            holder.matiere = (TextView) view.findViewById(R.id.nom_matiere);
            holder.note = (TextView) view.findViewById(R.id.note);
            holder.coeff = (TextView) view.findViewById(R.id.coefficient);
            holder.annee = (ImageView) view.findViewById(R.id.logo_annee);
            holder.semestre = (TextView) view.findViewById(R.id.miniature_semestre);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.matiere.setText(listeNotes.get(position).getMatiere());
        holder.note.setText(String.valueOf(listeNotes.get(position).getNote()));
        holder.coeff.setText(String.valueOf(listeNotes.get(position).getCoeff()));

        String an = listeNotes.get(position).getPeriode().getAnnee();
        Bitmap icon;
        switch (an){
            case "Licence 1":
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_l1);
                break;
            case "Licence 2":
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_l2);
                break;
            case "Licence 3":
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_l3);
                break;
            case "Master 1":
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_m1);
                break;
            default :
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_m2);
                break;
        }
        holder.annee.setImageBitmap(new CircleTransform().transform(icon));
        holder.semestre.setText(String.valueOf(listeNotes.get(position).getPeriode().getSemestre()));
        return view;
    }
}