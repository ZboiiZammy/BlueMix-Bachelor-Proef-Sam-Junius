package com.ibm.bluelist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ibm.bluelist.dataobjects.BestPractise;
import com.ibm.mobile.services.data.IBMDataObject;

import bolts.Continuation;
import bolts.Task;


public class BPAddActivity extends Activity {

    BlueListApplication blueListApplication;
    String topic;
    public static final String CLASS_NAME="BPAddActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blueListApplication = (BlueListApplication) getApplicationContext();
        setContentView(R.layout.activity_bpadd);

        Button btnAdd = (Button) findViewById(R.id.btnAdd);

        Intent intent = getIntent();
        topic = intent.getStringExtra(TopicActivity.EXTRA_TOPIC_ADD);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtTitle = (EditText) findViewById(R.id.txtTitleAdd);
                String addTitle = txtTitle.getText().toString();

                EditText txtBP = (EditText) findViewById(R.id.txtBestPractiseAdd);
                String addBP = txtBP.getText().toString();

                BestPractise bestPractise = new BestPractise();

                if(!addTitle.equals("") && !addBP.equals("")){
                    bestPractise.setTitle(addTitle);
                    bestPractise.setText(addBP);
                    bestPractise.setTopic(topic);

                    bestPractise.save().continueWith(new Continuation<IBMDataObject, Object>() {
                        @Override
                        public Object then(Task<IBMDataObject> task) throws Exception {

                            // Log if the save was cancelled.
                            if (task.isCancelled()) {
                                Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
                            }
                            // Log error message, if the save task fails.
                            else if (task.isFaulted()) {
                                Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                            }

                            // If the result succeeds, load the list.
                            else {
                                Intent returnIntent = new Intent();
                                setResult(1,returnIntent);
                                finish();
                            }

                            return null;
                        }
                    },Task.UI_THREAD_EXECUTOR);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bpadd, menu);
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
}
