import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class Question {
    String questionText;
    String[] options;
    char correctAnswer;

    public Question(String questionText, String[] options, char correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public void displayQuestion() {
        System.out.println(questionText);
        for (int i = 0; i < options.length; i++) {
            System.out.println((char) ('A' + i) + ". " + options[i]);
        }
    }

    public boolean checkAnswer(char answer) {
        return answer == correctAnswer;
    }
}

class Quiz {
    private Question[] questions;
    private int score;
    private char[] userAnswers;

    public Quiz(Question[] questions) {
        this.questions = questions;
        this.score = 0;
        this.userAnswers = new char[questions.length];
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < questions.length; i++) {
            Question currentQuestion = questions[i];
            currentQuestion.displayQuestion();

            boolean[] answered = {false}; // Array to modify within TimerTask
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!answered[0]) {
                        System.out.println("\nTime's up! Moving to the next question.\n");
                    }
                }
            }, 15000); // 15-second timer

            char userAnswer = ' ';
            long startTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - startTime) < 15000 && !answered[0]) {
                System.out.print("Enter your answer (A/B/C/D): ");
                if (scanner.hasNext()) {
                    userAnswer = scanner.next().toUpperCase().charAt(0);
                    answered[0] = true;
                }
            }
            timer.cancel();

            if (answered[0] && currentQuestion.checkAnswer(userAnswer)) {
                score++;
                System.out.println("Correct!\n");
            } else if (answered[0]) {
                System.out.println("Incorrect. The correct answer was " + currentQuestion.correctAnswer + "\n");
            }
            userAnswers[i] = userAnswer;
        }
        scanner.close();
        displayResults();
    }

    public void displayResults() {
        System.out.println("Quiz Over!");
        System.out.println("Your final score: " + score + "/" + questions.length);
        for (int i = 0; i < questions.length; i++) {
            System.out.println("Question " + (i + 1) + ": " + questions[i].questionText);
            System.out.println("Your answer: " + userAnswers[i] + " | Correct answer: " + questions[i].correctAnswer);
        }
    }
}

class QuizApp {
    public static void main(String[] args) {
        Question[] questions = new Question[5];
        questions[0] = new Question("What is the capital of France?", new String[] { "Berlin", "London", "Paris", "Rome" }, 'C');
        questions[1] = new Question("Which planet is known as the Red Planet?", new String[] { "Venus", "Mars", "Jupiter", "Saturn" }, 'B');
        questions[2] = new Question("Who wrote 'To Kill a Mockingbird'?", new String[] { "Harper Lee", "Mark Twain", "Ernest Hemingway", "F. Scott Fitzgerald" }, 'A');
        questions[3] = new Question("What is the largest mammal?", new String[] { "Elephant", "Blue Whale", "Giraffe", "Human" }, 'B');
        questions[4] = new Question("What is the chemical symbol for water?", new String[] { "O2", "H2O", "CO2", "HO" }, 'B');

        Quiz quiz = new Quiz(questions);
        quiz.start();
    }
}