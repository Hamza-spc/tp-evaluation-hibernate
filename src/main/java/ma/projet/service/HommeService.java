package ma.projet.service;

import java.util.Date;
import java.util.List;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.AbstractDao;
import ma.projet.util.DateUtil;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

public class HommeService extends AbstractDao<Homme> {

    public HommeService() {
        super(Homme.class);
    }

    public List<Mariage> findMariagesEntreDates(int hommeId, Date dateDebut, Date dateFin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Mariage m WHERE m.homme.id = :id AND m.dateDebut BETWEEN :debut AND :fin "
                    + "ORDER BY m.dateDebut", Mariage.class)
                    .setParameter("id", hommeId)
                    .setParameter("debut", dateDebut)
                    .setParameter("fin", dateFin)
                    .getResultList();
        }
    }

    public void afficherEpousesEntreDates(int hommeId, Date dateDebut, Date dateFin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Homme homme = session.get(Homme.class, hommeId);
            if (homme == null) {
                System.out.println("Aucun homme avec l'id " + hommeId);
                return;
            }
            System.out.println("Épouses de " + homme.getNom() + " " + homme.getPrenom()
                    + " mariées entre le " + DateUtil.court(dateDebut)
                    + " et le " + DateUtil.court(dateFin) + " :");
            System.out.printf("%-14s%-14s%-14s%s%n", "Nom", "Prénom", "Date Début", "Date Fin");
            for (Mariage m : findMariagesEntreDates(hommeId, dateDebut, dateFin)) {
                System.out.printf("%-14s%-14s%-14s%s%n", m.getFemme().getNom(),
                        m.getFemme().getPrenom(), DateUtil.court(m.getDateDebut()),
                        m.getDateFin() == null ? "En cours" : DateUtil.court(m.getDateFin()));
            }
        }
    }
}
