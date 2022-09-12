package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentFirstBinding;

import java.util.List;
import java.util.Map;

import notion.api.v1.NotionClient;
import notion.api.v1.model.common.PropertyType;
import notion.api.v1.model.common.RichTextType;
import notion.api.v1.model.search.SearchResults;
import notion.api.v1.model.pages.PageParent;
import notion.api.v1.model.pages.PageProperty;
import notion.api.v1.model.pages.Page;
import notion.api.v1.model.databases.DatabaseProperty;
import notion.api.v1.model.databases.Database;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.EditText;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private NotionClient client;
    private String idd;

    private class MyTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            EditText text = (EditText)getView().findViewById(R.id.idText);
            String result = text.getText().toString();
            String notionToken = BuildConfig.NOTION_TOKEN;
            String dbName = BuildConfig.DB_NAME;
            client = new NotionClient(notionToken);
            SearchResults results = client.search(dbName);
            idd = results.getResults().get(0).getId();
            Database db = client.retrieveDatabase(idd);

            PageProperty prop = new PageProperty();
            PageProperty.RichText.Text rt = new PageProperty.RichText.Text(result);
            PageProperty.RichText prop1 = new PageProperty.RichText(RichTextType.Text, rt);
            List<PageProperty.RichText> myList = new ArrayList<>();
            myList.add(prop1);
            prop.setTitle(myList);
            prop.setType(PropertyType.Title);

            Map <String, PageProperty> myMap = new HashMap<String, PageProperty>();
            myMap.put("title", prop);

            prop = new PageProperty();
            prop.setType(PropertyType.Number);
            prop.setNumber(4.2);
            myMap.put("Priority", prop);

            prop = new PageProperty();
            prop.setType(PropertyType.MultiSelect);
            List<DatabaseProperty.MultiSelect.Option> myListSstatus = new ArrayList<>();

            List statuss = db.getProperties().get("Status").getMultiSelect().getOptions();
            int nextActionID = 0;
            for (int i = 0; i < statuss.size(); i++) {
                String trt = ((DatabaseProperty.MultiSelect.Option) statuss.get(i)).getName();
                if (trt.equals("Next Action")) {
                    nextActionID = i;
                }
            }
            DatabaseProperty.MultiSelect.Option t = (DatabaseProperty.MultiSelect.Option)statuss.get(nextActionID);
            myListSstatus.add(t);
            prop.setMultiSelect(myListSstatus);
            myMap.put("Status", prop);

            PageParent pp = PageParent.database(idd);
            try {
                Page page = client.createPage(pp, myMap, null, null, null);
                text.setText("");
            } catch (Exception e) {
                Exception r;
                r = e;
            }

            client.close();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText text = (EditText)getView().findViewById(R.id.idText);
        text.setFocusableInTouchMode(true);
        text.requestFocus();
        binding.buttonFirst.setEnabled(false);
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyTask().execute();
            }
        });
        // Enable submit button if entry field is not empty
        text.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    binding.buttonFirst.setEnabled(false);
                } else {
                    binding.buttonFirst.setEnabled(true);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}