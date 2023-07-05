package algonquin.cst2335.alja0062;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import algonquin.cst2335.alja0062.databinding.ActivityChatRoomBinding;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // variable binding section
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // load variables
        binding.list.setAdapter(new RecyclerView.Adapter<>());
    }
}