package thoughtworks.eventapp.presenter;

import java.util.ArrayList;
import java.util.List;

import thoughtworks.eventapp.apiclient.APIClient;
import thoughtworks.eventapp.apiclient.APIClientCallback;
import thoughtworks.eventapp.constant.Constants;
import thoughtworks.eventapp.model.Session;
import thoughtworks.eventapp.model.Sessions;
import thoughtworks.eventapp.view.AgendaView;
import thoughtworks.eventapp.viewmodel.SessionViewModel;

public class AgendaPresenter {

  private final APIClient apiClient;
  private final AgendaView agendaView;

  public AgendaPresenter(APIClient apiClient, AgendaView agendaView) {
    this.apiClient = apiClient;
    this.agendaView = agendaView;
  }

  public void renderSessions(){
    apiClient.get(Constants.api, new APIClientCallback<Sessions>() {
      @Override
      public void onSuccess(Sessions sessions) {
        List<SessionViewModel> sessionViewModels = new ArrayList<>();
        for (Session session : sessions.getSessions()) {
          sessionViewModels.add(new SessionViewModel(session));
        }
        agendaView.render(sessionViewModels);
      }

      @Override
      public Class<Sessions> getClassOfType() {
        return Sessions.class;
      }
    });
  }
}
