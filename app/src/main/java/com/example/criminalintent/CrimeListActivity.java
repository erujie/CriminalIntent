package com.example.criminalintent;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    public void onCrimeDeleted(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            finish();
        } else {
            Fragment detailFragment = getSupportFragmentManager()
                    .findFragmentById(R.id.detail_fragment_container);
            if (detailFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(detailFragment)
                        .commit();
            }
            onCrimeUpdated(crime);
        }
    }
}
