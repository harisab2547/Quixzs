package com.ha7m.quixzs.data;

import com.ha7m.quixzs.Model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {

    void processFinished(ArrayList<Question> questionArrayList);


}
