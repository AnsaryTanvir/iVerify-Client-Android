package com.iverify;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/** @brief Handling Non UI Thread for Network Request
 *
 *  To prevent blocking the main UI thread, network request must run in a different thread.
 *  We are creating a class that uses AsyncTask class to implement a non UI background thread.
 *
 *  doInBackground() method calls "doInBackgroundNative" C/C++ function
 *  onPostExecute    method calls "onPostExecuteNative"  C/C++ function
 *
 */
public class Network extends AsyncTask<Void, Void, String> {

    Context context;
    String SERVER_URL;
    FormBody requestBody;
    static ProgressBar progressBar;
    static Button loginButton;
    Login loginActivity; // Add this

    /**@brief Constructor
     *
     * @param context
     */
    public Network(Context context, String SERVER_URL, FormBody requestBody, ProgressBar progressBar, Button loginButton, Login loginActivity) {
        this.context = context;
        this.SERVER_URL = SERVER_URL;
        this.requestBody = requestBody;
        this.progressBar = progressBar;
        this.loginButton = loginButton;
        this.loginActivity = loginActivity; // Add this
    }

    @Override
    public String doInBackground(Void... voids) {

        if (Utils.isInternetAvailable(context)) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(SERVER_URL).post(requestBody).build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Server error";
        }

        return "Internet not available";

    }

    @Override
    protected void onPostExecute(String response) {

        progressBar.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);

        if (response.equals("Internet not available") || response.equals("Server error")) {
            Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
            if (response.contains("Success")) {
                loginActivity.startMainActivity();
            }
        }
    }
}
