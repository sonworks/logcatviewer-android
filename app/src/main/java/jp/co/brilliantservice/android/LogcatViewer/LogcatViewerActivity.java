package jp.co.brilliantservice.android.LogcatViewer;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

public class LogcatViewerActivity extends ListActivity implements View.OnClickListener
{
    private ILogcatViewerService logcatViewerService = null;
    private boolean mUserStartedService = false;
    private boolean mUserStoppedService = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnUpdate).setOnClickListener(this);

        findViewById(R.id.btnUpdate).requestFocus();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, R.string.menu_stopservice);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if( logcatViewerService != null ) {
            menu.findItem(Menu.FIRST).setTitle(R.string.menu_stopservice);
        } else {
            menu.findItem(Menu.FIRST).setTitle(R.string.menu_startservice);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = true;
        if( item.getItemId() == Menu.FIRST ) {
            if( logcatViewerService != null ) {
                mUserStoppedService = true;
                stopLogcatService();
            } else {
                mUserStartedService = true;
                startLogcatService();
            }
        } else {
            ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        String item = (String)getListAdapter().getItem(position);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(item);
        alertDialogBuilder.setPositiveButton("OK", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    protected final void onResume() {
        super.onResume();

        if ( logcatViewerService == null ) {
            startLogcatService();
        }
    }
    
    protected void onDestroy() {
        super.onDestroy();

        stopLogcatService();
    }
    
    public void onClick(View v) {
        if( v == findViewById(R.id.btnUpdate) ) {
            updateLogDisp();
        } else if ( v == findViewById(R.id.btnSave) ) {
            saveLog();
        }
    }

    private void startLogcatService() {
        Intent intent = new Intent(this, LogcatViewerService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void stopLogcatService() {
        try {
            if( mUserStoppedService ) {
                logcatViewerService.setLogBreak();
            }
            if (null != logcatViewerService) {
                serviceConnection.onServiceDisconnected(null);
                unbindService(serviceConnection);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void saveLog() {
        if( logcatViewerService == null ) {
            Toast.makeText( getApplicationContext(), R.string.service_warning, Toast.LENGTH_SHORT ).show();
            return;
        }
        
        EditText edt = (EditText)findViewById(R.id.edtFileName);
        try {
            int ret = logcatViewerService.saveLog(edt.getText().toString());
            int resid = R.string.save_complete;
            if( ret < 0 ) {
                resid = R.string.save_failed;
            }
            Toast.makeText( getApplicationContext(), resid, Toast.LENGTH_SHORT ).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
        
    private void updateLogDisp() {
        if( logcatViewerService == null ) {
            Toast.makeText( getApplicationContext(), R.string.service_warning, Toast.LENGTH_SHORT ).show();
            return;
        }
        
        try {
            List<String> list = logcatViewerService.getDispData();
            ArrayAdapter<String> logList = new ArrayAdapter<String>(this, R.layout.list_row, list);
            setListAdapter(logList);
            getListView().setSelection(this.getListView().getCount()-1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private LogcatViewerServiceConnection serviceConnection = new LogcatViewerServiceConnection();
    private class LogcatViewerServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder service) {
            logcatViewerService = ILogcatViewerService.Stub.asInterface(service);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            LogcatViewerActivity.this.updateLogDisp();
            if( mUserStartedService ) {
                Toast.makeText( LogcatViewerActivity.this, R.string.service_started, Toast.LENGTH_SHORT ).show();
                mUserStartedService = false;
            }
        }

        public void onServiceDisconnected (ComponentName name) {
            logcatViewerService = null;
            if( mUserStoppedService ) {
                Toast.makeText( LogcatViewerActivity.this, R.string.service_stopped, Toast.LENGTH_SHORT ).show();
                mUserStoppedService = false;
            }
        }

    }
}

