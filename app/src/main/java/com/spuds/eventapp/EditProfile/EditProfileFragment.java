package com.spuds.eventapp.EditProfile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.User;


public class  EditProfileFragment extends Fragment {

    //interactable objects
    ImageView pictureView;
    Button updateButton;
    ImageButton editProfilePictureButton;
    EditText editFullName;
    EditText editDescription;
    Fragment editProfileFragment;

    User user;
    String picturepush;

    public EditProfileFragment() {
        //this is to leave this fragment when done
        editProfileFragment = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        user = (User) extras.get(getString(R.string.user_details));

        ((MainActivity) getActivity()).picture = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        overrideFonts(view.getContext(),view);

        Typeface raleway_medium = Typeface.createFromAsset(getActivity().getAssets(),  "Raleway-Medium.ttf");

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setTypeface(raleway_medium);

        TextView description = (TextView) view.findViewById(R.id.description);
        description.setTypeface(raleway_medium);

        TextView upload = (TextView) view.findViewById(R.id.update_button);
        upload.setTypeface(raleway_medium);

        updateButton = (Button) view.findViewById(R.id.update_button);
        editProfilePictureButton = (ImageButton) view.findViewById(R.id.edit_profile_picture);
        editFullName = (EditText) view.findViewById(R.id.edit_full_name);
        editDescription = (EditText) view.findViewById(R.id.edit_description);
        pictureView = (ImageButton) view.findViewById(R.id.edit_profile_picture);


        pictureView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).picture = null;

                // For camera
                //UploadPictureDialogFragment dialogFragment = new UploadPictureDialogFragment();

                //dialogFragment.show(getFragmentManager(), "Add a Picture");


                ((MainActivity) getActivity()).pickImage(true);

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (((MainActivity) getActivity()).picture == null) {
                            try {
                                Thread.sleep(75);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                /*pictureView.setImageURI(null);
                                pictureView.setImageURI(((MainActivity) getActivity()).picture);
                                Log.v("EditProfileFragment", "here");
                                Log.v("EditProfileFragment", ((MainActivity) getActivity()).picture.toString());
                                pictureView.invalidate();*/

                                String imageFile = UserFirebase.convert(getActivity(),((MainActivity) getActivity()).picture);
                                picturepush = imageFile;
                                Bitmap src = null;
                                try {
                                    byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
                                    src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                                } catch(OutOfMemoryError e) {
                                    System.err.println(e.toString());
                                }

                                if (src != null) {

                                    Log.v("EditProfileFragment", "imagefile" + imageFile);
                                    RoundedBitmapDrawable dr =
                                            RoundedBitmapDrawableFactory.create(getActivity().getResources(), src);

                                    dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
                                    pictureView.setImageDrawable(dr);
                                }

                            }
                        });

                    }
                }).start();

            }
        });

        String imageFile = user.getPicture();

        Bitmap src = null;
        try {
            byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
            src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        } catch(OutOfMemoryError e) {
            System.err.println(e.toString());
        }

        if (src != null) {
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getResources(), src);
            circularBitmapDrawable.setCircular(true);
            circularBitmapDrawable.setAntiAlias(true);
            pictureView.setImageDrawable(circularBitmapDrawable);
        }
        /*Bitmap src = BitmapFactory.decodeResource(this.getResources(), R.id.edit_profile_picture);
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(this.getResources(), src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
        editProfilePictureButton.setImageDrawable(dr);
*/

        //Set Custom Fonts
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "raleway-light.ttf");
        editFullName.setTypeface(custom_font);
        editDescription.setTypeface(custom_font);



        editFullName.setText(user.getName());
        editDescription.setText(user.getDescription());



        //When update button is clicked, get the Text the user input, and send to Firebase.
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO (M): Push this data
                ;      //Full Name to update to db
                //editCollege.getText().toString();       //College text to update to db
                //editMajor.getText().toString();         //Major text to update to db
                ;   //description to update to db


                User user = new User(editFullName.getText().toString(),
                        editDescription.getText().toString(),
                        picturepush);

                Log.v("EditProfilementasdfasdf", "imagefile" + user.getPicture());

                UserFirebase.updateUser(user);


                // TODO (C): Refresh after updating profile
                // Pop this fragment from backstack
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "raleway-regular.ttf"));
            }
        }
        catch (Exception e) {
        }
    }

}
