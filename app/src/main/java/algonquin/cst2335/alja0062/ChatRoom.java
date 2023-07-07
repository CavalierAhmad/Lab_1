package algonquin.cst2335.alja0062;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import algonquin.cst2335.alja0062.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.alja0062.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    // Inner class section
        class RowHolder extends RecyclerView.ViewHolder {
            TextView message;
            TextView time;

            public RowHolder(@NonNull View itemView){
                super(itemView);
                message = itemView.findViewById(R.id.message);
                time = itemView.findViewById(R.id.time);
            }
        }

    // This ChatRoom attributes
        ActivityChatRoomBinding binding;
        ArrayList<String> messages = new ArrayList<>();
        private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // VARIABLE BINDING SECTION
            binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

        // EVENTS SECTION
            // "Send" button
                binding.send.setOnClickListener(click -> {
                    messages.add(binding.textField.getText().toString());
                    adapter.notifyItemInserted(messages.size()-1);
                    binding.list.setLayoutManager(new LinearLayoutManager(this));
                    binding.textField.setText(""); // clear text
                });
            // "Receive" button

        // LIST ADAPTER SECTION
        binding.list.setAdapter(adapter = new RecyclerView.Adapter<RowHolder>() {

            @NonNull
            @Override
            public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                return new RowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull RowHolder holder, int position) {
                String obj = messages.get(position);
                holder.message.setText(obj);
                holder.time.setText("");
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }
        });
    }
}