package com.example.sebastian.token;

import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class AddCommentActivity extends AppCompatActivity {

    public String request_id;
    public String rapier_user_id;
    public String problem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);




        setTitle("Wysyłanie komentarza");

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            request_id = bundle.getString("id_request");
            rapier_user_id = bundle.getString("id_user");
        }

        Button Send = (Button) findViewById(R.id.sendcomment);
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AcceptRequest();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public void AcceptRequest()
    {
        EditText text = (EditText)findViewById(R.id.editcomment);
        String test = text.getText().toString();
        try {
            byte[] bytes = test.getBytes("UTF-8");
            String s2 = new String(bytes, "UTF-8");
            problem = s2;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        String url = "http://xdes.pl/crm2/user/zgloszenia/SendComment.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("request_id", request_id);
        params.put("rapier_user_id", rapier_user_id);
        params.put("problem", problem);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Toast.makeText(getApplicationContext(),
                        "Komentarz został wysłany" , Toast.LENGTH_LONG).show();
                Button button = (Button) findViewById(R.id.sendcomment);
                button.setBackgroundColor(Color.GREEN);
                button.setTextColor(Color.BLACK);
                button.setEnabled(false);
                button.setText("Komentarz wysłany");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(AddCommentActivity.this,"Problem z wysłaniem wiadomości" , Toast.LENGTH_SHORT).show();
            }
        });

    }
}
