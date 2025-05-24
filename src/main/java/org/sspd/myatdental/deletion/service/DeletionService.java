package org.sspd.myatdental.deletion.service;

import org.springframework.stereotype.Service;
import org.sspd.myatdental.deletion.impl.DeletionLogImpl;
import org.sspd.myatdental.deletion.model.DeletionLog;

import java.util.List;

@Service
public class DeletionService {

    private final DeletionLogImpl deletionLogRepository;

    public DeletionService(DeletionLogImpl deletionLogRepository) {
        this.deletionLogRepository = deletionLogRepository;
    }

    public List<DeletionLog> getAllDeletionLogs() {
        return deletionLogRepository.findAll();
    }

    public boolean createDeletionLog(DeletionLog deletionLog) {
        if (deletionLog == null || deletionLog.getAppointmentId() == null || deletionLog.getPatientId() == null) {
            return false;
        }
        return deletionLogRepository.save(deletionLog);
    }

    public boolean deleteDeletionLog(DeletionLog deletionLog) {
        if (deletionLog == null || deletionLog.getId() == null) {
            return false;
        }
        return deletionLogRepository.delete(deletionLog);
    }

    public boolean updateDeletionLog(DeletionLog deletionLog) {
        if (deletionLog == null || deletionLog.getId() == null) {
            return false;
        }
        return deletionLogRepository.update(deletionLog);
    }

    public DeletionLog getDeletionLogById(int id) {
        if (id <= 0) {
            return null;
        }
        return deletionLogRepository.findById(id);
    }

    public boolean deleteDeletionLogById(int id) {
        if (id <= 0) {
            return false;
        }
        return deletionLogRepository.deleteById(id);
    }

    public boolean updateDeletionLogById(DeletionLog deletionLog) {
        if (deletionLog == null || deletionLog.getId() == null) {
            return false;
        }
        return deletionLogRepository.updateById(deletionLog);
    }
}