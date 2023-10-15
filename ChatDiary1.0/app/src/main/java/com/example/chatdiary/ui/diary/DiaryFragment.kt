package com.example.chatdiary.ui.diary

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chatdiary.databinding.FragmentDiaryBinding
import com.example.chatdiary.ui.login.LoginViewModel
import com.example.chatdiary.ui.login.LoginViewModelFactory

class DiaryFragment : Fragment() {
    private lateinit var diaryViewModel: DiaryViewModel
    private var messageInput: EditText? = null
    private var _binding: FragmentDiaryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        diaryViewModel = ViewModelProvider(this, DiaryViewModelFactory())
            .get(DiaryViewModel::class.java)
        messageInput = binding.messageInput
        messageInput!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 文本变化之前的操作
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 文本变化时的操作
                val newText = s.toString()
                // 在用户输入时更新 ViewModel 中的文本
                diaryViewModel.updateMessageText(newText)
            }

            override fun afterTextChanged(s: Editable?) {
                // 文本变化之后的操作
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}