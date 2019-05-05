package fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retrofit.R;
import com.example.retrofit.api.RetrofitClient;
import com.example.retrofit.model.Temperature;
import com.example.retrofit.model.User;
import com.example.retrofit.model.UserResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapter.Adapter_2;
import adapter.UserAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import storage.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> userList;
    private UserAdapter adapter;
    private List<Temperature> temperatureList;
    private List<Temperature> temp2;
    private Adapter_2 adapter_2;
    DatabaseReference databaseReference;


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);
        databaseReference= FirebaseDatabase.getInstance().getReference(SharedPrefManager.getInstance(getActivity()).getUser().getName());
        temperatureList=new ArrayList<>();
        temp2=new ArrayList<>();
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter_2=new Adapter_2(getActivity(),temperatureList);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                temperatureList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Temperature temperature=dataSnapshot1.getValue(Temperature.class);
                    temperatureList.add(temperature);
                }
                Collections.reverse(temperatureList);
                recyclerView.setAdapter(adapter_2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        Call<UserResponse> call= RetrofitClient.getInstance().getApi().getUsers();
//
//        call.enqueue(new Callback<UserResponse>() {
//            @Override
//            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                userList=response.body().getUsers();
//                adapter=new UserAdapter(getActivity(),userList);
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onFailure(Call<UserResponse> call, Throwable t) {
//
//            }
//        });

        return view;
    }



}
