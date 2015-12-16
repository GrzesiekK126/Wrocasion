package app.wrocasion.JSONs;


import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class RestClient {

    private static RestAPI REST_CLIENT;
    private static final String ROOT = "http://188.122.12.144:50000/";

    static {
        setupRestClient();
    }

    private RestClient(){}

    public static RestAPI get(){
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(RestAPI.class);
    }

}
