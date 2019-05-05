package fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.retrofit.R;
import com.example.retrofit.activities.MainActivity;
import com.example.retrofit.activities.ProfileActivity;
import com.example.retrofit.api.RetrofitClient;
import com.example.retrofit.model.DefaultResponse;
import com.example.retrofit.model.LoginResponse;
import com.example.retrofit.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import storage.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {


    private EditText currntPassword,newPassword,editTextEmail,editTextName;
    private Button changePassword,logoutButton,delete,update;


    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_setting, container, false);


        currntPassword=view.findViewById(R.id.editTextCurrentPassword);
        newPassword=view.findViewById(R.id.editTextNewPassword);
        editTextEmail=view.findViewById(R.id.editTextEmail);
        editTextName=view.findViewById(R.id.editTextName);


        changePassword=view.findViewById(R.id.buttonChangePassword);
        logoutButton=view.findViewById(R.id.buttonLogout);
        delete=view.findViewById(R.id.buttonDelete);
        update=view.findViewById(R.id.buttonSave);
        changePassword.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        delete.setOnClickListener(this);
        update.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.buttonSave:
                updateProfile();
                break;
            case R.id.buttonChangePassword:
                updatePassword();
                break;

            case R.id.buttonDelete:
                deleteUser();
                break;

            case R.id.buttonLogout:
                logout();
                break;

        }

    }

    private void deleteUser() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Are You Sure ?");
        builder.setMessage("This Action is irrevisiable...");
        builder.setPositiveButton("YES" ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User user=SharedPrefManager.getInstance(getActivity()).getUser();
                Call<DefaultResponse> call=RetrofitClient.getInstance().getApi().deleteUser(user.getId());
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (!response.body().isErr()){
                            SharedPrefManager.getInstance(getActivity()).clear();
                            Intent intent=new Intent(getActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }

                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {

                    }
                });
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog ad=builder.create();
        ad.show();
    }

    private void logout() {
        SharedPrefManager.getInstance(getActivity()).clear();
        Intent intent=new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void updatePassword() {
        String currPass=currntPassword.getText().toString().trim();
        String newPass=newPassword.getText().toString().trim();

        if (currPass.isEmpty()){
            currntPassword.setError("Current Password Required");
            currntPassword.requestFocus();
            return;
        }

        if (newPass.isEmpty()){
            newPassword.setError("New Password Required");
            newPassword.requestFocus();
            return;
        }

        User user=SharedPrefManager.getInstance(getActivity()).getUser();
        Call<DefaultResponse> call=RetrofitClient
                .getInstance()
                .getApi()
                .updatePassword(currPass,newPass,user.getEmail());


        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    private void updateProfile() {
        String email=editTextEmail.getText().toString().trim();
        String name=editTextName.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }



        if (name.isEmpty()){
            editTextName.setError("Enter a Name");
            editTextName.requestFocus();
            return;
        }

        User user= SharedPrefManager.getInstance(getActivity()).getUser();

        Call<LoginResponse> call= RetrofitClient
                .getInstance()
                .getApi()
                .updateUser(
                        user.getId(),email,name
                );

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse=response.body();

                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                if (!loginResponse.getError()){
                    SharedPrefManager.getInstance(getActivity()).saveUser(response.body().getUser());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });


    }
}
