package app.wrocasion;

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

public class AppRating extends Fragment implements View.OnClickListener{

    private RatingBar appRatingBar;
    private Button sendFeedbackButton;
    private EditText etFeedback;
    private LinearLayout appRatingFields;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_rating,container,false);

        sendFeedbackButton = (Button) v.findViewById(R.id.sendFeedback);
        sendFeedbackButton.setOnClickListener(this);

        etFeedback = (EditText) v.findViewById(R.id.etFeedback);

        appRatingFields = (LinearLayout) v.findViewById(R.id.appRatingFields);

        appRatingBar = (RatingBar) v.findViewById(R.id.appRatingBar);
        appRatingBar.setStepSize(1);
        appRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(appRatingBar.getRating() > 0){
                    appRatingFields.setVisibility(View.VISIBLE);
                } else{
                    appRatingFields.setVisibility(View.INVISIBLE);
                }
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {

    }
}
