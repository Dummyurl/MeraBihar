package app.zingo.merabihar.UI.ActivityScreen.Events;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import app.zingo.merabihar.R;

public class TermsPolicyScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_terms_policy_screen);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("Privacy Policy");

        }catch(Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                TermsPolicyScreen.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
