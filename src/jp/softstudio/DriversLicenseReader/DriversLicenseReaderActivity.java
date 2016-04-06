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


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class DriversLicenseReaderActivity extends Activity {
    /** Called when the activity is first created. */
	
	private String s1;
	private String s2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	//縦固定
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  	//スリープしない

        setContentView(R.layout.main);
 /*
        //アプリケーションで共通に利用するオブジェクトには、メモリリークが発生しないようにthisではなく  
        //Context.getApplicationContext()を使用します。  
        Context context = this.getApplicationContext();   
        //キャッチされない例外により、スレッドが突然終了したときや、  
        //このスレッドに対してほかにハンドラが定義されていないときに  
        //呼び出されるデフォルトのハンドラを設定します。  
        Thread.setDefaultUncaughtExceptionHandler(new CsUncaughtExceptionHandler(context));  
*/
        CheckBox cb=(CheckBox)findViewById(R.id.checkBox1);
        cb.setChecked(true);
        cb.setEnabled(false);
        
        Button button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener () {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText edittext1 = (EditText)findViewById(R.id.editText1);
                EditText edittext2 = (EditText)findViewById(R.id.editText2);
                CheckBox cb1=(CheckBox)findViewById(R.id.checkBox1);
                CheckBox cb2=(CheckBox)findViewById(R.id.checkBox2);
                s1 = edittext1.getText().toString();
                s2 = edittext2.getText().toString();
                if (((s1.length()==0) && (cb1.isChecked())) && ((s2.length()==0) && (cb2.isChecked()))) {
                	// 両方有効でかつ未設定
                	showDialogConf1("暗証番号１・２が未入力です。\n読み込みを続けますか？");
                	return;
                } else if (((s1.length()==0) && (cb1.isChecked())) || ((s2.length()==0) && (cb2.isChecked()))) {
                	// どちらかが有効でかつ未設定
                	if ((s1.length()==0) && (cb1.isChecked())) {
                		//暗証番号１が未設定
                    	showDialogConf1("暗証番号１が未入力です。\n読み込みを続けますか？");
                    	return;
                	}
                	if ((s2.length()==0) && (cb2.isChecked())) {
                		//暗証番号２が未設定
                    	showDialogConf1("暗証番号２が未入力です。\n読み込みを続けますか？");
                    	return;
                	}
                }
                
                /*                if ((s2.length()==0)) {
                	showDialogYESNO("暗証番号２が未入力です。\n読み込みを続けますか？");
                	return;
                }*/
                startReader();
                
			}
        	
        });
        
    }
    
    private void startReader()
    {
        CheckBox cb1=(CheckBox)findViewById(R.id.checkBox1);
        CheckBox cb2=(CheckBox)findViewById(R.id.checkBox2);
        
    	if (((s1.length()!=4) && (cb1.isChecked())) || ((s2.length()!=4) && (cb2.isChecked()))) {
         	showDialogOK("暗証番号は４桁です。");
         	return;
         }
         
         Context context = getApplicationContext();
         Intent intent = new Intent(context, ReaderActivity.class);
         intent.putExtra("pass1", s1);
         intent.putExtra("pass2", s2);
         intent.putExtra("pass1use", cb1.isChecked());
         intent.putExtra("pass2use", cb2.isChecked());
         
         startActivity(intent);

    }
    
    private void showDialogOK(String msg)
    {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setPositiveButton("確認",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();    	
    }
    
    
    private void showDialogConf1(String msg)
    {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setPositiveButton("はい",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	//暗証番号空白を"****"にする
                        CheckBox cb1=(CheckBox)findViewById(R.id.checkBox1);
                        CheckBox cb2=(CheckBox)findViewById(R.id.checkBox2);
                    	if ((s1.length()==0) && (cb1.isChecked())) {
                    		//暗証番号１が未設定
                    		s1="****";
                    	}
                    	if ((s2.length()==0) && (cb2.isChecked())) {
                    		//暗証番号２が未設定
                    		s2="****";
                    	}
                    	startReader();
                    }
                });
        // アラートダイアログの否定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setNegativeButton("いいえ",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();    	
    }
    

    @Override  
    protected void onStart() {  
        super.onStart();  
//        CsUncaughtExceptionHandler.SendBugReport(this);  
    } 
}