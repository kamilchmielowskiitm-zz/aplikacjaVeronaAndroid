package com.example.sebastian.token;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClients;

public class EndTaskListAllActivity extends AppCompatActivity {


    private String url = "http://xdes.pl/crm2/user/zgloszenia/GetListEndAll.php";
    private ListView listView;
    private String jsonResult;
    private static final String TAG = "MyActivity";
    private String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_task_list);
        listView = (ListView) findViewById(R.id.listview1);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id_user = bundle.getString("id_user");
        }

        accessWebService();

        setTitle("Lista Zadań Wykonanych");
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        accessWebService();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    // Async Task to access the web
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
                //Cały String JSON bez przerobienia
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }

            catch (IOException e) {
                // e.printStackTrace();
//                Toast.makeText(getApplicationContext(),
//                        "List Pusta...", Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ListDrwaer();
        }
    }// end async task


    public void accessWebService() {
        EndTaskListAllActivity.JsonReadTask task = new EndTaskListAllActivity.JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[] { url });
    }

    // build hash set for list view
    public void ListDrwaer() {
        final List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>(); // Lista do prechowywania danych

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("problem_json"); // nazwa główna tabeli

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String problem = jsonChildNode.optString("problem"); // wyłuskaniedanych
                String opis = jsonChildNode.optString("opis"); // wyłuskaniedanych
                String typ = jsonChildNode.optString("typ");
                String oddzial = jsonChildNode.optString("oddzial");
                String id = jsonChildNode.optString("id");
                String data = jsonChildNode.optString("datazgl");
                String priorytet = jsonChildNode.optString("priorytet");
                String dzialanie = jsonChildNode.optString("dzialanie");
                String naprawia = jsonChildNode.optString("id_user_repair");
                String zglosil = jsonChildNode.optString("id_user_maker");

                String realizacjaod = jsonChildNode.optString("data_start");
                String realizacjado = jsonChildNode.optString("data_stop");

                employeeList.add(createEmployee("departments", oddzial, "problem", problem, "typ", typ, "data", data, "id", id
                        ,"dzialanie", dzialanie,"naprawia", naprawia,"zglosil", zglosil,"opis", opis,"priorytet", priorytet
                ,"realizacjaod", realizacjaod,"realizacjado", realizacjado));
            }

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Lista Pusta...",
                    Toast.LENGTH_SHORT).show();
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, employeeList,
                R.layout.lista,
                new String[] { "departments","typ","problem","data","id" }, new int[] { R.id.textView_oddzial,R.id.textView_typ,R.id.textView_notka
                ,R.id.textView_data,R.id.textView_id}){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                //HERE I ADD THE CODE FROM situee
                String piorytet = employeeList.get(position).get("priorytet");
                int number = Integer.parseInt(piorytet);
                LinearLayout linearLayout;
                linearLayout =  (LinearLayout)view.findViewById(R.id.ViewContent);

                if (number == 3) {
                    linearLayout.setBackgroundColor(Color.parseColor("#ffcccc"));
                } else if (number == 2) {
                    linearLayout.setBackgroundColor(Color.parseColor("#ffffcc"));
                }else
                {
                    linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                return view;
            }
        };
        listView.setAdapter(simpleAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView text = (TextView) view.findViewById(R.id.textView_id);
                Log.d(TAG, "**************************"+   listView.getItemAtPosition(position).toString()  +" *************************");

                Map<String,Object> map = (Map<String,Object>)listView.getItemAtPosition(position);
                String numberid = (String) map.get("id");
                String oddzial = (String) map.get("departments");
                // strona po kliknieci na na wybrany listveiw
                Intent myintent = new Intent(view.getContext(), EndTaskViewActivity.class);

                myintent.putExtra("departments", (String) map.get("departments"));
                myintent.putExtra("problem", (String) map.get("problem"));
                myintent.putExtra("typ", (String) map.get("typ"));
                myintent.putExtra("data", (String) map.get("data"));
                myintent.putExtra("id", (String) map.get("id"));
                myintent.putExtra("dzialanie", (String) map.get("dzialanie"));
                myintent.putExtra("naprawia", (String) map.get("naprawia"));
                myintent.putExtra("zglosil", (String) map.get("zglosil"));
                myintent.putExtra("opis", (String) map.get("opis"));

                myintent.putExtra("odr", (String) map.get("realizacjaod"));
                myintent.putExtra("dor", (String) map.get("realizacjado"));
                startActivityForResult(myintent, 0);
            }
        });
    }

    private HashMap<String, String> createEmployee(String namedep, String numberdep, String nameprob, String numberprob
            , String nametyp, String numbertyp, String namedata, String numberdata, String nameid, String numberid,
                                                   String namedzialanie, String numberdzialanie, String namenaprawia, String numbernaprawia,
                                                   String namezlosil, String numberzglosil, String nameopis, String numberopis
            ,String namepriorytet, String numberpiorytet,String namerealizacjaod, String numberrealizacjaod,String namedata_stop, String numberdata_stop) {
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(namedep, numberdep);
        employeeNameNo.put(nameprob, numberprob);
        employeeNameNo.put(nametyp, numbertyp);
        employeeNameNo.put(namedata, numberdata);
        employeeNameNo.put(nameid, numberid);
        employeeNameNo.put(namedzialanie, numberdzialanie);
        employeeNameNo.put(namenaprawia, numbernaprawia);
        employeeNameNo.put(namezlosil, numberzglosil);
        employeeNameNo.put(nameopis, numberopis);
        employeeNameNo.put(namepriorytet, numberpiorytet);
        employeeNameNo.put(namerealizacjaod, numberrealizacjaod);
        employeeNameNo.put(namedata_stop, numberdata_stop);
        return employeeNameNo;
    }





}

