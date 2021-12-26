package com.aidr.aidr;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import com.aidr.aidr.Adapter.ReminderAdapter;
import com.aidr.aidr.Model.Reminder;

public class ReminderFragment extends Fragment {
    private RecyclerView recyclerView;
    private ReminderAdapter reminderAdapter;
    private TextView mNoReminderCaptionTextView;

    private File file;
    final static public String filename = "reminders.json";
    public static JSONArray currReminders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.reminder, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoReminderCaptionTextView = view.findViewById(R.id.tv_no_reminder);

        //retrieve from database
        refreshReminders();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshReminders();

        //assign ReminderAdapter
        reminderAdapter = new ReminderAdapter(getContext(), currReminders);

        //use findViewById for reference to xml view
        recyclerView = getView().findViewById(R.id.rv_reminder_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(reminderAdapter);
    }

    /* Reading reminder file */
    public void refreshReminders() {
        ReminderDB.reload();
        currReminders = ReminderDB.getReminders();

        if (currReminders.length() > 0){
            mNoReminderCaptionTextView.setVisibility(View.INVISIBLE);
            for (int i = 0; i < currReminders.length();i++) {
                try {
                    JSONObject temp = currReminders.getJSONObject(i);
                    ReminderDB.scheduleNotification(new Reminder(temp),i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            mNoReminderCaptionTextView.setVisibility(View.VISIBLE);
        }

    }



}