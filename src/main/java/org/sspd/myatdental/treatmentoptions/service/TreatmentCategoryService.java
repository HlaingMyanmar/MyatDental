package org.sspd.myatdental.treatmentoptions.service;

import org.springframework.stereotype.Service;
import org.sspd.myatdental.treatmentoptions.impl.TreatmentCategoryImpl;
import org.sspd.myatdental.treatmentoptions.model.Treatment;
import org.sspd.myatdental.treatmentoptions.model.TreatmentCategory;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TreatmentCategoryService {

    private final TreatmentCategoryImpl treatmentCategoryDAO;

    public TreatmentCategoryService(TreatmentCategoryImpl treatmentCategoryDAO) {
        this.treatmentCategoryDAO = treatmentCategoryDAO;
    }

    /**
     * Gets a list of treatment names under the given category.
     * @param categoryId the ID of the treatment category
     * @return List of treatment names, or empty list if none
     */
    public List<String> getTreatmentNamesByCategoryId(int categoryId) {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Invalid treatment category ID: " + categoryId);
        }

        TreatmentCategory category = treatmentCategoryDAO.findById(categoryId);
        if (category == null) {
            return Collections.emptyList(); // or throw exception if needed
        }

        Set<Treatment> treatmentSet = category.getTreatments();

        return treatmentSet.stream()
                .map(Treatment::getName)
                .collect(Collectors.toList());
    }


    /**
     * Retrieves all treatment categories.
     * @return List of all treatment categories.
     */
    public List<TreatmentCategory> getAllTreatmentCategories() {
        return treatmentCategoryDAO.findAll();
    }

    /**
     * Saves a new treatment category after validation.
     * @param treatmentCategory The treatment category to save.
     * @return true if saved successfully, false otherwise.
     * @throws IllegalArgumentException if the treatment category is invalid.
     */
    public boolean saveTreatmentCategory(TreatmentCategory treatmentCategory) {
        validateTreatmentCategory(treatmentCategory);
        return treatmentCategoryDAO.save(treatmentCategory);
    }

    /**
     * Deletes a treatment category.
     * @param treatmentCategory The treatment category to delete.
     * @return true if deleted successfully, false otherwise.
     * @throws IllegalArgumentException if the treatment category is null.
     */
    public boolean deleteTreatmentCategory(TreatmentCategory treatmentCategory) {
        if (treatmentCategory == null) {
            throw new IllegalArgumentException("Treatment category cannot be null");
        }
        return treatmentCategoryDAO.delete(treatmentCategory);
    }

    /**
     * Updates an existing treatment category.
     * @param treatmentCategory The treatment category to update.
     * @return true if updated successfully, false otherwise.
     * @throws IllegalArgumentException if the treatment category is invalid.
     */
    public boolean updateTreatmentCategory(TreatmentCategory treatmentCategory) {
        validateTreatmentCategory(treatmentCategory);
        if (treatmentCategory.getCategory_id() == 0) {
            throw new IllegalArgumentException("Treatment category ID cannot be null for update");
        }
        return treatmentCategoryDAO.update(treatmentCategory);
    }

    /**
     * Finds a treatment category by its ID.
     * @param id The ID of the treatment category.
     * @return The treatment category, or null if not found.
     * @throws IllegalArgumentException if the ID is invalid.
     */
    public TreatmentCategory getTreatmentCategoryById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid treatment category ID: " + id);
        }
        return treatmentCategoryDAO.findById(id);
    }

    /**
     * Deletes a treatment category by its ID.
     * @param id The ID of the treatment category to delete.
     * @return true if deleted successfully, false if not found.
     * @throws IllegalArgumentException if the ID is invalid.
     */
    public boolean deleteTreatmentCategoryById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid treatment category ID: " + id);
        }
        return treatmentCategoryDAO.deleteById(id);
    }

    /**
     * Updates a treatment category by its ID.
     * @param treatmentCategory The treatment category with updated data.
     * @return true if updated successfully, false if not found.
     * @throws IllegalArgumentException if the treatment category or ID is invalid.
     */
    public boolean updateTreatmentCategoryById(TreatmentCategory treatmentCategory) {
        validateTreatmentCategory(treatmentCategory);
        if (treatmentCategory.getCategory_id() ==0) {
            throw new IllegalArgumentException("Treatment category ID cannot be null for update");
        }
        return treatmentCategoryDAO.updateById(treatmentCategory);
    }

    /**
     * Validates a treatment category before saving or updating.
     * @param treatmentCategory The treatment category to validate.
     * @throws IllegalArgumentException if the treatment category is invalid.
     */
    private void validateTreatmentCategory(TreatmentCategory treatmentCategory) {
        if (treatmentCategory == null) {
            throw new IllegalArgumentException("Treatment category cannot be null");
        }
        if (treatmentCategory.getName() == null || treatmentCategory.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Treatment category name cannot be null or empty");
        }
    }
}