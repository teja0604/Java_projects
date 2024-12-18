import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class User {
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }
}

class Question {
    private String questionText;
    private String[] options;
    private String correctAnswer;

    public Question(String questionText, String[] options, String correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}

class ExamManager {
    private List<Question> questions;
    private User user;
    private int score;

    public ExamManager(User user) {
        this.user = user;
        this.questions = new ArrayList<>();
        this.score = 0;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void startQuiz() {
        Scanner sc = new Scanner(System.in);

        for (Question question : questions) {
            System.out.println(question.getQuestionText());
            String[] options = question.getOptions();
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }

            TimerThread timer = new TimerThread(30);
            timer.start();

            String answer = null;

            System.out.print("Enter your answer (1-4): ");
            while (true) {
                if (sc.hasNextLine()) {
                    answer = sc.nextLine();
                    timer.interrupt();
                    break;
                }
            }

            try {
                timer.join();
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }

            if (answer != null && !answer.isEmpty()) {
                int choice = Integer.parseInt(answer);
                if (choice >= 1 && choice <= options.length &&
                        options[choice - 1].equalsIgnoreCase(question.getCorrectAnswer())) {
                    score++;
                }
            }

            System.out.println("Answer submitted. Proceeding...\n");
        }

        System.out.println("Your final score is: " + score);
        sc.close();
    }

    class TimerThread extends Thread {
        private int timeRemaining;

        public TimerThread(int time) {
            this.timeRemaining = time;
        }

        @Override
        public void run() {
            try {
                while (timeRemaining > 0 && !Thread.currentThread().isInterrupted()) {
                    System.out.print("\rTime left: " + timeRemaining + " seconds ");
                    Thread.sleep(1000);
                    timeRemaining--;
                }
                if (!Thread.currentThread().isInterrupted()) {
                    System.out.print("\rTime is up! Moving to the next question.\n");
                }
            } catch (InterruptedException e) {
                System.out.print("\r                          \r");
            }
        }
    }
}

public class OnlineExamSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Your Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Your Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Your Password: ");
        String password = sc.nextLine();

        User user = new User(name, email, password);
        ExamManager examManager = new ExamManager(user);

        String[] options1 = {"4", "5", "6", "9"};
        Question question1 = new Question("What is 2+2?", options1, "4");
        String[] options2 = {"10", "11", "13", "14"};
        Question question2 = new Question("What is 5 x 2?", options2, "10");
        String[] options3 = {"3", "4", "5", "6"};
        Question question3 = new Question("What is 10 - 7?", options3, "3");

        examManager.addQuestion(question1);
        examManager.addQuestion(question2);
        examManager.addQuestion(question3);

        examManager.startQuiz();
    }
}
