package org.sspd.myatdental.treatmentoptions.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.sspd.myatdental.dto.DataAccessObject;
import org.sspd.myatdental.treatmentoptions.model.TreatmentCategory;

import java.util.List;

@Repository
public class TreatmentCategoryimpl implements DataAccessObject<TreatmentCategory> {

    private SessionFactory sessionFactory;

    public TreatmentCategoryimpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<TreatmentCategory> findAll() {
        return List.of();
    }

    @Override
    public boolean save(TreatmentCategory treatmentCategory) {
        return false;
    }

    @Override
    public boolean delete(TreatmentCategory treatmentCategory) {
        return false;
    }

    @Override
    public boolean update(TreatmentCategory treatmentCategory) {
        return false;
    }

    @Override
    public TreatmentCategory findById(int id) {
        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public boolean updateById(TreatmentCategory treatmentCategory) {
        return false;
    }
}
