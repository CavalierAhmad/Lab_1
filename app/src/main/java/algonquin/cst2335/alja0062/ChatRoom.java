package algonquin.cst2335.alja0062;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.alja0062.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.alja0062.databinding.ReceiveMessageBinding;
import algonquin.cst2335.alja0062.databinding.SendMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;
    ArrayList<ChatMessage> messages;
    private RecyclerView.Adapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
//        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setLayoutManager(new GridLayoutManager(this, 1));
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if(messages == null)
        {
            chatModel.messages.postValue( messages = new ArrayList<>());
        }

        binding.sendButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, true);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            // clear the previous text:
            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, false);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            // clear the previous text:
            binding.textInput.setText("");
        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SendMessageBinding binding = SendMessageBinding.inflate(getLayoutInflater());
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
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            public int getItemViewType(int position){
                ChatMessage obj = messages.get(position);
                if (obj.isSentButton) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}