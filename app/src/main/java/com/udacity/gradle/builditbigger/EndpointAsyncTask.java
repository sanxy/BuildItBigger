package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


class EndpointAsyncTask extends AsyncTask<MainActivityFragment, Void, String> {
    private static MyApi myApiService = null;
    private Context context;
    private MainActivityFragment mainActivityFragment;

    @Override
    protected String doInBackground(MainActivityFragment... mainActivityFragments) {
        mainActivityFragment = mainActivityFragments[0];
        context = mainActivityFragment.getActivity();

        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local dev app server
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local dev app server
                    //not required
                    // .setRootUrl("https://joketellingapp-219013.appspot.com/_ah/api/");
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(abstractGoogleClientRequest -> abstractGoogleClientRequest.setDisableGZipContent(true));
            // end options for dev app server

            myApiService = builder.build();
        }

        try {
            return myApiService.tellJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }


    @Override
    protected void onPostExecute(String result) {
        mainActivityFragment.loadedJoke = result;
        mainActivityFragment.launchDisplayJokeActivity();

    }
}
