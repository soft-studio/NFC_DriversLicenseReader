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
                s1 = edittext1.getText().toString();
                s2 = edittext2.getText().toString();
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
    
    
    private void showDialogYESNO(String msg)
    {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setPositiveButton("はい",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
    
    
}