package com.ibm.bluelist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ibm.bluelist.dataobjects.BestPractise;
import com.ibm.mobile.services.cloudcode.IBMCloudCode;
import com.ibm.mobile.services.core.http.IBMHttpResponse;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bolts.Continuation;
import bolts.Task;


public class TopicActivity extends Activity {

    List<BestPractise> bestPractiseList;
    BlueListApplication blApplication;
    ListBPAdapter lvArrayAdapter;
    ActionMode mActionMode = null;
    int listItemPosition;
    public static final String CLASS_NAME = "MainActivity";
    private String selectedTopic;
    public final static String EXTRA_TOPIC_ADD = "com.ibm.bluelist.TOPIC";
    public final static String EXTRA_BP_TITLE = "com.ibm.bluelist.TITLE";
    public final static String EXTRA_BP_TEXT = "com.ibm.bluelist.TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Intent intent = getIntent();
        this.selectedTopic = intent.getStringExtra(MainActivity.EXTRA_TOPIC);

        setTitle(this.selectedTopic + " Best Practises");

        blApplication = (BlueListApplication) getApplication();
        bestPractiseList = blApplication.getBestPractiseList();

        ListView bestPractisesLV = (ListView) findViewById(R.id.bestPractisesList);
        lvArrayAdapter = new ListBPAdapter<BestPractise>(this, bestPractiseList); /*CHECK OUT THIS*/
        bestPractisesLV.setAdapter(lvArrayAdapter);

        listBPs();

        /* SET KEY LISTENERS */

        bestPractisesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BestPractise bp = bestPractiseList.get(position);

                Intent intent = new Intent(TopicActivity.this, BPActivity.class);
                intent.putExtra(EXTRA_BP_TITLE,bp.getTitle());
                intent.putExtra(EXTRA_BP_TEXT,bp.getText());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(CLASS_NAME,"Activity Result code : " + resultCode);
        switch (resultCode)
        {
            case BlueListApplication.EDIT_ACTIVITY_RC:
                updateOtherDevices();
                sortItems(bestPractiseList);
                lvArrayAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * Send a notification to all devices whenever the BlueList is modified (create, update, or delete).
     */
    private void updateOtherDevices() {

        // Initialize and retrieve an instance of the IBM CloudCode service.
        IBMCloudCode.initializeService();
        IBMCloudCode myCloudCodeService = IBMCloudCode.getService();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("key1", "value1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

		/*
		 * Call the node.js application hosted in the IBM Cloud Code service
		 * with a POST call, passing in a non-essential JSONObject.
		 * The URI is relative to/appended to the BlueMix context root.
		 */

        myCloudCodeService.post("notifyOtherDevices", jsonObj).continueWith(new Continuation<IBMHttpResponse, Void>() {

            @Override
            public Void then(Task<IBMHttpResponse> task) throws Exception {
                if (task.isCancelled()) {
                    Log.e(CLASS_NAME, "Exception : Task" + task.isCancelled() + "was cancelled.");
                } else if (task.isFaulted()) {
                    Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                } else {
                    InputStream is = task.getResult().getInputStream();
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(is));
                        String responseString = "";
                        String myString = "";
                        while ((myString = in.readLine()) != null)
                            responseString += myString;

                        in.close();
                        Log.i(CLASS_NAME, "Response Body: " + responseString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Log.i(CLASS_NAME, "Response Status from notifyOtherDevices: " + task.getResult().getHttpResponseCode());
                }

                return null;
            }

        });

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
        if (id == R.id.action_add_topic) {
            Intent intent = new Intent(TopicActivity.this, BPAddActivity.class);
            intent.putExtra(EXTRA_TOPIC_ADD, selectedTopic);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void listBPs() {
        try {
            IBMQuery<BestPractise> query = IBMQuery.queryForClass(BestPractise.class);

            query.find().continueWith(new Continuation<List<BestPractise>, Void>() {
                @Override
                public Void then(Task<List<BestPractise>> listTask) throws Exception {
                    if (listTask.isCancelled()) {
                        Log.e(CLASS_NAME, "Exception : Task " + listTask.toString() + " was cancelled.");
                    } else if (listTask.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + listTask.getError().getMessage());
                    } else {

                        final List<BestPractise> objects = listTask.getResult();

                        bestPractiseList.clear();
                        for (IBMDataObject bp : objects) {
                            BestPractise bpadd = (BestPractise) bp;
                            if (bpadd.getTopic().equals(selectedTopic)) {
                                bestPractiseList.add(bpadd);
                            }
                        }
                        sortItems(bestPractiseList);
                        lvArrayAdapter.notifyDataSetChanged();
                    }
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        } catch (IBMDataException e) {
            Log.e(CLASS_NAME, "Exception : " + e.getMessage());
        }
    }

    private void sortItems(List<BestPractise> theList) {
        Collections.sort(theList, new Comparator<BestPractise>() {
            @Override
            public int compare(BestPractise lhs, BestPractise rhs) {
                String lhsTitle = lhs.getTitle();
                String rhsTitle = rhs.getTitle();
                return lhsTitle.compareToIgnoreCase(rhsTitle);
            }
        });
    }

    private class ListBPAdapter<T> extends BaseAdapter {
        Context context;

        protected List<T> bestPractiseList;
        LayoutInflater inflater;

        public ListBPAdapter(Context context, List<T> bestPractiseList) {
            this.bestPractiseList = bestPractiseList;
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return bestPractiseList.size();
        }

        @Override
        public Object getItem(int position) {
            return bestPractiseList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return bestPractiseList.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolderItem viewHolderItem;

            if (convertView == null) {
                viewHolderItem = new ViewHolderItem();
                convertView = this.inflater.inflate(R.layout.list_bp_1, parent, false);

                viewHolderItem.txtBPTitle = (TextView) convertView.findViewById(R.id.txtTitle);
                viewHolderItem.txtSummary = (TextView) convertView.findViewById(R.id.txtSummary);
                convertView.setTag(viewHolderItem);
            } else {
                viewHolderItem = (ViewHolderItem) convertView.getTag();
            }

            BestPractise bestPractise = (BestPractise) bestPractiseList.get(position);
            viewHolderItem.txtBPTitle.setText(bestPractise.getTitle());

            int MAX_CHAR = 10;
            int summarylenght = (bestPractise.getText().length() < MAX_CHAR) ? bestPractise.getText().length() : MAX_CHAR;
            viewHolderItem.txtSummary.setText(bestPractise.getText().substring(0, summarylenght));

            return convertView;
        }

        private class ViewHolderItem {
            TextView txtBPTitle;
            TextView txtSummary;
        }
    }
}
