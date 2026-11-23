package fr.dolistocks.app;

import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;
import androidx.lifecycle.MutableLiveData;
import fr.dolistocks.app.ui.workflow.WorkflowFragment;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import androidx.fragment.app.Fragment;

import fr.dolistocks.app.ui.sync.ReadyFragment;
import fr.dolistocks.app.ui.sync.SyncFragment;

public class MainActivity extends FragmentActivity {
    public final MutableLiveData<Boolean> connected = new MutableLiveData<>();
    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshNetwork();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        refreshNetwork();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkReceiver);
        super.onStop();
    }

    private void refreshNetwork() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean available;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            available = manager.getActiveNetwork() != null;
        } else {
            NetworkInfo network = manager.getActiveNetworkInfo();
            available = network != null && network.isConnected();
        }
        if (!Boolean.valueOf(available).equals(connected.getValue())) {
            connected.setValue(available);
            updateHeader();
        }
    }
    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().addOnBackStackChangedListener(this::updateHeader);
        updateHeader();
    }

    private void updateHeader() {
        Fragment page = getSupportFragmentManager().findFragmentById(R.id.page);
        boolean showStatus = page instanceof SyncFragment || page instanceof ReadyFragment
                || page instanceof WorkflowFragment;
        TextView status = findViewById(R.id.connection_status);
        status.setVisibility(showStatus ? View.VISIBLE : View.GONE);
        status.setText(Boolean.FALSE.equals(connected.getValue()) ? R.string.offline : R.string.sync_connected);
    }
}
