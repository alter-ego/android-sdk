package com.sensorberg.sdk.demo.HistoryService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sensorberg.sdk.action.Action;
import com.sensorberg.sdk.model.ISO8601TypeAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class HistoryService extends Service {

    public static List<Action> actions = null;
    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, ISO8601TypeAdapter.DATE_ADAPTER).create();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void restoreList(Context context){
        if (actions == null){
            actions = new ArrayList<>();
            try {
                BufferedReader br = new BufferedReader(
                        new FileReader(new File(context.getFilesDir() + File.separator + "actionList.serializeable")));
                Type listType = new TypeToken<List<Action>>() {}.getType();
                actions.addAll((Collection<Action>) gson.fromJson(br, listType));
            } catch (FileNotFoundException e) {

            }
        }
        Log.d("SensorbergDemo", "restored a list with size " + actions.size());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Action action = intent.getExtras().getParcelable(Action.INTENT_KEY);
        addAction(action, getApplicationContext());
        return START_NOT_STICKY;
    }

    public static void addAction(Action action, Context context) {
        actions.add(action);
        String json = gson.toJson(actions);

        try {
            FileWriter writer = new FileWriter(new File(context.getFilesDir() + File.separator + "actionList.serializeable"));
            writer.write(json);
            writer.close();
        }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
}
