package ma.projet.test;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;
import ma.projet.util.DateUtil;
import ma.projet.util.HibernateUtil;

public class Exercice2Test {

    public static void main(String[] args) {
        EmployeService employeService = new EmployeService();
        ProjetService projetService = new ProjetService();
        TacheService tacheService = new TacheService();
        EmployeTacheService employeTacheService = new EmployeTacheService();

        Employe safi = new Employe("SAFI", "SAID", "0600000001");
        Employe rami = new Employe("RAMI", "AMINE", "0600000002");
        Employe alami = new Employe("ALAMI", "NADIA", "0600000003");
        employeService.create(safi);
        employeService.create(rami);
        employeService.create(alami);

        Projet siteWeb = new Projet("Site web", DateUtil.date("02", "01", "2013"),
                DateUtil.date("30", "06", "2013"), safi);
        Projet application = new Projet("Application mobile", DateUtil.date("05", "02", "2013"),
                DateUtil.date("31", "08", "2013"), safi);
        Projet reseau = new Projet("Refonte réseau", DateUtil.date("01", "03", "2013"),
                DateUtil.date("30", "09", "2013"), alami);
        Projet gestionStock = new Projet("Gestion de stock", DateUtil.date("14", "01", "2013"),
                DateUtil.date("31", "12", "2013"), safi);
        projetService.create(siteWeb);
        projetService.create(application);
        projetService.create(reseau);
        projetService.create(gestionStock);

        Tache analyse = new Tache("Analyse", DateUtil.date("01", "02", "2013"),
                DateUtil.date("28", "02", "2013"), 1500, gestionStock);
        Tache conception = new Tache("Conception", DateUtil.date("01", "03", "2013"),
                DateUtil.date("31", "03", "2013"), 2000, gestionStock);
        Tache developpement = new Tache("Développement", DateUtil.date("01", "04", "2013"),
                DateUtil.date("31", "05", "2013"), 3000, gestionStock);
        Tache maquette = new Tache("Maquette", DateUtil.date("10", "01", "2013"),
                DateUtil.date("25", "01", "2013"), 800, siteWeb);
        Tache deploiement = new Tache("Déploiement", DateUtil.date("01", "06", "2013"),
                DateUtil.date("30", "06", "2013"), 600, siteWeb);
        tacheService.create(analyse);
        tacheService.create(conception);
        tacheService.create(developpement);
        tacheService.create(maquette);
        tacheService.create(deploiement);

        employeTacheService.create(new EmployeTache(safi, analyse,
                DateUtil.date("10", "02", "2013"), DateUtil.date("20", "02", "2013")));
        employeTacheService.create(new EmployeTache(rami, conception,
                DateUtil.date("10", "03", "2013"), DateUtil.date("15", "03", "2013")));
        employeTacheService.create(new EmployeTache(safi, developpement,
                DateUtil.date("10", "04", "2013"), DateUtil.date("25", "04", "2013")));
        employeTacheService.create(new EmployeTache(alami, maquette,
                DateUtil.date("12", "01", "2013"), DateUtil.date("22", "01", "2013")));

        System.out.println("===== Tâches réalisées par un employé =====");
        employeService.afficherTachesRealiseesParEmploye(safi.getId());

        System.out.println();
        System.out.println("===== Projets gérés par un employé =====");
        employeService.afficherProjetsGeresParEmploye(safi.getId());

        System.out.println();
        System.out.println("===== Tâches planifiées pour un projet =====");
        projetService.afficherTachesPlanifiees(gestionStock.getId());

        System.out.println();
        System.out.println("===== Tâches réalisées avec les dates réelles =====");
        projetService.afficherTachesRealisees(gestionStock.getId());

        System.out.println();
        System.out.println("===== Tâches dont le prix est supérieur à 1000 DH =====");
        tacheService.afficherTachesPrixSuperieurA1000();

        System.out.println();
        System.out.println("===== Tâches réalisées entre deux dates =====");
        tacheService.afficherTachesRealiseesEntreDates(
                DateUtil.date("01", "01", "2013"), DateUtil.date("31", "12", "2013"));

        HibernateUtil.shutdown();
    }
}
