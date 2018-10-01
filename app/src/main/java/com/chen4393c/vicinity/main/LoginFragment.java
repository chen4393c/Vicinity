package com.chen4393c.vicinity.main;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chen4393c.vicinity.Config;
import com.chen4393c.vicinity.ControlPanelActivity;
import com.chen4393c.vicinity.R;
import com.chen4393c.vicinity.model.User;
import com.chen4393c.vicinity.utils.UIUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.chen4393c.vicinity.utils.SecurityUtils.md5Encryption;
import static com.chen4393c.vicinity.utils.UIUtils.detectAndHideKeyboard;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private View mLoginView;
    private View mLogoutView;
    private AutoCompleteTextView mUsernameTextView;
    private EditText mPasswordEditText;
    private View mProgressView;
    private View mLoginFormView;

    private DatabaseReference mDatabase;

    /**
     * Static function, create loginFragment instance
     * @return new instance of login fragment
     * */
    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginView = view.findViewById(R.id.login_layout);
        mLogoutView = view.findViewById(R.id.logout_layout);
        updateUI();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsernameTextView = (AutoCompleteTextView) view.findViewById(R.id.user_name_text_view);
        mPasswordEditText = (EditText) view.findViewById(R.id.editTextPassword);
        Button submitButton = (Button) view.findViewById(R.id.submit);
        Button registerButton = (Button) view.findViewById(R.id.register);
        Button logoutButton = (Button) view.findViewById(R.id.logout);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.username = null;
                updateUI();
            }
        });

        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);

        return view;
    }

    private void updateUI() {
        if (Config.username == null) {
            mLogoutView.setVisibility(View.GONE);
            mLoginView.setVisibility(View.VISIBLE);
        } else {
            mLogoutView.setVisibility(View.VISIBLE);
            mLoginView.setVisibility(View.GONE);
        }
    }

    private void attemptLogin() {
        final String username = mUsernameTextView.getText().toString();
        final String password = md5Encryption(mPasswordEditText.getText().toString());

        if (username.isEmpty()) {
            Toast.makeText(getActivity(),
                    getResources().getText(R.string.empty_username_toast),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        UIUtils.detectAndHideKeyboard(getActivity());
        showProgress(true);

        mDatabase.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(username) &&
                        (password.equals(dataSnapshot
                                .child(username)
                                .child("userPassword")
                                .getValue()))) {
                    Config.username = username;

                    try {
                        // Simulate network access.
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        Log.i(TAG, "Sleep error");
                    }
                    showProgress(false);
                    updateUI();
                } else {
                    Toast.makeText(getActivity(),
                            getResources().getText(R.string.sign_in_failed_toast),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void attemptRegister() {
        final String username = mUsernameTextView.getText().toString();
        final String password = mPasswordEditText.getText().toString();

        if (username.isEmpty()) {
            Toast.makeText(getActivity(),
                    getResources().getText(R.string.empty_username_toast),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabase.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(username)) {
                    Toast.makeText(getActivity(),
                            getResources().getText(R.string.duplicate_username_toast),
                            Toast.LENGTH_SHORT).show();
                } else if (!username.equals("") && !password.equals("")) {
                    // put username as key to set value
                    final User user = new User();
                    user.setUserAccount(username);
                    user.setUserPassword(md5Encryption(password));
                    user.setUserTimestamp(System.currentTimeMillis());

                    mDatabase.child("user").child(user.getUserAccount()).setValue(user);
                    Toast.makeText(getActivity(),
                            getResources().getText(R.string.sign_up_success_toast),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
