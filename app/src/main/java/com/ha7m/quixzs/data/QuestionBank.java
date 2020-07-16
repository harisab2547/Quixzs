package com.ha7m.quixzs.data;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ha7m.quixzs.Model.Question;
import com.ha7m.quixzs.controller.AppController;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    private static final String url ="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements.json";

    ArrayList<Question> questionArrayList = new ArrayList<>();

    public List<Question> getQuestion(final AnswerListAsyncResponse callback){


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, (JSONArray) null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i<= response.length(); i++){
                            try {

                                Question question = new Question();
                                question.setAnswer(response.getJSONArray(i).get(0).toString());
                                question.setAnswerTure(response.getJSONArray(i).getBoolean(1));


                                questionArrayList.add(question);
                            }
                            catch (JSONException e)

                            {

                                e.printStackTrace();
                            }

                        }
                        if (callback!=null){
                            callback.processFinished(questionArrayList);
                        }
                    }

                    },

 new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

// Access the RequestQueue through your singleton class.
    AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        return questionArrayList ;
    }



}
