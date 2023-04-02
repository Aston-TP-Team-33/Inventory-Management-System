# Inventory-Management-System
## Summary 
This is the Inventory Management System for Galaxy Gadget. This was created using Java, JavaFX and Scene Builder.

## Features
The JavaFX application allows admin users to interact with our database. Admins can create, update and delete users, update & delete orders, manage products, view user 
queries and generate a report for a summary of the current status of orders and products.

The application features a side navigation bar for ease of use. Each page has a form on the left side of the page where you can fill in the data for the item you'd like 
to create or update. By clicking on an item in the table the form will autofill with its information. You can then hit either the create button (certain information that doesn't
allow duplicates will need to change e.g username), update button which will update the selected item or delete button which will remove it from the database.

The report page has a generate button which, when clicked, will create a pdf in the root directory of the project. 

The project is only open to admins so customer accoutns will not be able to access the system. 

## Dependencies
The following dependencies are needed to run the project.
- JavaFX (Details below)
- Bcrypt (included in pom.xml)
- JSch (included in pom.xml)
- mysql-connector-java (included in pom.xml)
- iTextPDF (included in pom.xml)

## Project Structure 
- All of the projects source code is stored in the `src` directory
- It contains 4 packages:
  - `application` - FXML files & Controllers for the main application
  - `application.model`- Java classes which model the database tables 
  - `db` - All of the code for querying the database 
  - `login` - The initial login screen

## How To Run
These instructions were written using Windows but should work on other OS

### 1 - Downloading project
1. Open eclipse
2. File -> Import -> Git -> Projects from Git 

![image](https://user-images.githubusercontent.com/62181722/229345961-7bf1433f-3798-4866-9d77-ef7eae24b939.png)
![image](https://user-images.githubusercontent.com/62181722/229345993-448d30c7-9480-4606-9291-3cdb67d66906.png)

3. Clone URI
4. Copy and paste the GitHub URL 

![image](https://user-images.githubusercontent.com/62181722/229346095-b9e1e7d8-8804-4e90-9b7a-9a88d6d00a0f.png)

5. Select main 

![image](https://user-images.githubusercontent.com/62181722/229346060-403a0301-29d1-4438-93cc-95fed5411d58.png)

6. Continue following setup until project is in eclipse  

### 2 - Download JavaFX SDK
1. Download JavaFX 17.0.6 SDK from [here](https://gluonhq.com/products/javafx/). Make sure you remember where you downloaded it to
2. Extract the folder
3. In Eclipse go to Window -> Java -> Build Path -> User Library 
4. Click new & name the library "JavaFX"
5. Select add External JARs, navigate to the `lib` folder in your JavaFX download and select all the JARs
6. Save & Close 

![image](https://user-images.githubusercontent.com/62181722/229346174-71582770-2118-4d45-ab6b-c6ea4a0cd54a.png)


### 3 - Download Extension
1. Help -> Eclipse Marketplace
2. Search for & install e(fx)clipse
https://marketplace.eclipse.org/content/efxclipse

![image](https://user-images.githubusercontent.com/62181722/229346200-d50f849f-5b90-4051-9c8c-5584651b5e53.png)


### 4 - Update VM arguments
1. Once the extension has installed right click the project -> run as... -> run configurations 
2. Go to arguments and paste this into VM arguments `--module-path /path/to/javafx-sdk-20/lib --add-modules javafx.controls,javafx.fxml`
![image](https://user-images.githubusercontent.com/62181722/229346258-e60f1650-e750-4654-96cf-acd97a31f9b3.png)

[This video should help with the steps above](https://www.youtube.com/watch?v=_7OM-cMYWbQ)

## 5 - Run the project
1. Right click the project -> run as... -> Java Application

![image](https://user-images.githubusercontent.com/62181722/229346359-6cbf8277-d5f2-4b2c-a36c-14df61870cfc.png)


## Potential Errors
### No location set
After following the steps above you're sometimes met with an error "no location set". Simply making a change to the string on line 17 and undoing it in Main.java seems to fix it. 

The line should look like this `FXMLLoader loader = new FXMLLoader(getClass().getResource("../login/login.fxml"));` before and after the change. 

Alternatively you could change the line to `FXMLLoader loader = new FXMLLoader(getClass().getResource("./../login/login.fxml"));`

This bug only seems to occur when the project is first imported regardless of what the string is initially. 

### JavaFX runtime components are missing
This means that there is an issue with your VM Arguments. Make sure you set the correct path to your `javafx-sdk-17.0.6\lib` (see step 4)

### Unable to initialize main class application.Main
This means there is an issue with your JavaFX user library. Make sure you have imported all of the JARs in the `javafx-sdk-17.0.6\lib` folder (see step 2)

## Helpful Resources
[JavaFX Setup Documentation (navigate to JavaFX for Eclipse)](https://openjfx.io/openjfx-docs/)
