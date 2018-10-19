package com.example.ssanusi.advert.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ssanusi.advert.adapter.HomeadvertAdapter;
import com.example.ssanusi.advert.model.AdvertDetails;
import com.example.ssanusi.advert.R;
import com.example.ssanusi.advert.interfaces.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomeadvertAdapter.onClistener {
    ImageView search;
    EditText et;
    List<AdvertDetails> details;
    List<AdvertDetails> searchDetails;
    HomeadvertAdapter homeadvertAdapter;
    RecyclerView rv;
    String searchStr;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Listener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search = view.findViewById(R.id.searchImage);
         et = view.findViewById(R.id.searchMe);
        rv = view.findViewById(R.id.rvForDetails);
        //searchStr = et.getText().toString();
        homeadvertAdapter = new HomeadvertAdapter(this,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(homeadvertAdapter);
        toAddDetails();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"button clicked",Toast.LENGTH_SHORT).show();
               et.setVisibility(View.VISIBLE);
            }
        });


        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(getContext(),s.toString(),Toast.LENGTH_SHORT).show();
                homeadvertAdapter.getFilter().filter(s.toString());
            }
        });

    }

    public void toAddDetails(){
        details = new ArrayList<>();
        details.add(new AdvertDetails("shoes","This is a Nigeria company that deals with shoe production of various kind across the country",
                R.drawable.logo,"Atele","12 July"));
        details.add(new AdvertDetails("Technology","Theis Nigrerisdvdfhfffffggdgddgdggggdzdzbzbzbzb",
                R.drawable.logo,"Dreammesh","12 July"));
        details.add(new AdvertDetails("shoes","Theis Nigrerisdvdfhaegsghyhshhshhhsshshshshdbxbnnxnxnxn",
                R.drawable.google,"google","12 July"));
        details.add(new AdvertDetails("Technology","Theis Nigrerisdvdfhaegsghyhshhshhhsshshshshdbxbnnxnxnxn",
                R.drawable.logo,"Apple","12 July"));
        details.add(new AdvertDetails("Technology","Theis Nigrerisdvdfhaegsghyhshhshhhsshshshshdbxbnnxnxnxn",
                R.drawable.logo,"MoneyBag","12 July"));
        details.add(new AdvertDetails("Technology","Theis Nigrerisdvdfhaegsghyhshhshhhsshshshshdbxbnnxnxnxn",
                R.drawable.logo,"PiggyBank","12 July"));
        details.add(new AdvertDetails("Finance","Theis Nigrerisdvdfhaegsghyhshhshhhsshshshshdbxbnnxnxnxn",
                R.drawable.logo,"SkyBank","12 July"));
        details.add(new AdvertDetails("shoes","Theis Nigrerisdvdfhaegsghyhshhshhhsshshshshdbxbnnxnxnxn",
                R.drawable.logo,"Atele","12 July"));
        homeadvertAdapter.swapItem(details);
        onSizeChanged(homeadvertAdapter.getItemCount());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void toview(AdvertDetails advertDetails) {
        String companyName = advertDetails.getCompanyName();
        String description = advertDetails.getDescription();
        int image = advertDetails.getLogo();
        Fragment fragment = FullDetails.newInstance(companyName,description,image);
        onClicked(fragment);
    }

    @Override
    public void onSizeChanged(int size) {

    }

    public void onClicked (@NonNull final Fragment fragment){
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayoutForBottomNav, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
