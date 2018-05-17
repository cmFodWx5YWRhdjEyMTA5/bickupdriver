package com.app.bickupdriver.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.adapter.NotificationRecyclerAdapter;
import com.app.bickupdriver.model.User;
import com.app.bickupdriver.restservices.ApiClientConnection;
import com.app.bickupdriver.restservices.ApiInterface;
import com.app.bickupdriver.restservices.NotificationListModel;
import com.app.bickupdriver.restservices.ServerResponseNotificationList;
import com.app.bickupdriver.utility.Utils;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListActivity extends AppCompatActivity implements View.OnClickListener {

    private static final java.lang.String TAG = "NOTIFICATION";
    private Toolbar toolbar;
    private TextView tvToolbarText;
    private ImageView ivBackImage;
    private RecyclerView recyclerView;
    private CircularProgressView circularProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        getId();
        setListener();
        setData();

        getNotificationListFromServer();

    }

    private void getNotificationListFromServer() {
        if (Utils.isNetworkAvailable(this)) {
            circularProgressView.setVisibility(View.VISIBLE);
            ApiInterface apiService = null;

            try {
                apiService = ApiClientConnection.getInstance().createService();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String accessToken = User.getInstance().getAccesstoken();
            Call<ServerResponseNotificationList> call = apiService.getNotificationList(accessToken);
            call.enqueue(new Callback<ServerResponseNotificationList>() {
                @Override
                public void onResponse(Call<ServerResponseNotificationList> call, Response<ServerResponseNotificationList> response) {
                    circularProgressView.setVisibility(View.GONE);
                    if (response != null) {
                        if (response.isSuccessful()) {
                            Utils.printLogs(TAG, "Notification list retrieved successfully .. ");
                            ServerResponseNotificationList notificationListResponse = response.body();
                            ArrayList<NotificationListModel> notificationList = notificationListResponse.response;

                            /**
                             * Set this data to Recycler View
                             */
                            NotificationRecyclerAdapter adapter = new NotificationRecyclerAdapter(notificationList, NotificationListActivity.this);
                            recyclerView.setAdapter(adapter);
                        } else {
                            try {
                                Utils.printLogs(TAG, "Response is not successful " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ServerResponseNotificationList> call, Throwable t) {
                    circularProgressView.setVisibility(View.GONE);
                    Utils.printLogs(TAG, "onFailure : " + t.getMessage());
                }
            });
        } else {

        }
    }

    private void getId() {
        toolbar = findViewById(R.id.include_toolbar_notification_list);
        tvToolbarText = toolbar.findViewById(R.id.txt_activty_header);
        ivBackImage = toolbar.findViewById(R.id.backImage_header);
        ivBackImage.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recycler_view_notification_list);
        circularProgressView = findViewById(R.id.circular_progress_view_notification_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
    }

    private void setListener() {
        ivBackImage.setOnClickListener(this);
    }

    private void setData() {
        tvToolbarText.setText(R.string.txt_notification);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backImage_header:
                onBackPressed();
                break;


        }
    }
}
