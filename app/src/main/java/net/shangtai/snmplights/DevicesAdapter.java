package net.shangtai.snmplights;

import android.os.AsyncTask;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Button;
import android.widget.SeekBar;

import android.support.v7.widget.RecyclerView;

// R.layout.switch
// R.layout.dimmer

class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {
	private final static int SWITCH=0;
	private final static int DIMMER=1;

	DeviceManager dm=null;

	DevicesAdapter(Context context) {
		setHasStableIds(true);

		new LoadDevicesTask(context, this).execute();
	}

	// Adapter methods

	@Override
	public int getItemCount() {
		if (dm == null)
			return 0;

		return dm.countDevices();
	}

	@Override
	public int getItemViewType(int position) {
		Device device = dm.getDeviceByIndex(position);

		if (device instanceof Switch) {
			return SWITCH;
		} else if (device instanceof Dimmer) {
			return DIMMER;
		}

		return -1;
	}


	@Override
	public long getItemId(int position) {
		return position;	// there's a 1:1 relationship between position and device index (the id)
	}

	@Override
	public DevicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int vt) {
		int layout;

		switch (vt) {
			default:
			case SWITCH:
				layout=R.layout.toggle;
				break;
			case DIMMER:
				layout=R.layout.dimmer;
				break;
		}

		final View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
		final ViewHolder holder = new ViewHolder(v);

		holder.setType(vt);

		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Device device = dm.getDeviceByIndex(position);

		TextView title = (TextView)holder.entry.findViewById(R.id.title);
		title.setText(device.getName());

		if (holder.getType() == SWITCH) {
			Button on = (Button)holder.entry.findViewById(R.id.on_button);
			Button off = (Button)holder.entry.findViewById(R.id.off_button);

			on.setTag(device);
			off.setTag(device);

			on.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					final View _v = v;
					new Thread(new Runnable() {
						@Override
						public void run() {
							Switch device=(Switch)_v.getTag();
							device.on();
						}
					}).start();
				}
			});

			off.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					final View _v = v;
					new Thread(new Runnable() {
						@Override
						public void run() {
							Switch device=(Switch)_v.getTag();
							device.off();
						}
					}).start();
				}
			});
		}

		if (holder.getType() == DIMMER) {
			SeekBar sb = (SeekBar)holder.entry.findViewById(R.id.seekbar);

			sb.setTag(device);
			sb.setMax(255);
			sb.setProgress(Integer.valueOf(device.getValue()));

			sb.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
				public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
				}

				public void onStartTrackingTouch(SeekBar sb) {
				}

				public void onStopTrackingTouch(SeekBar sb) {
					final SeekBar _sb = sb;
					new Thread(new Runnable() {
						@Override
						public void run() {
							Dimmer device=(Dimmer)_sb.getTag();
							device.dim(_sb.getProgress());
						}
					}).start();
				}
			});
		}
		
	}



	public class ViewHolder extends RecyclerView.ViewHolder {
		public View entry;
		private int vt;

		public ViewHolder(View v) {
			super(v);
			entry=v;
		}

		public void setType(int type) {
			vt = type;
		}

		public int getType() {
			return vt;
		}
	}

	private class LoadDevicesTask extends AsyncTask<Void, Void, DeviceManager> {
		Context context = null;
		RecyclerView.Adapter adapter = null;

		LoadDevicesTask(Context context, RecyclerView.Adapter adapter) {
			this.adapter = adapter;
			this.context = context;
		}

		protected DeviceManager doInBackground(Void... none) {
			return new DeviceManager(context);
		}

		protected void onPostExecute(DeviceManager result) {
			dm = result;
			adapter.notifyDataSetChanged();
		}
	}
}
