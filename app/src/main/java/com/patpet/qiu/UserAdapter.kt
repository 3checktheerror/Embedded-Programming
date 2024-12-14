package com.patpet.qiu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    private val users: List<User>,
    private val onUpdateClicked: (User) -> Unit,
    private val onDeleteClicked: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val avatars = listOf(
        R.mipmap.avatar01,
        R.mipmap.avatar02,
        R.mipmap.avatar03,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.userName.text = user.username
        holder.userDetails.text = "Phone: ${user.phoneNumber}\n" +
                "Role: ${user.role}\n" +
                "Gender: ${user.gender}\n" +
                "Age: ${user.age}\n" +
                "Address: ${user.address}"

        // 设置头像
        holder.userAvatar.setImageResource(avatars[position % avatars.size])

        // 按钮点击事件
        holder.updateButton.setOnClickListener { onUpdateClicked(user) }
        holder.deleteButton.setOnClickListener { onDeleteClicked(user) }
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.userName)
        val userDetails: TextView = view.findViewById(R.id.userDetails)
        val userAvatar: ImageView = view.findViewById(R.id.userAvatar)
        val updateButton: Button = view.findViewById(R.id.updateButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }
}
