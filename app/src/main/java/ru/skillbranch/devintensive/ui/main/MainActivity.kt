package ru.skillbranch.devintensive.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.group.GroupActivity
import ru.skillbranch.devintensive.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolBar()
        initViews()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getChatData().observe(this, Observer { chatAdapter.updateData(it) })
    }

    private fun initViews() {

        chatAdapter = ChatAdapter{
            Snackbar.make(rv_chat_list, "Click on ${it.title}", Snackbar.LENGTH_LONG).show()
        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(resources.getDrawable(R.drawable.divider,theme))

        val touchCallback = ChatItemTouchHelperCallback(chatAdapter){
            viewModel.addToArchive(it.id)
            Snackbar.make(rv_chat_list, "Вы точно хотите добавить ${it.title} в архив", Snackbar.LENGTH_LONG)
                    .setAction("ОТМЕНА"){ _ -> viewModel.restoreFromArchive(it.id) }
                    .show()
        }
        val itemTouchHelper = ItemTouchHelper(touchCallback)
        itemTouchHelper.attachToRecyclerView(rv_chat_list)

        with(rv_chat_list){
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(divider) //не подчеркивать аватар!
        }


        fab.setOnClickListener{
            val intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
    }
}
