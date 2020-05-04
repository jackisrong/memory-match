package com.example.memorymatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> imageLinks = new ArrayList<String>();
    public static ArrayList<Drawable> images = new ArrayList<Drawable>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.startGameButton).setBackgroundColor(Color.GRAY);
        findViewById(R.id.startGameButton).setEnabled(false);

        // Load JSON and images
        loadJSONAndImages();
    }

    private void loadJSONAndImages() {
        // Parse JSON from endpoint
        new JSONProcessor().execute("https://shopicruit.myshopify.com/admin/products.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6");
    }

    private void setGameLoaded() {
        findViewById(R.id.startGameButton).setBackgroundResource(R.color.colorPrimary);
        findViewById(R.id.startGameButton).setEnabled(true);
        ((Button) findViewById(R.id.startGameButton)).setText(R.string.button_play);
    }

    public void startGame(View view) {
        // Go to GameScreenActivity
        Intent intent = new Intent(this, GameScreenActivity.class);
        startActivity(intent);
    }



    private class JSONProcessor extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            // Get JSON from endpoint and return as string
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                br = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // Parse JSON and add image src links to imageLinks
            try {
                JSONObject fullJSON = new JSONObject(result);
                JSONArray productsArray = fullJSON.getJSONArray("products");

                for (int i = 0; i < productsArray.length(); i++) {
                    String link = (String) ((JSONObject) productsArray.get(i)).getJSONObject("image").get("src");
                    if (!imageLinks.contains(link)) {
                        imageLinks.add(link);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Get images as Drawables
            new ImageProcessor().execute();
        }
    }

    private class ImageProcessor extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Get image as Drawable
            for (int i = 0; i < imageLinks.size(); i++) {
                try {
                    InputStream is = (InputStream) new URL(imageLinks.get(i)).getContent();
                    images.add(Drawable.createFromStream(is, "Image"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setGameLoaded();
        }
    }
}
