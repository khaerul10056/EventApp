package thoughtworks.eventapp.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import thoughtworks.eventapp.R;
import thoughtworks.eventapp.adapter.ViewPagerAdapter;
import thoughtworks.eventapp.apiclient.APIClient;
import thoughtworks.eventapp.presenter.AgendaPresenter;
import thoughtworks.eventapp.repository.SessionRepository;
import thoughtworks.eventapp.viewmodel.ConferenceViewModel;

public class AgendaActivity extends AppCompatActivity implements AgendaView {

  private ViewPager viewPager;
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_agenda);
    setupToolbar();
    fetchSessions();
  }

  private void fetchSessions() {
    new AgendaPresenter(getApiClient(), this, new SessionRepository()).fetchSessions();
  }

  @NonNull
  protected APIClient getApiClient() {
    return new APIClient();
  }

  @Override
  public void render(ConferenceViewModel conferenceViewModel) {
    setupViewPager(conferenceViewModel);
    setupTabs();
  }

  @Override
  public void showProgressDialog() {
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage(getResources().getString(R.string.loading));
    progressDialog.setCancelable(false);
    progressDialog.show();
  }

  @Override
  public void dismissProgressDialog() {
    progressDialog.dismiss();
  }

  @Override
  public void showConflictPopup() {

  }

  @Override
  public void showSessionAddedSuccessfully() {

  }

  private void setupTabs() {
    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(viewPager);
  }

  private void setupViewPager(ConferenceViewModel conferenceViewModel) {
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), conferenceViewModel);
    viewPager.setAdapter(adapter);
  }

  private void setupToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }
}