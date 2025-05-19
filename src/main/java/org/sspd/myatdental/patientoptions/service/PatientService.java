package org.sspd.myatdental.patientoptions.service;

import org.springframework.stereotype.Service;
import org.sspd.myatdental.patientoptions.impl.Patientimpl;
import org.sspd.myatdental.patientoptions.model.Patient;

import java.util.List;

@Service
public class PatientService {

   private final  Patientimpl patientimpl;

    public PatientService(Patientimpl patientimpl) {
        this.patientimpl = patientimpl;
    }

    public List<Patient> getPatients() {

        return patientimpl.findAll();

    }

    public boolean addTreatment(Patient patient) {
        return patientimpl.save(patient);
    }

    public boolean updatePatient(Patient patient) {
        return patientimpl.update(patient);
    }

    public boolean deletePatient(Patient patient) {
        return patientimpl.delete(patient);
    }

    public boolean deleteById(int id) {
        return patientimpl.deleteById(id);
    }

}
