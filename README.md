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

1) Storage url: <br>
   you can find storage url from "firebase console --> storage".

    ```xml
    <meta-data
            android:name="storage_url"
            android:value="YOUR_STORAGE_URL" />
    ```

2) Server key: <br>
   you can find auth key from "firebase console --> project settings --> cloud messaging --> server key".

    ```xml
    <meta-data
            android:name="server_key"
            android:value="YOUR_SERVER_KEY" />
    ```
    
3) Request id token: <br>
   You can find value from google-services.json file<br>
    a) Open google-services.json file -> client -> oauth_client -> client_id<br>
    b) Copy this client ID and hardcode this to below meta-data tag<br>
    
    ```xml
    <meta-data
            android:name="request_id_token"
            android:value="YOUR_REQUEST_ID_TOKEN" />
    ```
    
# Check firebase login
   
   Calling startChat method of <a href="https://github.com/dhavalsoneji/Test/blob/master/java/FQCConfig.java">FQCConfig.java</a> class
   
   ```java
   FQCConfig.initializeLibrary(MainActivity.this, new DBQueryHandler.OnInitializeLibraryListener() {
                    @Override
                    public void loginRequire() {
                        // write your code that redirect to login activity
                    }

                    @Override
                    public void homeRequire() {
                        // write your code that redirect to home activity
                    }

                    @Override
                    public void onException(Exception e) {
                        // write your code
                    }
                });
```     
# Login with google

   Define object variable of <a href="https://github.com/dhavalsoneji/Test/blob/master/java/FQCLogin.java">FQCLogin.java</a> class:
   ```java
   private FQCLogin mFqcLogin;
   ```

   Create constructor of FQCLogin by passing FragmentActivity object:
   ```java
   mFqcLogin = new FQCLogin(LoginActivity.this);
   ```
   
   Calling loginWithGoogle method to login with google:
   ```java
   mFqcLogin.loginWithGoogle(new DBQueryHandler.OnLoginListener() {
            @Override
            public void onRequest() {
                // write your code 
            }

            @Override
            public void onResponse() {
                // write your code 
            }

            @Override
            public void onException(Exception e) {
                // write your code 
            }
        });
   ```
   add below lines of code, contains a) requestCode, b) resultCode, c) data parameters:
   
   ```java
   if (mFqcLogin != null) {
       mFqcLogin.onActivityResult(requestCode, resultCode, data);
   }
   ```
   
   # Retrieve contact list
   
   Define object variable of <a href="https://github.com/dhavalsoneji/Test/blob/master/java/FQCContacts.java">FQCContacts.java</a> class:
   
   ```
   private FQCContacts mFqcContacts;
   ```
   
   Create constructor of FQCContacts by passing Context:
   
   ```
   mFqcContacts = new FQCContacts(mFragment.getActivity());
   ```
   
   Retrieve a contact list by calling fetchContacts method of FQCContacts class:
   
   ```
   mFqcContacts.fetchContacts(new DBQueryHandler.OnQueryHandlerListener<List<UserTable>>() {
            @Override
            public void onStart() {
                //you can show progressBar code or something else
            }

            @Override
            public void onComplete(List<UserTable> list) {
                //you can get list of Contacts from here
            }

            @Override
            public void onException(Exception e) {
                //write your code to handle exception
            }
        });
   ```
        
