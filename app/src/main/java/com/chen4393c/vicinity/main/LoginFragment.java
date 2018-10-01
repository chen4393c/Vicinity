package com.chen4393c.vicinity.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chen4393c.vicinity.Config;
import com.chen4393c.vicinity.R;
import com.chen4393c.vicinity.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.chen4393c.vicinity.utils.SecurityUtils.md5Encryption;
import static com.chen4393c.vicinity.utils.UIUtils.hideKeyboard;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private View loginLayout;
    private View logoutLayout;
    private AutoCompleteTextView mUsernameTextView;
    private EditText mPasswordEditText;

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

        loginLayout = view.findViewById(R.id.loginLayout);
        logoutLayout = view.findViewById(R.id.logoutLayout);
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
                final String username = mUsernameTextView.getText().toString();
                final String password = mPasswordEditText.getText().toString();

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
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsernameTextView.getText().toString();
                final String password = md5Encryption(mPasswordEditText.getText().toString());

                mDatabase.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(username) &&
                                (password.equals(dataSnapshot
                                        .child(username)
                                        .child("userPassword")
                                        .getValue()))) {
                            Config.username = username;
                            hideKeyboard(getActivity());
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
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.username = null;
                updateUI();
            }
        });

        return view;
    }

    private void updateUI() {
        if (Config.username == null) {
            logoutLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        } else {
            logoutLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
        }
    }
}
