package ma.projet.service;

import java.util.Date;
import java.util.List;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Tache;
import ma.projet.dao.AbstractDao;
import ma.projet.util.DateUtil;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

public class TacheService extends AbstractDao<Tache> {

    public TacheService() {
        super(Tache.class);
    }

    public List<Tache> findByPrixSuperieur(double prix) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Tache.findByPrixSuperieur", Tache.class)
                    .setParameter("prix", prix)
                    .getResultList();
        }
    }

    public void afficherTachesPrixSuperieurA1000() {
        System.out.println("Tâches dont le prix est supérieur à 1000 DH (requête nommée) :");
        System.out.printf("%-6s%-16s%-14s%n", "Num", "Nom", "Prix");
        for (Tache t : findByPrixSuperieur(1000)) {
            System.out.printf("%-6s%-16s%s%n", t.getId(), t.getNom(),
                    String.format("%.0f DH", t.getPrix()));
        }
    }

    public void afficherTachesRealiseesEntreDates(Date dateDebut, Date dateFin) {
        System.out.println("Tâches réalisées entre le " + DateUtil.court(dateDebut)
                + " et le " + DateUtil.court(dateFin) + " :");
        System.out.printf("%-6s%-16s%-20s%s%n", "Num", "Nom", "Date Début Réelle", "Date Fin Réelle");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<EmployeTache> affectations = session.createQuery(
                    "FROM EmployeTache et WHERE et.dateDebutReelle BETWEEN :debut AND :fin "
                    + "ORDER BY et.tache.id", EmployeTache.class)
                    .setParameter("debut", dateDebut)
                    .setParameter("fin", dateFin)
                    .getResultList();
            for (EmployeTache et : affectations) {
                System.out.printf("%-6s%-16s%-20s%s%n", et.getTache().getId(),
                        et.getTache().getNom(), DateUtil.court(et.getDateDebutReelle()),
                        DateUtil.court(et.getDateFinReelle()));
            }
        }
    }
}
