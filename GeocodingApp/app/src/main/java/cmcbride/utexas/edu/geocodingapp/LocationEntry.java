package cmcbride.utexas.edu.geocodingapp;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;

public class LocationEntry extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_entry);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.location_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view){
        EditText location = (EditText)findViewById(R.id.editText);
        Geocoder g = new Geocoder(getApplicationContext());
        ArrayList<Address> addresses = new ArrayList<Address>();

        /* generate address from the user input string */
        try {
            addresses = (ArrayList<Address>)g.getFromLocationName(location.getText().toString(), 2);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(this, PlotLocation.class);
        Bundle b = new Bundle();

        b.putParcelableArrayList("locations", addresses);
        i.putExtras(b);

        /* plot location on a Google Map */
        startActivity(i);
    }
}
