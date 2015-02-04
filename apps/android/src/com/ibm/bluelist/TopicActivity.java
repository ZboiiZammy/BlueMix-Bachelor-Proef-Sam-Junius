package com.ibm.bluelist;

import android.app.Activity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.ibm.bluelist.dataobjects.BestPractise;

import java.util.List;


public class TopicActivity extends Activity {

    List<BestPractise> bestPractiseList;
    BlueListApplication blApplication;
    ListBPAdapter lvArrayAdapter;
    ActionMode mActionMode = null;
    int listItemPosition;
    public static final String CLASS_NAME = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        blApplication = (BlueListApplication) getApplication();
        bestPractiseList =
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<BestPractise> getBestPractiseList() {
        return bestPractiseList;
    }

    private class ListBPAdapter {
    }
}
