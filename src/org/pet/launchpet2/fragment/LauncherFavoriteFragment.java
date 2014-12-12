package org.pet.launchpet2.fragment;

import org.pet.launchpet2.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LauncherFavoriteFragment extends LauncherHomeFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_launcher, container, false);
		return rootView;
	}

}
