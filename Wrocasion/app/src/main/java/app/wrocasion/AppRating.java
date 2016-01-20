package app.wrocasion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wrapp.floatlabelededittext.FloatLabeledEditText;

public class AppRating extends Fragment implements View.OnClickListener{

    private RatingBar appRatingBar;
    private Button sendFeedbackButton;
    private EditText etFeedback;
    private LinearLayout feedbackLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_rating,container,false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        sendFeedbackButton = (Button) v.findViewById(R.id.sendFeedback);
        sendFeedbackButton.setOnClickListener(this);

        etFeedback = (EditText) v.findViewById(R.id.etFeedback);

        feedbackLayout = (LinearLayout) v.findViewById(R.id.feedbackLayout);

        appRatingBar = (RatingBar) v.findViewById(R.id.appRatingBar);
        appRatingBar.setStepSize(1);
        appRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (appRatingBar.getRating() > 0) {
                    //sendFeedbackButton.setVisibility(View.VISIBLE);
                    //etFeedback.setVisibility(View.VISIBLE);
                    feedbackLayout.setVisibility(View.VISIBLE);

                } else {
                    //sendFeedbackButton.setVisibility(View.INVISIBLE);
                    //etFeedback.setVisibility(View.INVISIBLE);
                    feedbackLayout.setVisibility(View.INVISIBLE);
                }
            }
        });


        return v;
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.sendFeedback){
            Toast.makeText(getActivity(), "D: " + etFeedback.getText(), Toast.LENGTH_SHORT).show();
        }

    }
}
