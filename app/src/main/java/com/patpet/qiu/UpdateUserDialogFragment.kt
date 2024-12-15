package com.patpet.qiu

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class UpdateUserDialogFragment : DialogFragment() {

    private var onUpdateListener: ((User) -> Unit)? = null

    // 用于设置更新监听器
    fun setOnUpdateListener(listener: (User) -> Unit) {
        onUpdateListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_update_user, null)

        // 初始化布局中的 EditText
        val editUsername = view.findViewById<EditText>(R.id.editUsername)
        val editPhoneNumber = view.findViewById<EditText>(R.id.editPhoneNumber)
        val editRole = view.findViewById<EditText>(R.id.editRole)
        val editGender = view.findViewById<EditText>(R.id.editGender)
        val editAge = view.findViewById<EditText>(R.id.editAge)
        val editAddress = view.findViewById<EditText>(R.id.editAddress)

        // 获取传递过来的用户信息并填充到输入框中
        val user = arguments?.getParcelable<User>("user")
        user?.let {
            editUsername.setText(it.username)
            editPhoneNumber.setText(it.phoneNumber)
            editRole.setText(it.role)
            editGender.setText(it.gender)
            editAge.setText(it.age.toString())
            editAddress.setText(it.address)
        }

        builder.setView(view)
            .setPositiveButton("Update") { _, _ ->
                // 获取输入框中的新数据
                val updatedUser = User(
                    username = editUsername.text.toString(),
                    password = user?.password ?: "", // 保留密码
                    phoneNumber = editPhoneNumber.text.toString(),
                    role = editRole.text.toString(),
                    gender = editGender.text.toString(),
                    age = editAge.text.toString().toIntOrNull() ?: 0,
                    address = editAddress.text.toString()
                )
                // 触发更新回调
                onUpdateListener?.invoke(updatedUser)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        return builder.create()
    }

    companion object {
        // 创建实例并传递用户信息
        fun newInstance(user: User): UpdateUserDialogFragment {
            val fragment = UpdateUserDialogFragment()
            val args = Bundle()
            args.putParcelable("user", user)
            fragment.arguments = args
            return fragment
        }
    }
}
