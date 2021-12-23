package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()   // Variable to hold our list of tasks
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Connects the activity_main.xml file

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongCLicked(position: Int) {
                // 1. Remove the item from the list
                listOfTasks.removeAt(position)
                // 2. Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }


//        // 1. Let's detect when the user clicks on the add button by grabbing a reference to the button view
//        findViewById<Button>(R.id.button).setOnClickListener {  // goes into layout file from line 10 and looks for the view with the specific id
//            // Code in here is going to be executed when the user clicks on a button
//            Log.i("Caren", "User clicked on button")
//        }

        loadItems()

        // Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Set up the button and input field so that the user can enter a task and add it to the list

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Get a reference to the button
        // and then set an onClickListener
        findViewById<Button>(R.id.button).setOnClickListener {
            // 1. Grab the text that the user has inputted into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()
            // 2. Add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            // Notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // 3. Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }


    // Save the data that the user has inputted
    // Save data by writing and reading from a file

    // Create a method to get the file we need; get the file we need
    fun getDataFile() : File {

        // Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException : IOException) {
            ioException.printStackTrace()
        }
    }

    // Save items by writing to the data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException : IOException) {
            ioException.printStackTrace()
        }
    }
}