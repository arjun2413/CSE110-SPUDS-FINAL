package com.spuds.eventapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class MySubscriptionsFragment extends Fragment {
    private List<Subscription> subscriptions;
    public MySubscriptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.recycler, container, false);
        RecyclerView rv=(RecyclerView) v.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);

        subscriptions = new ArrayList<>();
        subscriptions.add(new Subscription("arjun", R.drawable.arjun));


        MySubscriptionsRVAdapter adapter = new MySubscriptionsRVAdapter(subscriptions);
        rv.setAdapter(adapter);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
