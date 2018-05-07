package com.example.olive.weatherinfo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.olive.weatherinfo.data.City;
import com.example.olive.weatherinfo.data.weatherdata.WeatherResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by olive on 5/6/18.
 */

public class CreateCityDialog extends DialogFragment {

    public interface CityHandler {
        public void onNewPlaceCreated(City city);

    }

    private CityHandler cityHandler;
    private EditText etName;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CityHandler) {
            cityHandler = (CityHandler)context;
        } else {
            throw new RuntimeException(
                    getString(R.string.runtime_exception));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_name);
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_city, null);

        etName = (EditText) rootView.findViewById(R.id.cityNameInput);

        builder.setView(rootView);
        builder.setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(etName.getText())) {
                    MainActivity.weatherAPI.getCurrentWeather(MainActivity.apiKey, "metric", etName.getText().toString()).enqueue(new Callback<WeatherResult>() {
                        @Override
                        public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                            if (response.body() != null) {
                                City city = new City(
                                        etName.getText().toString()
                                );

                                cityHandler.onNewPlaceCreated(city);
                            } else {
                                etName.setError(getString(R.string.invalid_city));
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherResult> call, Throwable t) {
                        }
                    });
                } else {
                    etName.setError(getString(R.string.empty_error));
                }
            }
        });
        return builder.create();
    }

}
