package com.health.threat.awareness.hospital.dialog_fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.threat.awareness.hospital.R;
import com.health.threat.awareness.hospital.model.Appointment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Objects;

public class AppointmentDetailDialogFragment extends DialogFragment {
    ImageButton cancelBtn;
    Appointment appointment;

    TextView HospitalName, DateAndTimeTV;
    TextView Sickness_title, Symptoms_description;
    TextView PatientName,PatientEmail,PatientPhone;
    TextView tvLatitude,tvLongitude,tvAltitude;
    Button btnSendToAdmin, btnCaseClose; //date_time_set
    String date, month, year, hour, minutes;
    EditText RemarksTV;
    View RemarksView;
    Spinner StatusSpinner;

    public AppointmentDetailDialogFragment(Appointment a) {
        // Required empty public constructor
        appointment = a;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroy() {
        if (getDialog() != null && getDialog().isShowing())
            getDialog().dismiss();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_detail_dialog, container, false);

        date = month = year = hour = minutes = null;

        cancelBtn = view.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        tvLatitude = view.findViewById(R.id.tvLatitude);
        tvLongitude = view.findViewById(R.id.tvLongitude);
        tvAltitude = view.findViewById(R.id.tvAltitude);
        HospitalName = view.findViewById(R.id.HospitalName);
        Sickness_title = view.findViewById(R.id.Sickness_title);
        Symptoms_description = view.findViewById(R.id.Symptoms_description);
        DateAndTimeTV = view.findViewById(R.id.DateAndTimeTV);
        PatientName = view.findViewById(R.id.PatientName);
        PatientEmail = view.findViewById(R.id.PatientEmail);
        PatientPhone = view.findViewById(R.id.PatientPhone);
        //RemarksView = view.findViewById(R.id.RemarksView);
        RemarksTV = view.findViewById(R.id.RemarksTV);
        StatusSpinner =view.findViewById(R.id.StatusSpinner);

        String[] statusSpinners={"Select","Normal","Virus","Emergency"};

        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,statusSpinners);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        StatusSpinner.setAdapter(aa);
        StatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*if (position!=0 && position!=1)
                {
                    //Show Remarks for Admin
                    RemarksView.setVisibility(View.VISIBLE);
                }else
                    RemarksView.setVisibility(View.GONE);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //date_time_set = view.findViewById(R.id.date_time_set);
        btnSendToAdmin = view.findViewById(R.id.btnSendToAdmin);
        btnCaseClose = view.findViewById(R.id.btnCaseClose);

        if (appointment.getDate() != null && !appointment.getDate().equals(""))
            DateAndTimeTV.setText("" + appointment.getDate() + "-" + appointment.getMonth() + "-" + appointment.getYear() + " " + appointment.getHour() + ":" + appointment.getMinutes());
        else
            DateAndTimeTV.setVisibility(View.GONE);

        if (appointment.getHospitalName() != null && !appointment.getHospitalName().equals(""))
            HospitalName.setText("" + appointment.getHospitalName());
        else
            HospitalName.setVisibility(View.GONE);

        if (appointment.getSickness_title() != null && !appointment.getSickness_title().equals(""))
            Sickness_title.setText("" + appointment.getSickness_title());
        else
            Sickness_title.setVisibility(View.GONE);

        if (appointment.getSymptoms_description() != null && !appointment.getSymptoms_description().equals(""))
            Symptoms_description.setText("" + appointment.getSymptoms_description());
        else
            Symptoms_description.setVisibility(View.GONE);

        if (appointment.getLatitude() != null && !appointment.getLatitude().equals(""))
            tvLatitude.setText("" + appointment.getLatitude());
        else
            tvLatitude.setVisibility(View.GONE);

        if (appointment.getLongitude() != null && !appointment.getLongitude().equals(""))
            tvLongitude.setText("" + appointment.getLongitude());
        else
            tvLongitude.setVisibility(View.GONE);

        if (appointment.getAltitude() != null && !appointment.getAltitude().equals(""))
            tvAltitude.setText("" + appointment.getAltitude());
        else
            tvAltitude.setVisibility(View.GONE);

        if (appointment.getCaseStatus() != null && !appointment.getCaseStatus().equals(""))
            StatusSpinner.setSelection(aa.getPosition("" + appointment.getCaseStatus()));

        if (appointment.getRemarks() != null && !appointment.getRemarks().equals(""))
            RemarksTV.setText("" + appointment.getRemarks());
        //else
            //RemarksTV.setVisibility(View.GONE);

        FirebaseDatabase.getInstance().getReference().child("AppUsers").child(appointment.getBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String patientName = snapshot.child("Name").getValue(String.class);
                    String patientMobile = snapshot.child("Mobile").getValue(String.class);
                    String patientEmail = snapshot.child("Email").getValue(String.class);

                    PatientName.setText(patientName + "");
                    PatientEmail.setText(patientEmail + "");
                    PatientPhone.setText(patientMobile + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnSendToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RemarksTV.getText() == null || RemarksTV.getText().toString().equals("")) {
                    Toast.makeText(requireActivity(), "Remarks is Required", Toast.LENGTH_SHORT).show();
                    RemarksTV.requestFocus();
                    RemarksTV.setError("Required");
                    return;
                }
                if (StatusSpinner.getSelectedItemPosition() == 0)
                {
                    Toast.makeText(requireActivity(), "Select Case Status First", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(requireActivity(), "Please wait...", Toast.LENGTH_SHORT).show();

                UpdateAppointmentToDatabase();
            }
        });

        btnCaseClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StatusSpinner.getSelectedItemPosition()==0) {
                    Toast.makeText(requireActivity(), "Please set Case Status Before Closing", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (RemarksTV.getText() == null || RemarksTV.getText().toString().equals("")) {
                    Toast.makeText(requireActivity(), "Remarks is Required", Toast.LENGTH_SHORT).show();
                    RemarksTV.requestFocus();
                    RemarksTV.setError("Required");
                    return;
                }


                Toast.makeText(getActivity(), "Please wait, marking Case as "+StatusSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                CloseCaseAppointment();
            }
        });

        return view;
    }

    private void UpdateAppointmentToDatabase() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("CaseStatus", StatusSpinner.getSelectedItem().toString());
        data.put("Remarks", RemarksTV.getText().toString());
        data.put("SendToAdmin", true);

        DatabaseReference ownerRef;
        ownerRef = FirebaseDatabase.getInstance().getReference().child("Appointments");

        ownerRef.child(appointment.getID()).updateChildren(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireActivity(), "Appointment updated successfully", Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(requireActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(requireActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void CloseCaseAppointment() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("CaseStatus", StatusSpinner.getSelectedItem().toString());
        data.put("Remarks", RemarksTV.getText().toString());

        DatabaseReference ownerRef;
        ownerRef = FirebaseDatabase.getInstance().getReference().child("Appointments");

        ownerRef.child(appointment.getID()).updateChildren(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireActivity(), "Appointment Updated successfully", Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(requireActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(requireActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}