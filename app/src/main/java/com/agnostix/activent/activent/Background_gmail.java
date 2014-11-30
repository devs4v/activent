package com.agnostix.activent.activent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.json.JSONException;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.UserRecoverableNotifiedException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListThreadsResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.Thread;

public class Background_gmail extends IntentService{
	static int a=0;
	//protected MainActivity mActivity;
	int count=0;
    DBHelper db=null;
	HashSet<String> history_first=new HashSet<String>();
	ArrayList<ArrayList<String>> all_mail_events=new ArrayList<ArrayList<String>>();
	ArrayList<String> first_history=new ArrayList<String>();
	private static final String mSCOPE =  "oauth2:https://www.googleapis.com/auth/gmail.modify";;
	public static final String EXTRA_ACCOUNTNAME = "extra_accountname";

	
	static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
	static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
	static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;

	String current_history_id=null;
	
	public static String TYPE_KEY = "type_key";
	static int one_time=0;
	static List<Thread> threads=null;
	protected String mEmail="me";
	static ArrayList<String> mail_data=new ArrayList<String>();
	static String token=null;
	Gmail mailService;
	static String last_history_id=null;
	boolean mails_no=true;
	public Background_gmail() {
		super("gmail_background");
		// TODO Auto-generated constructor stub
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		return super.onStartCommand(intent, flags, startId);
	}



	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}




	public static Message getMessage(Gmail service, String userId, String messageId)
			throws IOException {
		Message message = service.users().messages().get(userId, messageId).setFormat("raw").execute();

		String s=new String(Base64.decode(message.getRaw(),Base64.URL_SAFE));
		
		mail_data.add(s);
		return message;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
db=new DBHelper(this);

		if(a==0)
		{
			System.out.println("a===0");
			/*try {
				fetchNameFromProfileServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
            try {
               // PrefHelper prefHelper = new PrefHelper(activityContext);
               // String username = prefHelper.getValueOrDefault(PrefHelper.PREF_USERNAME, "none");
               /* if(username.equals("none")){
                    Log.d("authtoken", username + " is  null");
                    Toast.makeText(activityContext, "No user logged in!", Toast.LENGTH_SHORT).show();
                    return null;
                }
               */
                System.out.println("Service"+LoginActivity.mEmail);
                token = GoogleAuthUtil.getToken(getApplicationContext(),
                        LoginActivity.mEmail, mSCOPE);

            } catch (UserRecoverableAuthException e) {
               // startActivityForResult(e.getIntent(), 1);
                try {
                    GoogleAuthUtil.clearToken(getApplicationContext(), token);
                    Log.d("getAuthToken", "authtoken task failed!");
                    token = GoogleAuthUtil.getToken(getApplicationContext(),
                            LoginActivity.mEmail, mSCOPE);

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



            GoogleCredential credential = new GoogleCredential()
			.setAccessToken(token);
			HttpTransport httpTransport = new NetHttpTransport();
			JsonFactory jsonFactory = new JacksonFactory();
			mailService = new Gmail.Builder(httpTransport, jsonFactory,
					credential).setApplicationName("Activent").build();
			ListThreadsResponse threadsResponse;
			threads = null;
			try {
				threadsResponse = mailService.users().threads().list("me").setMaxResults(Long.parseLong("15"))
						.execute();
				threads = threadsResponse.getThreads();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("hello"+threads);

			last_history_id=threads.get(0).get("id").toString();
			System.out.println(last_history_id);
			if(threads!=null){
				for(Thread thread:threads)
				{

					try {
						
						getMessage(mailService, "me", thread.get("id").toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}}



			for(String s:mail_data){
				boolean  b=false;
				//System.out.println(s);
				ArrayList<String> per_mail_event_desc=new ArrayList<String>();
				String[] lines=s.split("\n");
				this_one :for(String m:lines)
				{
					m=m.toLowerCase();
					if(m.startsWith("title")||m.startsWith("venue")||m.startsWith("info"))
					{
						String[] l2=m.split(":");
						per_mail_event_desc.add(l2[1]);


						if(m.startsWith("info"))
						{
							b=true;
							break this_one;
						}

					}
					if(m.startsWith("time"))
					{ 
						String[] l2=m.split(":");
						System.out.println(l2[1]+" dddd"+l2[2]+"hhh"+l2[3]);
						per_mail_event_desc.add(l2[1]+l2[2]+l2[3]);

					}
					if(m.startsWith("date"))
					{
						count++;
						if(count==2)
						{
							String[] l2=m.split(":");
							per_mail_event_desc.add(l2[1]);
						}

					}
				}


				System.out.println(per_mail_event_desc);


				if(b){
                    String h[]=new String[6];
                    h[0]=per_mail_event_desc.get(0);
                    h[1]=per_mail_event_desc.get(1);
                    h[2]=per_mail_event_desc.get(2);
                    h[3]="00";
                    h[4]=per_mail_event_desc.get(4);
                    h[5]="0000";
                    db.addNewActivityEntry(h);
					//all_mail_events.add( per_mail_event_desc);

				}


			} 

			System.out.println(all_mail_events);
			all_mail_events.clear();mail_data.clear();
			a=1;

		}
		else{

			System.out.println("a=========1");
			mails_no=true;


			GoogleCredential credential = new GoogleCredential()
			.setAccessToken(token);
			HttpTransport httpTransport = new NetHttpTransport();
			JsonFactory jsonFactory = new JacksonFactory();
			Gmail mailService = new Gmail.Builder(httpTransport, jsonFactory,
					credential).setApplicationName("Activent").build();
			ListThreadsResponse threadsResponse;
			List<Thread> threads = null;


			while(mails_no)
			{

				try {
					threadsResponse = mailService.users().threads().list("me").setMaxResults(Long.parseLong("15"))
							.execute();
					threads = threadsResponse.getThreads();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String local_id=threads.get(0).get("id").toString();

				System.out.println("hello"+threads);

				if(threads!=null){
					thread_loop: for(Thread thread:threads)
					{current_history_id=thread.get("id").toString();
					try {
						System.out.println(current_history_id+" "+ last_history_id);

						if(current_history_id.equals(last_history_id)){
							mails_no=false;
							//System.out.println(current_history_id+" "+ last_history_id);
							last_history_id=local_id;
							break thread_loop;
						}
						getMessage(mailService, "me", thread.get("id").toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					}

				}



				for(String s:mail_data){
					boolean  b=false;
					//System.out.println(s);
					ArrayList<String> per_mail_event_desc=new ArrayList<String>();
					String[] lines=s.split("\n");
					this_one :for(String m:lines)
					{
						m=m.toLowerCase();
						if(m.startsWith("title")||m.startsWith("venue")||m.startsWith("info"))
						{
							String[] l2=m.split(":");
							per_mail_event_desc.add(l2[1]);


							if(m.startsWith("info"))
							{
								b=true;
								break this_one;
							}

						}
						if(m.startsWith("time"))
						{ 
							String[] l2=m.split(":");
							System.out.println(l2[1]+" dddd"+l2[2]+"hhh"+l2[3]);
							per_mail_event_desc.add(l2[1]+l2[2]+l2[3]);

						}
						if(m.startsWith("date"))
						{
							count++;
							if(count==2)
							{
								String[] l2=m.split(":");
								per_mail_event_desc.add(l2[1]);
							}

						}
					}


					System.out.println(per_mail_event_desc);


					if(b){
                        String h[]=new String[6];
                        h[0]=per_mail_event_desc.get(0);
                        h[1]=per_mail_event_desc.get(1);
                        h[2]=per_mail_event_desc.get(2);
                        h[3]="00";
                        h[4]=per_mail_event_desc.get(4);
                        h[5]="0000";
                        db.addNewActivityEntry(h);
						//all_mail_events.add( per_mail_event_desc);

					}


				} 
			}
			System.out.println(all_mail_events);
			all_mail_events.clear();
			mail_data.clear();

		}
	}

	protected String fetchToken() throws IOException {
		try {

			System.out.println("lolll"+LoginActivity.mEmail+mSCOPE);

			return GoogleAuthUtil.getToken(
					this, LoginActivity.mEmail, mSCOPE);
		} catch (UserRecoverableNotifiedException userRecoverableException) {
			} catch (GoogleAuthException fatalException) {
			// onError("Unrecoverable error " + fatalException.getMessage(), fatalException);
		}
		return null;
	}


	
	private void fetchNameFromProfileServer() throws IOException, JSONException {
		token = fetchToken();
		if (token == null) {

            System.out.println("hello");
            try{
            GoogleAuthUtil.clearToken(getApplicationContext(), token);}
            catch(Exception e)
            {
                token=fetchToken();
            }
			// error has already been handled in fetchToken()
			return;
		}
		
	}

}
