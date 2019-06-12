package br.edu.ifspsaocarlos.sdm.chatsdm.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.edu.ifspsaocarlos.sdm.chatsdm.R;

public class AboutActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private TextView tvId;
    private TextView tvName;
    private String idSharedPreferences;
    private String nameSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvId = (TextView) findViewById(R.id.tv_id);
        tvName = (TextView) findViewById(R.id.tv_name);

        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        idSharedPreferences = sharedPreferences.getString("id", null);
        tvId.setText("Id: " + idSharedPreferences);
        nameSharedPreferences = sharedPreferences.getString("name", null);
        tvName.setText(nameSharedPreferences);
    }
}
