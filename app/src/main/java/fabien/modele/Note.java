package fabien.modele;

public class Note {
    private int id;
    private String matiere;
    private double note;
    private int coeff;
    private Periode periode;

    public Note(int id, String matiere, double note, int coeff, Periode periode) {
        this.id = id;
        this.matiere = matiere;
        this.note = note;
        this.coeff = coeff;
        this.periode = periode;
    }

    public Note() {
        id = 0;
        matiere = "";
        note = 0;
        coeff = 0;
        periode = new Periode();
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

    public Periode getPeriode() {
        return periode;
    }

    public void setPeriode(Periode periode) {
        this.periode = periode;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", matiere='" + matiere + '\'' +
                ", note=" + note +
                ", coeff=" + coeff +
                ", periode=" + periode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return (((Note)o).getMatiere().equals(matiere) && ((Note)o).getPeriode().equals(periode));
    }
}
