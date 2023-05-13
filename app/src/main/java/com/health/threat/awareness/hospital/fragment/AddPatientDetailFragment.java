package com.health.threat.awareness.hospital.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.health.threat.awareness.hospital.R;
import com.health.threat.awareness.hospital.model.Patient;

public class AddPatientDetailFragment extends Fragment {
    private EditText fullNameInput;
    private EditText dateOfBirthInput;

    private EditText ageInput;
    private EditText weightInput;
    private EditText heightInput;
    private EditText bloodPressureInput;
    private EditText pulseRateInput;
    private EditText respiratoryRateInput;
    private EditText bodyTemperatureInput;
    private EditText latitudeInput;
    private EditText longitudeInput;
    private EditText altitudeInput;
    private EditText contactInput;
    private EditText insuranceInput;
    private EditText historyInput,hrInput;
    private EditText symptoms_input;
    RadioGroup grp;
    
    public AddPatientDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_patient_detail, container, false);

        // Get references to all the EditText views
        fullNameInput = view.findViewById(R.id.name_input);
        dateOfBirthInput = view.findViewById(R.id.dob_input);
        ageInput = view.findViewById(R.id.age_input);
        weightInput = view.findViewById(R.id.weight_input);
        heightInput = view.findViewById(R.id.height_input);
        bloodPressureInput = view.findViewById(R.id.blood_pressure_input);
        pulseRateInput = view.findViewById(R.id.pulse_rate_input);
        respiratoryRateInput = view.findViewById(R.id.respiratory_rate_input);
        bodyTemperatureInput = view.findViewById(R.id.body_temperature_input);
        latitudeInput = view.findViewById(R.id.latitude_input);
        longitudeInput = view.findViewById(R.id.longitude_input);
        altitudeInput = view.findViewById(R.id.altitude_input);
        symptoms_input = view.findViewById(R.id.symptoms_input);
        contactInput = view.findViewById(R.id.contact_info_input);
        hrInput = view.findViewById(R.id.heart_rate_input);
        insuranceInput = view.findViewById(R.id.insurance_details_input);
        historyInput = view.findViewById(R.id.medical_history_input);
        grp = view.findViewById(R.id.gender_radio_group);

        // Set a click listener for the "Save" button
        Button saveButton = view.findViewById(R.id.save_button);

        // Set an onClickListener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validate())
                {
                    String name = fullNameInput.getText().toString();
                    String dateOfBirth = dateOfBirthInput.getText().toString();

                    String gender = "";
                    double bloodPressure = 0;

                    if (grp.getCheckedRadioButtonId()!=-1) {
                        RadioButton selectedGenderBtn = grp.findViewById(grp.getCheckedRadioButtonId());
                        gender = selectedGenderBtn.getText().toString();
                    }
                    String contactInformation = contactInput.getText().toString();
                    String insuranceDetails = insuranceInput.getText().toString();
                    String medicalHistory = historyInput.getText().toString();
                    String symptoms = symptoms_input.getText().toString();
                    if (bloodPressureInput.getText()!=null && !bloodPressureInput.getText().toString().equals(""))
                        bloodPressure = Double.parseDouble(bloodPressureInput.getText().toString());
                    int heartRate = Integer.parseInt(hrInput.getText().toString());
                    double temperature = Double.parseDouble(bodyTemperatureInput.getText().toString());
                    int respiratoryRate = Integer.parseInt(respiratoryRateInput.getText().toString());
                    double latitude = Double.parseDouble(latitudeInput.getText().toString());
                    double longitude = Double.parseDouble(longitudeInput.getText().toString());
                    double altitude = Double.parseDouble(altitudeInput.getText().toString());
                    int age = Integer.parseInt(ageInput.getText().toString());
                    double weight = Double.parseDouble(weightInput.getText().toString());
                    double height = Double.parseDouble(heightInput.getText().toString());
                    int pulseRate = Integer.parseInt(pulseRateInput.getText().toString());
                    double bodyTemperature = Double.parseDouble(bodyTemperatureInput.getText().toString());

                    String UID = FirebaseAuth.getInstance().getUid();

                    // Create a new Patient object with the entered values
                    Patient patient = new Patient(name, dateOfBirth, gender, contactInformation, insuranceDetails, medicalHistory, bloodPressure, heartRate, temperature, respiratoryRate, latitude, longitude, altitude, age, weight, height, pulseRate, bodyTemperature,UID,symptoms);

                    DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("Patients");

                    String mGroupId = ref.push().getKey();

                    ref.child(mGroupId).setValue(patient);

                    // Clear the EditText views
                    fullNameInput.setText("");
                    ageInput.setText("");
                    weightInput.setText("");
                    heightInput.setText("");
                    symptoms_input.setText("");
                    bloodPressureInput.setText("");
                    pulseRateInput.setText("");
                    respiratoryRateInput.setText("");
                    bodyTemperatureInput.setText("");
                    latitudeInput.setText("");
                    longitudeInput.setText("");
                    altitudeInput.setText("");

                    // Show a toast message to indicate successful save
                    Toast.makeText(getActivity(), "Patient data saved!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return view;
    }

    private boolean Validate() {
        if (fullNameInput.getText()==null || fullNameInput.getText().toString().equals(""))
        {
            fullNameInput.setError("Full Name required");
            fullNameInput.requestFocus();
            return false;
        }

        if (ageInput.getText()==null || ageInput.getText().toString().equals(""))
        {
            ageInput.setError("Age Input required");
            ageInput.requestFocus();
            return false;
        }

        if (weightInput.getText()==null || weightInput.getText().toString().equals(""))
        {
            weightInput.setError("Weight Input required");
            weightInput.requestFocus();
            return false;
        }

        if (heightInput.getText()==null || heightInput.getText().toString().equals(""))
        {
            heightInput.setError("Height Input required");
            heightInput.requestFocus();
            return false;
        }

        if (symptoms_input.getText()==null || symptoms_input.getText().toString().equals(""))
        {
            symptoms_input.setError("Symptoms Input required");
            symptoms_input.requestFocus();
            return false;
        }

        if (dateOfBirthInput.getText()==null || dateOfBirthInput.getText().toString().equals(""))
        {
            dateOfBirthInput.setError("Date Of Birth required");
            dateOfBirthInput.requestFocus();
            return false;
        }

        if (grp.getCheckedRadioButtonId()==-1) {
            Toast.makeText(getActivity(), "Gender is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (contactInput.getText()==null || contactInput.getText().toString().equals(""))
        {
            contactInput.setError("Contact required");
            contactInput.requestFocus();
            return false;
        }

        if (insuranceInput.getText()==null || insuranceInput.getText().toString().equals(""))
        {
            insuranceInput.setError("Insurance Detail Input required");
            insuranceInput.requestFocus();
            return false;
        }

        if (historyInput.getText()==null || historyInput.getText().toString().equals(""))
        {
            historyInput.setError("Medical history Input required");
            historyInput.requestFocus();
            return false;
        }

        if (bloodPressureInput.getText()==null || bloodPressureInput.getText().toString().equals(""))
        {
            bloodPressureInput.setError("Blood Pressure Input required");
            bloodPressureInput.requestFocus();
            return false;
        }

        if (pulseRateInput.getText()==null || pulseRateInput.getText().toString().equals(""))
        {
            pulseRateInput.setError("Pulse Rate Input required");
            pulseRateInput.requestFocus();
            return false;
        }

        if (respiratoryRateInput.getText()==null || respiratoryRateInput.getText().toString().equals(""))
        {
            respiratoryRateInput.setError("Respiratory Rate Input required");
            respiratoryRateInput.requestFocus();
            return false;
        }

        if (hrInput.getText()==null || hrInput.getText().toString().equals(""))
        {
            hrInput.setError("Heart rate Input Input required");
            hrInput.requestFocus();
            return false;
        }

        if (bodyTemperatureInput.getText()==null || bodyTemperatureInput.getText().toString().equals(""))
        {
            bodyTemperatureInput.setError("Body Temperature Input required");
            bodyTemperatureInput.requestFocus();
            return false;
        }

        if (latitudeInput.getText()==null || latitudeInput.getText().toString().equals(""))
        {
            latitudeInput.setError("Latitude Input required");
            latitudeInput.requestFocus();
            return false;
        }

        if (longitudeInput.getText()==null || longitudeInput.getText().toString().equals(""))
        {
            longitudeInput.setError("Longitude Input required");
            longitudeInput.requestFocus();
            return false;
        }

        if (altitudeInput.getText()==null || altitudeInput.getText().toString().equals(""))
        {
            altitudeInput.setError("Altitude Input required");
            altitudeInput.requestFocus();
            return false;
        }

        return true;
    }
}