package net.shangtai.snmplights;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import android.util.Log;

public class DevicesFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		View root=inflater.inflate(R.layout.devices_fragment, container, false);
/*
		LinearLayoutManager llm = new LinearLayoutManager((Context)getActivity());

		RecyclerView reclist = (RecyclerView)root.findViewById(R.id.devices);
		reclist.setLayoutManager(llm);
*/

		Button doit=(Button)root.findViewById(R.id.do_it);

		doit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(SNMPLightsActivity.TAG, "Do It Now!");
			}
		});


		return root;
	}
}
