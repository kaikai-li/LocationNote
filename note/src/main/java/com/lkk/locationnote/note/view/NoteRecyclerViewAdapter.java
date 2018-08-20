package com.lkk.locationnote.note.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.lkk.locationnote.common.data.NoteEntity;
import com.lkk.locationnote.common.log.Log;
import com.lkk.locationnote.common.utils.Util;
import com.lkk.locationnote.note.R;
import com.lkk.locationnote.note.R2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteRecyclerViewAdapter extends
        Adapter<NoteRecyclerViewAdapter.NoteItemViewHolder> {

    private static final String TAG = NoteRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;
    private List<NoteEntity> mNotes;

    public NoteRecyclerViewAdapter(Context context, List<NoteEntity> notes) {
        mContext = context;
        mNotes = notes;
    }

    @NonNull
    @Override
    public NoteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteItemViewHolder(View.inflate(mContext, R.layout.note_list_item_view, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteItemViewHolder holder, int position) {
        NoteEntity note = mNotes.get(position);
        GeocodeSearch geocodeSearch = new GeocodeSearch(mContext);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int code) {
                Log.d(TAG, "onRegeocodeSearched, code= " + code);
                // 1000为成功，其他为失败
                if (code == 1000) {
                    RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
                    String location = address.getCity();
                    if (TextUtils.isEmpty(location)) {
                        location = address.getProvince();
                    }
                    holder.mLocation.setText(location);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {}
        });
        LatLonPoint latLonPoint = new LatLonPoint(note.getLatitude(), note.getLongitude());
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
        holder.mTitle.setText(note.getTitle());
        holder.mTime.setText(Util.getFormatTime(mContext, note.getTime()));
        holder.mContent.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return mNotes != null ? mNotes.size() : 0;
    }

    public void replaceData(List<NoteEntity> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    public static class NoteItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.note_item_location)
        TextView mLocation;
        @BindView(R2.id.note_item_title)
        TextView mTitle;
        @BindView(R2.id.note_item_time)
        TextView mTime;
        @BindView(R2.id.note_item_content)
        TextView mContent;

        public NoteItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
