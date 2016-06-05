package com.spuds.eventapp.EditProfile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.User;

/*---------------------------------------------------------------------------
Class Name:                EditProfileFragment
Description:               Sets up screen for the user to edit the profile
---------------------------------------------------------------------------*/
public class EditProfileFragment extends Fragment {

    // Views for edit profile layout
    ImageView pictureView;
    Button updateButton;
    ImageButton editProfilePictureButton;
    EditText editFullName;
    EditText editDescription;
    TextView errorMessage;

    // Contains information of user
    User user;

    // Reference to this fragment
    Fragment editProfileFragment;

    /*---------------------------------------------------------------------------
    Function Name:                EditProfileFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public EditProfileFragment() {
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreate()
    Description:                  Called each time fragment is created.
                                  Initializes instance variables getting information
                                  about user passed to fragment
    Input:                        Bundle savedInstanceState
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get user details passed to this fragment
        Bundle extras = getArguments();
        user = (User) extras.get(getString(R.string.user_details));

        editProfileFragment = this;

    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateView()
    Description:                  Inflates View layout and sets fonts programmatically
    Input:                        LayoutInflater inflater - inflates layout
                                  ViewGroup container - parent view group
                                  Bundle savedInstanceState
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflates layout for edit profile
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Profile");

        // Set custom fonts for text views in this fragment
        overrideFonts(view.getContext(),view);

        // Manually set fonts for text views name, desc, upload in this fragment
        Typeface raleway_medium = Typeface.createFromAsset(getActivity().getAssets(),  "Raleway-Medium.ttf");

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setTypeface(raleway_medium);

        TextView description = (TextView) view.findViewById(R.id.description);
        description.setTypeface(raleway_medium);

        TextView upload = (TextView) view.findViewById(R.id.update_button);
        upload.setTypeface(raleway_medium);

        // Setup view objects on this layout to be manipulated
        updateButton = (Button) view.findViewById(R.id.update_button);
        editProfilePictureButton = (ImageButton) view.findViewById(R.id.edit_profile_picture);
        editFullName = (EditText) view.findViewById(R.id.edit_full_name);
        editDescription = (EditText) view.findViewById(R.id.edit_description);
        pictureView = (ImageButton) view.findViewById(R.id.edit_profile_picture);
        errorMessage = (TextView) view.findViewById(R.id.edit_error_message);

        //On click listener to allow user to edit their profile picture when they click on the picture view
        pictureView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Reset picture variable in Main Activity to allow
                ((MainActivity) getActivity()).picture = null;
                // Call pickImage() that takes care of allowing the user to picking an image
                ((MainActivity) getActivity()).pickImage(true);

                // A thread to check if the user has finished picking the image
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        // Keep checking if the user is done picking a picture
                        while (((MainActivity) getActivity()).picture == null) {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (((MainActivity) getActivity()).picture == null) {
                                break;
                            }
                        }

                        // After the user has finished picking a picture
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                // Convert the picture from main activity to a string using convert()
                                String imageFile = UserFirebase.convert(getActivity(), ((MainActivity) getActivity()).picture);

                                // If the image has been chosen by the user
                                if (imageFile != null) {

                                    // Attempt to create the image string into a bitmap
                                    Bitmap src = null;
                                    try {
                                        byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
                                        src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                                    } catch (OutOfMemoryError e) {
                                        System.err.println(e.toString());
                                    }

                                    // If the bitmap was created successfully
                                    if (src != null) {

                                        // Change the bitmap to a circle
                                        RoundedBitmapDrawable dr =
                                                RoundedBitmapDrawableFactory.create(getActivity().getResources(), src);

                                        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);

                                        // Set the view for the profile picture to the new circle picture
                                        pictureView.setImageDrawable(dr);
                                    // If the bitmap was not created successful
                                    } else {

                                        // Convert the stock profile picture icon into a bitmap
                                        src = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic_icon);

                                        // Convert the image to a circle
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(getResources(), src);
                                        circularBitmapDrawable.setCircular(true);
                                        circularBitmapDrawable.setAntiAlias(true);

                                        // Set the view for the profile picture to the stock circle picture
                                        editProfilePictureButton.setImageDrawable(circularBitmapDrawable);
                                    }
                                // If the user cancelled picking a new picture
                                } else {

                                    // Convert the stock profile picture icon into a bitmap
                                    Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic_icon);

                                    // Convert the image to a circle
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getResources(), src);
                                    circularBitmapDrawable.setCircular(true);
                                    circularBitmapDrawable.setAntiAlias(true);

                                    // Set the view for the profile picture to the stock circle picture
                                    editProfilePictureButton.setImageDrawable(circularBitmapDrawable);
                                }

                            }
                        });

                    }
                }).start();

            }
        });

        // Keeps track of the user's picture
        final String imageFile = user.getPicture();

        // If the user has a profile picture
        if (imageFile != null) {

            // Try changing the picture into a bitmap
            Bitmap src = null;
            try {
                byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            } catch (OutOfMemoryError e) {
                System.err.println(e.toString());
            }

            // If the bitmap was created successfully
            if (src != null) {

                // Change the bitmap to a circle
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);

                // Load the circle image to the picture view for the user
                pictureView.setImageDrawable(circularBitmapDrawable);
            }
        // If the user does not have a profile picture
        } else {

            // Create a bitmap from default profile pic icon
            Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic_icon);

            // Create a circle from previous bitmap
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getResources(), src);
            circularBitmapDrawable.setCircular(true);
            circularBitmapDrawable.setAntiAlias(true);

            // Load the stock profile pic icon circle image to the picture view for the user
            pictureView.setImageDrawable(circularBitmapDrawable);
        }

        //Set Custom Fonts for the textviews
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "raleway-light.ttf");
        editFullName.setTypeface(custom_font);
        editDescription.setTypeface(custom_font);
        editFullName.setText(user.getName());
        editDescription.setText(user.getDescription());



        // When update button is clicked, get the user input, and send to Firebase.
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If there are empty fields for required fields, create error message
                if (editFullName.getText().toString().equals("")) {
                    errorMessage.setText(getString(R.string.errorEmptyFields));
                } else {

                    // Create user object from the user inputted data
                    User user = new User(editFullName.getText().toString(),
                            editDescription.getText().toString(),
                            imageFile);

                    // Push the new user information to the database
                    UserFirebase.updateUser(user);

                    // Get the user/account details for this user
                    final UserFirebase userFirebase = new UserFirebase();
                    userFirebase.getMyAccountDetails();

                    // A thread to check if the data has been pushed to the database
                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            while (!userFirebase.threadCheck) {
                                try {
                                    Thread.sleep(75);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }

                            // Once the information has been passed ot the dataabse, update the drawer
                            // and pop this fragment from the backstack
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    ((MainActivity) getActivity()).setupProfileDrawer();
                                    getActivity().getSupportFragmentManager().popBackStack();

                                }
                            });
                        }
                    }).start();

                    // Hides keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                }
            }
        });

        return view;
    }


    /*---------------------------------------------------------------------------
    Function Name:                overrideFonts()
    Description:                  Sets fonts for all TextViews
    Input:                        final Context context
                                  final View v
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    private void overrideFonts(final Context context, final View v) {
        try {

            // If the view is a ViewGroup
            if (v instanceof ViewGroup) {

                ViewGroup vg = (ViewGroup) v;

                // Iterate through ViewGroup children
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);

                    // Call method again for each child
                    overrideFonts(context, child);
                }

                // If the view is a TextView set the font
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "raleway-regular.ttf"));
            }

        }
        catch (Exception e) {
            // Print out error if one is encountered
            System.err.println(e.toString());
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                onResume()
    Description:                  Every time the About Fragment comes into view
                                  remove the search toolbar
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();

        // Call the remove search toolbar method in activity
        ((MainActivity)getActivity()).removeSearchToolbar();
    }

}
