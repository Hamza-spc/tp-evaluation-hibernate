package ma.projet.test;

import java.util.List;
import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.DateUtil;
import ma.projet.util.HibernateUtil;

public class Exercice3Test {

    public static void main(String[] args) {
        HommeService hommeService = new HommeService();
        FemmeService femmeService = new FemmeService();
        MariageService mariageService = new MariageService();

        Femme salima = new Femme("RAMI", "SALIMA", "0610000001", "Casablanca",
                DateUtil.date("15", "04", "1968"));
        Femme amal = new Femme("ALI", "AMAL", "0610000002", "Rabat",
                DateUtil.date("03", "09", "1972"));
        Femme wafa = new Femme("ALAOUI", "WAFA", "0610000003", "Fès",
                DateUtil.date("22", "11", "1975"));
        Femme karima = new Femme("ALAMI", "KARIMA", "0610000004", "Marrakech",
                DateUtil.date("05", "06", "1965"));
        Femme hanane = new Femme("BENANI", "HANANE", "0610000005", "Tanger",
                DateUtil.date("10", "10", "1980"));
        Femme lalla = new Femme("IDRISSI", "LALLA", "0610000006", "Agadir",
                DateUtil.date("07", "07", "1978"));
        Femme nadia = new Femme("CHRAIBI", "NADIA", "0610000007", "Meknès",
                DateUtil.date("01", "01", "1985"));
        Femme sana = new Femme("BERADA", "SANA", "0610000008", "Oujda",
                DateUtil.date("12", "12", "1988"));
        Femme imane = new Femme("TAZI", "IMANE", "0610000009", "Kénitra",
                DateUtil.date("20", "03", "1990"));
        Femme ghita = new Femme("MORSI", "GHITA", "0610000010", "Tétouan",
                DateUtil.date("25", "08", "1992"));
        Femme[] femmes = {salima, amal, wafa, karima, hanane, lalla, nadia, sana, imane, ghita};
        for (Femme femme : femmes) {
            femmeService.create(femme);
        }

        Homme said = new Homme("SAFI", "SAID", "0620000001", "Casablanca",
                DateUtil.date("01", "01", "1965"));
        Homme omar = new Homme("BENNANI", "OMAR", "0620000002", "Rabat",
                DateUtil.date("05", "03", "1970"));
        Homme karim = new Homme("TAZI", "KARIM", "0620000003", "Fès",
                DateUtil.date("15", "09", "1975"));
        Homme younes = new Homme("ALAOUI", "YOUNES", "0620000004", "Marrakech",
                DateUtil.date("20", "12", "1980"));
        Homme hicham = new Homme("IDRISSI", "HICHAM", "0620000005", "Tanger",
                DateUtil.date("03", "04", "1985"));
        Homme[] hommes = {said, omar, karim, younes, hicham};
        for (Homme homme : hommes) {
            hommeService.create(homme);
        }

        mariageService.create(new Mariage(said, karima, DateUtil.date("03", "09", "1989"),
                DateUtil.date("03", "09", "1990"), 0));
        mariageService.create(new Mariage(said, salima, DateUtil.date("03", "09", "1990"),
                null, 4));
        mariageService.create(new Mariage(said, amal, DateUtil.date("03", "09", "1995"),
                null, 2));
        mariageService.create(new Mariage(said, wafa, DateUtil.date("04", "11", "2000"),
                null, 3));
        mariageService.create(new Mariage(omar, karima, DateUtil.date("15", "06", "1995"),
                DateUtil.date("10", "01", "2000"), 1));
        mariageService.create(new Mariage(omar, imane, DateUtil.date("14", "07", "2003"),
                DateUtil.date("01", "01", "2006"), 0));
        mariageService.create(new Mariage(karim, hanane, DateUtil.date("01", "05", "2005"),
                null, 0));
        mariageService.create(new Mariage(karim, lalla, DateUtil.date("12", "09", "2010"),
                null, 2));
        mariageService.create(new Mariage(younes, nadia, DateUtil.date("20", "02", "2012"),
                null, 1));
        mariageService.create(new Mariage(hicham, sana, DateUtil.date("08", "08", "2015"),
                null, 0));
        mariageService.create(new Mariage(hicham, ghita, DateUtil.date("01", "06", "2018"),
                null, 1));
        mariageService.create(new Mariage(karim, ghita, DateUtil.date("15", "03", "2020"),
                null, 1));

        System.out.println("===== Liste des femmes =====");
        System.out.printf("%-4s%-14s%-14s%-16s%-14s%s%n", "Id", "Nom", "Prénom", "Téléphone",
                "Adresse", "Date Naissance");
        for (Femme femme : femmeService.findAll()) {
            System.out.printf("%-4s%-14s%-14s%-16s%-14s%s%n", femme.getId(), femme.getNom(),
                    femme.getPrenom(), femme.getTelephone(), femme.getAdresse(),
                    DateUtil.court(femme.getDateNaissance()));
        }

        System.out.println();
        System.out.println("===== Femme la plus âgée =====");
        Femme agee = femmeService.femmeLaPlusAgee();
        if (agee != null) {
            System.out.println("Nom : " + agee.getNom() + " " + agee.getPrenom()
                    + "    Date Naissance : " + DateUtil.court(agee.getDateNaissance()));
        }

        System.out.println();
        System.out.println("===== Épouses d'un homme entre deux dates =====");
        hommeService.afficherEpousesEntreDates(said.getId(),
                DateUtil.date("01", "01", "1985"), DateUtil.date("31", "12", "2005"));

        System.out.println();
        System.out.println("===== Nombre d'enfants d'une femme entre deux dates "
                + "(requête native nommée) =====");
        System.out.println("Femme : " + salima.getNom() + " " + salima.getPrenom()
                + "    Nombre d'enfants entre le 01/01/1990 et le 31/12/1991 : "
                + femmeService.nombreEnfantsEntreDates(salima.getId(),
                        DateUtil.date("01", "01", "1990"), DateUtil.date("31", "12", "1991")));

        System.out.println();
        System.out.println("===== Femmes mariées au moins deux fois (requête nommée) =====");
        List<Femme> mariees = femmeService.femmesMarieesAuMoinsDeuxFois();
        System.out.printf("%-4s%-14s%-14s%s%n", "Id", "Nom", "Prénom", "Date Naissance");
        for (Femme femme : mariees) {
            System.out.printf("%-4s%-14s%-14s%s%n", femme.getId(), femme.getNom(),
                    femme.getPrenom(), DateUtil.court(femme.getDateNaissance()));
        }

        System.out.println();
        System.out.println("===== Hommes mariés à quatre femmes entre deux dates "
                + "(API Criteria) =====");
        mariageService.afficherNombreHommesMariesAQuatreFemmes(
                DateUtil.date("01", "01", "1985"), DateUtil.date("31", "12", "2005"));

        System.out.println();
        System.out.println("===== Mariages d'un homme avec tous les détails =====");
        mariageService.afficherMariagesDunHomme(said.getId());

        HibernateUtil.shutdown();
    }
}
