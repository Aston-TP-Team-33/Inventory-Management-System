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
The following dependencies are needed to run the project. They're included in the pom.xml file.
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
3. Clone URI
4. Copy and paste the GitHub URL 
5. Select main 
6. Continue following setup until project is in eclipse  

### 2 - Download JavaFX SDK
1. Download JavaFX 17.0.6 SDK from [here](https://gluonhq.com/products/javafx/). Make sure you remember where you downloaded it to
2. Extract the folder
3. In Eclipse go to Window -> Java -> Build Path -> User Library 
4. Click new & name the library "JavaFX"
5. Select add External JARs, navigate to the `lib` folder in your JavaFX download and select all the JARs
6. Save & Close 

### 3 - Download Extension
1. Help -> Eclipse Marketplace
2. Search for & install e(fx)clipse
https://marketplace.eclipse.org/content/efxclipse

### 4 - Update VM arguments
1. Once the extension has installed right click the project -> run as... -> run configurations 
2. Go to arguments and paste this into VM arguments `--module-path /path/to/javafx-sdk-20/lib --add-modules javafx.controls,javafx.fxml`

[This video should help with the steps above](https://www.youtube.com/watch?v=_7OM-cMYWbQ)

### Potential Errors
After folowing the steps above you're sometimes met with an error "no location set". Simply making a change to the string on line 17 and undoing it in Main.java seems to fix it. 

The line should look like this `FXMLLoader loader = new FXMLLoader(getClass().getResource("../login/login.fxml"));` before and after the change. 

Alternatively you could change the line to `FXMLLoader loader = new FXMLLoader(getClass().getResource("./../login/login.fxml"));`

This bug only seems to occur when the project is first imported regardless of what the string is initially. 
