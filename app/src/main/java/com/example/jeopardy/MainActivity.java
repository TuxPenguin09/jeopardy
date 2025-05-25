package com.example.jeopardy;

import android.content.DialogInterface;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jeopardy.model.Question;
import com.example.jeopardy.utils.QuestionLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Map<String, List<Question>> questionMap;
    private Map<Button, Question> buttonQuestionMap = new HashMap<>();
    private int score = 0;
    private int totalQuestions = 9;
    TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // assuming the layout XML is activity_main.xml

        questionMap = QuestionLoader.loadQuestions(this);
        setupButtons();

        scoreTextView = findViewById(R.id.scoreView);
    }

    private void setupButtons() {
        String[] categories = {"General", "Guess the movie by EMOJI", "iACADEMY"};
        int[][] buttonIds = {
                {R.id.column1_q1, R.id.column1_q2, R.id.column1_q3},
                {R.id.column2_q1, R.id.column2_q2, R.id.column2_q3},
                {R.id.column3_q1, R.id.column3_q2, R.id.column3_q3}
        };

        for (int col = 0; col < 3; col++) {
            List<Question> questions = questionMap.get(categories[col]);
            for (int row = 0; row < 3; row++) {
                Button button = findViewById(buttonIds[col][row]);
                Question q = questions.get(row);
                buttonQuestionMap.put(button, q);

                button.setOnClickListener(v -> showQuestionDialog(button));
            }
        }
    }

    private void showQuestionDialog(Button button) {
        Question q = buttonQuestionMap.get(button);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Question");
        builder.setMessage(q.getQuestion());

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String answer = input.getText().toString().trim().toLowerCase();
            if (q.getAnswer().contains(answer)) {
                score += 100;
                button.setEnabled(false);
                button.setText("✓");
                scoreTextView.setText("Score: " + score);
            } else {
                button.setEnabled(false);
                button.setText("✗");
                Toast.makeText(this, "Wrong! Correct: " + q.getAnswer().get(0), Toast.LENGTH_LONG).show();
            }

            totalQuestions--;
            if (totalQuestions == 0) showGameOver();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showGameOver() {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Final Score: " + score)
                .setPositiveButton("Retry", (dialog, which) -> {
                    for (Button button : buttonQuestionMap.keySet()) {
                        button.setEnabled(true);
                        button.setText(button.getText().toString().replace("✓", "100 pts").replace("✗", "100 pts"));
                        score = 0;
                        scoreTextView.setText("Score: " + score);
                    }
                })
                .show();
    }
}