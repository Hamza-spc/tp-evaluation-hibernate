package ma.projet.service;

import java.util.List;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.AbstractDao;
import ma.projet.util.DateUtil;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

public class ProjetService extends AbstractDao<Projet> {

    public ProjetService() {
        super(Projet.class);
    }

    public void afficherTachesPlanifiees(int projetId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Projet projet = session.get(Projet.class, projetId);
            if (projet == null) {
                System.out.println("Aucun projet avec l'id " + projetId);
                return;
            }
            System.out.printf("Projet : %d      Nom : %s     Date début : %s%n",
                    projet.getId(), projet.getNom(), DateUtil.longFormat(projet.getDateDebut()));
            System.out.println("Liste des tâches planifiées:");
            System.out.printf("%-6s%-16s%-24s%s%n", "Num", "Nom", "Date Début Planifiée",
                    "Date Fin Planifiée");
            List<Tache> taches = session.createQuery(
                    "FROM Tache t WHERE t.projet.id = :id ORDER BY t.id", Tache.class)
                    .setParameter("id", projetId)
                    .getResultList();
            for (Tache t : taches) {
                System.out.printf("%-6s%-16s%-24s%s%n", t.getId(), t.getNom(),
                        DateUtil.court(t.getDateDebut()), DateUtil.court(t.getDateFin()));
            }
        }
    }

    public void afficherTachesRealisees(int projetId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Projet projet = session.get(Projet.class, projetId);
            if (projet == null) {
                System.out.println("Aucun projet avec l'id " + projetId);
                return;
            }
            System.out.printf("Projet : %d      Nom : %s     Date début : %s%n",
                    projet.getId(), projet.getNom(), DateUtil.longFormat(projet.getDateDebut()));
            System.out.println("Liste des tâches:");
            System.out.printf("%-6s%-16s%-20s%s%n", "Num", "Nom", "Date Début Réelle",
                    "Date Fin Réelle");
            List<EmployeTache> affectations = session.createQuery(
                    "FROM EmployeTache et WHERE et.tache.projet.id = :id ORDER BY et.tache.id",
                    EmployeTache.class)
                    .setParameter("id", projetId)
                    .getResultList();
            for (EmployeTache et : affectations) {
                System.out.printf("%-6s%-16s%-20s%s%n", et.getTache().getId(),
                        et.getTache().getNom(), DateUtil.court(et.getDateDebutReelle()),
                        DateUtil.court(et.getDateFinReelle()));
            }
        }
    }
}
