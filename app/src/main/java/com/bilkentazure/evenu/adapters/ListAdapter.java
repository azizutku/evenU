package com.bilkentazure.evenu.adapters;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bilkentazure.evenu.ListActivity;
import com.bilkentazure.evenu.MainActivity;
import com.bilkentazure.evenu.R;
import com.bilkentazure.evenu.models.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Aziz Utku Kağıtcı on 06/05/2018
 *
 * @author Aziz Utku Kağıtcı
 * @version 06/05/2018
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

	private List<Item> items;
	private Context context;
	private int type;

	//Firebase
	private FirebaseAuth mAuth;
	private FirebaseFirestore db;

	public ListAdapter(List<Item> items, Context context, int type){
		this.context = context;
		this.items = items;
		this.type = type;
		db = FirebaseFirestore.getInstance();
		mAuth = FirebaseAuth.getInstance();
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorize_item,parent,false);
		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, int position) {

		final Item item = items.get(position);
		final String name = item.getName();
		final FloatingActionButton btnFollow = holder.getButton();

		holder.setName(name);

		if (MainActivity.userModel != null) {

			ArrayList<String> subscribed = null;
			if( item.getKind().equals(Item.ITEM_DEPARTMENT) ){
				subscribed = MainActivity.userModel.getSubscribedDepartments();
			}
			else if ( item.getKind().equals(Item.ITEM_CLUB) ){
				subscribed = MainActivity.userModel.getSubscribedClubs();
			}
			else if ( item.getKind().equals(Item.ITEM_INTEREST) ) {
				subscribed = MainActivity.userModel.getSubscribeInterests();
			}


			for (String item_name : subscribed) {

				if (item_name.equals(name)) {
					btnFollow.setImageResource(R.drawable.ic_favorite_selected);
				}

			}

		}

		holder.rlt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, ListActivity.class);
				intent.putExtra("name", name);
				intent.putExtra("type", type);
				context.startActivity(intent);

			}
		});

		btnFollow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (MainActivity.userModel != null) {


					ArrayList<String> subscribed = null;
					if( item.getKind().equals(Item.ITEM_DEPARTMENT) ){
						subscribed = MainActivity.userModel.getSubscribedDepartments();
					}
					else if ( item.getKind().equals(Item.ITEM_CLUB) ){
						subscribed = MainActivity.userModel.getSubscribedClubs();
					}
					else if ( item.getKind().equals(Item.ITEM_INTEREST) ) {
						subscribed = MainActivity.userModel.getSubscribeInterests();
					}

					boolean isPresent = false;

					for (Iterator<String> iterator = subscribed.iterator(); iterator.hasNext(); ) {
						String value = iterator.next();

						if (value.equals(name)) {

							//FIXME add success listener
							iterator.remove();
							MainActivity.userModel.setFavoriteEvents(subscribed);
							isPresent = true;
							btnFollow.setImageResource(R.drawable.ic_favorite_border);

							if( item.getKind().equals(Item.ITEM_DEPARTMENT) ){
								db.collection("users").document(mAuth.getCurrentUser().getUid()).update("subscribedDepartments", subscribed);
							}
							else if ( item.getKind().equals(Item.ITEM_CLUB) ){
								db.collection("users").document(mAuth.getCurrentUser().getUid()).update("subscribedClubs", subscribed);
							}
							else if ( item.getKind().equals(Item.ITEM_INTEREST) ) {
								db.collection("users").document(mAuth.getCurrentUser().getUid()).update("subscribeInterests", subscribed);
							}


						}


					}

					if (!isPresent) {
						subscribed.add(name);
						MainActivity.userModel.setFavoriteEvents(subscribed);
						btnFollow.setImageResource(R.drawable.ic_favorite_selected);

						if( item.getKind().equals(Item.ITEM_DEPARTMENT) ){
							db.collection("users").document(mAuth.getCurrentUser().getUid()).update("subscribedDepartments", subscribed);
						}
						else if ( item.getKind().equals(Item.ITEM_CLUB) ){
							db.collection("users").document(mAuth.getCurrentUser().getUid()).update("subscribedClubs", subscribed);
						}
						else if ( item.getKind().equals(Item.ITEM_INTEREST) ) {
							db.collection("users").document(mAuth.getCurrentUser().getUid()).update("subscribeInterests", subscribed);
						}
					}


				}

			}
		});


	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {

		private TextView txtName;
		public FloatingActionButton btnFollow;
		public RelativeLayout rlt;

		public MyViewHolder(View itemView) {
			super(itemView);
			rlt = itemView.findViewById(R.id.item_categorize_rlt_main);
			txtName = itemView.findViewById(R.id.item_categorize_txt_name);
			btnFollow = itemView.findViewById(R.id.item_categorize_btn_follow);
		}

		public void setName(String name){
			txtName.setText(name);
		}

		public FloatingActionButton getButton(){
			return btnFollow;
		}

	}
}
