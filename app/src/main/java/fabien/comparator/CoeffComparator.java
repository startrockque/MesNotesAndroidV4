package fabien.comparator;

import java.util.Comparator;

import fabien.modele.Note;

/**
 * Created by Fabien on 22/06/2015.
 */
public class CoeffComparator implements Comparator<Note> {
    @Override
    public int compare(Note lhs, Note rhs) {
        if (lhs.getCoeff() > rhs.getCoeff()){
            return -1;
        } else if (lhs.getCoeff() < rhs.getCoeff()){
            return 1;
        } else {
            return lhs.getMatiere().compareTo(rhs.getMatiere());
        }
    }
}
