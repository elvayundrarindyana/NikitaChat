package com.juaracoding.nikitachat.Model;


import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.juaracoding.nikitachat.R;
import com.nikita.chat.Chat;
import com.nikita.chat.ChatListener;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText editText;
    ArrayList<MessageModel> messageModels = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        setTitle(getIntent().getStringExtra("nama"));

        editText = findViewById(R.id.txtMsg);
        final RecyclerView recyclerView = findViewById(R.id.List);
        findViewById(R.id.tblSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();

                MessageModel messageModel = new MessageModel();
                messageModel.setNama(getIntent().getStringExtra("nama"));
                messageModel.setMessage(msg);
                messageModel.setStatus(true);
                messageModel.setDate("now");
                messageModel.setImage("");

                messageModels.add(messageModel);
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerView.scrollToPosition(messageModels.size() - 1);

                com.nikita.chat.Chat.sendMessage(messageModel.getNama(), messageModel.getMessage());
                editText.setText("");


            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new RecyclerView.Adapter() {

            @Override
            public int getItemViewType(int position) {
                if (messageModels.get(position).isStatus()) {
                    //saya kirim pesan
                    return R.layout.messageitemsend;
                } else {
                    //saya terima pesan
                    return R.layout.messageitem;
                }
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
                return new RecyclerView.ViewHolder(v) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                TextView textView = holder.itemView.findViewById(R.id.txtNama);
                textView.setText(messageModels.get(position).getNama());

                textView = holder.itemView.findViewById(R.id.txtMessage);
                textView.setText(messageModels.get(position).getMessage());

                textView = holder.itemView.findViewById(R.id.txtDate);
                textView.setText(messageModels.get(position).getDate());
            }

            @Override
            public int getItemCount() {
                return messageModels.size();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }


        //baca
        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/chat";// + getIntent().getStringExtra("nama");
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            int len = dataInputStream.readInt();
            for (int i = 0; i < len; i++) {
                MessageModel messageModel = new MessageModel();
                messageModel.setNama(dataInputStream.readUTF());
                messageModel.setMessage(dataInputStream.readUTF());
                messageModel.setDate(dataInputStream.readUTF());
                messageModel.setImage(dataInputStream.readUTF());
                messageModel.setStatus(dataInputStream.readBoolean());
                messageModels.add(messageModel);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scrollToPosition(messageModels.size() - 1);


        com.nikita.chat.Chat.setUp(this, "elva@12345", new ChatListener() {
            public void onMessageReceive(String s, String s1, String s2) {
                MessageModel messageModel = new MessageModel();
                messageModel.setNama(s);
                messageModel.setMessage(s1);
                messageModel.setStatus(false);
                messageModel.setDate(s2);
                messageModel.setImage("");

                messageModels.add(messageModel);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.getAdapter().notifyDataSetChanged();
                        recyclerView.scrollToPosition(messageModels.size() - 1);


                        //kasih notif
//                        NotificationCompat.Builder builder=new NotificationCompat.Builder(MessageActivity.this,"chat");
//                        builder.setContentTitle("Chat");
//                        builder.setAutoCancel(true);
//                        builder.setContentText("ada pesan baru");
//
//                        NotificationManager mNotificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//                        mNotificationManager.notify(1,builder.build());

                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //simpan
        try {
            String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/chat";// + getIntent().getStringExtra("nama");
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);

            dataOutputStream.writeInt(messageModels.size());
            for (int i = 0; i < messageModels.size(); i++) {
                dataOutputStream.writeUTF(messageModels.get(i).getNama());
                dataOutputStream.writeUTF(messageModels.get(i).getMessage());
                dataOutputStream.writeUTF(messageModels.get(i).getDate());
                dataOutputStream.writeUTF(messageModels.get(i).getImage());
                dataOutputStream.writeBoolean(messageModels.get(i).isStatus());
            }
            dataOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


   // @Override
    public boolean onCreateOptionMenu(Menu menu){
        getMenuInflater().inflate(R.menu.delmenu, menu);
        return true;
    }

   //@Override
    public boolean onOptionItemSelected(MenuItem item){
        if(item.getItemId()==R.menu.delmenu){

    String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() +"/chat";//+ getIntent().getStringExtra("nama");
    new File(fileName).delete();

    messageModels.clear();
    recyclerView.getAdapter().notifyDataSetChanged();
    }
    return super.onOptionsItemSelected(item);
}
            }