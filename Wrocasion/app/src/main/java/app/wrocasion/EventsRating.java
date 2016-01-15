package app.wrocasion;

import android.content.Intent;
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

public class EventsRating extends Fragment implements View.OnClickListener{

    private RatingBar eventsRatingBar;
    private Button sendEventsFeedbackButton;
    private EditText etEventsFeedback;
    private TextView tvEventName;
    private LinearLayout eventsRatingFields, eventsRatingLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.events_rating,container,false);



        sendEventsFeedbackButton = (Button) v.findViewById(R.id.sendEventsFeedback);
        sendEventsFeedbackButton.setOnClickListener(this);

        etEventsFeedback = (EditText) v.findViewById(R.id.etEventsFeedback);

        tvEventName = (TextView) v.findViewById(R.id.tvEventName);

        eventsRatingFields = (LinearLayout) v.findViewById(R.id.appRatingFields);
        eventsRatingLayout = (LinearLayout) v.findViewById(R.id.eventsRatingLayout);

        eventsRatingBar = (RatingBar) v.findViewById(R.id.appRatingBar);
        eventsRatingBar.setStepSize(1);
        eventsRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (eventsRatingBar.getRating() > 0) {
                        eventsRatingFields.setVisibility(View.VISIBLE);
                    } else {
                        eventsRatingFields.setVisibility(View.INVISIBLE);
                    }
                }
            });


        return v;
    }

    @Override
    public void onClick(View v) {

       if(v.getId() == R.id.sendEventsFeedback) {

       }
    }

}
