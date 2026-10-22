package ma.projet.service;

import java.util.Date;
import java.util.List;
import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.dao.AbstractDao;
import ma.projet.util.DateUtil;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

public class ProduitService extends AbstractDao<Produit> {

    public ProduitService() {
        super(Produit.class);
    }

    public List<Produit> findByCategorie(int categorieId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Produit p WHERE p.categorie.id = :id ORDER BY p.reference", Produit.class)
                    .setParameter("id", categorieId)
                    .getResultList();
        }
    }

    public void afficherProduitsParCategorie(int categorieId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Categorie categorie = session.get(Categorie.class, categorieId);
            if (categorie == null) {
                System.out.println("Aucune catégorie avec l'id " + categorieId);
                return;
            }
            System.out.println("Catégorie : " + categorie.getCode() + " - " + categorie.getLibelle());
            System.out.println("Liste des produits :");
            System.out.printf("%-12s%-10s%n", "Référence", "Prix");
            for (Produit p : findByCategorie(categorieId)) {
                System.out.printf("%-12s%-10s%n", p.getReference(),
                        String.format("%.0f DH", p.getPrix()));
            }
        }
    }

    public List<Produit> findProduitsCommandesEntreDates(Date dateDebut, Date dateFin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT DISTINCT l.produit FROM LigneCommandeProduit l "
                    + "WHERE l.commande.date BETWEEN :debut AND :fin ORDER BY l.produit.reference",
                    Produit.class)
                    .setParameter("debut", dateDebut)
                    .setParameter("fin", dateFin)
                    .getResultList();
        }
    }

    public void afficherProduitsCommandesEntreDates(Date dateDebut, Date dateFin) {
        System.out.println("Produits commandés entre le " + DateUtil.court(dateDebut)
                + " et le " + DateUtil.court(dateFin) + " :");
        System.out.printf("%-12s%-8s%s%n", "Référence", "Prix", "Quantité");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> lignes = session.createQuery(
                    "SELECT l.produit, SUM(l.quantite) FROM LigneCommandeProduit l "
                    + "WHERE l.commande.date BETWEEN :debut AND :fin "
                    + "GROUP BY l.produit ORDER BY l.produit.reference")
                    .setParameter("debut", dateDebut)
                    .setParameter("fin", dateFin)
                    .getResultList();
            for (Object[] ligne : lignes) {
                Produit produit = (Produit) ligne[0];
                System.out.printf("%-12s%-8s%s%n", produit.getReference(),
                        String.format("%.0f DH", produit.getPrix()), ligne[1]);
            }
        }
    }

    public void afficherProduitsDuneCommande(int commandeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Commande commande = session.get(Commande.class, commandeId);
            if (commande == null) {
                System.out.println("Aucune commande avec l'id " + commandeId);
                return;
            }
            System.out.printf("Commande : %d     Date : %s%n",
                    commande.getId(), DateUtil.longFormat(commande.getDate()));
            System.out.println("Liste des produits :");
            System.out.printf("%-12s%-8s%s%n", "Référence", "Prix", "Quantité");
            List<LigneCommandeProduit> lignes = session.createQuery(
                    "FROM LigneCommandeProduit l WHERE l.commande.id = :id ORDER BY l.produit.reference",
                    LigneCommandeProduit.class)
                    .setParameter("id", commandeId)
                    .getResultList();
            for (LigneCommandeProduit ligne : lignes) {
                System.out.printf("%-12s%-8s%s%n", ligne.getProduit().getReference(),
                        String.format("%.0f DH", ligne.getProduit().getPrix()), ligne.getQuantite());
            }
        }
    }

    public List<Produit> findByPrixSuperieur(float prix) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Produit.findByPrixSuperieur", Produit.class)
                    .setParameter("prix", prix)
                    .getResultList();
        }
    }

    public void afficherProduitsPrixSuperieurA100() {
        System.out.println("Produits dont le prix est supérieur à 100 DH (requête nommée) :");
        System.out.printf("%-12s%-10s%s%n", "Référence", "Prix", "Catégorie");
        for (Produit p : findByPrixSuperieur(100)) {
            System.out.printf("%-12s%-10s%s%n", p.getReference(),
                    String.format("%.0f DH", p.getPrix()), p.getCategorie().getCode());
        }
    }
}
