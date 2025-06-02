package pokemon;

public class Pokemon {
    private String nom;
    private String type;
    private int pv;

    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setPv(int pv) {
        this.pv = pv;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getNom() {
        return nom;
    }
    public int getPv() {
        return pv;
    }
    public String getType() {
        return type;
    }
    @Override
    public String toString() {
        return nom + " (" + pv + ") [" + type + "]";
    }
}