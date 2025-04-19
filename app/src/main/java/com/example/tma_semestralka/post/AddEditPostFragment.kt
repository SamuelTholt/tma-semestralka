package com.example.tma_semestralka.post

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tma_semestralka.databinding.FragmentAddEditPostBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

@Suppress("DEPRECATION")
class AddEditPostFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddEditPostBinding
    private var listener : AddEditPostListener? = null

    private var post: Post? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditPostBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val target = targetFragment
        if (target is AddEditPostListener) {
            listener = target
        } else {
            Log.e(TAG, "Target fragment does not implement AddEditPostListener")
            throw ClassCastException("Target fragment must implement AddEditPostListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            post = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                arguments?.getParcelable("post", Post::class.java)
            else
                arguments?.getParcelable("post")
        }
        post?.let { setExistingDataOnUi(it) }
        attachUiListener()
    }

    private fun attachUiListener() {
        binding.saveBtn.setOnClickListener {
            val postHeader = binding.postHeaderEditText.text.toString()
            val postText = binding.postTexteEditText.text.toString()

            if (postHeader.isNotEmpty() && postText.isNotEmpty()) {
                val post1 = Post(
                    post?.id ?: 0, postHeader, postText
                )

                Log.d("PostData", "Saving post: $post1")
                listener?.onSaveBtnClicked(post != null, post1)
            }
            dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setExistingDataOnUi(post: Post) {
        binding.postHeaderEditText.setText(post.postHeader)
        binding.postTexteEditText.setText(post.postText)

        binding.saveBtn.text = "Update"
    }

    companion object {
        const val TAG = "AddEditPostFragment"

        @JvmStatic
        fun newInstance(post: Post?) = AddEditPostFragment().apply {
            arguments = Bundle().apply {
                putParcelable("post", post)
            }
        }
    }

    interface AddEditPostListener {
        fun onSaveBtnClicked(isUpdate: Boolean, post: Post)
    }

}