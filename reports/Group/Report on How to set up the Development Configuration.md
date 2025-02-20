# Development Environment Setup

## 1. Project Creation

To set up the development environment, a new project was first created based on the starter called **Hello-World**. Then, the project name was changed, along with the containing folder and the reference in the `pom.xml` file. In this file, the deliverable version was also set.

## 2. Workspace Cleanup

Before starting any task and to ensure that the workspace is clean, a **workspace cleanup** was performed with the script `clean-workspace`. This guarantees that everything works correctly from the beginning.

## 3. Database Initialization

The `start-mariadb` script was executed to start the database and allow the use of the **DBeaver** IDE. Through DBeaver, several scripts are introduced to populate the database with the necessary information for the project.

## 4. Importing Projects into Eclipse

Next, the **launchers** are created with the script `create-launchers`, and the projects are imported into **Eclipse** following these steps:

1. Use the **Import New Maven Projects** option.
2. In the advanced options, select **ArtifactId, Version**.
3. First, import **Acme-Framework**, then **Acme-ANS-D01**.

If any **warnings** appear, they can be fixed using **Quick Fix**.

## 5. Running the Project

To finalize the setup, the **Acme-ANS-D01** launchers are used, following these steps:

1. Run `development-populator#initial`.
2. Run `development-application#run` to start the application.

## 6. Verification and Application Access

Once the application is running and everything works correctly:

1. Copy the **URL** displayed in the terminal.
2. Open the **URL** in **Mozilla Firefox Developer Edition**.
3. From the framework interface, the application can be managed and tested.

---
