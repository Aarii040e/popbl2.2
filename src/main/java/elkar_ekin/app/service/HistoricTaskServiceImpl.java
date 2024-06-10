package elkar_ekin.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import elkar_ekin.app.model.HistoricTask;
import elkar_ekin.app.repositories.HistoricTaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricTaskServiceImpl implements HistoricTaskService {

    @Autowired
    private HistoricTaskRepository historicTaskRepository;

    @Override
    @Transactional
    public List<HistoricTask> getAllPastTasks(Long clientId) {
        return historicTaskRepository.findByClientId(clientId);
    }

    @Override
    @Transactional
    public List<HistoricTask> getFirstFivePastTasks(Long clientId) {
        return historicTaskRepository.findByClientId(clientId).stream()
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<HistoricTask> getPastVolunteerTasks(Long volunteerId) {
        return historicTaskRepository.findByVolunteerId(volunteerId);
    }

    @Override
    @Transactional
    public List<HistoricTask> getFirstFiveVolunteerTasks(Long volunteerId) {
        return historicTaskRepository.findByVolunteerId(volunteerId).stream()
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<HistoricTask> getFirstFiveClientTasks(Long clientId) {
        return historicTaskRepository.findByClientId(clientId).stream()
                .limit(5)
                .collect(Collectors.toList());
    }
}
