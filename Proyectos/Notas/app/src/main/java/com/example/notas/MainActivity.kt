package com.example.notas

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notas.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), OnClickListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteFinishedAdapter: NoteAdapter

    private lateinit var database: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = DataBaseHelper(this)


        noteAdapter = NoteAdapter(mutableListOf(), this)
        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteAdapter
        }
        noteFinishedAdapter = NoteAdapter(mutableListOf(), this)
        binding.rvNotesFinished.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteFinishedAdapter
        }

        binding.btnAdd.setOnClickListener {
            if (binding.etDescription.text.toString().isNotBlank()){
                val note = Note(description = binding.etDescription.text.toString().trim())
                note.id = database.insertNote(note)

                if (note.id != Constants.ID_ERROR) {
                    addNoteAuto(note)
                    binding.etDescription.text?.clear()
                    showMessage(R.string.message_write_database_success)
                }else{
                    showMessage(R.string.message_write_database_error)
                }
            }else{
                binding.etDescription.error = getString(R.string.validation_fiel_required)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun getData(){
       /* val data = mutableListOf(Note(1, "Estudiar"),
            Note(2,"Comprar coasas"),
            Note(3,"Compras super", true))*/
        val data = database.getAllNotes()
        data.forEach{
            note -> addNoteAuto(note)
        }
    }

    private fun addNoteAuto(note: Note) {
        if (note.isFinished){
            noteFinishedAdapter.add(note)
        }else noteAdapter.add(note)
    }
    private fun deleteNoteAuto(note: Note){
        if (note.isFinished) {
            noteAdapter.remove(note)
        }else{
            noteFinishedAdapter.remove(note)
        }
    }

    override fun onCheck(note: Note) {
        if (database.updateNote(note)) {
            deleteNoteAuto(note)
            addNoteAuto(note)
        }else{
            showMessage(R.string.message_write_database_error)
        }
    }

    override fun onLongClick(note: Note, currentAdapter: NoteAdapter) {
        val builder = AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title))
            .setPositiveButton(getString(R.string.dialog_ok)) { _, _ ->
                if (database.deleteNote(note)) {
                    currentAdapter.remove(note)
                    showMessage(R.string.message_write_database_success)
                } else {
                    showMessage(R.string.message_write_database_error)
                }
            }
            .setNegativeButton(getString(R.string.dialog_cancel), null)
        builder.create().show()
    }
    private fun showMessage(msgRes:Int){
        Snackbar.make(binding.root,
            getString(msgRes), Snackbar.LENGTH_SHORT).show()
    }
}