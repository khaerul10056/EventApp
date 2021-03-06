package thoughtworks.eventapp.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConferenceTest {

  @Test
  public void filterSessionsByCategory() {
    Session session1 = new Session("Craft", "Try your hand at craft", null, null, Category.BELONG, "Ballroom");
    Session session2 = new Session("Craft", "Try your hand at craft", null, null, Category.CREATE, "Pre function area");
    List<Session> sessionList = new ArrayList<>();
    sessionList.add(session1);
    sessionList.add(session2);
    Conference conference = new Conference(sessionList);

    final List<Session> filteredSessions = conference.filterByCategory(Category.CREATE);

    assertThat(filteredSessions.size(), is(1));
    assertThat(filteredSessions.get(0), is(session2));
  }
}
