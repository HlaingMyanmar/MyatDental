package org.sspd.myatdental.dentistsoptions.service;

import org.springframework.stereotype.Service;
import org.sspd.myatdental.dentistsoptions.impl.Dentistimpl;
import org.sspd.myatdental.dentistsoptions.model.Dentist;

import java.util.List;

@Service
public class DentistServices {

    private final Dentistimpl dentistdb;

    public DentistServices(Dentistimpl dentistdb) {
        this.dentistdb = dentistdb;
    }
    public List<Dentist> getDentists() {

        return dentistdb.findAll();

    }
    public boolean addDentist(Dentist dentist) {
        return dentistdb.save(dentist);
    }
    public boolean updateDentist(Dentist dentist) {
        return dentistdb.update(dentist);
    }
    public boolean deleteDentist(Dentist dentist) {
        return dentistdb.delete(dentist);
    }

    public boolean deleteById(int id) {
        return dentistdb.deleteById(id);
    }
}
