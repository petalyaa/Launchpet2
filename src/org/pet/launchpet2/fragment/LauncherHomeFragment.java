package org.pet.launchpet2.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class LauncherHomeFragment extends Fragment {
	
	public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	
}
