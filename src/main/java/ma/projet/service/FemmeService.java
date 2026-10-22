package ma.projet.service;

import java.util.Date;
import java.util.List;
import ma.projet.beans.Femme;
import ma.projet.dao.AbstractDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

public class FemmeService extends AbstractDao<Femme> {

    public FemmeService() {
        super(Femme.class);
    }

    public int nombreEnfantsEntreDates(int femmeId, Date dateDebut, Date dateFin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Object resultat = session.createNamedQuery("Femme.nombreEnfantsEntreDates")
                    .setParameter("femmeId", femmeId)
                    .setParameter("dateDebut", dateDebut)
                    .setParameter("dateFin", dateFin)
                    .getSingleResult();
            return resultat == null ? 0 : ((Number) resultat).intValue();
        }
    }

    public List<Femme> femmesMarieesAuMoinsDeuxFois() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Femme.marieesAuMoinsDeuxFois", Femme.class)
                    .getResultList();
        }
    }

    public Femme femmeLaPlusAgee() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Femme> femmes = session.createQuery(
                    "FROM Femme f ORDER BY f.dateNaissance ASC", Femme.class)
                    .setMaxResults(1)
                    .getResultList();
            return femmes.isEmpty() ? null : femmes.get(0);
        }
    }
}
