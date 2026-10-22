package ma.projet.test;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.service.CategorieService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.service.ProduitService;
import ma.projet.util.DateUtil;
import ma.projet.util.HibernateUtil;

public class Exercice1Test {

    public static void main(String[] args) {
        CategorieService categorieService = new CategorieService();
        ProduitService produitService = new ProduitService();
        CommandeService commandeService = new CommandeService();
        LigneCommandeService ligneCommandeService = new LigneCommandeService();

        Categorie informatique = new Categorie("INF", "Informatique");
        Categorie bureautique = new Categorie("BUR", "Bureautique");
        categorieService.create(informatique);
        categorieService.create(bureautique);

        Produit es12 = new Produit("ES12", 120f, informatique);
        Produit zr85 = new Produit("ZR85", 100f, informatique);
        Produit ee85 = new Produit("EE85", 200f, informatique);
        Produit hp01 = new Produit("HP01", 4500f, informatique);
        Produit bu12 = new Produit("BU12", 90f, bureautique);
        produitService.create(es12);
        produitService.create(zr85);
        produitService.create(ee85);
        produitService.create(hp01);
        produitService.create(bu12);

        Commande commande1 = new Commande(DateUtil.date("05", "01", "2013"));
        Commande commande2 = new Commande(DateUtil.date("10", "02", "2013"));
        Commande commande3 = new Commande(DateUtil.date("20", "02", "2013"));
        Commande commande4 = new Commande(DateUtil.date("14", "03", "2013"));
        commandeService.create(commande1);
        commandeService.create(commande2);
        commandeService.create(commande3);
        commandeService.create(commande4);

        ligneCommandeService.create(ligne(commande1, bu12, 3));
        ligneCommandeService.create(ligne(commande2, hp01, 1));
        ligneCommandeService.create(ligne(commande2, es12, 2));
        ligneCommandeService.create(ligne(commande3, zr85, 5));
        ligneCommandeService.create(ligne(commande4, es12, 7));
        ligneCommandeService.create(ligne(commande4, zr85, 14));
        ligneCommandeService.create(ligne(commande4, ee85, 5));

        System.out.println("===== Produits de la catégorie Informatique =====");
        produitService.afficherProduitsParCategorie(informatique.getId());

        System.out.println();
        System.out.println("===== Produits commandés dans la commande "
                + commande4.getId() + " =====");
        produitService.afficherProduitsDuneCommande(commande4.getId());

        System.out.println();
        System.out.println("===== Produits commandés entre deux dates =====");
        produitService.afficherProduitsCommandesEntreDates(
                DateUtil.date("01", "01", "2013"), DateUtil.date("31", "03", "2013"));

        System.out.println();
        System.out.println("===== Produits dont le prix est supérieur à 100 DH =====");
        produitService.afficherProduitsPrixSuperieurA100();

        HibernateUtil.shutdown();
    }

    private static LigneCommandeProduit ligne(Commande commande, Produit produit, int quantite) {
        LigneCommandeProduit ligne = new LigneCommandeProduit(produit, quantite);
        ligne.setCommande(commande);
        return ligne;
    }
}
