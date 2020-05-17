package com.example.konovodov_hw_412_sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String KEY_1 = "one";
    public static final String KEY_2 = "two";

    private static String NOTE_TEXT = "note_text";
    //private EditText mInputNote;
    //--------------------------------------
    private SharedPreferences myNoteSharedPref;
    //--------------------------------------
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public List<Map<String, String>> mapList = new ArrayList<>();
    //public Map<String, String> map = new HashMap<>();
    //private ListView simpleListView;
    private ListView list;
    private SimpleAdapter simpleAdapter;

    //private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_view);
        myNoteSharedPref = getSharedPreferences("MyNote", MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        // делаем повеселее

        //mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.green, R.color.yellow, R.color.red);

        newPrepareContent(getPref());
        initView();

        simpleAdapter = (SimpleAdapter) createSimpleAdapter(mapList);
        list.setAdapter(simpleAdapter);


    }

    @NonNull
    private BaseAdapter createSimpleAdapter(List<Map<String, String>> values) {
        return new SimpleAdapter(this, values, R.layout.activity_main,
                new String[]{KEY_1, KEY_2}, new int[]{R.id.oneTttv, R.id.twoTttv});
        //simpleListView.setAdapter(list);
    }

    @NonNull
    private void prepareContent() {

        for (int i = 0; i < getString(R.string.large_text).split("\n\n").length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put(KEY_2, String.valueOf(getString(R.string.large_text).split("\n\n")[i].length()));
            map.put(KEY_1, getString(R.string.large_text).split("\n\n")[i]);
            mapList.add(map);
        }


    }

    private void initView() {


        list = findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mapList.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }

        });


    }

    @NonNull
    private void newPrepareContent(String str) {
        mapList.clear();
        for (int i = 0; i < str.split("\n\n").length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put(KEY_2, String.valueOf(str.split("\n\n")[i].length()));
            map.put(KEY_1, str.split("\n\n")[i]);
            mapList.add(map);
        }
    }


    private String getPref() {

        String noteTxt = myNoteSharedPref.getString(NOTE_TEXT, "false");

        if (noteTxt.equals("false")) {
            return setPref();

        } else {
            return noteTxt;
        }

    }

    private String setPref() {

        SharedPreferences.Editor myEditor = myNoteSharedPref.edit();
        String noteSave = getString(R.string.large_text); //-это данные для сохранения
        myEditor.putString(NOTE_TEXT, noteSave);
        myEditor.apply();
        Toast.makeText(ListViewActivity.this, "данные сохранены", Toast.LENGTH_LONG).show();
        return noteSave;
    }

    @Override
    public void onRefresh() {
        // говорим о том, что собираемся начать
        //Toast.makeText(this, R.string.refresh_started, Toast.LENGTH_SHORT).show();
        // начинаем показывать прогресс

        //
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(false);
        // ждем 0.1 секунды и прячем прогресс
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                newPrepareContent(getPref());
                simpleAdapter = (SimpleAdapter) createSimpleAdapter(mapList);
                list.setAdapter(simpleAdapter);

                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(true);
                // говорим о том, что собираемся закончить
                Toast.makeText(ListViewActivity.this, R.string.refresh_finished, Toast.LENGTH_SHORT).show();
            }
        }, 100);
    }
}
