package com.health.threat.awareness.hospital.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.threat.awareness.hospital.R;
import com.health.threat.awareness.hospital.adapter.AppointmentRecyclerViewAdaptor;
import com.health.threat.awareness.hospital.adapter.PatientsRecyclerViewAdaptor;
import com.health.threat.awareness.hospital.model.Appointment;
import com.health.threat.awareness.hospital.model.Patient;

import java.util.ArrayList;

public class AllPatientsFragment extends Fragment {
    public FirebaseAuth mAuth;
    ArrayList<Patient> al;
    PatientsRecyclerViewAdaptor md;
    RecyclerView rv;
    DatabaseReference databaseReference;

    public AllPatientsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_patients, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        rv = view.findViewById(R.id.rec);
        RecyclerView.LayoutManager rlm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rlm);

        mAuth = FirebaseAuth.getInstance();

        al = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Patients").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                al.clear();

                if (dataSnapshot.exists()) {
                    String HospitalID = FirebaseAuth.getInstance().getUid();
                    for (DataSnapshot eachAdRecord : dataSnapshot.getChildren()) {
                        {
                            Patient p = eachAdRecord.getValue(Patient.class);

                            assert HospitalID != null;
                            if (HospitalID.equals(p.getAddedBy()))
                                al.add(p);
                        }
                    }
                    if (!al.isEmpty()) {
                        rv.setVisibility(View.VISIBLE);
                        md = new PatientsRecyclerViewAdaptor(getActivity(), al);
                        rv.setAdapter(md);
                    } else {
                        Toast.makeText(getActivity(), "No Patients added by you", Toast.LENGTH_SHORT).show();
                        rv.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getActivity(), "No Patients added by you", Toast.LENGTH_SHORT).show();
                    rv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}