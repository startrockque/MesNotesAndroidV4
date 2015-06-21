package fabien.modele;

/**
 * Created by Fabien on 21/06/2015.
 */
public class Periode {
    private String annee;
    private int semestre;

    public Periode(String annee, int semestre) {
        this.annee = annee;
        this.semestre = semestre;
    }

    public Periode() {
        annee = "";
        semestre = 0;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    @Override
    public String toString() {
        return "Periode{" +
                "annee='" + annee + '\'' +
                ", semestre=" + semestre +
                '}';
    }

    @Override
    public boolean equals(Object o){
        return (((Periode)o).getAnnee().equals(annee) && ((Periode)o).getSemestre() == semestre);
    }
}
