package fr.dolistocks.app;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import androidx.fragment.app.Fragment;

import fr.dolistocks.app.ui.sync.ReadyFragment;
import fr.dolistocks.app.ui.home.HomeFragment;
import fr.dolistocks.app.ui.entry.EntryFragment;
import fr.dolistocks.app.ui.sync.SyncFragment;

public class MainActivity extends FragmentActivity {
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
                || page instanceof HomeFragment || page instanceof EntryFragment;
        findViewById(R.id.connection_status).setVisibility(showStatus ? View.VISIBLE : View.GONE);
    }
}
