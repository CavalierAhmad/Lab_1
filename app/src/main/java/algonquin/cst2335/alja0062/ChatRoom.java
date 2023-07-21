package algonquin.cst2335.alja0062;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.alja0062.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.alja0062.databinding.ReceiveMessageBinding;
import algonquin.cst2335.alja0062.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    // INNER CLASS
        class RowHolder extends RecyclerView.ViewHolder {
            TextView message;
            TextView time;

            public RowHolder(@NonNull View itemView){
                super(itemView);

                itemView.setOnClickListener(clk ->{
                    int position = getAbsoluteAdapterPosition();
                    AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                    builder.setMessage("Do you want to delete the message: " + message.getText());
                    builder.setTitle("Question:");
                    builder.setPositiveButton("Yes",(dialog, cl) -> { });
                    builder.setNegativeButton("No",(dialog, cl) -> { });
                });

                message = itemView.findViewById(R.id.message);
                time = itemView.findViewById(R.id.time);
            }
        }

    // ATTRIBUTES
        ActivityChatRoomBinding binding;
        ArrayList<ChatMessage> messages;
        ChatRoomViewModel chatModel;
        private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // VARIABLE BINDING SECTION
            binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

        // OPEN A DATABASE
            MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
            ChatMessageDAO mDAO = db.cmDAO();

        // ROTATION SURVIVABILITY
            chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
            messages = chatModel.messages.getValue();
            if (messages == null){
                chatModel.messages.postValue(messages = new ArrayList<ChatMessage>());

                // OPEN NEW THREAD FOR QUERY EXECUTION
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() ->
                {
                    messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database
                    runOnUiThread( () ->  binding.list.setAdapter(adapter)); //You can then load the RecyclerView
                });
            }

        // SEND EVENT
            binding.send.setOnClickListener(click -> {
                messages.add(new ChatMessage(
                        binding.textField.getText().toString(),
                        new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a").format(new Date()),
                        true
                ));
                adapter.notifyItemInserted(messages.size()-1);
                binding.list.setLayoutManager(new LinearLayoutManager(this)); // display messages
                binding.textField.setText(""); // clear text
            });
        // RECEIVE EVENT
            binding.receive.setOnClickListener(click -> {
                messages.add(new ChatMessage(
                        binding.textField.getText().toString(),
                        new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a").format(new Date()),
                        false
                ));
                adapter.notifyItemInserted(messages.size()-1);
                binding.list.setLayoutManager(new LinearLayoutManager(this)); // display messages
                binding.textField.setText(""); // clear text
            });

        // LIST ADAPTER SECTION
        binding.list.setAdapter(adapter = new RecyclerView.Adapter<RowHolder>() {

            @NonNull
            @Override
            public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0){
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new RowHolder(binding.getRoot());
                } else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new RowHolder(binding.getRoot());
                }

            }

            @Override
            public void onBindViewHolder(@NonNull RowHolder holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.message.setText(obj.getMessage());
                holder.time.setText(obj.getTime());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                return messages.get(position).isSent() ? 0 : 1;
            }

        });
    }
}