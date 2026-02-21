package com.example.flavor.presentation.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flavor.R;
import com.example.flavor.core.storage.PrefsManager;
import com.example.flavor.data.repo.AuthRepository;
import com.example.flavor.presentation.auth.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private ImageView ivProfilePicture;
    private Button btnLogout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> handleLogout());
    }

    private void loadUserData() {

    }

    private void handleLogout() {
        AuthRepository.getInstance(requireContext())
                .logout()
                .subscribe(
                        () -> {
                            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            if (getActivity() != null) getActivity().finish();
                        },
                        throwable -> Toast.makeText(getContext(), "Logout failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

}