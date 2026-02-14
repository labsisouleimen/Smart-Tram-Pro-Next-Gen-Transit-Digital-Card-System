package com.example.myapplicationnewconver;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.labsisouleimanedev.touchflip3d.RotateView2;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */
public class card extends Fragment {
    private CardView frontCard, backCard;
    private boolean isFrontVisible = true;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public card() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment card.
     */
    // TODO: Rename and change types and number of parameters
  /*  public static card newInstance(String username, String regNumber, String lastname, String imageUri,String rawdata) {
        card fragment = new card();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("regNumber", regNumber);
        args.putString("lastname", lastname);
        args.putString("imageUri", imageUri);
        args.putString("rawData", rawdata);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_card,container,false);
        // Inflate the layout for this fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = requireActivity().getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            window.setStatusBarColor(Color.TRANSPARENT);  }


        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
        }

        frontCard = view.findViewById(R.id.cardView);
        backCard = view.findViewById(R.id.cardView2);

        RotateView2 flipView = view.findViewById(R.id.flipView);


/*
        String username = getArguments().getString("username");
        String regNumber = getArguments().getString("regNumber");
        String lastname = getArguments().getString("lastname");
        String imageUri = getArguments().getString("imageUri");
        String rawdata = getArguments().getString("rawData");*/
        ///////////////////////////////////////
        String user = DataHolder.username;
        String reg = DataHolder.regNumber;
        String last = DataHolder.lastname;
        String image = DataHolder.imageUri;
        String raw = DataHolder.rawData;

        //////////////////////////////////////////

        // عرض البيانات في الواجهة
        TextView fullName = view.findViewById(R.id.fullName);
        TextView lastName = view.findViewById(R.id.lastName);
        TextView idNumber = view.findViewById(R.id.idNumber);
        TextView exprytedate = view.findViewById(R.id.expiryDate);
        TextView status=view.findViewById(R.id.subscriptionStatus);
        TextView dayesremined=view.findViewById(R.id.daysRemaining);

       CircleImageView profileImage = view.findViewById(R.id.profileImage);

       if (reg != null){ try{
           BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
           Bitmap bitmap = barcodeEncoder.encodeBitmap(reg, BarcodeFormat.QR_CODE, 300, 300);
           ImageView qrImage = view.findViewById(R.id.qrCode);
           ImageView qrImage2 = view.findViewById(R.id.qrCode2);
           qrImage.setImageBitmap(bitmap);
           qrImage2.setImageBitmap(bitmap);

       }catch (Exception e){}

       }

        fullName.setText(user);
        lastName.setText(last);
        idNumber.setText(reg);
        if (image!=null){
        profileImage.setImageURI(Uri.parse(image));

        }
        exprytedate.setText(raw);
       if(raw.trim().equals("00/00/00")){
           exprytedate.setTextColor(0xFFFF0000);
           status.setText("❌ غير صالحة للاستعمال");
           dayesremined.setText("الأيام المتبقية: 00");
           status.setTextColor(0xFFFF0000);
       }
       else {
           status.setText("صالحة للاستعمال ✅");
           dayesremined.setText("الأيام المتبقية: 30");
       }


        return view;
    }



}