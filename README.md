# What is this?
Tiny Android app that inserts a single row into an existing database in Notion. This project uses a [Java implementation of the Notion API.
](https://github.com/seratch/notion-sdk-jvm)

# Why this app?
I keep my todo items in Notion and I needed a way to quickly capture a todo item. Unfortunatly, on my phone, starting the official Notion app takes a long time, and the same is true for using Notion in Chrome. This was a good reason to check out the Notion API and write a tiny app that does this one thing really well.

# Requirements
This app is only useful for people who want to change the source code and adapt it to their needs. This app mostly exists to show how to use a subset of the Notion API on Android. This means that the requirements for using this app are that you can compile and install your own Android app.

# Required code changes
To build the project you need to create a file called `appconfig.properties` in the root directory and add the following content:
```
NOTION_TOKEN="secret_replace_with_your_notion_integration_token"
DB_NAME="DatabaseName"
```
`NOTION_TOKEN` is the token of your integration, as described in the article ["Create integrations with the Notion API".](https://www.notion.so/help/create-integrations-with-the-notion-api) `DB_NAME` is the name of the Notion database that you want to add a row into. This database has to be shared with the integration in order to allow the app to insert a new row into the database.

You will also need to adapt the code of the `doInBackground` method in the `MyTask` class to work with the columns of your Notion table (/app/src/main/java/com/example/myapplication/FirstFragment.java). I suggest looking at the sample code provided [here](https://github.com/seratch/notion-sdk-jvm). The code as it is finds the database by it's name and then inserts a new row using the specific properties of my todo item table. If you are not by chance using a database that has columns named "Status" and "Priority" **you will need to modify this code**. ;-)

``` Java
    private class MyTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            EditText text = (EditText)getView().findViewById(R.id.idText);
            ...
```
