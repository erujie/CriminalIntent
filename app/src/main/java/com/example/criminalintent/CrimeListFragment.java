package com.example.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CrimeListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private LinearLayout mEmptyView;
    private Button mAddFirstCrimeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private int mLastUpdatedPosition = -1;

    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_POLICE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mEmptyView = (LinearLayout) view.findViewById(R.id.empty_view);
        mAddFirstCrimeButton = (Button) view.findViewById(R.id.add_first_crime_button);
        mAddFirstCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCrime();
            }
        });

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem newCrimeItem = menu.findItem(R.id.new_crime);
        if (newCrimeItem != null) {
            boolean canAddMore = CrimeLab.get(getActivity()).getCrimes().size() < 5;
            // Always keep enabled so it is clickable and can show the limit message Toast
            newCrimeItem.setEnabled(true);
            if (newCrimeItem.getIcon() != null) {
                newCrimeItem.getIcon().setAlpha(canAddMore ? 255 : 128);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.new_crime:
//                Crime crime = new Crime();
//                CrimeLab.get(getActivity()).addCrime(crime);
//                Intent intent = CrimePagerActivity
//                        .newIntent(getActivity(), crime.getId());
//                startActivity(intent);
//                return true;
//            case R.id.show_subtitle:
//                mSubtitleVisible = !mSubtitleVisible;
//                getActivity().invalidateOptionsMenu();
//                updateSubtitle();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.new_crime) {
            createNewCrime();
            return true;
        } else if (id == R.id.show_subtitle) {
            mSubtitleVisible = !mSubtitleVisible;
            getActivity().invalidateOptionsMenu();
            updateSubtitle();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void createNewCrime() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        if (crimeLab.getCrimes().size() >= 5) {
            Toast.makeText(getActivity(), R.string.crime_limit_reached, Toast.LENGTH_SHORT).show();
            return;
        }

        Crime crime = new Crime();
        crimeLab.addCrime(crime);
        Intent intent = CrimePagerActivity
                .newIntent(getActivity(), crime.getId());
        startActivity(intent);
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setSubtitle(subtitle);
        }
    }

/////////////////chapter 10 challenge
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (crimes.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
            mCrimeRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mCrimeRecyclerView.setVisibility(View.VISIBLE);
        }

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
            mLastUpdatedPosition = -1;
        }
        updateSubtitle();
        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

//    private void updateUI() {
//        CrimeLab crimeLab = CrimeLab.get(getActivity());
//        List<Crime> crimes = crimeLab.getCrimes();
//        mAdapter = new CrimeAdapter(crimes);
//        mCrimeRecyclerView.setAdapter(mAdapter);
//
//        if (mAdapter == null) {
//            mAdapter = new CrimeAdapter(crimes);
//            mCrimeRecyclerView.setAdapter(mAdapter);
//        } else {
//            mAdapter.notifyDataSetChanged();
//        }
//    }
/////////////////////////////////////
    private class CrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());

            String formattedDate = DateFormat.format("EEEE, MMM dd, yyyy", mCrime.getDate()).toString();
            mDateTextView.setText(formattedDate);

            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);

            if (crime.isSolved()) {
                itemView.setBackgroundColor(0xFFE0E0E0);
                itemView.setAlpha(0.7f);
            } else {
                itemView.setBackgroundColor(0xFFFFFFFF);
                itemView.setAlpha(1.0f);
            }
        }

        @Override
        public void onClick(View view) {
            mLastUpdatedPosition = getBindingAdapterPosition();
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }

    private class CrimePoliceHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Button mContactPoliceButton;

        public CrimePoliceHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime_police, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mContactPoliceButton = (Button) itemView.findViewById(R.id.contact_police_button);

            mContactPoliceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            "Contacting police for: " + mCrime.getTitle(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());

            String formattedDate = DateFormat.format("EEEE, MMM dd, yyyy", mCrime.getDate()).toString();
            mDateTextView.setText(formattedDate);

            if (crime.isSolved()) {
                itemView.setBackgroundColor(0xFFD0D0D0);
                itemView.setAlpha(0.7f);
            } else {
                itemView.setBackgroundColor(0xFFFFFFFF);
                itemView.setAlpha(1.0f);
            }
        }

//////////////////chapter 10 challenge
        @Override
        public void onClick(View view) {
            mLastUpdatedPosition = getBindingAdapterPosition();
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }

//        @Override
//        public void onClick(View view) {
//            Toast.makeText(getActivity(),
//                            mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT)
//                    .show();
//        }
///////////////////////////////////////////////////////////////////////////////////////

    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimes.get(position);
            if (crime.isRequiresPolice()) {
                return VIEW_TYPE_POLICE;
            } else {
                return VIEW_TYPE_NORMAL;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            if (viewType == VIEW_TYPE_POLICE) {
                return new CrimePoliceHolder(layoutInflater, parent);
            } else {
                return new CrimeHolder(layoutInflater, parent);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);

            if (holder instanceof CrimePoliceHolder) {
                ((CrimePoliceHolder) holder).bind(crime);
            } else if (holder instanceof CrimeHolder) {
                ((CrimeHolder) holder).bind(crime);
            }
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
