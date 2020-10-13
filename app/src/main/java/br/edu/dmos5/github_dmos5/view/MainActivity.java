package br.edu.dmos5.github_dmos5.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import br.edu.dmos5.github_dmos5.R;
import br.edu.dmos5.github_dmos5.model.Repository;
import br.edu.dmos5.github_dmos5.service.RetrofitService;
import br.edu.dmos5.github_dmos5.view.adapter.ItemRepositoryAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * MainActivity to search Repository Gitub
 * @author vinicius.montouro
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PERMISSION = 64;

    private static final String BASE_URL = "https://api.github.com/users/";

    private EditText nameEditText;

    private Button searchButton;

    private RecyclerView repositoriesRecyclerView;

    private ItemRepositoryAdapter itemRepositoryAdapter;

    private List<Repository> repositories;

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.user_name_et);
        searchButton = findViewById(R.id.user_name_btn);
        repositoriesRecyclerView = findViewById(R.id.repositories_recycler_view);
        repositories = new LinkedList<>();

        searchButton.setOnClickListener(this);
        searchButton.setOnClickListener(this);

        itemRepositoryAdapter = new ItemRepositoryAdapter(repositories);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        repositoriesRecyclerView.setLayoutManager(layoutManager);
        repositoriesRecyclerView.setAdapter(itemRepositoryAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_name_btn:
                if (hasPermission()){
                    searchRepositories();
                } else {
                    requestPermission();
                }
                break;
        }
    }

    private boolean hasPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
            final Activity activity = this;
            new AlertDialog.Builder(this)
                    .setMessage(R.string.resquest_permission_msg)
                    .setPositiveButton(R.string.request_permission_ok_msg, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, REQUEST_PERMISSION);
                        }
                    })
                    .setNegativeButton(getString(R.string.request_permission_not_msg), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.INTERNET
                    },
                    REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equalsIgnoreCase(Manifest.permission.INTERNET) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    searchRepositories();
                }

            }
        }
    }

    private void searchRepositories() {
        retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        String name = nameEditText.getText().toString();
        if(name.isEmpty()){
            showMessage(getString(R.string.empty_repository_name_msg));
        }else{
            name += "/repos";
            final RetrofitService retrofitService = retrofit.create(RetrofitService.class);
            Call<List<Repository>> call = retrofitService.findByName(name);
            call.enqueue(new Callback<List<Repository>>() {
                @Override
                public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                    if (response.isSuccessful()) {
                        List<Repository> list = response.body();
                        if (list != null){
                            repositoriesRecyclerView.setVisibility(View.VISIBLE);
                            repositories.clear();
                            repositories.addAll(list);
                            itemRepositoryAdapter.notifyDataSetChanged();
                        } else {
                            repositoriesRecyclerView.setVisibility(View.GONE);
                            showMessage(getString(R.string.repositories_not_found));
                        }
                    } else {
                        repositoriesRecyclerView.setVisibility(View.GONE);
                        showMessage(getString(R.string.repository_not_found));
                    }
                }

                @Override
                public void onFailure(Call<List<Repository>> call, Throwable t) {
                    repositoriesRecyclerView.setVisibility(View.GONE);
                    showMessage(getString(R.string.api_error_msg));
                }
            });
        }
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
