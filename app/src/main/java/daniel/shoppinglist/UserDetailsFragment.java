package daniel.shoppinglist;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import daniel.shoppinglist.R;
import daniel.shoppinglist.model.Model;
import daniel.shoppinglist.model.Product;
import daniel.shoppinglist.utils.AuthManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailsFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 103;

    private String userEmail;

    private Activity activity;

    private TextView emailText;
    private ImageView imageView;
    private Button pictureBtn;
    private ProgressBar progressBar;

    private boolean isAlreadtInitialized;

    public UserDetailsFragment() {
        // Required empty public constructor
        isAlreadtInitialized = false;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isAlreadtInitialized) {

            emailText = (TextView) getView().findViewById(R.id.emailText);
            imageView = (ImageView) getView().findViewById(R.id.imageView);
            pictureBtn = (Button) getView().findViewById(R.id.takePictureBtn);
            progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);

            activity = getActivity();

            if (!userEmail.equals("")) {
                emailText.setText(userEmail);

                if (!AuthManager.getInstance().getSignedUser().getEmail().equals(userEmail)) {
                    pictureBtn.setEnabled(false);
                }

                pictureBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startCameraIntent();
                    }
                });


                progressBar.setVisibility(View.VISIBLE);
                Model.getInstance().getImage(userEmail, new Model.Listener() {
                    @Override
                    public void onResult(final Object obj) {
                        while (getActivity() == null) ;

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (obj != null) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Bitmap img = (Bitmap) obj;
                                    imageView.setImageBitmap(img);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                });
            }
        }

        isAlreadtInitialized = true;
    }

    private void startCameraIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            //progressBar.setVisibility(View.VISIBLE);
            Model.getInstance().saveImage(imageBitmap, userEmail, new Model.Listener() {
                @Override
                public void onResult(Object obj) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.setVisibility(View.INVISIBLE);
//                        }
//                    });
                }

                @Override
                public void onCancel() {
//                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
