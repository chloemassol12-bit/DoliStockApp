package fr.dolistocks.app;

import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;
import android.widget.PopupMenu;
import android.view.MenuItem;
import fr.dolistocks.app.ui.home.HomeFragment;
import fr.dolistocks.app.ui.workflow.PendingFragment;
import fr.dolistocks.app.ui.workflow.SettingsFragment;
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
        findViewById(R.id.navigation_menu).setOnClickListener(this::showNavigation);
        getSupportFragmentManager().addOnBackStackChangedListener(this::updateHeader);
        updateHeader();
    }

    private void updateHeader() {
        Fragment page = getSupportFragmentManager().findFragmentById(R.id.page);
        findViewById(R.id.navigation_menu).setVisibility(page instanceof WorkflowFragment ? View.VISIBLE : View.GONE);
        boolean showStatus = page instanceof SyncFragment || page instanceof ReadyFragment
                || page instanceof WorkflowFragment;
        TextView status = findViewById(R.id.connection_status);
        status.setVisibility(showStatus ? View.VISIBLE : View.GONE);
        status.setText(Boolean.FALSE.equals(connected.getValue()) ? R.string.offline : R.string.sync_connected);
    }

    private void showNavigation(View anchor) {
        PopupMenu menu = new PopupMenu(this, anchor);
        addDestination(menu, R.string.home, HomeFragment.class);
        addDestination(menu, R.string.lists, PendingFragment.class);
        addDestination(menu, R.string.settings, SettingsFragment.class);
        menu.show();
    }

    private void addDestination(PopupMenu menu, int label, Class<? extends Fragment> destination) {
        MenuItem item = menu.getMenu().add(label);
        item.setEnabled(!destination.isInstance(getSupportFragmentManager().findFragmentById(R.id.page)));
        item.setOnMenuItemClickListener(selected -> {
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .replace(R.id.page, destination, null).addToBackStack(null).commit();
            return true;
        });
    }
}
