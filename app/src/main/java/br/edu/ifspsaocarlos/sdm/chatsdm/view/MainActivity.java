package br.edu.ifspsaocarlos.sdm.chatsdm.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.edu.ifspsaocarlos.sdm.chatsdm.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonAbout;
    private Button buttonAddContact;
    private Button buttonContactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAddContact = (Button) findViewById(R.id.btn_add_contact);
        buttonAddContact.setOnClickListener(this);

        buttonAbout = (Button) findViewById(R.id.btn_about);
        buttonAbout.setOnClickListener(this);

        buttonContactsList = (Button) findViewById(R.id.btn_contatcts_list);
        buttonContactsList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAddContact) {
            Intent intentThisToAddContactActivity = new Intent(this, AddContactActivity.class);
            startActivity(intentThisToAddContactActivity);
        }
        if (v == buttonAbout) {
            Intent intentThisToAboutActivity = new Intent(this, AboutActivity.class);
            startActivity(intentThisToAboutActivity);
        }
        if (v == buttonContactsList) {
            Intent intentThisToContactsListActivity = new Intent(this, ContactsListActivity.class);
            startActivity(intentThisToContactsListActivity);
        }
    }
}
