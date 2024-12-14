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
    private val currentUsername: String?,
    private val currentUserRole: String,
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

        holder.userAvatar.setImageResource(avatars[position % avatars.size])

        // 根据当前用户角色与记录所属用户的关系，控制按钮显示与交互
        if (currentUserRole == "admin") {
            // admin可以对所有用户进行update和delete
            holder.updateButton.visibility = View.VISIBLE
            holder.deleteButton.visibility = View.VISIBLE
            holder.updateButton.isEnabled = true
            holder.deleteButton.isEnabled = true
        } else {
            // 非admin用户
            if (user.username == currentUsername) {
                // 可以更新自己的条目，但不可以删除
                holder.updateButton.visibility = View.VISIBLE
                holder.updateButton.isEnabled = true

                holder.deleteButton.visibility = View.VISIBLE
                holder.deleteButton.isEnabled = false
            } else {
                // 其他用户的条目不可操作
                holder.updateButton.visibility = View.INVISIBLE
                holder.deleteButton.visibility = View.INVISIBLE
            }
        }

        // 按钮点击事件
        holder.updateButton.setOnClickListener {
            if (holder.updateButton.isEnabled) onUpdateClicked(user)
        }
        holder.deleteButton.setOnClickListener {
            if (holder.deleteButton.isEnabled) onDeleteClicked(user)
        }
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
