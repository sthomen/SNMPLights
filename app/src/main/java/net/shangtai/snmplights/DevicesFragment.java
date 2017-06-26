package net.shangtai.snmplights;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

public class DevicesFragment extends Fragment {
	private DevicesAdapter da=null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		View root=inflater.inflate(R.layout.devices_fragment, container, false);

		LinearLayoutManager llm = new LinearLayoutManager((Context)getActivity());

		RecyclerView reclist = (RecyclerView)root.findViewById(R.id.devices);
		reclist.setLayoutManager(llm);

		da = new DevicesAdapter(getContext());

		reclist.setAdapter(da);

		return root;
	}

	public void refresh() {
		da.refresh();
	}
}
