package algonquin.cst2335.alja0062;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import algonquin.cst2335.alja0062.databinding.ActivityChatRoomBinding;

public class ChatRoom extends AppCompatActivity {

    class RowHolder extends RecyclerView.ViewHolder {
        public RowHolder(@NonNull View itemView){
            super(itemView);
        }
    }

    ActivityChatRoomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // variable binding section
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // load variables
        binding.list.setAdapter(new RecyclerView.Adapter<RowHolder>() {
            @NonNull
            @Override
            public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RowHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });
    }
}