package api;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Grade;
import entity.Team;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


/**
 * MongoGradeDataBase class.
 */
public class MongoGradeDataBase implements GradeDataBase {
    // Defining some constants.
    private static final String API_URL = "https://grade-apis.panchen.ca";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String STATUS_CODE = "status_code";
    private static final String GRADE = "grade";
    private static final String MESSAGE = "message";
    private static final String NAME = "name";
    private static final String TOKEN = "token";
    private static final String COURSE = "course";
    private static final String USERNAME = "username";
    private static final int SUCCESS_CODE = 200;

    // load token from env variable.
    public static String getAPIToken() {
        return System.getenv(TOKEN);
    }

    @Override
    public Grade getGrade(String username, String course) {

        // Build the request to get the grade.
        // Note: The API requires the token to be passed as a header.
        // Note: The API requires the course and username to be passed as query parameters.
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final Request request = new Request.Builder()
                .url(String.format("%s/grade?course=%s&username=%s", API_URL, course, username))
                .addHeader(TOKEN, getAPIToken())
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        // Hint: look at the API documentation to understand what the response looks like.
        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE) == SUCCESS_CODE) {
                final JSONObject grade = responseBody.getJSONObject(GRADE);
                return Grade.builder()
                        .username(grade.getString(USERNAME))
                        .course(grade.getString(COURSE))
                        .grade(grade.getInt(GRADE))
                        .build();
            }
            else {
                throw new RuntimeException("Grade could not be found for course: " + course
                                           + " and username: " + username);
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public Grade[] getGrades(String username) {

        // Build the request to get all grades for a user.
        // Note: The API requires the token to be passed as a header.
        // Note: The API requires the username to be passed as a query parameter.
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final Request request = new Request.Builder()
                .url(String.format("%s/grade?username=%s", API_URL, username))
                .addHeader(TOKEN, getAPIToken())
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        // Note: you can look at the API documentation to understand what the response looks like
        // to better understand how this parses the response.
        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE) == SUCCESS_CODE) {
                final JSONArray grades = responseBody.getJSONArray("grades");
                final Grade[] result = new Grade[grades.length()];
                for (int i = 0; i < grades.length(); i++) {
                    final JSONObject grade = grades.getJSONObject(i);
                    result[i] = Grade.builder()
                            .username(grade.getString(USERNAME))
                            .course(grade.getString(COURSE))
                            .grade(grade.getInt(GRADE))
                            .build();
                }
                return result;
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public Grade logGrade(String course, int grade) throws JSONException {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final MediaType mediaType = MediaType.parse(APPLICATION_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(COURSE, course);
        requestBody.put(GRADE, grade);
        final RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        final Request request = new Request.Builder()
                .url(String.format("%s/grade", API_URL))
                .method("POST", body)
                .addHeader(TOKEN, getAPIToken())
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE) == SUCCESS_CODE) {
                return null;
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public Team formTeam(String name) throws JSONException {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final MediaType mediaType = MediaType.parse(APPLICATION_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(NAME, name);
        final RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        final Request request = new Request.Builder()
                .url(String.format("%s/team", API_URL))
                .method("POST", body)
                .addHeader(TOKEN, getAPIToken())
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE) == SUCCESS_CODE) {
                final JSONObject team = responseBody.getJSONObject("team");
                final JSONArray membersArray = team.getJSONArray("members");
                final String[] members = new String[membersArray.length()];
                for (int i = 0; i < membersArray.length(); i++) {
                    members[i] = membersArray.getString(i);
                }

                return Team.builder()
                        .name(team.getString(NAME))
                        .members(members)
                        .build();
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public Team joinTeam(String name) throws JSONException {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final MediaType mediaType = MediaType.parse(APPLICATION_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(NAME, name);
        final RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        final Request request = new Request.Builder()
                .url(String.format("%s/team", API_URL))
                .method("PUT", body)
                .addHeader(TOKEN, getAPIToken())
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE) == SUCCESS_CODE) {
                return null;
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public void leaveTeam() throws JSONException {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final MediaType mediaType = MediaType.parse(APPLICATION_JSON);
        final JSONObject requestBody = new JSONObject();
        final RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        final Request request = new Request.Builder()
                .url(String.format("%s/leaveTeam", API_URL))
                .method("PUT", body)
                .addHeader(TOKEN, getAPIToken())
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE) != SUCCESS_CODE) {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    @Override
    public Team getMyTeam() {
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        final Request request = new Request.Builder()
                .url(String.format("%s/team", API_URL))
                .method("GET", null)
                .addHeader(TOKEN, getAPIToken())
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Failed to fetch team: " + response.code());
                return new Team("Unknown", new String[0]);
            }

            final String responseBody = response.body().string();
            System.out.println("Team API response: " + responseBody); // Optional debug print

            JSONObject json = new JSONObject(responseBody);
            JSONObject teamObject = json.getJSONObject("team");
            JSONArray membersArray = teamObject.getJSONArray("members");

            String[] members = new String[membersArray.length()];
            for (int i = 0; i < membersArray.length(); i++) {
                members[i] = membersArray.getString(i);
            }

            return new Team(teamObject.getString("name"), members);

        } catch (Exception e) {
            e.printStackTrace();
            return new Team("Unknown", new String[0]);
        }
    }
}
