package app.wrocasion.Events.TabsControl;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.wrocasion.JSONs.GetEvents;
import app.wrocasion.JSONs.RestClient;
import app.wrocasion.JSONs.SetCurrentLocation;
import app.wrocasion.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventDetail extends AppCompatActivity {

    ViewPager pager;
    ViewPagerAdapterEventDetail adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Info","Mapa", "Zdjęcia"};
    String imageURL;
    Target target, target2;
    public static Bitmap image;

    int Numboftabs = 3;
    static ImageView imageViewDetail;
    static Button btn;
    public static boolean expand = true;
    public static ArrayList<Bitmap> imagesGallery;


    static LinearLayout imageLayout, swipeLayout;
    //private DragTopLayout dragLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagesGallery = new ArrayList<>();

        imageViewDetail = (ImageView) findViewById(R.id.imageViewDetail);
        imageViewDetail.setImageResource(ListViewAdapter.imageId.get(ListViewAdapter.imageNumber));

        adapter = new ViewPagerAdapterEventDetail(getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.Grey);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        imageLayout = (LinearLayout) findViewById(R.id.top_view);

        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expand) {
                    collapse();
                }
            }
        });

        btn = (Button) findViewById(R.id.expand);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expand){
                    collapse();
                }
                else{
                    expand();
                }
            }
        });

        imageURL = "http://188.122.12.144:50000/";

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                image = bitmap;
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.e("TAG", "Failed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.e("TAG", "PrepareLoad");
            }
        };

        SetCurrentLocation setCurrentLocation = new SetCurrentLocation();
        setCurrentLocation.setUserName("");
        setCurrentLocation.setLatitude(62.11);
        setCurrentLocation.setLongtitude(12.23);

        RestClient.get().getEvents(setCurrentLocation, new Callback<List<GetEvents>>() {
            @Override
            public void success(final List<GetEvents> events, Response response) {
                for(int i=0; i<events.size(); i++) {
                    Picasso.with(getApplicationContext())
                            .load(imageURL + events.get(i).getImage())
                            .into(target);
                    //imagesGallery.add(i, image);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onEvent(Boolean b){
        //dragLayout.setTouchMode(b);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public static void expand() {
        //set Visible
        imageLayout.setVisibility(View.VISIBLE);
        btn.setBackgroundResource(R.drawable.up);
        expand = true;

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
         imageLayout.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, imageLayout.getMeasuredHeight());
        mAnimator.start();
    }

    public static void collapse() {
        int finalHeight =  imageLayout.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                imageLayout.setVisibility(View.GONE);
                btn.setBackgroundResource(R.drawable.down);
                expand = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    public static ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = imageLayout.getLayoutParams();
                layoutParams.height = value;
                imageLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

}

