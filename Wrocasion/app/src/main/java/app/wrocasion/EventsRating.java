package app.wrocasion;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;

import app.wrocasion.JSONs.ChangeCategoriesResponse;
import app.wrocasion.JSONs.Feedback;
import app.wrocasion.JSONs.RestClient;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventsRating extends Fragment implements View.OnClickListener{

    private RatingBar eventsRatingBar;
    private Button sendEventsFeedbackButton;
    private EditText etEventsFeedback;
    private TextView tvEventName;
    private LinearLayout eventsRatingLayout, eventsFeedbackLayout;
    private int rate = 0;
    private Context context;
    private String accountName;

    public static String eventName;
    public static int eventId;

    SweetAlertDialog sweetAlertDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.events_rating,container,false);

        sendEventsFeedbackButton = (Button) v.findViewById(R.id.sendEventsFeedback);
        sendEventsFeedbackButton.setOnClickListener(this);

        etEventsFeedback = (EditText) v.findViewById(R.id.etEventsFeedback);

        tvEventName = (TextView) v.findViewById(R.id.tvEventName);
        tvEventName.setText(eventName);

        eventsRatingLayout = (LinearLayout) v.findViewById(R.id.eventsRatingLayout);
        eventsFeedbackLayout = (LinearLayout) v.findViewById(R.id.eventsFeedbackLayout);

        eventsRatingBar = (RatingBar) v.findViewById(R.id.eventsRatingBar);
        eventsRatingBar.setStepSize(1);
        eventsRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (eventsRatingBar.getRating() > 0) {
                        //sendEventsFeedbackButton.setVisibility(View.VISIBLE);
                        //etEventsFeedback.setVisibility(View.VISIBLE);
                        eventsFeedbackLayout.setVisibility(View.VISIBLE);
                        rate = (int) eventsRatingBar.getRating();
                    } else {
                        //sendEventsFeedbackButton.setVisibility(View.INVISIBLE);
                        //etEventsFeedback.setVisibility(View.INVISIBLE);
                        eventsFeedbackLayout.setVisibility(View.INVISIBLE);
                        rate = 0;
                    }
                }
            });

        if(Account.checkLoginToApp()){
            accountName = Account.getUsername();
        }else if(Account.checkLogInFacebook()){
            accountName = Account.getName(Profile.getCurrentProfile());
        }

        context = getActivity();
        return v;
    }

    @Override
    public void onClick(View v) {

       if(v.getId() == R.id.sendEventsFeedback) {
           Feedback feedback = new Feedback();
           feedback.setEventId(eventId);
           feedback.setDescription(etEventsFeedback.getText().toString());
           feedback.setRate(rate);
           feedback.setUserName(accountName);

           RestClient.get().getFeedback(feedback, new Callback<ChangeCategoriesResponse>() {
               @Override
               public void success(ChangeCategoriesResponse changeCategoriesResponse, Response response) {
                   sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                   sweetAlertDialog.setTitleText("Ocena wydarzenia");
                   sweetAlertDialog.setContentText("Dziękujemy za ocenę wydarzenia :)");
                   sweetAlertDialog.show();
               }

               @Override
               public void failure(RetrofitError error) {
                    error.printStackTrace();
               }
           });
       }
    }

}
