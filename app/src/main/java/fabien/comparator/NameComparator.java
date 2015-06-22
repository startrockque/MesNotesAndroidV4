package fabien.comparator;

import java.util.Comparator;

import fabien.modele.Note;

/**
 * Created by Fabien on 22/06/2015.
 */
public class NameComparator implements Comparator<Note> {
    @Override
    public int compare(Note lhs, Note rhs) {
        return lhs.getMatiere().compareTo(rhs.getMatiere());
    }
}
