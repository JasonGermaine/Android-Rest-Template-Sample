package com.jgermaine.fyp.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


public class GetStringActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_string);

        new MyStringTask().execute();
    }

    public void doSomething(String s) {
        ((TextView) findViewById(R.id.text)).setText(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_get_string, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyStringTask extends AsyncTask<Void, Void, ResponseEntity<String>> {

        private final String myUrl = "http://your-api.cloudapp.net/api/values/1";

        @Override
        protected ResponseEntity<String> doInBackground(Void... params) {
            RestTemplate rt = new RestTemplate();
            rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                return rt.getForEntity(myUrl, String.class);
            } catch (HttpClientErrorException e) {
                return new ResponseEntity<String>(e.getMessage(), e.getStatusCode());
            } catch (HttpMessageConversionException e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            } catch (RestClientException e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        @Override
        protected void onPostExecute(ResponseEntity<String> response) {
            doSomething(response.getBody());
        }
    }
}
