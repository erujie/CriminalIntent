package com.example.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;
    private Map<UUID, Crime> mCrimeMap;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        mCrimeMap = new HashMap<>();
    }

    public void addCrime(Crime c) {
        mCrimes.add(c);
        mCrimeMap.put(c.getId(), c);
    }

    public void deleteCrime(Crime crime) {
        mCrimes.remove(crime);
        mCrimeMap.remove(crime.getId());
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

/// //////////////////////ch10 chal
    public Crime getCrime(UUID id) {
        return mCrimeMap.get(id);
    }

//    public Crime getCrime(UUID id) {
//        for (Crime crime : mCrimes) {
//            if (crime.getId().equals(id)) {
//                return crime;
//            }
//        }
//        return null;
//    }
///////////////////////

}