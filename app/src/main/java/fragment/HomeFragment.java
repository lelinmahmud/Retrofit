package fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit.R;
import com.example.retrofit.model.Temperature;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import storage.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    TextView email,name;
    EditText tempEditText,phEditText;
    Button saveButton;
    DatabaseReference databaseReference;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        email=view.findViewById(R.id.textViewEmail);
        name=view.findViewById(R.id.textViewName);
        tempEditText=view.findViewById(R.id.inputTemp);
        phEditText=view.findViewById(R.id.inputPh);
        saveButton=view.findViewById(R.id.saveButton);
        databaseReference= FirebaseDatabase.getInstance().getReference(SharedPrefManager.getInstance(getActivity()).getUser().getName());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });

        email.setText(SharedPrefManager.getInstance(getActivity()).getUser().getEmail());
        name.setText(SharedPrefManager.getInstance(getActivity()).getUser().getName());


        return view;
    }


    public void sendData(){
        String temp=tempEditText.getText().toString().trim();
        String ph=phEditText.getText().toString().trim();

        if(temp.isEmpty()){
            tempEditText.setError("Enter a temperature");
            tempEditText.requestFocus();
            return;
        }
        if(ph.isEmpty()){
            phEditText.setError("Enter a Ph");
            phEditText.requestFocus();
            return;
        }

        String key=databaseReference.push().getKey();

        Temperature temperature=new Temperature(temp,ph);

        databaseReference.child(key).setValue(temperature);
        tempEditText.setText("");
        phEditText.setText("");
        Toast.makeText(getActivity(), "Data Stored", Toast.LENGTH_SHORT).show();


    }

}
