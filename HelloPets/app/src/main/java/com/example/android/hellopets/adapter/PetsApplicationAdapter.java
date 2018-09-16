package com.example.android.hellopets.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.hellopets.R;
import com.example.android.hellopets.data.Pet;

import java.util.List;

/**
 * Created by wkp on 2016/12/6.
 */
public class PetsApplicationAdapter extends ArrayAdapter<Pet> {

    public PetsApplicationAdapter(Context context, List<Pet> pets) {
        super(context, 0, pets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.pet_application_list_item, parent, false);
        }

        // 设置3个TextView的属性
        Pet currentPet=getItem(position);

        TextView petnameTextView=(TextView)listItemView.findViewById(R.id.pet_name);
        petnameTextView.setText(currentPet.getPetname());

        TextView breedTextView=(TextView)listItemView.findViewById(R.id.breed);
        breedTextView.setText(currentPet.getBreed());

        TextView sexTextView=(TextView)listItemView.findViewById(R.id.pet_sex);
        sexTextView.setText(parseSex(currentPet.getSex()));

        TextView ageTextView=(TextView)listItemView.findViewById(R.id.pet_age);
        ageTextView.setText(currentPet.getAge()+"");

        TextView ispassTextView=(TextView)listItemView.findViewById(R.id.is_pass);
        ispassTextView.setText(parseState(currentPet.getIspass()));

        return listItemView;
    }

    private String parseState(Integer ispass){
        if(ispass==-1){
            return "失败";
        }
        if(ispass==0){
            return "等待";
        }
        if(ispass==1){
            return "通过";
        }
        return "";
    }

    private String parseSex(String sex){
        if(sex.equals("m")){
            return "雄性";
        }
        if(sex.equals("f")){
            return "雌性";
        }
        return "雄性";
    }
}
