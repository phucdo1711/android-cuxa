package com.example.dell.appcuxa.CuxaAPI;

import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkController {
    private static volatile CuXaAPI cuXaAPI;
   public static final String BASE_URL = "https://cx.emmasoft.com.vn/api/";
   // public static final String BASE_URL = "http://192.168.1.112:8080/api/";

    //public static final String BASE_AUTH_URL = "https://cx.emmasoft.com.vn/api/auth/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static CuXaAPI upload() {
        if (cuXaAPI == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .registerTypeAdapter(Long.class,new LongTypeAdapter())
                    .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                    .registerTypeAdapter(Double.class,new DoubleTypeAdapter())
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    //.client(getUnsafeOkHttpClient(60,60))
                    // .client(okHttpClient())
                    .build();
            cuXaAPI = retrofit.create(CuXaAPI.class);
        }
        return cuXaAPI;
    }

    public static CuXaAPI signInAuth() {
        if (cuXaAPI == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .registerTypeAdapter(Long.class,new LongTypeAdapter())
                    .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                    .registerTypeAdapter(Double.class,new DoubleTypeAdapter())
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    //.client(getUnsafeOkHttpClient(60,60))
                   // .client(okHttpClient())
                    .build();
            cuXaAPI = retrofit.create(CuXaAPI.class);
        }
        return cuXaAPI;
    }

    public static class LongTypeAdapter extends TypeAdapter<Long> {
        @Override
        public Long read(JsonReader reader) throws IOException {
            if(reader.peek() == JsonToken.NULL){
                reader.nextNull();
                return null;
            }
            String stringValue = reader.nextString();
            try{
                Long value = Long.valueOf(stringValue);
                return value;
            }catch(NumberFormatException e){
                return null;
            }
        }
        @Override
        public void write(JsonWriter writer, Long value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }
    public static class IntegerTypeAdapter extends TypeAdapter<Integer> {
        @Override
        public Integer read(JsonReader reader) throws IOException {
            if(reader.peek() == JsonToken.NULL){
                reader.nextNull();
                return null;
            }
            String stringValue = reader.nextString();
            try{
                Integer value = Integer.valueOf(stringValue);
                return value;
            }catch(NumberFormatException e){
                return null;
            }
        }
        @Override
        public void write(JsonWriter writer, Integer value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    public static class DoubleTypeAdapter extends TypeAdapter<Double> {
        @Override
        public Double read(JsonReader reader) throws IOException {
            if(reader.peek() == JsonToken.NULL){
                reader.nextNull();
                return null;
            }
            String stringValue = reader.nextString();
            try{
                Double value = Double.valueOf(stringValue);
                return value;
            }catch(NumberFormatException e){
                return null;
            }
        }
        @Override
        public void write(JsonWriter writer, Double value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }
}
