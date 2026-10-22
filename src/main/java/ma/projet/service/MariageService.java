package ma.projet.service;

import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.AbstractDao;
import ma.projet.util.DateUtil;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

public class MariageService extends AbstractDao<Mariage> {

    public MariageService() {
        super(Mariage.class);
    }

    public int nombreHommesMariesAQuatreFemmes(Date dateDebut, Date dateFin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<Mariage> mariage = cq.from(Mariage.class);
            cq.select(mariage.get("homme").get("id"));
            cq.where(cb.between(mariage.get("dateDebut"), dateDebut, dateFin));
            cq.groupBy(mariage.get("homme").get("id"));
            cq.having(cb.equal(cb.countDistinct(mariage.get("femme").get("id")), 4L));
            return session.createQuery(cq).getResultList().size();
        }
    }

    public void afficherNombreHommesMariesAQuatreFemmes(Date dateDebut, Date dateFin) {
        System.out.println("Nombre d'hommes mariés à quatre femmes entre le "
                + DateUtil.court(dateDebut) + " et le " + DateUtil.court(dateFin)
                + " (API Criteria) : " + nombreHommesMariesAQuatreFemmes(dateDebut, dateFin));
    }

    public void afficherMariagesDunHomme(int hommeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Homme homme = session.get(Homme.class, hommeId);
            if (homme == null) {
                System.out.println("Aucun homme avec l'id " + hommeId);
                return;
            }
            List<Mariage> mariages = session.createQuery(
                    "FROM Mariage m WHERE m.homme.id = :id ORDER BY m.dateDebut", Mariage.class)
                    .setParameter("id", hommeId)
                    .getResultList();

            System.out.println("Nom : " + homme.getNom() + " " + homme.getPrenom());

            System.out.println("Mariages En Cours :");
            int numero = 1;
            for (Mariage m : mariages) {
                if (m.getDateFin() == null) {
                    System.out.printf("%d. Femme : %-18sDate Début : %s    Nbr Enfants : %d%n",
                            numero++, m.getFemme().getPrenom() + " " + m.getFemme().getNom(),
                            DateUtil.court(m.getDateDebut()), m.getNbrEnfant());
                }
            }

            System.out.println();
            System.out.println("Mariages échoués :");
            numero = 1;
            for (Mariage m : mariages) {
                if (m.getDateFin() != null) {
                    System.out.printf("%d. Femme : %-18sDate Début : %s    %n",
                            numero++, m.getFemme().getPrenom() + " " + m.getFemme().getNom(),
                            DateUtil.court(m.getDateDebut()));
                    System.out.printf("Date Fin : %s    Nbr Enfants : %d%n",
                            DateUtil.court(m.getDateFin()), m.getNbrEnfant());
                }
            }
        }
    }
}
