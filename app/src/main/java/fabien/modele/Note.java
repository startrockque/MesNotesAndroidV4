package fabien.modele;

public class Note {
    private int id;
    private String matiere;
    private double note;
    private int coeff;

    public Note(int id, String matiere, double note, int coeff) {
        this.id = id;
        this.matiere = matiere;
        this.note = note;
        this.coeff = coeff;
    }

    public Note() {
        id = 0;
        matiere = "";
        note = 0;
        coeff = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public int getCoeff() {
        return coeff;
    }

    public void setCoeff(int coeff) {
        this.coeff = coeff;
    }

    @Override
    public String toString() {
        return "Note{" +
                "matiere='" + matiere + '\'' +
                ", note=" + note +
                ", coeff=" + coeff +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return ((Note)o).getMatiere().equals(matiere);
    }
}
