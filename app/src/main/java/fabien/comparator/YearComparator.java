package fabien.comparator;

import java.util.Comparator;

import fabien.modele.Note;

/**
 * Created by Fabien on 22/06/2015.
 */
public class YearComparator implements Comparator<Note> {
    @Override
    public int compare(Note lhs, Note rhs) {
        int res = lhs.getPeriode().getAnnee().compareTo(rhs.getPeriode().getAnnee());
        if (res == 0){  //si c'est la même année on différencie au semestre
            if (lhs.getPeriode().getSemestre() > rhs.getPeriode().getSemestre()){
                res = 1;
            } else if (lhs.getPeriode().getSemestre() < rhs.getPeriode().getSemestre()){
                res = -1;
            }
        }
        if (res == 0){  // si c'est aussi le même semestre on différencie au nom
            res = lhs.getMatiere().compareTo(rhs.getMatiere());
        }
        return res;
    }
}
