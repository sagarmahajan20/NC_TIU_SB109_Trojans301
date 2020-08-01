package model;

public class HomeModel
{
    private String question;


    public HomeModel() {
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "HomeModel{" +
                "question='" + question + '\'' +

                '}';
    }
}

