/*
 * Copyright 2014 IBM Corp. All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.bluelist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ibm.bluelist.dataobjects.Topic;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import bolts.Continuation;
import bolts.Task;

public class MainActivity extends Activity {

    List<Topic> topicList;
    BlueListApplication blApplication;
    ListTopicsAdapter lvArrayAdapter;
    ActionMode mActionMode = null;
    int listItemPosition;
    public static final String CLASS_NAME = "MainActivity";
    public final static String EXTRA_TOPIC = "com.ibm.bluelist.TOPIC";

    @Override
    /**
     * onCreate called when main activity is created.
     *
     * Sets up the topicList, application, and sets listeners.
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		/* Use application class to maintain global state. */
        blApplication = (BlueListApplication) getApplication();
        topicList = blApplication.getTopicList();
		
		/* Set up the array adapter for items list view. */
        ListView itemsLV = (ListView) findViewById(R.id.itemsList);
        lvArrayAdapter = new ListTopicsAdapter(this, topicList);
        itemsLV.setAdapter(lvArrayAdapter);

		
		/* Refresh the list. */
        listItems();

        /* Set short click listener */

        itemsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic topic = topicList.get(position);
                Log.i("Topic Selected", topic.toString());

                Intent intent = new Intent(MainActivity.this, TopicActivity.class);
                intent.putExtra(EXTRA_TOPIC,topic.toString());
                startActivity(intent);
            }
        });

		/* Set long click listener. */
        itemsLV.setOnItemLongClickListener(new OnItemLongClickListener() {
            /* Called when the user long clicks on the textview in the list. */
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position,
                                           long rowId) {
                listItemPosition = position;
                if (mActionMode != null) {
                    return false;
                }
		        /* Start the contextual action bar using the ActionMode.Callback. */
                mActionMode = MainActivity.this.startActionMode(mActionModeCallback);
                return true;
            }
        });
        EditText itemToAdd = (EditText) findViewById(R.id.itemToAdd);

		/* Set key listener for edittext (done key to accept item to list). */
        itemToAdd.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    createItem(v);
                    return true;
                }
                return false;
            }
        });
    }

    private class ListTopicsAdapter extends BaseAdapter {
        Context context;

        protected List<Topic> topicList;
        LayoutInflater inflater;

        public ListTopicsAdapter(Context context, List<Topic> topicList) {
            this.topicList = topicList;
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return topicList.size();
        }

        @Override
        public Object getItem(int position) {
            return topicList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return topicList.get(position).getObjectId().hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolderItem viewHolderItem;

            if (convertView == null) {
                viewHolderItem = new ViewHolderItem();
                convertView = this.inflater.inflate(R.layout.list_item_1, parent, false);

                viewHolderItem.txtTopicName = (TextView) convertView.findViewById(R.id.text1);
                convertView.setTag(viewHolderItem);
            } else {
                viewHolderItem = (ViewHolderItem) convertView.getTag();
            }

            Topic topic = topicList.get(position);
            viewHolderItem.txtTopicName.setText(topic.getName());

            Drawable img = null;

            String url = "http://sam-informatics.com/projects/finalwork/" + topic.getName().toLowerCase() + ".jpg";

            try {
                DownloadBackground task = new DownloadBackground();
                img = task.execute(url).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (img != null) {
                if(Build.VERSION.SDK_INT >=16) {
                    viewHolderItem.txtTopicName.setBackground(img);
                }else{
                    viewHolderItem.txtTopicName.setBackgroundDrawable(img);
                }
            }else {
                Log.i("image", url);
            }

            return convertView;
        }

        private class ViewHolderItem {
            TextView txtTopicName;
        }
    }

    private class DownloadBackground extends AsyncTask<String,Void,Drawable>{
        Drawable x = null;
        @Override
        protected Drawable doInBackground(String... params) {

            try {
                InputStream inputStream = (InputStream) new URL(params[0]).getContent();
                 x = Drawable.createFromStream(inputStream, "src");
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return x;
        }
    }


    /**
     * Removes text on click of x button.
     *
     * @param v the edittext view.
     */
    public void clearText(View v) {
        EditText itemToAdd = (EditText) findViewById(R.id.itemToAdd);
        itemToAdd.setText("");
    }

    /**
     * Refreshes topicList from data service.
     * <p/>
     * An IBMQuery is used to find all the list items.
     */
    public void listItems() {
        try {
            IBMQuery<Topic> query = IBMQuery.queryForClass(Topic.class);
            // Query all the Topic objects from the server.
            query.find().continueWith(new Continuation<List<Topic>, Void>() {

                @Override
                public Void then(Task<List<Topic>> task) throws Exception {
                    final List<Topic> objects = task.getResult();
                    // Log if the find was cancelled.
                    if (task.isCancelled()) {
                        Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
                    }
                    // Log error message, if the find task fails.
                    else if (task.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                    }


                    // If the result succeeds, load the list.
                    else {
                        // Clear local topicList.
                        // We'll be reordering and repopulating from DataService.
                        topicList.clear();
                        for (IBMDataObject item : objects) {
                            topicList.add((Topic) item);

                        }
                        sortItems(topicList);
                        lvArrayAdapter.notifyDataSetChanged();
                    }
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);

        } catch (IBMDataException error) {
            Log.e(CLASS_NAME, "Exception : " + error.getMessage());
        }
    }

    /**
     * On return from other activity, check result code to determine behavior.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
		/* If an edit has been made, notify that the data set has changed. */
            case BlueListApplication.EDIT_ACTIVITY_RC:
                sortItems(topicList);
                lvArrayAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * Called on done and will add item to list.
     *
     * @param v edittext View to get item from.
     * @throws IBMDataException
     */
    public void createItem(View v) {
        EditText itemToAdd = (EditText) findViewById(R.id.itemToAdd);
        String toAdd = itemToAdd.getText().toString();
        Topic topic = new Topic();
        if (!toAdd.equals("")) {
            topic.setName(toAdd);
            // Use the IBMDataObject to create and persist the Topic object.
            topic.save().continueWith(new Continuation<IBMDataObject, Void>() {

                @Override
                public Void then(Task<IBMDataObject> task) throws Exception {
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
                        listItems();
                    }
                    return null;
                }

            });

            // Set text field back to empty after topic is added.
            itemToAdd.setText("");
        }
    }

    /**
     * Will delete an topic from the list.
     *
     * @param Item topic to be deleted
     */
    public void deleteItem(Topic topic) {
        topicList.remove(listItemPosition);

        // This will attempt to delete the topic on the server.
        topic.delete().continueWith(new Continuation<IBMDataObject, Void>() {

            @Override
            public Void then(Task<IBMDataObject> task) throws Exception {
                // Log if the delete was cancelled.
                if (task.isCancelled()) {
                    Log.e(CLASS_NAME, "Exception : Task " + task.toString() + " was cancelled.");
                }

                // Log error message, if the delete task fails.
                else if (task.isFaulted()) {
                    Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                }

                // If the result succeeds, reload the list.
                else {
                    lvArrayAdapter.notifyDataSetChanged();
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

        lvArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Will call new activity for editing item on list.
     *
     * @parm String name - name of the item.
     */
    public void updateItem(String name) {
        Intent editIntent = new Intent(getBaseContext(), EditActivity.class);
        editIntent.putExtra("ItemText", name);
        editIntent.putExtra("ItemLocation", listItemPosition);
        startActivityForResult(editIntent, BlueListApplication.EDIT_ACTIVITY_RC);
    }

    /**
     * Sort a list of Items.
     *
     * @param List<Topic> theList
     */
    private void sortItems(List<Topic> theList) {
        // Sort collection by case insensitive alphabetical order.
        Collections.sort(theList, new Comparator<Topic>() {
            public int compare(Topic lhs,
                               Topic rhs) {
                String lhsName = lhs.getName();
                String rhsName = rhs.getName();
                return lhsName.compareToIgnoreCase(rhsName);
            }
        });
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        /* Inflate a menu resource with context menu items. */
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.editaction, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        /**
         * Called when user clicks on contextual action bar menu item.
         *
         * Determined which item was clicked, and then determine behavior appropriately.
         *
         * @param ActionMode mode and MenuItem item clicked
         */
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Topic lTopic = topicList.get(listItemPosition);
	    	/* Switch dependent on which action item was clicked. */
            switch (item.getItemId()) {
	    		/* On edit, get all info needed & send to new, edit activity. */
                case R.id.action_edit:
                    updateItem(lTopic.getName());
                    mode.finish(); /* Action picked, so close the CAB. */
                    return true;
	            /* On delete, remove list item & update. */
                case R.id.action_delete:
                    deleteItem(lTopic);
                    mode.finish(); /* Action picked, so close the CAB. */
                default:
                    return false;
            }
        }

        /* Called on exit of action mode. */
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
}
