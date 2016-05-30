package thoughtworks.eventapp.presenter;

import android.support.annotation.NonNull;

import org.joda.time.Interval;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import thoughtworks.eventapp.apiclient.APIClient;
import thoughtworks.eventapp.apiclient.APIClientCallback;
import thoughtworks.eventapp.model.Category;
import thoughtworks.eventapp.model.Session;
import thoughtworks.eventapp.model.Conference;
import thoughtworks.eventapp.repository.SessionRepository;
import thoughtworks.eventapp.view.AgendaView;
import thoughtworks.eventapp.viewmodel.SessionViewModel;

public class AgendaPresenter {

  public final static String api = "https://intense-fire-9666.firebaseio.com/";
  private final APIClient apiClient;
  private final AgendaView agendaView;
  private final SessionRepository sessionRepository;

  public AgendaPresenter(APIClient apiClient, AgendaView agendaView, SessionRepository sessionRepository) {
    this.apiClient = apiClient;
    this.agendaView = agendaView;
    this.sessionRepository = sessionRepository;
  }

  public void fetchSessions(){
    agendaView.showProgressDialog();
    apiClient.get(api, new APIClientCallback<Conference>() {
      @Override
      public void onSuccess(Conference conference) {
        List<ArrayList<SessionViewModel>> sessionViewModels = new ArrayList<>();
        for (Category category : Category.values()) {
          sessionViewModels.add(getSessionViewModelsByCategory(conference, category));
        }
        agendaView.render(sessionViewModels);
        agendaView.dismissProgressDialog();
      }

      @Override
      public Class<Conference> getClassOfType() {
        return Conference.class;
      }
    });
  }

  public void addSession(SessionViewModel sessionViewModel){
    List<Session> allSavedSessions = sessionRepository.getSavedSessions();
    final Date startTimeOfNewSession = sessionViewModel.getStartTime();
    final Date endTimeOfNewSession = sessionViewModel.getEndTime();
    for (Session session : allSavedSessions) {
      Interval interval1 = new Interval(startTimeOfNewSession.getTime(), endTimeOfNewSession.getTime());
      Interval interval2 = new Interval(session.getStartTime().getTime(), session.getEndTime().getTime());
      if(interval1.overlap(interval2) != null){
        agendaView.showConflictPopup();
      } else {
        agendaView.showSessionAddedSuccessfully();
        sessionRepository.saveSession(sessionViewModel);
      }
    }
  }

  @NonNull
  private ArrayList<SessionViewModel> getSessionViewModelsByCategory(Conference conference, Category category) {
    ArrayList<SessionViewModel> sessionViewModels = new ArrayList<>();
    final List<Session> filteredSessions = conference.filterByCategory(category);
    for (Session session : filteredSessions) {
      sessionViewModels.add(new SessionViewModel(session));
    }
    return sessionViewModels;
  }
}