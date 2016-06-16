package banqo.blessing.firebasegoogleauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    private SignInButton signInButton;
    private TextView statusTextView;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusTextView = (TextView) findViewById(R.id.status_textview);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        /*Requesting just the Email address of the user. This simplifies the sign in flow
        so that no further elevated permissions
        are required*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
     // An unresolvable error has occurred and Google APIs (including Sign-In) will not
     // be available.
     Toast.makeText(getBaseContext(),"Connection failed : " + connectionResult.getErrorMessage().toString(),Toast.LENGTH_LONG).show();
    }

    //Handle Activity Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*The Activity was generated with the code RC_SIGN_IN (we declared this earlier),
        so when we check the activity result, we want to check if it's the result for the activity with this code.*/
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result =
                    Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /*When the request code matches, we know that the data returned from the activity will have a Sign In Result
     we can create a GoogleSignInResult object from it*/
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
           /* gets the account details from the result, and pulls the user name from that
            , setting it to the contents of the text view*/
            GoogleSignInAccount acct = result.getSignInAccount();
            statusTextView.setText("Hello, " + acct.getDisplayName());
        } else {
        }
    }

    //Sign in method
    private void signIn(){
        /*creates a new Intent called signInIntent, using the Google Sign In API.
        It then starts an activity for the result of that intent.
       */
        Intent signInIntent =
                Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
