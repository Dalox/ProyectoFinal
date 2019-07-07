package com.app.dalox.proyectofinal;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText correo, contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo = (EditText)findViewById(R.id.etCorreo);
        contraseña = (EditText)findViewById(R.id.etContraseña);
    }

    public void Log_In(View view){
    if (!correo.getText().toString().trim().equalsIgnoreCase("")||
            !contraseña.getText().toString().trim().equalsIgnoreCase("")){
        new Log_In(MainActivity.this).execute();
    }else {
        Toast.makeText(MainActivity.this,"Falta Infomacion",Toast.LENGTH_LONG).show();
    }
    }

    private boolean log_in(){
        String request="";
        String uri="http://192.168.100.2/pfinal/admin/log_in.php";
        HttpClient httpClient;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(uri);
        List<NameValuePair> nameValuePairs;
        nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("mail",correo.getText().toString().trim()));
        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            request = httpClient.execute(httpPost,responseHandler);
            String user_info[] = request.split("<br>");
            String user_correo, user_contraseña;
            user_correo = user_info[0];
            user_contraseña = user_info[1];
            if (correo.getText().toString().trim() == user_correo){
                if (contraseña.getText().toString().trim() == user_contraseña){
                    return true;
                }else{
                    Toast.makeText(this,"Contraseña Equivicada",Toast.LENGTH_LONG).show();
                    return false;
                }
            }else {
                Toast.makeText(this,"Usuario Equivicado",Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    class Log_In extends AsyncTask<String, String, String>{

        private Activity context;

        Log_In(Activity context){this.context = context;};

        @Override
        protected String doInBackground(String... strings) {

            if (log_in()){
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"Usuario Admitido",Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
    }
}
