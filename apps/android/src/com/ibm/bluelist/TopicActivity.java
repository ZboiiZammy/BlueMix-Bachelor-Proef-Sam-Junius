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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ibm.bluelist.dataobjects.BestPractise;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMQuery;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Intent intent = getIntent();
        this.selectedTopic = intent.getStringExtra(MainActivity.EXTRA_TOPIC);

        blApplication = (BlueListApplication) getApplication();
        bestPractiseList = blApplication.getBestPractiseList();

        ListView bestPractisesLV = (ListView) findViewById(R.id.bestPractisesList);
        lvArrayAdapter = new ListBPAdapter(this, bestPractiseList);
        bestPractisesLV.setAdapter(lvArrayAdapter);

        listBPs();

        /* SET KEY LISTENERS */
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
        IBMQuery<BestPractise> query;
        try {
            query = IBMQuery.queryForClass(BestPractise.class);

            query.find().continueWith(new Continuation<List<BestPractise>, Void>() {
                @Override
                public Void then(Task<List<BestPractise>> listTask) throws Exception {
                    final List<BestPractise> objects = listTask.getResult();


                    if (listTask.isCancelled()) {
                        Log.e(CLASS_NAME, "Exception : Task " + listTask.toString() + " was cancelled.");
                    } else if (listTask.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + listTask.getError().getMessage());
                    } else {
                        Log.i("test", String.valueOf(objects.size()));
                        Log.i("test", String.valueOf(objects.get(0).getClass()));
                        bestPractiseList.clear();
                        for (BestPractise bp : objects) {
                            Log.i("test", "test");
                            try {
                                bestPractiseList.add(bp);
                            }catch(Exception e){
                                Log.i("ERROR",e.getMessage());
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

    private class ListBPAdapter extends BaseAdapter {
        Context context;

        protected List<BestPractise> bestPractiseList;
        LayoutInflater inflater;

        public ListBPAdapter(Context context, List<BestPractise> bestPractiseList) {
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
            return bestPractiseList.get(position).getObjectId().hashCode();
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

            BestPractise bestPractise = bestPractiseList.get(position);
            viewHolderItem.txtBPTitle.setText(bestPractise.getTitle());

            /*
             * int maxLength = (inputString.length() < MAX_CHAR)?inputString.length():MAX_CHAR;
             * inputString = inputString.substring(0, maxLength);
            */
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
