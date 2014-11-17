package com.agnostix.activent.activent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Mails extends PlusBaseActivity {


    private String accountName;
    private Gmail mailService;
    private String authToken;
    private static final int CHECK_VALID_TOKEN = 1993;

    private static final String GMAIL_SCOPE = "oauth2:https://www.googleapis.com/auth/gmail.modify";
    private static final String APP_NAME = "Activent";

    @Override
    protected void onPlusClientRevokeAccess() {

    }

    @Override
    protected void onPlusClientSignIn() {
        Toast.makeText(getApplicationContext(), "Signed in!", Toast.LENGTH_SHORT).show();
        Log.v("Mails", "Signed in!");

        getMessagesTask messagesTask = new getMessagesTask();
        messagesTask.execute();
    }

    @Override
    protected void onPlusClientSignOut() {

    }

    @Override
    protected void onPlusClientBlockingUI(boolean show) {

    }

    @Override
    protected void updateConnectButtonState() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mails);

        accountName = "shivamone@gmail.com";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mails, menu);
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
        if(id == R.id.action_mails_refresh){
            Toast.makeText(getApplicationContext(), "Refreshing...", Toast.LENGTH_SHORT).show();
            new getMessagesTask().execute();

        }

        return super.onOptionsItemSelected(item);
    }

    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
    }


    private class getAuthTokenTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                authToken = GoogleAuthUtil.getToken(getApplicationContext(),
                        getPlusClient().getAccountName(), GMAIL_SCOPE);

            } catch (UserRecoverableAuthException e) {
                startActivityForResult(e.getIntent(), 1);
                try {
                    GoogleAuthUtil.clearToken(getApplicationContext(), authToken);
                } catch (GoogleAuthException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                Log.d("AUTH_TOKEN", extras.toString());

            }
        }
    }

    private class getMessagesTask extends AsyncTask<Void, Void, ArrayList<ArrayList<String>>>{

        @Override
        protected ArrayList<ArrayList<String>> doInBackground(Void... params) {
            ArrayList<ArrayList<String>> messages = new ArrayList<ArrayList<String>>();
            try {
                if(authToken == null){
                    new getAuthTokenTask().execute();
                }
                GoogleCredential credential = new GoogleCredential()
                        .setAccessToken(authToken);
                HttpTransport httpTransport = new NetHttpTransport();
                JsonFactory jsonFactory = new JacksonFactory();
                mailService = new Gmail.Builder(httpTransport, jsonFactory,
                        credential).setApplicationName(APP_NAME).build();
                ListMessagesResponse messagesResponse;
                List<Message> emails = null;
                ArrayList<String> labelIDs = new ArrayList<String>();
                labelIDs.add("INBOX");
                labelIDs.add("CATEGORY_UPDATES");
                labelIDs.add("CATEGORY_SOCIAL");
                labelIDs.add("CATEGORY_PROMOTIONS");
                labelIDs.add("CATEGORY_FORUMS");
                try {
                    messagesResponse = mailService.users().messages()
                            .list("me")
                            .setLabelIds(labelIDs)
                            .setMaxResults(10L)
                            .execute();
                    //response = mailService.users().messages().get("me", "1497fdee53dda12d").setFormat("full").execute();
                    emails = messagesResponse.getMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                for(Message thisEmail: emails){
                    ArrayList<String> message = new ArrayList<String>();

                    //fetch details of the individual mail
                    Message emailDetails = null;
                    try {
                        //threadsResponse = mailService.users().messages().list("me")
                        //        .execute();
                        emailDetails = mailService.users().messages()
                                .get("me", thisEmail.getId())
                                .setFormat("full")
                                .setFields("id,payload,snippet,threadId")
                                .execute();
                        //threads = threadsResponse.getMessages();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String fromHeader = "";
                    String subjectHeader = "";
                    List<MessagePartHeader> headers = emailDetails.getPayload().getHeaders();
                    for(MessagePartHeader header: headers){
                        if(header.getName().equals("From")){
                            fromHeader = header.getValue();
                        }
                        if(header.getName().equals("Subject")){
                            subjectHeader = header.getValue();
                        }
                    }
                    String mailSnippet = emailDetails.getSnippet();

                    message.add(fromHeader);
                    message.add(thisEmail.getId());
                    message.add(mailSnippet);
                    Log.d("Messages: ", thisEmail.toPrettyString());
                    messages.add(message);
                }
                //messages.add(response.toPrettyString());
                return messages;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList<String> blankMsg = new ArrayList<String>();
            blankMsg.add("No messages");
            blankMsg.add("#threadid");
            blankMsg.add("@snippet");
            messages.add(blankMsg);

            return messages;
        }
        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> messages){
            /*for(String message: messages){
                Log.d("Gmail Message", "Gmail Message:" + message);
            }*/

            ListView listView = (ListView)findViewById(R.id.mails_list);
            listView.setAdapter(new EmailListAdapter(getApplicationContext(), messages));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView threadIdText = (TextView)view.findViewById(R.id.email_item_thread_id);
                    String threadId = threadIdText.getText().toString();
                    new getSingleMessageTask().execute(threadId);
                }
            });
            /*ClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView threadIdText = (TextView)v.findViewById(R.id.email_item_thread_id);
                    String threadId = threadIdText.getText().toString();
                    new getSingleMessageTask().execute(threadId);

                }
            });*/
        }
    }

    private class getSingleMessageTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                if(authToken == null){
                    new getAuthTokenTask().execute();
                }
                String threadID = params[0];
                GoogleCredential credential = new GoogleCredential()
                        .setAccessToken(authToken);
                HttpTransport httpTransport = new NetHttpTransport();
                JsonFactory jsonFactory = new JacksonFactory();
                mailService = new Gmail.Builder(httpTransport, jsonFactory,
                        credential).setApplicationName(APP_NAME).build();
                //ListMessagesResponse threadsResponse;
                Message response = null;
                try {
                    //threadsResponse = mailService.users().messages().list("me")
                    //        .execute();
                    response = mailService.users().messages()
                            .get("me", threadID)
                            .setFormat("full")
                            .setFields("id,payload,snippet,threadId")
                            .execute();
                    //threads = threadsResponse.getMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                StringBuffer sb = new StringBuffer();
                sb.append("From:");
                String fromHeader = "";
                String subjectHeader = "";
                List<MessagePartHeader> headers = response.getPayload().getHeaders();
                for(MessagePartHeader header: headers){
                    if(header.getName().equals("From")){
                        fromHeader = header.getValue();
                    }
                    if(header.getName().equals("Subject")){
                        subjectHeader = header.getValue();
                    }
                }
                sb.append(fromHeader + "\n");
                sb.append("Subject: " + subjectHeader + "\n");

                String message = sb.toString();
                return message;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "No such message";
        }

        @Override
        protected void onPostExecute(String message){
            Log.d("Gmail Message", "Gmail Message:" + message);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}