package com.tech.programming.Authentication_server.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tech.programming.Authentication_server.Exception.ValidationException;
import com.tech.programming.Authentication_server.dto.LoginRequest;
import com.tech.programming.Authentication_server.dto.UserRequest;
import com.tech.programming.Authentication_server.dto.UserResponse;
import com.tech.programming.Authentication_server.entity.User;
import com.tech.programming.Authentication_server.jpa.UserRepository;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Value("${auth.github.clientId}")
    private String gitHubClientId;

    @Value("${auth.github.clientSecret}")
    private String gitHubClientSecret;

    @Value("${auth.github.redirect_url}")
    private String gitHubCallBackUrl;

    @Value("${auth.github.tokenURl")
    private String tokenURl;

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWTService jwtService;

    public UserService() {
        this.encoder = new BCryptPasswordEncoder(12);
    }

    public UserResponse addUser(UserRequest request){
        User user = request.toEntity(null);
        user.setPasswordHash(encoder.encode(request.getPasswordHash()));
        userRepository.save(user);
        return new UserResponse(user);
    }

    public UserResponse getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new IllegalArgumentException("Invalid User ID");
        }
        return new UserResponse(optionalUser.get());
    }

    public UserResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if(user == null) {
            throw new IllegalArgumentException("Invalid Email Id");
        }else if(!Objects.equals(user.getPasswordHash(), loginRequest.getPassword())){
            throw new IllegalArgumentException("Invalid password");
        }

        return new UserResponse(user);
    }

    public String verifyLogin(LoginRequest request) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
        ));

        if(authentication.isAuthenticated()){
            return jwtService.generateToken(request.getEmail());
        }
        return "failed";
    }

    public User verifyGoogleToken(String token) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url("https://oauth2.googleapis.com/tokeninfo?id_token="+token)
                .build();

        try{
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){
                throw new IOException("Unexpected response code: " + response);
            }
            String responseBody = response.body().string();

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
            String email = jsonObject.get("email").getAsString();
            String name = jsonObject.get("name").getAsString();
            if (!StringUtils.hasText(email)) {
                throw new ValidationException("Could not verify token");
            }
            User user = userRepository.findByEmail(email);
            if(user == null){
                user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setDescription("tester for test");
                user.setPasswordHash(encoder.encode(token));
                userRepository.save(user);
            }
            return user;
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public User verifyGitHubLogin(String code) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("client_id", gitHubClientId);
        bodyParams.put("client_secret", gitHubClientSecret);
        bodyParams.put("code", code);
        bodyParams.put("redirect_uri", gitHubCallBackUrl);

        String access_token = exchangeCodeForToken(bodyParams);

        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .header("Authorization", "token " + code)
                .header("Accept", "application/json")
                .build();

        try{
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){
                throw new IOException("Unexpected response code: " + response);
            }
            String responseBody = response.body().string();

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
            String email = jsonObject.get("email").getAsString();
            String name = jsonObject.get("name").getAsString();
            if (!StringUtils.hasText(email)) {
                throw new ValidationException("Could not verify token");
            }
            User user = userRepository.findByEmail(email);
            if(user == null){
                user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setDescription("tester for test");
                user.setPasswordHash(encoder.encode(access_token));
                userRepository.save(user);
            }
            return user;
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private String exchangeCodeForToken(Map<String, String> bodyParams) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
            formBodyBuilder.add(entry.getKey(), entry.getValue());
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(tokenURl)
                .post(formBodyBuilder.build())
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String access_token = null;
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                access_token = jsonObject.get("access_token").getAsString();
            }
            if(access_token == null){
                throw new ValidationException("Could not verify code");
            }
            return access_token;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
