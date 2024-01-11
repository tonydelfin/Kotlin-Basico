package com.example.notas

interface OnClickListener {
    fun onCheck(note: Note)
    fun onLongClick(note: Note, currentAdapter: NoteAdapter)
}