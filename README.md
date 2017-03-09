# Test
Test

<ul><li>Creating a new project from below website</li></ul>

https://console.firebase.google.com

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

# To start FireBase Quick Chat:

FireBaseConfig.startChat(MainActivity.this);

FireBaseConfig.java:

    /*
            you can find storage url from "firebase console --> storage"
     */
    public static final String STORAGE_URL = "gs://xyzabc.appspot.com";

      2)  /*
       you can find auth key from "firebase console --> project settings --> cloud messaging --> server key"
     */
    public static final String FIREBASE_AUTH_KEY = "key=YOUR_SERVER_KEY";
    
GoogleManager.java:

//you can find value from google-services.json file
    // 1) Open google-services.json file -> client -> oauth_client -> client_id
    // 2) Copy this client ID and hardcode this to below variable
    
private String requestIdToken = "YOUR_REQUEST_ID_TOKEN";
