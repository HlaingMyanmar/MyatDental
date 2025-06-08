package org.sspd.myatdental.treatmentoptions.service;


import org.springframework.stereotype.Service;
import org.sspd.myatdental.treatmentoptions.impl.TreatmentCategoryimpl;

@Service
public class TreatmentCategoryService {

    private TreatmentCategoryimpl treatmentCategory;

    public TreatmentCategoryService(TreatmentCategoryimpl treatmentCategory) {
        this.treatmentCategory = treatmentCategory;
    }



}
