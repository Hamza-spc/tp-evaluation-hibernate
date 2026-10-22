package ma.projet.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "femme")
@NamedQuery(name = "Femme.marieesAuMoinsDeuxFois",
        query = "SELECT m.femme FROM Mariage m GROUP BY m.femme HAVING COUNT(m) >= 2")
@NamedNativeQuery(name = "Femme.nombreEnfantsEntreDates",
        query = "SELECT COALESCE(SUM(m.nbr_enfant), 0) FROM mariage m "
                + "WHERE m.femme_id = :femmeId AND m.date_debut BETWEEN :dateDebut AND :dateFin")
public class Femme extends Personne {

    @OneToMany(mappedBy = "femme")
    private List<Mariage> mariages = new ArrayList<>();

    public Femme() {
    }

    public Femme(String nom, String prenom, String telephone, String adresse, Date dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }

    public List<Mariage> getMariages() {
        return mariages;
    }

    public void setMariages(List<Mariage> mariages) {
        this.mariages = mariages;
    }

    @Override
    public String toString() {
        return "Nom : " + nom + " " + prenom;
    }
}
