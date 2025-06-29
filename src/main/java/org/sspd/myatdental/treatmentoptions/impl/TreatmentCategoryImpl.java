package org.sspd.myatdental.treatmentoptions.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.dto.DataAccessObject;
import org.sspd.myatdental.treatmentoptions.model.TreatmentCategory;

import java.util.List;

@Repository
public class TreatmentCategoryImpl implements DataAccessObject<TreatmentCategory> {

    private final SessionFactory sessionFactory;

    public TreatmentCategoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<TreatmentCategory> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<TreatmentCategory> query = session.createQuery("FROM TreatmentCategory", TreatmentCategory.class);
            return query.getResultList();
        } catch (Exception e) {
            // Log the exception (e.g., using SLF4J or another logging framework)
            throw new RuntimeException("Error retrieving all treatment categories", e);
        }
    }

    @Override
    public boolean save(TreatmentCategory treatmentCategory) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(treatmentCategory);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            // Log the exception
            throw new RuntimeException("Error saving treatment category", e);
        }
    }

    @Override
    public boolean delete(TreatmentCategory treatmentCategory) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(session.merge(treatmentCategory));
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            // Log the exception
            throw new RuntimeException("Error deleting treatment category", e);
        }
    }

    @Override
    public boolean update(TreatmentCategory treatmentCategory) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(treatmentCategory);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            // Log the exception
            throw new RuntimeException("Error updating treatment category", e);
        }
    }

    @Override
    public TreatmentCategory findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(TreatmentCategory.class, id);
        } catch (Exception e) {
            // Log the exception
            throw new RuntimeException("Error finding treatment category by ID: " + id, e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            TreatmentCategory treatmentCategory = session.find(TreatmentCategory.class, id);
            if (treatmentCategory != null) {
                session.remove(treatmentCategory);
                session.getTransaction().commit();
                return true;
            }
            session.getTransaction().commit();
            return false;
        } catch (Exception e) {
            // Log the exception
            throw new RuntimeException("Error deleting treatment category by ID: " + id, e);
        }
    }

    @Override
    public boolean updateById(TreatmentCategory treatmentCategory) {
        if (treatmentCategory.getCategory_id() == 0) {
            return false;
        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            TreatmentCategory existing = session.find(TreatmentCategory.class, treatmentCategory.getCategory_id());
            if (existing != null) {
                session.merge(treatmentCategory);
                session.getTransaction().commit();
                return true;
            }
            session.getTransaction().commit();
            return false;
        } catch (Exception e) {
            // Log the exception
            throw new RuntimeException("Error updating treatment category by ID: " + treatmentCategory.getCategory_id(), e);
        }
    }
}