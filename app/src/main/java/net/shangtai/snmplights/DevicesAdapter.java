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

import net.shangtai.snmplights.dataholders.*;

import android.util.Log;

// R.layout.switch
// R.layout.dimmer

class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {
	private final static int EMPTY = 0;
	private final static int INVALID = 1;
	private final static int SWITCH = 2;
	private final static int DIMMER = 3;

	private Context context;

	private View.OnClickListener buttonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			final View _v = v;
			final ViewHolder viewHolder = (ViewHolder)_v.getTag();

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Integer type = viewHolder.getType();

					if (type == SWITCH) {
						Switch device = (Switch)viewHolder.getDevice();

						switch (_v.getId()) {
							case R.id.on_button:
								device.on();
								break;
							case R.id.off_button:
								device.off();
								break;
						}
					} else if (type == DIMMER) { // this can only ever be off
						Dimmer device = (Dimmer)viewHolder.getDevice();
						device.dim(0);

						SeekBar sb = viewHolder.getView().findViewById(R.id.seekbar);
						sb.setProgress(0);
					}
				}
			});

			t.start();
		}
	};

	private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(final SeekBar seekBar) {
			final ViewHolder viewHolder = (ViewHolder)seekBar.getTag();

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Dimmer device = (Dimmer)viewHolder.getDevice();
					device.dim(seekBar.getProgress());
				}
			});

			t.start();

		}
	};

	private DeviceManager dm = null;

	DevicesAdapter(Context context) {
		setHasStableIds(true);
		this.context = context;

		refresh();
	}

	void refresh() {
		new LoadDevicesTask(context, this).execute();
	}

	private boolean isValid() {
		return dm != null && dm.isValid();
	}

	// Adapter methods

	@Override
	public int getItemCount() {
		int count = 1;                // invalid view

		if (isValid()) {
			count = dm.countDevices();

			if (count == 0)
				count = 1;        // empty view
		}

		return count;
	}

	@Override
	public int getItemViewType(int position) {
		if (!isValid())
			return INVALID;

		Device device = dm.getDeviceByIndex(position);

		if (device instanceof Switch) {
			return SWITCH;
		} else if (device instanceof Dimmer) {
			return DIMMER;
		}

		return EMPTY;
	}


	@Override
	public long getItemId(int position) {
		return position;    // there's a 1:1 relationship between position and device index (the id)
	}

	@Override
	public DevicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		int layout;

		Log.d(SNMPLightsActivity.TAG, "The view type is " + Integer.valueOf(viewType).toString());

		switch (viewType) {
			default:
			case EMPTY:
				layout = R.layout.empty;
				break;
			case INVALID:
				layout = R.layout.invalid;
				break;
			case SWITCH:
				layout = R.layout.toggle;
				break;
			case DIMMER:
				layout = R.layout.dimmer;
				break;
		}

		final View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
		final ViewHolder holder = new ViewHolder(v, viewType);

		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Integer type = holder.getType();

		// short circuit for invalid and empty types
		if (type == INVALID || type == EMPTY)
			return;

		Device device = dm.getDeviceByIndex(position);

		holder.setDevice(device);

		View base = holder.getView();

		TextView title = base.findViewById(R.id.title);
		title.setText(device.getName());

		Button on = base.findViewById(R.id.on_button);
		Button off = base.findViewById(R.id.off_button);
		SeekBar sb = base.findViewById(R.id.seekbar);

		// they all have off buttons
		off.setTag(holder);
		off.setOnClickListener(buttonListener);

		if (on != null) {
			on.setTag(holder);
			on.setOnClickListener(buttonListener);
		}

		if (sb != null) {
			sb.setTag(holder);
			sb.setMax(255);
			sb.setProgress(Integer.valueOf(device.getValue()));
			sb.setOnSeekBarChangeListener(seekBarChangeListener);
		}
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		View view;
		private Integer type;
		private Device device;

		ViewHolder(View view, Integer type) {
			super(view);
			setView(view);
			setType(type);
		}

		void setView(View view) {
			this.view = view;
		}

		View getView() {
			return view;
		}

		void setType(int type) {
			this.type = type;
		}

		int getType() {
			return type;
		}

		void setDevice(Device device) {
			this.device = device;
		}

		Device getDevice() {
			return device;
		}
	}

	private class LoadDevicesTask extends AsyncTask<Void, Void, DeviceManager> {
		Context context;
		RecyclerView.Adapter adapter;

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
