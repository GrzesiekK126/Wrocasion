package app.wrocasion;

import android.content.Context;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;

import app.wrocasion.JSONs.ChangeCategoriesResponse;
import app.wrocasion.JSONs.Feedback;
import app.wrocasion.JSONs.RestClient;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AppRating extends Fragment implements View.OnClickListener{

    private RatingBar appRatingBar;
    private Button sendFeedbackButton;
    private EditText etFeedback;
    private LinearLayout feedbackLayout;
    private int rate = 0;

    static SweetAlertDialog sweetAlertDialog;
    static Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_rating,container,false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if(Profile.getCurrentProfile() != null || Account.checkLoginToApp()) {
            sendFeedbackButton = (Button) v.findViewById(R.id.sendFeedback);
            sendFeedbackButton.setOnClickListener(this);

            etFeedback = (EditText) v.findViewById(R.id.etFeedback);
            etFeedback.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etFeedback.getWindowToken(), 0);
                    return true;
                }
            });



            feedbackLayout = (LinearLayout) v.findViewById(R.id.appRatingFields);

            appRatingBar = (RatingBar) v.findViewById(R.id.appRatingBar);
            appRatingBar.setStepSize(1);
            appRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (appRatingBar.getRating() > 0) {
                        //sendFeedbackButton.setVisibility(View.VISIBLE);
                        //etFeedback.setVisibility(View.VISIBLE);
                        feedbackLayout.setVisibility(View.VISIBLE);
                        rate = (int) appRatingBar.getRating();

                    } else {
                        //sendFeedbackButton.setVisibility(View.INVISIBLE);
                        //etFeedback.setVisibility(View.INVISIBLE);
                        feedbackLayout.setVisibility(View.INVISIBLE);
                        rate = 0;
                    }
                }
            });

            context = getActivity();
        } else{
                Toast.makeText(getActivity().getApplicationContext(), "Opcja dostępna tylko po zalogowaniu!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), FirstActivity.class);
                startActivity(intent);
            }

        return v;
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.sendFeedback){

            Feedback feedback = new Feedback();
            feedback.setEventId(-1);
            feedback.setDescription(etFeedback.getText().toString());
            feedback.setRate(rate);
            feedback.setUserName(Account.getUsername());

            RestClient.get().getFeedback(feedback, new Callback<ChangeCategoriesResponse>() {
                @Override
                public void success(ChangeCategoriesResponse changeCategoriesResponse, Response response) {
                    sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setTitleText("Ocena aplikacji");
                    sweetAlertDialog.setContentText("Dziękujemy za ocenę naszej aplikacji :)");
                    sweetAlertDialog.show();

                    Toast.makeText(getActivity(), "R: " + changeCategoriesResponse.getSpecjalnyModelDlaGrzesia(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

            //Toast.makeText(getActivity(),"R: " + rate + "\nF: " + etFeedback.getText(), Toast.LENGTH_SHORT).show();
        }

    }
}
