package fabien.comparator;

import java.util.Comparator;

import fabien.modele.Note;

/**
 * Created by Fabien on 22/06/2015.
 */
public class NoteComparator implements Comparator<Note> {
    @Override
    public int compare(Note lhs, Note rhs) {
        if (lhs.getNote() > rhs.getNote()){
            return -1;
        } else if (lhs.getNote() < rhs.getNote()){
            return 1;
        } else {    // si les deux notes sont égales
            if (lhs.getCoeff() > rhs.getCoeff()){
                return -1;
            } else if (lhs.getCoeff() < rhs.getCoeff()){
                return 1;
            } else { // si les deux coeffs sont égaux
                return lhs.getMatiere().compareTo(rhs.getMatiere());
            }
        }
    }
}
