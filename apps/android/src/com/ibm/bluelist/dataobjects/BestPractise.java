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
package com.ibm.bluelist.dataobjects;

import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

@IBMDataObjectSpecialization("BestPractise")
public class BestPractise extends IBMDataObject {
	public static final String CLASS_NAME = "BestPractise";
	private static final String TITLE = "title";
	private static final String TEXT = "text";
	private static final String TOPIC = "topic";

    public String getTitle() {
        return (String) getObject(TITLE);
    }

    public String getText() {
        return (String) getObject(TEXT);
    }

    public String getTopic() {
        return (String) getObject(TOPIC);
    }

    public void setTitle(String bpTitle){
        setObject(TITLE, (bpTitle != null) ? bpTitle : "");
    }

    public void setText(String bpText){
        setObject(TEXT, (bpText != null) ? bpText : "");
    }

    public void setTopic(String bpTopic){
        setObject(TOPIC, (bpTopic != null) ? bpTopic : "");
    }
	
	/**
	 * When calling toString() for an item, we'd really only want the name.
	 * @return String theTopicName
	 */
	public String toString() {
		String theBPTitle = "";
        theBPTitle = getTitle();
		return theBPTitle;
	}
}
