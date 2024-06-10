package elkar_ekin.app.service;

import elkar_ekin.app.model.HistoricTask;

import java.util.List;

public interface HistoricTaskService {
    List<HistoricTask> getAllPastTasks(Long clientId);
    List<HistoricTask> getFirstFivePastTasks(Long clientId);
    List<HistoricTask> getPastVolunteerTasks(Long volunteerId);
    List<HistoricTask> getFirstFiveVolunteerTasks(Long volunteerId);
    List<HistoricTask> getFirstFiveClientTasks(Long clientId);
}
