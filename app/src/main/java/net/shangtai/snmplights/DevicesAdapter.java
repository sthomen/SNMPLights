package net.shangtai.snmplights;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.RecyclerView;

// R.layout.switch
// R.layout.dimmer

class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {
	DevicesAdapter() {
		setHasStableIds(true);
	}

	@Override
	public int getItemCount() {
		return 0;
	}


	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public DevicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int vt) {
		return null;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

	}



	public class ViewHolder extends RecyclerView.ViewHolder {
		public View entry;

		public ViewHolder(View v) {
			super(v);
			entry=v;
		}
	}
}
