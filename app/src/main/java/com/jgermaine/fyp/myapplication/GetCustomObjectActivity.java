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


public class GetCustomObjectActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_custom_object);

        new MyCustomObjectTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_get_custom_object, menu);
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

    public void doSomethingOnSuccess(Greeting greeting) {
        ((TextView) findViewById(R.id.object_text)).setText(greeting.getMessage());
    }

    public void doSomethingOnFailure(String failureMessage) {
        ((TextView) findViewById(R.id.object_text)).setText(failureMessage);
    }

    public class MyCustomObjectTask extends AsyncTask<Void, Void, ResponseEntity<Greeting>> {

        private final String myUrl = "http://your-api.cloudapp.net/api/correct-path";

        @Override
        protected ResponseEntity<Greeting> doInBackground(Void... params) {
            RestTemplate rt = new RestTemplate();
            rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                return rt.getForEntity(myUrl, Greeting.class);
            } catch (HttpClientErrorException e) {
                return new ResponseEntity<Greeting>(e.getStatusCode());
            } catch (HttpMessageConversionException e) {
                return new ResponseEntity<Greeting>(HttpStatus.BAD_REQUEST);
            } catch (RestClientException e) {
                return new ResponseEntity<Greeting>(HttpStatus.BAD_REQUEST);
            }
        }

        @Override
        protected void onPostExecute(ResponseEntity<Greeting> response) {
            if (response.getStatusCode().value() < 300) {
                doSomethingOnSuccess(response.getBody());
            } else {
                doSomethingOnFailure(response.getStatusCode().getReasonPhrase());
            }
        }
    }

    public class Greeting {

        private String Message;
        private String To;

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public String getTo() {
            return To;
        }

        public void setTo(String to) {
            To = to;
        }
    }
}
