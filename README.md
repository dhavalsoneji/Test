# Test
Test

<h5>Creating a new project by click <a href="https://console.firebase.google.com" target="_blank">here (Firebase console)</a> & follow below steps:</h5>

<ul><li>Click in “Create new project”</li></ul>

![Create new project](/images/image_01.png?raw=true)

<ul><li>Type your project name</li></ul>

![Type your project name](/images/image_02.png?raw=true)

<ul><li>Add a new android app by click in “add firebase to your android app”</li></ul>

![add new android app](/images/image_03.png?raw=true)

<ul><li>Step 1<br>Copy SHA1 from your project</li></ul>

![](/images/image_04.png?raw=true)

<ul><li>Step 2<br>Write your package name & SHA1</li></ul>

![](/images/image_05.png?raw=true)

<ul><li>Step 3<br>download configuration file (named: “google-services.json”) and put into “app” folder of android studio project</li></ul>

![](/images/image_06.png?raw=true)

# Configuration:

<h4>Add this three meta-data tag to your AndroidManifest.xml file.</h4>

1) you can find storage url from "firebase console --> storage".

    ```xml
    <meta-data
            android:name="storage_url"
            android:value="YOUR_STORAGE_URL" />
    ```

2) you can find auth key from "firebase console --> project settings --> cloud messaging --> server key".

    ```xml
    <meta-data
            android:name="server_key"
            android:value="YOUR_SERVER_KEY" />
    ```
    
3) You can find value from google-services.json file<br>
    a) Open google-services.json file -> client -> oauth_client -> client_id<br>
    b) Copy this client ID and hardcode this to below meta-data tag<br>
    
    GoogleManager.java:
    
    ```xml
    <meta-data
            android:name="request_id_token"
            android:value="YOUR_REQUEST_ID_TOKEN" />
    ```
    
    
# Login with google

    ```java
    pivate FQCLogin mFqcLogin;
    
    //.......
    
    mFqcLogin = new FQCLogin(fragmentActivity);
    
    //.......
    
    mFqcLogin.loginWithGoogle(new FQCLoginListener() {
            @Override
            public void onStart() {
                // write your code 
            }

            @Override
            public void onComplete() {
                // write your code 
            }

            @Override
            public void onException(Exception e) {
                // write your code 
            }
        });
   ```
