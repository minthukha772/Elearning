package com.blissstock.mappingSite.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/* class to demonstrate use of Drive files list API */
@Component
public class GoogleDriveService {
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "My Project";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "./credentials.json";
    // Embedding JSON data as a String in the class
    // private static final String CREDENTIALS_JSON_DATA = "{\n" +
    //         "  \"web\": {\n" +
    //         "    \"client_id\": \"807260581765-ni6h7imb5ss3pk8860scnsukd0h9oour.apps.googleusercontent.com\",\n" +
    //         "    \"project_id\": \"pyinnyar-subuu\",\n" +
    //         "    \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
    //         "    \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
    //         "    \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
    //         "    \"client_secret\": \"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\",\n" +
    //         "    \"redirect_uris\": [\"https://pyinnyarsubuu.com:8888/Callback\"]\n" +
    //         "  }\n" +
    //         "}";

    private static final String CREDENTIALS_JSON_DATA = "{\n" +
            "  \"type\": \"service_account\",\n" +
            "  \"project_id\": \"pyinnyar-subuu\",\n" +
            "  \"private_key_id\": \"c1eeb751b029eafba8aaf9d0e8ccff31953f07ac\",\n" +
            "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDHWdZqJtm2Oc3c\\n91CjNozgLqwpdNQ72c9pgr0Dv+34K2Au/56/iQ/AonFU2h75n5myp44KPFly5W4t\\ny6prW0nX2By1EITK3/fuJmjJkvdFSaD6VIjcJ3ATRxiFVws/3EkX5dYC0eOGKpZt\\nBBUuknO9RrmeG/JJYPNKxFAYK6LvCYNiFTFV749DliOp8lX3xjG81HPhWzvKtLoU\\n//3tVBFzgQH2kXvIYUnCJFGJD+OcLQBL9yGqAUHvvlcBQtdt1q94d0vPyuzjDLmI\\nFrQw5YZ9KSja3FBMCmhaIDgCaHxppoI9DBG+xp2wZ4ygrY6OMv5IV5VPj3axddPt\\nBviLGSY9AgMBAAECggEAMVF+870P0H7FeNywlOhaet3hFtKB7RqqLxK6m1Jg16w3\\ne/ked4k7ne8yNYlO73e2KRY91Ddw87ViCC9iRnQLtTwQghUHnT9Ckabr9q6GwRwB\\nmNzQDRMurX5ByT8rF83yQVjJlOIDNX6Uv3yqlt6BhrqYEk8cpl23YvBlVk15XkRe\\ncSh0DbpaXRNosn1KdhkcgZiVNTEIS9QZ0+wevCTQ13+cmfrg5QqAHx9uGlpy9MHc\\n19Eaz3H+5WUUnBeQTisJYRboJp4oFrdqxVoAEijahk8FEthRxBZw3c5OLSHdkgVs\\nQahlIvJqB6h2lAFfZP31m7n+ZiHRB2pgpGStZuSysQKBgQD41sNOKWBPJVGKt20f\\n9CLp8Oe4cGRyH+mU7U7hZSWogq6YiINA633zEwkvbreYaIpi+a4ds9IHZYJQMjBv\\nsYhXgbHVzYsaHHLiUOito3yvEeDeBTiC+xdnu6cUzSMG0hX1fBMl17S2ycmo59zL\\nwDMq5QenfhH9Svj4unTN43K4dQKBgQDNFn0PV4AFLYgr4FrdZgELK5U3EzwgcIRN\\nzeQVsuwSRKn1QjN6mQDoz+hPzvlK3/BeDwgOZclRrlJCBB0EasnOsI9ErLi+Ff1Q\\nRJivAYqGatbvUF0st3lOmTp6WaxEq7Av7VqM0GFAYriJxU47d/9Lkr/lmxu8X/vn\\nbhNpy5O9qQKBgQCUlrmdDLwDxscATDOPZxsKEt5rlkGEnGXAlkwJ73ojO5XCY5Hs\\nzDIzWeScPELJcXNhUrKT7fK2k/jSpoWhzKMbjciBYPF1MBOx7JpkRUGI0OYz7W4n\\nWIRLXxSQKUaHJiASrHpKmJnCpJAUmg+QYz6qmISPkYpwc7pj3AMvhmfUmQKBgQCC\\n7btzQuow5AuN71IzC77Y0b8WBQqarxpXzXyyOaKqWvmh4NyahPlmp+xonvrZyTNb\\n6P68qlCm+8H58URRIJerg15Den0KpWEtkE1lVHeKLsT+zPBe7tbbzcjeeUcYI/97\\n6WyofxPHFf83vSWfo5DxP0ORIr/HOAPKkKSGIvgz2QKBgQC+Jj7pLqBBGv+mzZBb\\nJfLjQcnI63/8GHT5iyvtfYpJ6i8Fswi+7RjKzJLYUUx/r+KPGluGAXHa/0iYQXmy\\nEm0HKn5nDGoi0JMOu5OL9DLh24TF8b9qrQrJ2Wc6Tg72yRpxsP21D7VWFOSXZi0n\\n9sKwR6g0aTlhR7hBdODYgTezFg==\\n-----END PRIVATE KEY-----\\n\",\n" +
            "  \"client_email\": \"pyinnyarsubuu-driveupload@pyinnyar-subuu.iam.gserviceaccount.com\",\n" +
            "  \"client_id\": \"111758249054068768109\",\n" +
            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/pyinnyarsubuu-driveupload%40pyinnyar-subuu.iam.gserviceaccount.com\",\n" +
            "  \"universe_domain\": \"googleapis.com\"\n" +
            "}";



    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */

    //  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
    //         throws IOException {
    //     // Load client secrets.
    //     InputStream in = new ByteArrayInputStream(CREDENTIALS_JSON_DATA.getBytes());
    //     GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    //     // Build flow and trigger user authorization request.
    //     GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
    //             HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
    //             .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
    //             .setAccessType("offline")
    //             .build();
    //     LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    //     Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    //     // returns an authorized Credential object.
    //     return credential;
    // }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        //InputStream in = getClass().getResourceAsStream(CREDENTIALS_FILE_PATH);
        InputStream in = convertToInputStream(CREDENTIALS_JSON_DATA);

        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        GoogleCredential credential = GoogleCredential.fromStream(in, HTTP_TRANSPORT, JSON_FACTORY)
                .createScoped(SCOPES);

        return credential;
    }

    private static InputStream convertToInputStream(String jsonData) {
        return new ByteArrayInputStream(jsonData.getBytes(StandardCharsets.UTF_8));
    }

    public Drive getInstance() throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        return service;
    }
    

    // Code needs to be implemented for the uploding a file to drive
    // uploading functions are as follows as
    // Using this code snippet you can do all drive functionality
    // getfiles()
    // uploadFile()

}