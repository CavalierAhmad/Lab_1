package algonquin.cst2335.alja0062;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.alja0062.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.alja0062.databinding.ReceiveMessageBinding;
import algonquin.cst2335.alja0062.databinding.SentMessageBinding;
import algonquin.cst2335.alja0062.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;
    ArrayList<ChatMessage> messages;

    ChatMessageDAO mDAO;
    private RecyclerView.Adapter myAdapter;

    Executor thread = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
//        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.list.setLayoutManager(new GridLayoutManager(this, 1));
        setContentView(binding.getRoot());

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if(messages == null)
        {
            chatModel.messages.postValue( messages = new ArrayList<>());
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database
                runOnUiThread( () ->  binding.list.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        binding.send.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(binding.textField.getText().toString(), currentDateandTime, true);
            thread.execute(() -> {
                mDAO.insertMessage(chatMessage);
                messages.clear();
                messages.addAll( mDAO.getAllMessages() );
            });

            myAdapter.notifyItemInserted(messages.size() - 1);
            // clear the previous text:
            binding.textField.setText("");
        });

        binding.receive.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(binding.textField.getText().toString(), currentDateandTime, false);
            thread.execute(() -> {
                mDAO.insertMessage(chatMessage);
                messages.clear();
                messages.addAll( mDAO.getAllMessages() );
            });
            myAdapter.notifyItemInserted(messages.size() - 1);
            // clear the previous text:
            binding.textField.setText("");
        });

        binding.list.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder( binding.getRoot() );
                } else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder( binding.getRoot() );
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTime());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            public int getItemViewType(int position){
                ChatMessage obj = messages.get(position);
                if (obj.isSent) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        chatModel.selectedMessage.observe(this, (newMessageValue) -> {
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);

            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.replace(R.id.fragmentLocation, chatFragment);
            tx.addToBackStack("");
            tx.commit();
        });
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);
                chatModel.selectedMessage.postValue(selected);
//                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
//                builder.setMessage("Do you want to delete the message: "  + messageText.getText())
//                        .setTitle("Question: ")
//                        .setPositiveButton("Yes", (d, c) -> {
//                            ChatMessage m = messages.get(position);
//                            thread.execute(() -> {
//                                mDAO.deleteMessage(m);
//                            });
//                            messages.remove(position);
//                            myAdapter.notifyItemRemoved(position);
//                            Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
//                                    .setAction("Undo", cl -> {
//                                        thread.execute(() -> {
//                                            mDAO.insertMessage(m);
//                                        });
//                                        messages.add(position, m);
//                                        myAdapter.notifyItemInserted(position);
//                                    }).show();
//                        }).setNegativeButton("No", (d, c) -> {
//                        }).create()
//                        .show();
            });
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}

//done