package fr.dolistocks.app;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

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
        boolean showStatus = getSupportFragmentManager().findFragmentById(R.id.page) instanceof SyncFragment;
        findViewById(R.id.connection_status).setVisibility(showStatus ? View.VISIBLE : View.GONE);
    }
}
