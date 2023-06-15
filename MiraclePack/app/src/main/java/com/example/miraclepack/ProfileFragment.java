package com.example.miraclepack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class ProfileFragment extends Fragment {

    private Button signOutButton;
    private Button passwordResetButton;
    private SessionManager sessionManager;

    public ProfileFragment() {
        // Empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sessionManager = new SessionManager(requireContext());

        signOutButton = view.findViewById(R.id.buttonSignOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the user as logged out
                sessionManager.setLoggedIn(false);

                Toast.makeText(requireContext(), "Succesvol uitgelogd", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.frame_layout, new LoginFragment())
                        .commit();
            }
        });


        passwordResetButton = view.findViewById(R.id.buttonPasswordReset);
        passwordResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPasswordResetActivity();
            }
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // Clear the back stack
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void openLoginFragment(View view) {
        replaceFragment(new LoginFragment());
    }

    public void openPasswordResetActivity() {
        Intent intent = new Intent(getActivity(), PasswordResetActivity.class);
        startActivity(intent);
    }
}

