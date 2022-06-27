
Skip to content
Pulls
Issues
Marketplace
Explore
@AlfredRds
dpa99c /
cordova-plugin-popupbridge
Public

Code
Issues
Pull requests
Actions
Projects
Wiki
Security

    Insights

cordova-plugin-popupbridge/src/android/MainActivity.java /
@dpa99c
dpa99c Fix activity
Latest commit 0a54803 on 17 Jul 2017
History
1 contributor
50 lines (39 sloc) 1.67 KB
/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at
         http://www.apache.org/licenses/LICENSE-2.0
       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package <%PACKAGE_NAME%>;

import android.os.Bundle;
import android.webkit.WebView;

import com.evergage.android.Evergage;

import org.apache.cordova.*;

public class MainActivity extends CordovaActivity
{
    private PopupBridge mPopupBridge;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Evergage.initialize(getApplication());
        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }

        // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);

    }
}

