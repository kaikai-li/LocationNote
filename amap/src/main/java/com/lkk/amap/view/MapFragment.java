package com.lkk.amap.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.lkk.amap.R;
import com.lkk.amap.R2;
import com.lkk.amap.viewmodel.MapViewModel;
import com.lkk.locationnote.common.BaseTabFragment;
import com.lkk.locationnote.common.data.NoteEntity;
import com.lkk.locationnote.common.log.Log;
import com.lkk.locationnote.common.utils.RouterPath;
import com.lkk.locationnote.common.utils.Util;

import java.util.List;

import butterknife.BindView;

public class MapFragment extends BaseTabFragment {

    public static final String TAG = MapFragment.class.getSimpleName();
    private static final int MAP_LOCATION_INTERNAL = 5000;

    @BindView(R2.id.mapView)
    MapView mMapView;

    private AMap mMap;
    private Marker mLocationMarker;
    private MapViewModel mViewModel;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        mMapView.onCreate(savedInstanceState);
        if (mMap == null) {
            mMap = mMapView.getMap();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMap();
        mViewModel.getNotes().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(@Nullable List<NoteEntity> noteEntities) {
                Log.d(TAG, "Notes onChanged, noteEntities= " + noteEntities);
                if (noteEntities == null) {
                    return;
                }
                LatLng latLng;
                Marker marker;
                for (NoteEntity entity : noteEntities) {
                    if (entity.getLatitude() == 0 || entity.getLongitude() == 0) {
                        continue;
                    }
                    latLng = new LatLng(entity.getLatitude(),entity.getLongitude());
                    marker = mMap.addMarker(new MarkerOptions().position(latLng)
                            .title(entity.getTitle()).snippet(entity.getContent()));
                    marker.setObject(entity.getId());
                }
            }
        });
    }

    private void initMap() {
        mMap.setMyLocationStyle(new MyLocationStyle()
                .interval(MAP_LOCATION_INTERNAL)
                .myLocationIcon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_add_location_alpha))
                .showMyLocation(true));
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        uiSettings.setCompassEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Log.d(TAG, "onMyLocationChange-->location= " + location);
                if (mLocationMarker != null) {
                    mLocationMarker.destroy();
                }
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_add_location)));
                mLocationMarker = marker;
            }
        });
        mMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG, "On Maker clicked, marker= " + marker);
                if (marker.equals(mLocationMarker)) {
                    ARouter.getInstance().build(RouterPath.NOTE_ADD_EDIT_ACTIVITY).navigation();
                    return true;
                }
                if (!marker.isInfoWindowShown()) {
                    marker.showInfoWindow();
                } else {
                    marker.hideInfoWindow();
                }
                return true;
            }
        });
        mMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d(TAG, "On info window clicked, marker= " + marker);
                ARouter.getInstance().build(RouterPath.NOTE_DETAIL_ACTIVITY)
                        .withInt(Util.EXTRA_NOTE_ID, (Integer) marker.getObject())
                        .navigation();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mViewModel.loadNotes();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        mMap.clear();
        mMap = null;
        mMapView.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }
}
