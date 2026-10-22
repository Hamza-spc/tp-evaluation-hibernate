package ma.projet.service;

import java.util.List;
import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.dao.AbstractDao;
import ma.projet.util.DateUtil;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

public class EmployeService extends AbstractDao<Employe> {

    public EmployeService() {
        super(Employe.class);
    }

    public void afficherTachesRealiseesParEmploye(int employeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Employe employe = session.get(Employe.class, employeId);
            if (employe == null) {
                System.out.println("Aucun employé avec l'id " + employeId);
                return;
            }
            System.out.println("Employé : " + employe.getNomComplet());
            System.out.println("Liste des tâches réalisées :");
            System.out.printf("%-6s%-16s%-14s%s%n", "Num", "Nom", "Date Début", "Date Fin");
            List<EmployeTache> affectations = session.createQuery(
                    "FROM EmployeTache et WHERE et.employe.id = :id ORDER BY et.tache.id",
                    EmployeTache.class)
                    .setParameter("id", employeId)
                    .getResultList();
            for (EmployeTache et : affectations) {
                System.out.printf("%-6s%-16s%-14s%s%n", et.getTache().getId(),
                        et.getTache().getNom(), DateUtil.court(et.getDateDebutReelle()),
                        DateUtil.court(et.getDateFinReelle()));
            }
        }
    }

    public void afficherProjetsGeresParEmploye(int employeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Employe employe = session.get(Employe.class, employeId);
            if (employe == null) {
                System.out.println("Aucun employé avec l'id " + employeId);
                return;
            }
            System.out.println("Employé : " + employe.getNomComplet());
            System.out.println("Liste des projets gérés :");
            System.out.printf("%-6s%-20s%-14s%s%n", "Num", "Nom", "Date Début", "Date Fin");
            List<Projet> projets = session.createQuery(
                    "FROM Projet p WHERE p.chefDeProjet.id = :id ORDER BY p.id", Projet.class)
                    .setParameter("id", employeId)
                    .getResultList();
            for (Projet p : projets) {
                System.out.printf("%-6s%-20s%-14s%s%n", p.getId(), p.getNom(),
                        DateUtil.court(p.getDateDebut()), DateUtil.court(p.getDateFin()));
            }
        }
    }
}
