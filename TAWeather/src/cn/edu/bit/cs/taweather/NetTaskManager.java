package cn.edu.bit.cs.taweather;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class NetTaskManager extends AsyncTask<String, Void, String> {

	private String TAG = "NetTaskManager";
	private Context mContext;
	private final int delay = 8000;
	
	public NetTaskManager (Context context){
        mContext = context;
    }
	
	public class HttpLinkTest implements Callable<Integer> {

		private int timeout;
		String testUrl;
		HttpLinkTest(String testUrl, int timeout)
		{
			this.testUrl = testUrl;
			this.timeout = timeout;
		}

	    public Integer call() throws Exception {
	    	URL url = new URL(testUrl);
	    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    	con.setRequestMethod("HEAD");
			con.setRequestProperty("Accept-Encoding", "");
			con.setConnectTimeout(timeout); 
			con.setReadTimeout(timeout);
			int response = con.getResponseCode();
			return response;
	    }
	    
	};
	
	private boolean isConnectedToServer( String url,  int timeout) {
		boolean isConnectionOk = false;
		ExecutorService pool = Executors.newFixedThreadPool(1);
		Callable<Integer> oneCallable = new HttpLinkTest(url, timeout);
		Future<Integer> f1 = pool.submit(oneCallable); 
		try {
			Integer result = f1.get();
			if(result == 200)
			{
				isConnectionOk = true;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
		
 		return isConnectionOk;
	}
	
	public boolean ConnectionTest(String URL, int timeout)
	{
		ConnectivityManager connectivity = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifiConn = ni.isConnected();
		ni = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobilConn = ni.isConnected();
		boolean isConnected = isConnectedToServer(URL, timeout);
		
		if((isWifiConn || isMobilConn) && isConnected)
			return true;
		else
			return false;
			
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected String doInBackground(String... params) {
		String weatherResult = null;
		
		if(!ConnectionTest(params[0], delay)){
			return null;
		}
		else
		{
			try {
				HttpGet httpGet = new HttpGet(params[0]);

				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = null;
				try {
					httpResponse = httpClient.execute(httpGet);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

					weatherResult = EntityUtils.toString(httpResponse.getEntity());
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
			return weatherResult;
		}
	}

	@Override
	protected void onPostExecute(String result) {

	}
}
