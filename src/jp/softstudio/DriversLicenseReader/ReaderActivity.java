/*
The MIT License (MIT)

Copyright (c) <2013> <Soft-Studio K.K. info@soft-studio.jp>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package jp.softstudio.DriversLicenseReader;

import jj2000.JJ2000Frontend;
import jp.softstudio.DriversLicenseReader.DriversLicenseReader.Reason;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReaderActivity extends Activity  implements Runnable {

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private static final String TAG = "ReaderActivity";

    private DriversLicenseReader mDriversLicenseReader;
    
    private ProgressDialog progressDialog;
    private Reason tagcheck;
    private Context mContext;
    private Bitmap decodedImage;

    
    private boolean bAction;
    private Tag tag;
    
	public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	//縦固定
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  	//スリープしない

        setContentView(R.layout.reader);
        
        progressDialog=null;
        mContext=this;
        
        Intent intent = getIntent();
        if(intent == null) {
            finish();
            return;
        }
        if(!intent.hasExtra("pass1") || !intent.hasExtra("pass2")) {
        	Toast.makeText(this, "パスワードが不正のため終了します", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        String pin1 = intent.getExtras().getString("pass1");
        String pin2 = intent.getExtras().getString("pass2");
        boolean cb1 = intent.getExtras().getBoolean("pass1use");
        boolean cb2 = intent.getExtras().getBoolean("pass2use");
        
        
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[] {
                ndef,
        };
        mTechLists = new String[][] { new String[] { NfcB.class.getName() },new String[] { IsoDep.class.getName() }};

        
        byte pin1byte[] = pin1.getBytes();
        byte pin2byte[] = pin2.getBytes();

        mDriversLicenseReader = new DriversLicenseReader(pin1byte,pin2byte,cb1,cb2);
        
        bAction=false;
    }

	   @Override 
	   public void onResume()
	   {
		   super.onResume();
	        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
	   }

	   @Override
	   public void onDestroy() {
		   super.onDestroy();
	        mAdapter=null;

	   }

	   
	   @Override
	    public void onNewIntent(Intent intent) {
	        Log.i(TAG, "Discovered tag with intent: " + intent);

	        if(intent == null) {
	            return;
	        }
	        if (bAction==true) {
	        	return;
	        }
	        if (progressDialog!=null) {
	        	return;
	        }

	        
	        bAction=true;
        	TextView tv1 = (TextView) findViewById(R.id.textView1);
        	tv1.setText("読み込み中");
	        
        	
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("読み込み中です。運転免許証を離さないでください。");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            tag = (Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            Thread thread = new Thread(this);
            thread.start();
            


	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        if ( this.isFinishing() ) {
	            mAdapter.disableForegroundDispatch(this);
	        }
	    }	  

	    public void run() {
	        // 時間のかかる処理をここに記述。
		        tagcheck=mDriversLicenseReader.tag_check(tag);
		        bAction=false;
		        if(tagcheck != Reason.SUCCESS)
		        {

		        } else {
		        	decodedImage=null;
	        	    // try to decode JPEG2000
		        	byte b[] = mDriversLicenseReader.getPicture();
		        	if (b!=null) {
		        		decodedImage = JJ2000Frontend.decode(mDriversLicenseReader.getPicture());
		        	}
		        }
		        
	        handler.sendEmptyMessage(0);
	    }

	    
	    @SuppressLint("HandlerLeak")
		private Handler handler = new Handler() {
	        public void handleMessage(Message msg) {
	            // 処理終了時の動作をここに記述。
		        if(tagcheck != Reason.SUCCESS)
		        {
			        if((tagcheck == Reason.PIN1_NG) || (tagcheck == Reason.PIN2_NG)) {
			        	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			        	builder.setMessage(R.string.pin_error);
			        	builder.setPositiveButton("確認", null);
			        	builder.setIcon(android.R.drawable.ic_dialog_alert);
			        	builder.show();
			        } else if((tagcheck == Reason.PIN1_LOCK) || (tagcheck == Reason.PIN2_LOCK)) {
			        	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			        	builder.setMessage(R.string.pin_lock);
			        	builder.setPositiveButton("確認", null);
			        	builder.setIcon(android.R.drawable.ic_dialog_alert);
			        	builder.show();
			        } else if(tagcheck == Reason.NOT_CARD) {
			        	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			        	builder.setMessage(R.string.not_card_error);
			        	builder.setPositiveButton("確認", null);
			        	builder.setIcon(android.R.drawable.ic_dialog_alert);
			        	builder.show();
			        } else {
			        	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			        	builder.setMessage(R.string.not_card_error);
			        	builder.setPositiveButton("確認", null);
			        	builder.setIcon(android.R.drawable.ic_dialog_alert);
			        	builder.show();
			        }
		        	
		        } else {
	        	    if (decodedImage != null) {
	            	    ImageView imageView = (ImageView)findViewById(R.id.imageView1);
	            	    imageView.setImageBitmap(decodedImage);
	        	    }
		        	TextView text = (TextView) findViewById(R.id.textView2);
		        	String work;
		        	work = "名前　　："+mDriversLicenseReader.getName()+"\n"+
		        		   "ヨミガナ："+mDriversLicenseReader.getKana()+"\n"+
		        		   "通称名　："+mDriversLicenseReader.getTusyo()+"\n"+
		        		   "統一氏名："+mDriversLicenseReader.getToitsu()+"\n"+
		        		   "住所　　："+mDriversLicenseReader.getAddress()+"\n"+
		        		   "本籍　　："+mDriversLicenseReader.getHonseki()+"\n"+
		        		   "区分　　："+mDriversLicenseReader.getKubun()+"\n"+
		        		   "誕生日　："+ConvertNego(mDriversLicenseReader.getBirthDay())+"\n"+
		        		   "免許番号："+mDriversLicenseReader.getMenkyonumber()+"\n"+
		        		   "条件１　："+mDriversLicenseReader.getJyoken1()+"\n"+
		        		   "条件２　："+mDriversLicenseReader.getJyoken2()+"\n"+
		        		   "条件３　："+mDriversLicenseReader.getJyoken3()+"\n"+
		        		   "条件４　："+mDriversLicenseReader.getJyoken4()+"\n"+
		        		   "公安名　："+mDriversLicenseReader.getKoanname()+"\n"+
		        		   "二小原　："+ConvertNego(mDriversLicenseReader.getNisyogenDay())+"\n"+
		        		   "他　　　："+ConvertNego(mDriversLicenseReader.getHokadayDay())+"\n"+
		        		   "二種　　："+ConvertNego(mDriversLicenseReader.getNisyuDay())+"\n"+
		        		   "大型　　："+ConvertNego(mDriversLicenseReader.getOgataDay())+"\n"+
		        		   "普通　　："+ConvertNego(mDriversLicenseReader.getFutuDay())+"\n"+
		        		   "大特　　："+ConvertNego(mDriversLicenseReader.getDaitokuDay())+"\n"+
		        		   "大自二　："+ConvertNego(mDriversLicenseReader.getDaijiniDay())+"\n"+
		        		   "普自二　："+ConvertNego(mDriversLicenseReader.getFutujiniDay())+"\n"+
		        		   "小特　　："+ConvertNego(mDriversLicenseReader.getKotokuDay())+"\n"+
		        		   "原付　　："+ConvertNego(mDriversLicenseReader.getGentukiDay())+"\n"+
		        		   "け引　　："+ConvertNego(mDriversLicenseReader.getKeninDay())+"\n"+
		        		   "大二　　："+ConvertNego(mDriversLicenseReader.getDaijiDay())+"\n"+
		        		   "普二　　："+ConvertNego(mDriversLicenseReader.getFujiDay())+"\n"+
		        		   "大特二　："+ConvertNego(mDriversLicenseReader.getDaitokujiDay())+"\n"+
		        		   "け引二　："+ConvertNego(mDriversLicenseReader.getKeninniDay())+"\n"+
		        		   "中型　　："+ConvertNego(mDriversLicenseReader.getChuDay())+"\n"+
		        		   "中二　　："+ConvertNego(mDriversLicenseReader.getChuniDay())
		        			;
		        	text.setText(work);
		        }
	        	TextView tv1 = (TextView) findViewById(R.id.textView1);
	        	tv1.setText("読み込み完了");

		        // プログレスダイアログ終了
	            progressDialog.dismiss();
	            progressDialog=null;
	        }
	    };
	    
	    private String ConvertNego(String topone) 
	    {
	    	String ret="昭和";
	    	String one=topone.substring(0, 1);
	    	String after=topone.substring(1);
	    	String year=topone.substring(1, 3);
	    	String month=topone.substring(3, 5);
	    	String day=topone.substring(5, 7);

	    	if (one.compareTo("1")==0) {
	    		ret="明治";
	    	}
	    	if (one.compareTo("2")==0) {
	    		ret="大正";
	    	}
	    	if (one.compareTo("3")==0) {
	    		ret="昭和";
	    	}
	    	if (one.compareTo("4")==0) {
	    		ret="平成";
	    	}
	    	ret = ret + year+"年"+month+"月"+day+"日";
	    	
	    	return ret;
	    }

	    
	 
	    
}