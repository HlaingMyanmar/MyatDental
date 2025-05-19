package org.sspd.myatdental.treatmentoptions.service;

import org.springframework.stereotype.Service;
import org.sspd.myatdental.treatmentoptions.impl.Treatmentimpl;
import org.sspd.myatdental.treatmentoptions.model.Treatment;

import java.util.List;

@Service
public class TreatmentService {

    private final Treatmentimpl treatmentimpl;


    public TreatmentService(Treatmentimpl treatmentimpl) {
        this.treatmentimpl = treatmentimpl;
    }


    public List<Treatment> getTreatments() {

        return treatmentimpl.findAll();

    }

    public boolean addTreatment(Treatment treatment) {
        return treatmentimpl.save(treatment);
    }

    public boolean updateTreatment(Treatment treatment) {
        return treatmentimpl.update(treatment);
    }

    public boolean deleteTreatment(Treatment treatment) {
        return treatmentimpl.delete(treatment);
    }

    public boolean deleteById(int id) {
        return treatmentimpl.deleteById(id);
    }
}
