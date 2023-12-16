package com.example.dietapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dietapp.dtos.GetUserInformationDto;
import com.example.dietapp.interfaces.IUserInformation;
import com.example.dietapp.dtos.SharedId;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NutrientsFragment extends Fragment {
    TextView carbonhydrate;
    TextView fat;
    TextView protein;
    TextView calorie;
    ListView listBreakfast, listLunch, listDinner;
    Button listBreakfastItems, listLunchItems, listDinnerItems;
    SharedId sharedId = SharedId.getInstance();
    int appUserId = sharedId.getSharedData();
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nutrients,container,false);
        carbonhydrate = rootView.findViewById(R.id.daytVCarbNutrients);
        fat = rootView.findViewById(R.id.daytVFatNutrients);
        protein = rootView.findViewById(R.id.daytVProteinNutrients);
        calorie = rootView.findViewById(R.id.daytVCalNutrients);
        listBreakfast = (ListView) rootView.findViewById(R.id.listBreakfast);
        listBreakfastItems = (Button) rootView.findViewById(R.id.breakfastButton);
        listLunchItems = (Button) rootView.findViewById(R.id.lunchButton);
        listLunch = (ListView) rootView.findViewById(R.id.listLunch);
        listDinnerItems = (Button) rootView.findViewById(R.id.dinnerButton);
        listDinner = (ListView) rootView.findViewById(R.id.listDinner);
        List<String> foods = Arrays.asList("Kebap", "Pide", "Lahmacun");
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        IUserInformation userInformation = RetrofitClient.getRetrofitInstance().create(IUserInformation.class);
        Call<GetUserInformationDto> call = userInformation.getUserInformationWithDailyInformation(appUserId);
        call.enqueue(new Callback<GetUserInformationDto>() {
            @Override
            public void onResponse(Call<GetUserInformationDto> call, Response<GetUserInformationDto> response) {
                if(response.isSuccessful()){
                    GetUserInformationDto model = response.body();
                    carbonhydrate.setText(decimalFormat.format(model.getDailyCarbonhydrateRequirement()));
                    fat.setText(decimalFormat.format(model.getDailyFatRequirement()));
                    protein.setText(decimalFormat.format(model.getDailyProteinRequirement()));
                    calorie.setText(decimalFormat.format(model.getDailyCalorieRequirement()));
                }else{
                    Toast.makeText(getContext(),"Merhaba",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetUserInformationDto> call, Throwable t) {

            }
        });
        listBreakfastItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, foods);
                listBreakfast.setAdapter(adapter);
            }
        });
        listLunchItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, foods);
                listLunch.setAdapter(adapter);
            }
        });
        listDinnerItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, foods);
                listDinner.setAdapter(adapter);
            }
        });
        return rootView;

    }
}
