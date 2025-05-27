package org.sspd.myatdental.treatmentoptions.service;

import org.springframework.stereotype.Service;
import org.sspd.myatdental.treatmentoptions.impl.TreatmentRecordImpl;
import org.sspd.myatdental.treatmentoptions.model.TreatmentRecord;

import java.util.List;
import java.util.Optional;

@Service
public class TreatmentRecordService {

    private final TreatmentRecordImpl treatmentRecordImpl;

    public TreatmentRecordService(TreatmentRecordImpl treatmentRecordImpl) {
        this.treatmentRecordImpl = treatmentRecordImpl;
    }

    /**
     * Retrieves all treatment records.
     *
     * @return List of all TreatmentRecord entities
     */
    public List<TreatmentRecord> findAll() {
        List<TreatmentRecord> records = treatmentRecordImpl.findAll();
        if (records == null || records.isEmpty()) {
            // Consider logging or throwing a custom exception
            return List.of();
        }
        return records;
    }

    /**
     * Saves a new treatment record.
     *
     * @param treatmentRecord The TreatmentRecord to save
     * @return true if saved successfully, false otherwise
     * @throws IllegalArgumentException if treatmentRecord is null
     */
    public boolean save(TreatmentRecord treatmentRecord) {
        if (treatmentRecord == null) {
            throw new IllegalArgumentException("TreatmentRecord cannot be null");
        }
        return treatmentRecordImpl.save(treatmentRecord);
    }

    /**
     * Deletes a treatment record.
     *
     * @param treatmentRecord The TreatmentRecord to delete
     * @return true if deleted successfully, false otherwise
     * @throws IllegalArgumentException if treatmentRecord is null
     */
    public boolean delete(TreatmentRecord treatmentRecord) {
        if (treatmentRecord == null) {
            throw new IllegalArgumentException("TreatmentRecord cannot be null");
        }
        return treatmentRecordImpl.delete(treatmentRecord);
    }

    /**
     * Updates an existing treatment record.
     *
     * @param treatmentRecord The TreatmentRecord to update
     * @return true if updated successfully, false otherwise
     * @throws IllegalArgumentException if treatmentRecord is null
     */
    public boolean update(TreatmentRecord treatmentRecord) {
        if (treatmentRecord == null) {
            throw new IllegalArgumentException("TreatmentRecord cannot be null");
        }
        return treatmentRecordImpl.update(treatmentRecord);
    }

    /**
     * Finds a treatment record by its ID.
     *
     * @param id The ID of the TreatmentRecord
     * @return Optional containing the TreatmentRecord if found, empty otherwise
     */
    public Optional<TreatmentRecord> findById(int id) {
        if (id <= 0) {
            return Optional.empty();
        }
        TreatmentRecord record = treatmentRecordImpl.findById(id);
        return Optional.ofNullable(record);
    }

    /**
     * Deletes a treatment record by its ID.
     *
     * @param id The ID of the TreatmentRecord to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteById(int id) {
        if (id <= 0) {
            return false;
        }
        return treatmentRecordImpl.deleteById(id);
    }

    /**
     * Updates a treatment record by its ID.
     *
     * @param treatmentRecord The TreatmentRecord with updated data
     * @return true if updated successfully, false otherwise
     * @throws IllegalArgumentException if treatmentRecord is null
     */
    public boolean updateById(TreatmentRecord treatmentRecord) {
        if (treatmentRecord == null) {
            throw new IllegalArgumentException("TreatmentRecord cannot be null");
        }
        return treatmentRecordImpl.updateById(treatmentRecord);
    }
}