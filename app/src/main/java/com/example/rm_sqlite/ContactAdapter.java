package com.example.rm_sqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private ArrayList<Contacts> listContacts;
    private ArrayList<Contacts> mArrayList;

    private SqliteDatabase mDatabase;

    public ContactAdapter(Context context, ArrayList<Contacts> listContacts) {
        this.context = context;
        this.listContacts = listContacts;
        this.mArrayList=listContacts;
        mDatabase = new SqliteDatabase(context);
    }




    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView name,ph_no;
        public ImageView deleteContact;
        public  ImageView editContact;

        public ContactViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.contact_name);
            ph_no = (TextView)itemView.findViewById(R.id.ph_no);
            deleteContact = (ImageView)itemView.findViewById(R.id.delete_contact);
            editContact = (ImageView)itemView.findViewById(R.id.edit_contact);
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list,parent,false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        final Contacts contacts = listContacts.get(position);

        holder.name.setText(contacts.getName());
        holder.ph_no.setText(contacts.getPhno());

        holder.editContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(contacts);
            }

            private void editTaskDialog(final Contacts contacts) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View subView = inflater.inflate(R.layout.add_contact,null);

                final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
                final EditText contactField = (EditText)subView.findViewById(R.id.enter_phno);

                if (contacts != null){
                    nameField.setText(contacts.getName());
                    contactField.setText(String.valueOf(contacts.getPhno()));
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit contact");
                builder.setView(subView);
                builder.create();

                builder.setPositiveButton("EDIT CONTACT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //MARK:增加欄位
                        final String name = nameField.getText().toString();
                        final String ph_no = contactField.getText().toString();

                        if (TextUtils.isEmpty(name)){
                            Toast.makeText(context,"Something went wrong.Check your input values",Toast.LENGTH_LONG).show();
                        }else {
                            //MARK:欄位的參數
                            mDatabase.updateContacts(new Contacts(contacts.getId(),name,ph_no));
                            //refresh the activity
                            ((Activity)context).finish();
                            context.startActivity(((Activity)context).getIntent());
                        }
                    }
                });


                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });


        holder.deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //delete row from database刪除

                mDatabase.deleteContact(contacts.getId());

                //refresh the activity page.

                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());

                //MARK:-確定取消 刪除
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Remove contact");
//                builder.create();
//
//                builder.setPositiveButton("REMOVE CONTACT", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mDatabase.deleteContact(contacts.getId());
//
//                        ((Activity)context).finish();
//                        context.startActivity(((Activity) context).getIntent());
//                    }
//                });
//
//                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
//                    }
//                });
//                builder.show();







            }
        });
    }

    @Override
    public int getItemCount() {

        return listContacts.size();
    }
}
